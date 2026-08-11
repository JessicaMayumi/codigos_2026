from sqlalchemy.orm import Session
from app.v1.modules.tipos_equipamento.model import TipoEquipamento


class TipoEquipamentoRepository:
    def __init__(self, db: Session):
        self.db = db

    def listar(self):
        return self.db.query(TipoEquipamento).all()

    def buscar_por_id(self, tipo_id: int):
        return self.db.query(TipoEquipamento).filter(TipoEquipamento.id == tipo_id).first()

    def criar(self, nome: str):
        tipo = TipoEquipamento(nome=nome)
        self.db.add(tipo)
        self.db.commit()
        self.db.refresh(tipo)
        return tipo
