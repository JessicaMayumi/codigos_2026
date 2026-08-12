# RestAPIFurb - Suficiência Programação Web II

API REST em Python + FastAPI para gerenciamento de equipamentos, conforme
enunciado da prova de suficiência.

## Como rodar

```bash
python3 -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
cp .env.example .env
python run.py
```

A API sobe em `http://localhost:8080/RestAPIFurb/`.

Documentação Swagger: `http://localhost:8080/docs`

## Estrutura

Organização por módulo (cada domínio junto: model, schema, repository, service, router):

```
app/v1/
  core/
    config.py          # configurações lidas do .env
    database.py       # config do banco (SQLite, tabelas geradas automaticamente)
    security.py        # JWT e hash de senha
  modules/
    equipamentos/
      model.py         # ORM (SQLAlchemy)
      schemas.py       # Pydantic - validação de entrada/saída
      repository.py    # acesso a dados (DAO)
      services.py       # regras de negócio
      routers.py        # rotas (FastAPI router)
    tipos_equipamento/  # mesma organização
    usuarios/            # mesma organização (registro/login)
  main.py                 # instância do FastAPI, registra os routers
```

## Rotas principais

Leitura é pública, escrita exige token JWT.

- `GET /RestAPIFurb/equipamentos`
- `GET /RestAPIFurb/equipamentos/{id}`
- `POST /RestAPIFurb/equipamentos` **(token)**
- `PUT /RestAPIFurb/equipamentos/{id}` **(token)**
- `DELETE /RestAPIFurb/equipamentos/{id}` **(token)**
- `GET /RestAPIFurb/tipos-equipamento`
- `POST /RestAPIFurb/tipos-equipamento` **(token)**
- `POST /RestAPIFurb/auth/registrar`
- `POST /RestAPIFurb/auth/login` (retorna o token JWT)

## Testando as rotas protegidas

```bash
curl -X POST http://localhost:8080/RestAPIFurb/auth/registrar \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","senha":"1234"}'

curl -X POST http://localhost:8080/RestAPIFurb/auth/login \
  -d "username=admin&password=1234"

curl -X DELETE http://localhost:8080/RestAPIFurb/equipamentos/1 \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```
