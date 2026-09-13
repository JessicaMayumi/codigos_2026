# Exercício 4 — Proteção contra ataque de força bruta

A API original permitia tentativas de login ilimitadas e instantâneas em `POST /login/`,
o que possibilitava testar milhares de senhas até acertar. Foram implementadas
**três** das técnicas propostas.

## Técnicas implementadas

### 1. Bloqueio temporário de contas

Após **3 tentativas falhas consecutivas**, a conta fica bloqueada por **5 minutos**.

- As falhas são contadas na tabela `tentativas_login` (colunas `falhas` e `bloqueado_ate`),
  gravada no mesmo SQLite — assim o bloqueio **sobrevive a um restart do servidor**,
  o que não aconteceria se o contador ficasse só em memória.
- Enquanto o bloqueio estiver ativo, a senha nem é verificada: mesmo a senha correta
  recebe `423 Locked`. Isso é o que impede o atacante de simplesmente continuar tentando.
- O bloqueio expira sozinho: na primeira requisição após os 5 minutos o contador é zerado.
- Um login bem-sucedido zera o contador de falhas.

### 2. Atraso progressivo

Cada falha aumenta o tempo de resposta exponencialmente: **2s, 4s, 8s, 16s...**,
com teto de 30s (`calcular_atraso`).

- O atraso usa `await asyncio.sleep()` em uma rota `async`, e não `time.sleep()`.
  Com `time.sleep()` a thread ficaria travada e um atacante conseguiria derrubar
  o serviço só abrindo várias conexões — a proteção viraria um vetor de DoS.
- O expoente usado é o **maior** entre as falhas do usuário e as falhas do IP, então
  quem varre vários usuários diferentes também sofre o atraso crescente.

### 3. Registro de tentativas por IP

Falhas são contadas por endereço IP numa janela deslizante de 5 minutos.
Ao atingir **10 falhas**, o IP é bloqueado por **15 minutos** (`429 Too Many Requests`).

- Cobre o caso que o bloqueio por conta não cobre: o atacante que testa uma senha
  comum contra muitos usuários diferentes (*password spraying*), onde nenhuma conta
  individual chega a 3 falhas.
- A checagem de IP é a **primeira** coisa da rota, antes de qualquer consulta ao banco,
  para que um IP já bloqueado não consuma recursos.
- Fica em memória (`_falhas_por_ip`), protegida por um `threading.Lock` porque o uvicorn
  atende requisições em threads diferentes.

## Como executar

```bash
pip install fastapi sqlalchemy uvicorn
python servico.py
```

## Códigos de resposta do `/login/`

| Código | Situação |
|--------|----------|
| `200 OK` | Login realizado com sucesso |
| `401 Unauthorized` | Credenciais inválidas (após o atraso progressivo) |
| `423 Locked` | Conta bloqueada por excesso de falhas |
| `429 Too Many Requests` | IP bloqueado por excesso de falhas |

## Teste realizado

Resultado obtido com o servidor rodando:

```
>>> login correto (baseline)
  200 {"message":"Login realizado com sucesso!"} (0.0s)

>>> 4 tentativas com senha errada
  tentativa 1: HTTP 401 em 2.0s -> Credenciais inválidas
  tentativa 2: HTTP 401 em 4.0s -> Credenciais inválidas
  tentativa 3: HTTP 423 em 8.0s -> Conta bloqueada por 5 minutos após 3 tentativas falhas.
  tentativa 4: HTTP 423 em 0.0s -> Tente novamente em 292 segundos.

>>> agora com a senha CERTA, conta segue bloqueada
  HTTP 423 -> Conta temporariamente bloqueada. Tente novamente em 292 segundos.

>>> falhas contra usuários diferentes até estourar o limite do IP
  ... HTTP 401 (atraso crescendo até o teto de 30s)
  HTTP 429 -> Muitas tentativas a partir deste IP. Tente novamente em 870 segundos.
```

Confirma as três técnicas: o atraso dobrando a cada falha, o bloqueio da conta
recusando até a senha correta, e o bloqueio do IP após 10 falhas na janela.

## Observação

As senhas continuam armazenadas em texto puro, como no código original — isso está
fora do escopo deste exercício, mas numa aplicação real seria necessário guardá-las
com um hash lento (bcrypt/argon2), já que o vazamento do banco tornaria a proteção
contra força bruta irrelevante.
