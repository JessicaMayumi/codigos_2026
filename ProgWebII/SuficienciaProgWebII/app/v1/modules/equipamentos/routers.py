from fastapi import APIRouter, Depends, status
from sqlalchemy.orm import Session
from app.v1.core.database import get_db
from app.v1.core.security import get_current_username
from app.v1.modules.equipamentos.services import EquipamentoService
from app.v1.modules.equipamentos.schemas import (
    EquipamentoCreate,
    EquipamentoUpdate,
    EquipamentoOut,
    EquipamentoListaOut,
)

router = APIRouter(prefix="/equipamentos", tags=["Equipamentos"])


@router.get("", response_model=EquipamentoListaOut, summary="Lista todos os equipamentos")
def listar_equipamentos(db: Session = Depends(get_db)):
    equipamentos = EquipamentoService(db).listar()
    return {"equipamentos": equipamentos}


@router.get("/{equipamento_id}", response_model=EquipamentoOut, summary="Busca um equipamento pelo id",
            responses={404: {"description": "Equipamento não encontrado"}})
def buscar_equipamento(equipamento_id: int, db: Session = Depends(get_db)):
    return EquipamentoService(db).buscar_por_id(equipamento_id)


@router.post("", response_model=EquipamentoOut, status_code=status.HTTP_201_CREATED,
             summary="Cadastra um novo equipamento (requer autenticação)",
             responses={400: {"description": "Tipo de equipamento inválido"},
                        401: {"description": "Token ausente, inválido ou expirado"},
                        422: {"description": "Dados inválidos"}})
def criar_equipamento(dados: EquipamentoCreate, db: Session = Depends(get_db),
                      username: str = Depends(get_current_username)):
    return EquipamentoService(db).criar(dados)


@router.put("/{equipamento_id}", response_model=EquipamentoOut,
            summary="Atualiza um equipamento (requer autenticação)",
            description="Aceita atualização parcial: envie somente os campos que deseja alterar.",
            responses={400: {"description": "Tipo de equipamento inválido"},
                       401: {"description": "Token ausente, inválido ou expirado"},
                       404: {"description": "Equipamento não encontrado"}})
def atualizar_equipamento(equipamento_id: int, dados: EquipamentoUpdate, db: Session = Depends(get_db),
                          username: str = Depends(get_current_username)):
    return EquipamentoService(db).atualizar(equipamento_id, dados)


@router.delete("/{equipamento_id}", status_code=status.HTTP_200_OK,
               summary="Remove um equipamento (requer autenticação)",
               description="Rota protegida: envie o token JWT no header `Authorization: Bearer <token>`.",
               responses={401: {"description": "Token ausente, inválido ou expirado"},
                          404: {"description": "Equipamento não encontrado"}})
def remover_equipamento(equipamento_id: int, db: Session = Depends(get_db),
                        username: str = Depends(get_current_username)):
    EquipamentoService(db).remover(equipamento_id)
    return {"success": {"text": "equipamento removido"}}
