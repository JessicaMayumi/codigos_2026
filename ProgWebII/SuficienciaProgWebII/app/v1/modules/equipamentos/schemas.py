from typing import Optional
from pydantic import BaseModel, ConfigDict, Field
from app.v1.modules.tipos_equipamento.schemas import TipoEquipamentoOut


class TipoRefIn(BaseModel):
    id: int


class EquipamentoCreate(BaseModel):
    nome: str = Field(..., min_length=2, max_length=120)
    tipo: TipoRefIn


class EquipamentoUpdate(BaseModel):
    nome: Optional[str] = Field(None, min_length=2, max_length=120)
    tipo: Optional[TipoRefIn] = None


class EquipamentoOut(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    id: int
    nome: str
    tipo: TipoEquipamentoOut


class EquipamentoListaOut(BaseModel):
    equipamentos: list[EquipamentoOut]
