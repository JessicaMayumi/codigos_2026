from fastapi import FastAPI
from app.v1.core.config import settings
from app.v1.core.database import Base, engine
from app.v1.modules.equipamentos import model as equipamento_model  # noqa: F401
from app.v1.modules.tipos_equipamento import model as tipo_equipamento_model  # noqa: F401
from app.v1.modules.usuarios import model as usuario_model  # noqa: F401
from app.v1.modules.equipamentos import routers as equipamento_routers
from app.v1.modules.tipos_equipamento import routers as tipo_equipamento_routers
from app.v1.modules.usuarios import routers as usuario_routers

Base.metadata.create_all(bind=engine)

app = FastAPI(
    title=settings.APP_NAME,
    version=settings.APP_VERSION,
    description="API REST de equipamentos - Prova de Suficiência de Programação Web II. "
                "Leitura é pública; criar, alterar e remover exigem token. "
                "Faça login em /auth/login e informe usuário e senha no botão Authorize "
                "(deixe client_id e client_secret em branco).",
    openapi_tags=[
        {"name": "Equipamentos", "description": "CRUD de equipamentos."},
        {"name": "Tipos de Equipamento", "description": "Cadastro dos tipos usados pelos equipamentos."},
        {"name": "Autenticação", "description": "Registro de usuário e geração do token JWT."},
    ],
)

BASE_PREFIX = "/RestAPIFurb"

app.include_router(equipamento_routers.router, prefix=BASE_PREFIX)
app.include_router(tipo_equipamento_routers.router, prefix=BASE_PREFIX)
app.include_router(usuario_routers.router, prefix=BASE_PREFIX)


@app.get("/", include_in_schema=False)
def root():
    return {"mensagem": "RestAPIFurb no ar. Documentação em /docs"}
