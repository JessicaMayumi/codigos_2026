"""Popula o banco com dados de exemplo do enunciado. Rode: python seed.py"""

from app.v1.core.database import Base, SessionLocal, engine
from app.v1.core.security import hash_password
from app.v1.modules.tipos_equipamento.model import TipoEquipamento
from app.v1.modules.equipamentos.model import Equipamento
from app.v1.modules.usuarios.model import Usuario

Base.metadata.create_all(bind=engine)
db = SessionLocal()

if db.query(TipoEquipamento).count() == 0:
    db.add_all([
        TipoEquipamento(nome="Computador"),
        TipoEquipamento(nome="audiovisual"),
        TipoEquipamento(nome="Impressora"),
    ])
    db.commit()
    print("tipos criados")

if db.query(Equipamento).count() == 0:
    db.add_all([
        Equipamento(nome="Notebook Dell", tipo_id=1),
        Equipamento(nome="Projetor Epson", tipo_id=2),
        Equipamento(nome="Notebook Lenovo", tipo_id=1),
    ])
    db.commit()
    print("equipamentos criados")

if not db.query(Usuario).filter(Usuario.username == "admin").first():
    db.add(Usuario(username="admin", senha_hash=hash_password("1234")))
    db.commit()
    print("usuario admin criado (senha: 1234)")

db.close()
print("pronto")
