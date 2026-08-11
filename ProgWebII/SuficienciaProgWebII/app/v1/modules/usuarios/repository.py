from sqlalchemy.orm import Session
from app.v1.modules.usuarios.model import Usuario


class UsuarioRepository:
    def __init__(self, db: Session):
        self.db = db

    def buscar_por_username(self, username: str):
        return self.db.query(Usuario).filter(Usuario.username == username).first()

    def criar(self, username: str, senha_hash: str):
        usuario = Usuario(username=username, senha_hash=senha_hash)
        self.db.add(usuario)
        self.db.commit()
        self.db.refresh(usuario)
        return usuario
