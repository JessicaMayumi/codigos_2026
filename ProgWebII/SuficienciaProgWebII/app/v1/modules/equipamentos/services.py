from fastapi import HTTPException, status
from sqlalchemy.orm import Session
from app.v1.modules.equipamentos.repository import EquipamentoRepository
from app.v1.modules.tipos_equipamento.repository import TipoEquipamentoRepository
from app.v1.modules.equipamentos.schemas import EquipamentoCreate, EquipamentoUpdate


class EquipamentoService:
    def __init__(self, db: Session):
        self.repo = EquipamentoRepository(db)
        self.tipo_repo = TipoEquipamentoRepository(db)

    def listar(self):
        return self.repo.listar()

    def buscar_por_id(self, equipamento_id: int):
        equipamento = self.repo.buscar_por_id(equipamento_id)
        if not equipamento:
            raise HTTPException(status.HTTP_404_NOT_FOUND, "Equipamento não encontrado")
        return equipamento

    def criar(self, dados: EquipamentoCreate):
        tipo = self.tipo_repo.buscar_por_id(dados.tipo.id)
        if not tipo:
            raise HTTPException(status.HTTP_400_BAD_REQUEST, "Tipo de equipamento inválido")
        return self.repo.criar(nome=dados.nome, tipo_id=tipo.id)

    def atualizar(self, equipamento_id: int, dados: EquipamentoUpdate):
        equipamento = self.buscar_por_id(equipamento_id)

        if dados.nome is not None:
            equipamento.nome = dados.nome

        if dados.tipo is not None:
            tipo = self.tipo_repo.buscar_por_id(dados.tipo.id)
            if not tipo:
                raise HTTPException(status.HTTP_400_BAD_REQUEST, "Tipo de equipamento inválido")
            equipamento.tipo_id = tipo.id

        return self.repo.atualizar(equipamento)

    def remover(self, equipamento_id: int):
        equipamento = self.buscar_por_id(equipamento_id)
        self.repo.remover(equipamento)
