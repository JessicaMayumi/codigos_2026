from fastapi import FastAPI, Depends, HTTPException, status, Request
from sqlalchemy import Column, Integer, String, DateTime, create_engine
from sqlalchemy.orm import sessionmaker, Session, declarative_base
from pydantic import BaseModel
from datetime import datetime, timedelta, timezone
from threading import Lock
import asyncio
import uvicorn

DATABASE_URL = "sqlite:///./users.db"
engine = create_engine(DATABASE_URL, connect_args={"check_same_thread": False})
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
Base = declarative_base()

# ---------------------------------------------------------------------------
# Parâmetros das proteções contra força bruta
# ---------------------------------------------------------------------------

# Técnica 1 - Bloqueio temporário de contas
MAX_FALHAS_USUARIO = 3          # falhas consecutivas antes de bloquear a conta
BLOQUEIO_USUARIO_SEGUNDOS = 300  # 5 minutos de bloqueio

# Técnica 2 - Atraso progressivo (2s, 4s, 8s, ...)
BASE_ATRASO = 2
ATRASO_MAXIMO_SEGUNDOS = 30

# Técnica 3 - Registro de tentativas por IP
MAX_FALHAS_IP = 10               # falhas permitidas por IP dentro da janela
JANELA_IP_SEGUNDOS = 300         # janela de contagem: 5 minutos
BLOQUEIO_IP_SEGUNDOS = 900       # 15 minutos de bloqueio do IP


def agora():
    """Horário atual em UTC, sem fuso, para comparar com o que o SQLite guarda."""
    return datetime.now(timezone.utc).replace(tzinfo=None)


class User(Base):
    __tablename__ = "users"
    id = Column(Integer, primary_key=True, index=True)
    username = Column(String, unique=True, index=True)
    password = Column(String)
    email = Column(String)


class TentativaLogin(Base):
    """Falhas consecutivas e bloqueio de cada usuário (técnicas 1 e 2)."""
    __tablename__ = "tentativas_login"
    username = Column(String, primary_key=True, index=True)
    falhas = Column(Integer, nullable=False, default=0)
    bloqueado_ate = Column(DateTime, nullable=True)
    ultima_tentativa = Column(DateTime, nullable=True)


Base.metadata.create_all(bind=engine)

app = FastAPI()

# Contagem de falhas por IP (técnica 3). Fica em memória e é protegida por um
# lock porque o uvicorn atende requisições em threads diferentes.
_falhas_por_ip = {}
_lock_ip = Lock()


class RegisterRequest(BaseModel):
    username: str
    email: str
    password: str


class LoginRequest(BaseModel):
    username: str
    password: str


def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()


# ---------------------------------------------------------------------------
# Técnica 3 - Registro de tentativas por IP
# ---------------------------------------------------------------------------

def ip_do_cliente(request: Request) -> str:
    return request.client.host if request.client else "desconhecido"


def segundos_de_bloqueio_do_ip(ip: str) -> int:
    """Retorna quantos segundos faltam para o IP sair do bloqueio (0 se liberado)."""
    with _lock_ip:
        registro = _falhas_por_ip.get(ip)
        if not registro or not registro["bloqueado_ate"]:
            return 0

        restante = (registro["bloqueado_ate"] - agora()).total_seconds()
        if restante <= 0:
            _falhas_por_ip.pop(ip, None)
            return 0
        return int(restante) + 1


def registrar_falha_do_ip(ip: str) -> int:
    """Soma uma falha ao IP e devolve o total de falhas na janela atual."""
    with _lock_ip:
        instante = agora()
        registro = _falhas_por_ip.get(ip)

        # Começa uma janela nova se não existe ou se a anterior já expirou.
        if not registro or instante - registro["inicio_janela"] > timedelta(seconds=JANELA_IP_SEGUNDOS):
            registro = {"falhas": 0, "inicio_janela": instante, "bloqueado_ate": None}

        registro["falhas"] += 1
        if registro["falhas"] >= MAX_FALHAS_IP:
            registro["bloqueado_ate"] = instante + timedelta(seconds=BLOQUEIO_IP_SEGUNDOS)

        _falhas_por_ip[ip] = registro
        return registro["falhas"]


def limpar_falhas_do_ip(ip: str):
    with _lock_ip:
        _falhas_por_ip.pop(ip, None)


# ---------------------------------------------------------------------------
# Técnicas 1 e 2 - Bloqueio da conta e atraso progressivo
# ---------------------------------------------------------------------------

def buscar_controle(db: Session, username: str) -> TentativaLogin:
    controle = db.query(TentativaLogin).filter(TentativaLogin.username == username).first()
    if not controle:
        controle = TentativaLogin(username=username, falhas=0)
        db.add(controle)
        db.commit()
        db.refresh(controle)
    return controle


def segundos_de_bloqueio_da_conta(db: Session, controle: TentativaLogin) -> int:
    """Segundos restantes de bloqueio da conta (0 se liberada). Expira sozinho."""
    if not controle.bloqueado_ate:
        return 0

    restante = (controle.bloqueado_ate - agora()).total_seconds()
    if restante <= 0:
        # Bloqueio venceu: zera o contador e libera a conta.
        controle.bloqueado_ate = None
        controle.falhas = 0
        db.commit()
        return 0
    return int(restante) + 1


def calcular_atraso(falhas: int) -> int:
    """Atraso progressivo: 1ª falha 2s, 2ª 4s, 3ª 8s... limitado pelo teto."""
    if falhas <= 0:
        return 0
    return min(BASE_ATRASO ** falhas, ATRASO_MAXIMO_SEGUNDOS)


# ---------------------------------------------------------------------------
# Rotas
# ---------------------------------------------------------------------------

@app.post("/register/")
def register_user(request: RegisterRequest, db: Session = Depends(get_db)):
    existing_user = db.query(User).filter(User.username == request.username).first()
    if existing_user:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="Usuário já existe"
        )

    user = User(
        username=request.username,
        email=request.email,
        password=request.password
    )
    db.add(user)
    db.commit()
    db.refresh(user)

    return {"message": "Usuário registrado com sucesso!"}


@app.post("/login/")
async def login(request: LoginRequest, http_request: Request, db: Session = Depends(get_db)):
    ip = ip_do_cliente(http_request)

    # Técnica 3: IP com muitas falhas nem chega a consultar o banco.
    restante_ip = segundos_de_bloqueio_do_ip(ip)
    if restante_ip > 0:
        raise HTTPException(
            status_code=status.HTTP_429_TOO_MANY_REQUESTS,
            detail=f"Muitas tentativas a partir deste IP. Tente novamente em {restante_ip} segundos."
        )

    controle = buscar_controle(db, request.username)

    # Técnica 1: conta bloqueada não valida a senha, nem se ela estiver correta.
    restante_conta = segundos_de_bloqueio_da_conta(db, controle)
    if restante_conta > 0:
        raise HTTPException(
            status_code=status.HTTP_423_LOCKED,
            detail=f"Conta temporariamente bloqueada. Tente novamente em {restante_conta} segundos."
        )

    user = db.query(User).filter(
        User.username == request.username,
        User.password == request.password
    ).first()

    if user:
        # Sucesso: zera os contadores do usuário e do IP.
        controle.falhas = 0
        controle.bloqueado_ate = None
        controle.ultima_tentativa = agora()
        db.commit()
        limpar_falhas_do_ip(ip)
        return {"message": "Login realizado com sucesso!"}

    # Falhou: registra no usuário e no IP.
    controle.falhas += 1
    controle.ultima_tentativa = agora()
    falhas_ip = registrar_falha_do_ip(ip)

    conta_bloqueada = controle.falhas >= MAX_FALHAS_USUARIO
    if conta_bloqueada:
        controle.bloqueado_ate = agora() + timedelta(seconds=BLOQUEIO_USUARIO_SEGUNDOS)
    db.commit()

    # Técnica 2: o atraso cresce conforme a pior das duas contagens.
    atraso = calcular_atraso(max(controle.falhas, falhas_ip))
    await asyncio.sleep(atraso)

    if conta_bloqueada:
        raise HTTPException(
            status_code=status.HTTP_423_LOCKED,
            detail=(
                "Credenciais inválidas. Conta bloqueada por "
                f"{BLOQUEIO_USUARIO_SEGUNDOS // 60} minutos após "
                f"{MAX_FALHAS_USUARIO} tentativas falhas."
            )
        )

    raise HTTPException(
        status_code=status.HTTP_401_UNAUTHORIZED,
        detail="Credenciais inválidas"
    )


# Inicialização direta do servidor
if __name__ == "__main__":
    uvicorn.run(
        "servico:app",
        host="0.0.0.0",
        port=8000,
        reload=False
    )
