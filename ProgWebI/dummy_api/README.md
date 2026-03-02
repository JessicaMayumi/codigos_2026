# Prova de suficiência Prog Web I

by **May**

---

## Aplicação CRUD (JavaScript)

Interface web para gerenciamento de funcionários com operações de **Inserir**, **Listar**, **Alterar** e **Excluir**. Duas versões disponíveis: **Vanilla JS** e **Vue.js**.

### Versões

| Versão   | Arquivo        | URL               | Descrição                              |
|----------|----------------|-------------------|----------------------------------------|
| Nativa   | `index.html`   | `/` ou `/interface/templates/index.html` | JavaScript nativo, arquitetura MVC modular |
| Vue      | `vue.html`     | `/vue` ou `/interface/templates/vue.html` | Vue.js 3, reatividade                  |

### Tecnologias

- **Frontend:** Material Design 3 (m3.material.io) via @material/web, Vue.js 3 na versão Vue
- **Validação:** Zod
- **Persistência local:** LocalStorage (para dados criados offline)
- **Integração:** API REST

### Estrutura do frontend

```
app/interface/
├── configs/settings.js      # URL da API e chave do LocalStorage
├── modules/
│   ├── base.css             # Cores, fontes, tabelas e estilos padrão
│   ├── http.js              # Cliente HTTP (fetch)
│   ├── notifications.js     # Notificações (versão nativa)
│   └── employee/
│       ├── style.css        # Estilos específicos da aplicação nativa
│       ├── style-vue.css    # Estilos específicos da aplicação Vue
│       ├── apiRequests.js   # Chamadas à API REST
│       ├── controller.js    # Lógica de negócio (merge API + LocalStorage)
│       ├── localStorage.js  # Persistência local
│       ├── models.js        # Schemas Zod
│       └── view-vue.js      # Aplicação Vue.js
├── templates/
│   ├── index.html          # Versão Vanilla JS
│   ├── vue.html            # Versão Vue.js
│   └── view.js             # View nativa (modo module)
```

### Como usar

1. Inicie a API em Python (veja seção abaixo).
2. Acesse:
   - **Vanilla JS:** `http://localhost:8000/` ou `http://localhost:8000/interface/templates/index.html`
   - **Vue:** `http://localhost:8000/vue` ou `http://localhost:8000/interface/templates/vue.html`

3. API: padrão `dummy.restapiexample.com`. Para API local, altere `API_BASE_URL` em `app/interface/configs/settings.js`.

---

## API em Python

API REST de funcionários desenvolvida com **FastAPI**, compatível com o formato da [Dummy REST API](https://dummy.restapiexample.com/).

### Tecnologias

- **Framework:** FastAPI
- **Banco:** SQLite (arquivo `employees.db` gerado automaticamente)
- **Servidor:** Uvicorn

### Endpoints

| Método | Endpoint            | Descrição                    |
|--------|---------------------|------------------------------|
| GET    | `/api/v1/employees` | Lista todos os funcionários   |
| GET    | `/api/v1/employee/{id}` | Busca funcionário por ID  |
| POST   | `/api/v1/create`    | Cria funcionário             |
| PUT    | `/api/v1/update/{id}` | Atualiza funcionário       |
| DELETE | `/api/v1/delete/{id}` | Remove funcionário         |

### Como executar

```bash
# Na raiz do projeto
python run.py
```

A API sobe em `http://127.0.0.1:8000`. Documentação interativa:

- Swagger UI: `http://localhost:8000/docs`
- ReDoc: `http://localhost:8000/redoc`
