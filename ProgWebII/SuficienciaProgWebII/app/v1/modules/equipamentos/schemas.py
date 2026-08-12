from typing import Optional
from pydantic import BaseModel, ConfigDict, Field
from app.v1.modules.tipos_equipamento.schemas import TipoEquipamentoOut


class TipoRefIn(BaseModel):
    # o nome, se vier no corpo, é ignorado: a fonte da verdade é a tabela de tipos
    id: int = Field(..., description="Id de um tipo já cadastrado.", examples=[3])


class EquipamentoCreate(BaseModel):
    model_config = ConfigDict(
        json_schema_extra={"example": {"nome": "Imp HP", "tipo": {"id": 3, "nome": "Impressora"}}}
    )

    nome: str = Field(..., min_length=2, max_length=120, description="Nome do equipamento.", examples=["Imp HP"])
    tipo: TipoRefIn = Field(..., description="Tipo ao qual o equipamento pertence.")


class EquipamentoUpdate(BaseModel):
    model_config = ConfigDict(
        json_schema_extra={"example": {"nome": "Imp HP", "tipo": {"id": 3, "nome": "Impressora"}}}
    )

    nome: Optional[str] = Field(None, min_length=2, max_length=120, description="Omita para manter o valor atual.")
    tipo: Optional[TipoRefIn] = Field(None, description="Omita para manter o valor atual.")


class EquipamentoOut(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    id: int = Field(..., description="Id gerado pelo banco.", examples=[4])
    nome: str = Field(..., description="Nome do equipamento.", examples=["Imp HP"])
    tipo: TipoEquipamentoOut = Field(..., description="Tipo do equipamento.")


class EquipamentoListaOut(BaseModel):
    equipamentos: list[EquipamentoOut] = Field(..., description="Equipamentos cadastrados.")
