from pydantic import BaseModel, ConfigDict, Field


class UsuarioCreate(BaseModel):
    model_config = ConfigDict(json_schema_extra={"example": {"username": "admin", "senha": "1234"}})

    username: str = Field(..., min_length=3, max_length=60, description="Precisa ser único.", examples=["admin"])
    senha: str = Field(..., min_length=4, max_length=100, description="Vira hash bcrypt antes de gravar.")


class UsuarioOut(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    id: int = Field(..., description="Id gerado pelo banco.", examples=[1])
    username: str = Field(..., description="Nome de usuário.", examples=["admin"])


class TokenOut(BaseModel):
    access_token: str = Field(..., description="Envie no header `Authorization: Bearer <token>`.")
    token_type: str = Field("bearer", description="Tipo do token, sempre `bearer`.", examples=["bearer"])
