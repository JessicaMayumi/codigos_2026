from sqlalchemy import Column, Integer, String, ForeignKey
from sqlalchemy.orm import relationship
from app.v1.core.database import Base


class Equipamento(Base):
    __tablename__ = "equipamentos"

    id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    nome = Column(String(120), nullable=False)
    tipo_id = Column(Integer, ForeignKey("tipos_equipamento.id"), nullable=False)

    tipo = relationship("TipoEquipamento", back_populates="equipamentos")
