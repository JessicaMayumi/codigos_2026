from fastapi import APIRouter, Depends, status
from sqlalchemy.orm import Session
from app.v1.core.database import get_db
from app.v1.modules.tipos_equipamento.services import TipoEquipamentoService
from app.v1.modules.tipos_equipamento.schemas import TipoEquipamentoCreate, TipoEquipamentoOut

router = APIRouter(prefix="/tipos-equipamento", tags=["Tipos de Equipamento"])


@router.get("", response_model=list[TipoEquipamentoOut], summary="Lista os tipos de equipamento")
def listar_tipos(db: Session = Depends(get_db)):
    return TipoEquipamentoService(db).listar()


@router.get("/{tipo_id}", response_model=TipoEquipamentoOut, summary="Busca um tipo pelo id",
            responses={404: {"description": "Tipo de equipamento nao encontrado"}})
def buscar_tipo(tipo_id: int, db: Session = Depends(get_db)):
    return TipoEquipamentoService(db).buscar_por_id(tipo_id)


@router.post("", response_model=TipoEquipamentoOut, status_code=status.HTTP_201_CREATED,
             summary="Cadastra um novo tipo de equipamento",
             responses={422: {"description": "Dados invalidos"}})
def criar_tipo(dados: TipoEquipamentoCreate, db: Session = Depends(get_db)):
    return TipoEquipamentoService(db).criar(dados)
