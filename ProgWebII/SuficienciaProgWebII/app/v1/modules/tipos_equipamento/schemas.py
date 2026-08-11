from pydantic import BaseModel, ConfigDict, Field


class TipoEquipamentoBase(BaseModel):
    nome: str = Field(..., min_length=2, max_length=80)


class TipoEquipamentoCreate(TipoEquipamentoBase):
    pass


class TipoEquipamentoOut(TipoEquipamentoBase):
    model_config = ConfigDict(from_attributes=True)

    id: int
