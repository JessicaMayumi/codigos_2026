from fastapi import HTTPException, status
from sqlalchemy.orm import Session
from app.v1.modules.tipos_equipamento.repository import TipoEquipamentoRepository
from app.v1.modules.tipos_equipamento.schemas import TipoEquipamentoCreate


class TipoEquipamentoService:
    def __init__(self, db: Session):
        self.repo = TipoEquipamentoRepository(db)

    def listar(self):
        return self.repo.listar()

    def buscar_por_id(self, tipo_id: int):
        tipo = self.repo.buscar_por_id(tipo_id)
        if not tipo:
            raise HTTPException(status.HTTP_404_NOT_FOUND, "Tipo de equipamento não encontrado")
        return tipo

    def criar(self, dados: TipoEquipamentoCreate):
        return self.repo.criar(nome=dados.nome)
