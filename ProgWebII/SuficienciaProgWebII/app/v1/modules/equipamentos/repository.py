from sqlalchemy.orm import Session, joinedload
from app.v1.modules.equipamentos.model import Equipamento


class EquipamentoRepository:
    def __init__(self, db: Session):
        self.db = db

    def listar(self):
        # joinedload traz o tipo no mesmo SELECT (evita N+1)
        return self.db.query(Equipamento).options(joinedload(Equipamento.tipo)).all()

    def buscar_por_id(self, equipamento_id: int):
        return (
            self.db.query(Equipamento)
            .options(joinedload(Equipamento.tipo))
            .filter(Equipamento.id == equipamento_id)
            .first()
        )

    def criar(self, nome: str, tipo_id: int):
        equipamento = Equipamento(nome=nome, tipo_id=tipo_id)
        self.db.add(equipamento)
        self.db.commit()
        self.db.refresh(equipamento)
        return equipamento

    def atualizar(self, equipamento: Equipamento):
        self.db.commit()
        self.db.refresh(equipamento)
        return equipamento

    def remover(self, equipamento: Equipamento):
        self.db.delete(equipamento)
        self.db.commit()
