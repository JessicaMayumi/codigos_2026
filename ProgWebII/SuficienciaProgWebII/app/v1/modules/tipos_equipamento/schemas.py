from pydantic import BaseModel, ConfigDict, Field


class TipoEquipamentoCreate(BaseModel):
    model_config = ConfigDict(json_schema_extra={"example": {"nome": "Impressora"}})

    nome: str = Field(..., min_length=2, max_length=80, description="Nome do tipo.", examples=["Computador"])


class TipoEquipamentoOut(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    id: int = Field(..., description="Id gerado pelo banco.", examples=[1])
    nome: str = Field(..., min_length=2, max_length=80, description="Nome do tipo.", examples=["Computador"])
