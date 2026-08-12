from fastapi import APIRouter, Depends, status
from fastapi.security import OAuth2PasswordRequestForm
from sqlalchemy.orm import Session
from app.v1.core.database import get_db
from app.v1.modules.usuarios.services import AuthService
from app.v1.modules.usuarios.schemas import UsuarioCreate, UsuarioOut, TokenOut

router = APIRouter(prefix="/auth", tags=["Autenticação"])


@router.post("/registrar", response_model=UsuarioOut, status_code=status.HTTP_201_CREATED,
             summary="Registra um novo usuário",
             description="A senha é armazenada com hash bcrypt, nunca em texto puro.",
             responses={409: {"description": "Username já cadastrado"}})
def registrar(dados: UsuarioCreate, db: Session = Depends(get_db)):
    return AuthService(db).registrar(dados)


@router.post("/login", response_model=TokenOut, summary="Autentica e retorna o token JWT",
             description="Use o token no header `Authorization: Bearer <token>` nas rotas protegidas.",
             responses={401: {"description": "Usuário ou senha inválidos"}})
def login(form: OAuth2PasswordRequestForm = Depends(), db: Session = Depends(get_db)):
    token = AuthService(db).login(form.username, form.password)
    return {"access_token": token, "token_type": "bearer"}
