from pydantic import BaseModel, ConfigDict, Field


class UsuarioCreate(BaseModel):
    username: str = Field(..., min_length=3, max_length=60)
    senha: str = Field(..., min_length=4, max_length=100)


class UsuarioOut(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    id: int
    username: str


class TokenOut(BaseModel):
    access_token: str
    token_type: str = "bearer"
