from pydantic import BaseModel, ConfigDict, Field


class TipoEquipamentoBase(BaseModel):
    nome: str = Field(
        ...,
        min_length=2,
        max_length=80,
        description="Nome do tipo de equipamento.",
        examples=["Computador"],
    )


class TipoEquipamentoCreate(TipoEquipamentoBase):
    model_config = ConfigDict(json_schema_extra={"example": {"nome": "Impressora"}})


class TipoEquipamentoOut(TipoEquipamentoBase):
    model_config = ConfigDict(from_attributes=True)

    id: int = Field(..., description="Identificador gerado pelo banco.", examples=[1])
