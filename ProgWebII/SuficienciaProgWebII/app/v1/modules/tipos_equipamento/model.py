from sqlalchemy import Column, Integer, String
from sqlalchemy.orm import relationship
from app.v1.core.database import Base

class TipoEquipamento(Base):
    __tablename__ = "tipos_equipamento"

    id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    nome = Column(String(80), nullable=False)

    equipamentos = relationship("Equipamento", back_populates="tipo")
