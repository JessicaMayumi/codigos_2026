from fastapi import HTTPException, status
from sqlalchemy.orm import Session
from app.v1.modules.usuarios.repository import UsuarioRepository
from app.v1.modules.usuarios.schemas import UsuarioCreate
from app.v1.core.security import hash_password, verify_password, create_access_token


class AuthService:
    def __init__(self, db: Session):
        self.repo = UsuarioRepository(db)

    def registrar(self, dados: UsuarioCreate):
        if self.repo.buscar_por_username(dados.username):
            raise HTTPException(status.HTTP_409_CONFLICT, "Username já cadastrado")
        senha_hash = hash_password(dados.senha)
        return self.repo.criar(username=dados.username, senha_hash=senha_hash)

    def login(self, username: str, senha: str) -> str:
        usuario = self.repo.buscar_por_username(username)
        if not usuario or not verify_password(senha, usuario.senha_hash):
            raise HTTPException(
                status.HTTP_401_UNAUTHORIZED,
                "Usuário ou senha inválidos",
                headers={"WWW-Authenticate": "Bearer"},
            )
        return create_access_token({"sub": usuario.username})
