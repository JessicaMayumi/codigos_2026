# Buscador de Arquivos

Trabalho de **Algoritmos e Estruturas de Dados** — FURB
Professor Gilvan Justino — 2026/1

Aplicação Java que indexa arquivos de texto de um diretório (recursivamente)
e permite buscar palavras nesses arquivos com tempo médio O(1) por termo,
utilizando um **mapa de dispersão** (tabela hash) implementado do zero.

---

## 1. Como executar

Requisitos: **JDK 11 ou superior** (testado com OpenJDK 21).

### Linux / macOS

```bash
./compilar.sh
./executar.sh             # abre a interface gráfica (padrão)
./executar.sh --console   # abre a interface de console
```

### Windows

```bat
compilar.bat
executar.bat              :: abre a interface gráfica
executar.bat --console    :: abre a interface de console
```

A aplicação detecta automaticamente o ambiente: se houver display
(`GraphicsEnvironment.isHeadless() == false`), abre a GUI Swing;
caso contrário (por exemplo, executado via SSH sem X11), cai
automaticamente para a interface de console — sem precisar de
nenhuma flag adicional.

Em ambas as interfaces, o programa carrega automaticamente o índice
salvo em `indice.dat` (se existir).

### Interface gráfica (Swing)

![GUI inicial](docs/screenshot-gui-inicial.png)
![GUI após busca](docs/screenshot-gui-busca.png)

Componentes da tela:

- **Indexação** — campo de diretório (com botão "Escolher…" abrindo
  um `JFileChooser`) e botão "Indexar" que dispara a indexação em
  uma `SwingWorker` (a UI continua responsiva durante a operação).
- **Busca** — campo de palavras + botão "Buscar". Pressionar `Enter`
  no campo também dispara a busca.
- **Resultados** — área de texto monoespaçada e rolável.
- **Barra de status** — mostra o estado do índice em memória.

### Roteiro de demonstração (console)

```text
1) Opção 1 → informe: arquivos-teste
2) Opção 2 → busque: lista
3) Opção 2 → busque: furb buscador        (AND, achará 1 doc)
4) Opção 2 → busque: estrutura dados      (AND, achará 1 doc)
5) Opção 2 → busque: palavraInexistente   (sem resultado)
6) Opção 3 → estatísticas do índice
7) Opção 0 → sair
8) ./executar.sh --console novamente → o índice é carregado do disco
```

### Roteiro de demonstração (GUI)

```text
1) Clicar em "Escolher…" → selecionar a pasta arquivos-teste
2) Clicar em "Indexar"  → ver estatísticas na área de resultados
3) Digitar "estrutura dados" no campo Palavras e pressionar Enter
4) Digitar "furb buscador" e clicar em Buscar
5) Digitar "palavraInexistente" → mensagem de "nenhum documento encontrado"
6) Fechar a janela e reabrir o programa → o índice é recarregado
   do disco e a barra de status já mostra "57 palavras únicas".
```

---

## 2. Estrutura do projeto

```
buscador-arquivos/
├── compilar.sh / compilar.bat
├── executar.sh / executar.bat
├── README.md
├── docs/
│   ├── diagrama-uml.svg          ← diagrama de classes (entrega)
│   ├── diagrama-uml.png          ← versão raster do diagrama
│   ├── diagrama-uml.puml         ← fonte editável em PlantUML
│   ├── screenshot-gui-inicial.png ← GUI no estado inicial
│   └── screenshot-gui-busca.png   ← GUI após uma busca
├── arquivos-teste/               ← .txt de exemplo para demo
│   ├── dados.txt
│   ├── indexacao.txt
│   ├── ignorar.md                ← deve ser IGNORADO pelo indexador
│   └── subpasta/
│       └── contexto.txt
└── src/br/furb/buscador/
    ├── Main.java
    ├── estruturas/               ← estruturas de dados próprias
    │   ├── No.java
    │   ├── Lista.java
    │   ├── EntradaMapa.java
    │   └── MapaDispersao.java
    ├── modelo/                   ← entidades de domínio
    │   ├── Documento.java
    │   └── Indice.java
    ├── servico/                  ← regras de negócio
    │   ├── ExtratorPalavras.java
    │   ├── Indexador.java
    │   ├── Buscador.java
    │   └── PersistenciaIndice.java
    └── ui/
        ├── ConsoleUI.java         ← interface de texto (menu numerado)
        └── SwingUI.java           ← interface gráfica Swing
```

---

## 3. Arquitetura

### 3.1. Estruturas de dados próprias (pacote `estruturas`)

Nenhuma estrutura nativa do Java (`ArrayList`, `HashMap`, `LinkedList`, etc.)
é utilizada. Tudo foi implementado a partir das aulas:

| Classe              | O que é                                                                |
| ------------------- | ---------------------------------------------------------------------- |
| `No<T>`             | Nó genérico para lista encadeada (valor + referência ao próximo)       |
| `Lista<T>`          | Lista encadeada simples com cabeça, cauda e tamanho                    |
| `EntradaMapa<K,V>`  | Par chave-valor armazenado nos baldes do mapa                          |
| `MapaDispersao<K,V>`| Tabela hash com encadeamento separado e redimensionamento dinâmico     |

**Por que não foi usado `Iterator`?**
A travessia da lista é feita externamente, expondo apenas o nó inicial via
`Lista.primeiro()`. O cliente percorre com um laço `for (No<T> n = lista.primeiro(); n != null; n = n.getProximo())`,
o que torna o uso do tipo `Iterator` de `java.util` desnecessário e
demonstra explicitamente o entendimento sobre listas encadeadas.

**Por que arrays são permitidos?**
A restrição do enunciado é sobre *classes* de estruturas de dados nativas
(`ArrayList`, `HashMap`, …). Arrays são uma construção primitiva da
linguagem Java, não uma classe do pacote `java.util` — equivalem ao
recurso usado em qualquer implementação de hash table à mão.

### 3.2. Mapa de dispersão

- **Tratamento de colisões:** encadeamento separado (cada balde é uma `Lista`).
- **Função de hash:** `hashCode()` da chave + mistura de bits
  (`h ^= h >>> 16`) para suavizar distribuições ruins, seguido de
  `% capacidade`.
- **Fator de carga:** quando ultrapassa **0,75**, o array de baldes é
  dobrado e todas as entradas são reinseridas (rehash).
- **Capacidade inicial:** 16 baldes.

Custos médios (com função de hash bem distribuída):

| Operação    | Custo médio | Pior caso (todas colidem) |
| ----------- | ----------- | ------------------------- |
| `colocar`   | O(1)        | O(n)                      |
| `obter`     | O(1)        | O(n)                      |
| `contemChave` | O(1)      | O(n)                      |

### 3.3. Índice invertido

`Indice` encapsula um `MapaDispersao<String, Lista<Documento>>`. Cada
palavra (já normalizada para minúscula) aponta para a lista de documentos
em que ocorre. A inserção é **idempotente**: mesmo que a palavra apareça
várias vezes no mesmo arquivo, o documento é registrado uma única vez
(graças ao `Lista.contem` no `Indice.adicionarOcorrencia`).

### 3.4. Extração de palavras

Regras do enunciado, todas aplicadas em `ExtratorPalavras.extrair`:

1. Não distingue maiúsculas/minúsculas → `Character.toLowerCase`.
2. Despreza pontuação → qualquer caractere que não seja letra ou dígito
   é separador (varredura caractere a caractere).
3. Descarta tokens compostos apenas por algarismos ou pontos →
   esses tokens têm **zero letras** e são automaticamente rejeitados.
4. Apenas tokens com **3 ou mais letras** entram no índice.

As regras 2, 3 e 4 colapsam em uma única condição implementada no
método `adicionarSeValida`: *"o token tem 3 ou mais caracteres alfabéticos"*.

### 3.5. Persistência

Formato textual simples (UTF-8), uma palavra por linha:

```
INDICE_BUSCADOR_V1
colisão<TAB>/caminho/arquivo1.txt<TAB>/caminho/arquivo2.txt
indexação<TAB>/caminho/arquivo1.txt
...
```

Vantagens dessa decisão sobre serialização binária Java:

- O arquivo é **legível** — fácil inspecionar e depurar.
- Independente de versão do JDK (não usa `Serializable`).
- Robusto: o cabeçalho `INDICE_BUSCADOR_V1` detecta arquivos corrompidos.

### 3.6. Busca com múltiplos termos

`Buscador.buscar(Lista<String> termos)`:

1. Pega a lista de documentos do primeiro termo (cópia).
2. Para cada termo seguinte, faz a **interseção** com a lista atual.
3. Curto-circuita se o resultado ficar vazio em algum passo.

Custo: O(t · d), onde *t* é a quantidade de termos e *d* o tamanho da
maior lista de documentos. As consultas individuais ao mapa custam O(1)
em média.

### 3.7. Camadas e dependências

```
ui      → servico → modelo → estruturas
                 ↘ modelo ↗
```

A camada `estruturas` não conhece nenhuma outra, o que permite reutilizá-la
em outros trabalhos da disciplina.

### 3.8. Duas interfaces sobre o mesmo núcleo

O pacote `ui` oferece duas apresentações que **compartilham todos os
serviços** (`Indexador`, `Buscador`, `PersistenciaIndice`,
`ExtratorPalavras`):

- **`ConsoleUI`** — menu de texto em terminal, ideal para ambientes
  sem display (SSH, contêineres) e para a demonstração rápida.
- **`SwingUI`** — janela gráfica em Swing com campos, botões e
  diálogo de seleção de diretório. A indexação roda em uma
  `SwingWorker` para não travar a thread da UI.

A escolha entre as duas é feita pelo `Main`, que detecta automaticamente
ambiente *headless* via `GraphicsEnvironment.isHeadless()` e respeita
as flags `--gui` / `--console`. **Nenhuma estrutura de dados nativa do
Java é usada na GUI** — apenas componentes Swing puros (botões,
campos de texto e `JTextArea`).

---

## 4. Conformidade com o enunciado

| Requisito do enunciado                                                       | Onde está atendido                       |
| ---------------------------------------------------------------------------- | ---------------------------------------- |
| Diretório raiz definido pelo usuário                                         | `ConsoleUI.opcaoIndexar`                 |
| Indexa todos os `.txt` do diretório **e subdiretórios**                      | `Indexador.percorrer` (recursivo)        |
| Case-insensitive (sem distinção maiúscula/minúscula)                         | `ExtratorPalavras.extrair`               |
| Ignora tokens compostos só por algarismos ou pontos                          | `ExtratorPalavras.adicionarSeValida`     |
| Apenas palavras com 3+ letras                                                | `ExtratorPalavras.adicionarSeValida`     |
| Despreza pontuação                                                           | `ExtratorPalavras.extrair`               |
| Índice via mapa de dispersão                                                 | `MapaDispersao` + `Indice`               |
| Cada palavra → lista de documentos                                           | `Indice` (mapa de String para Lista)     |
| Índice salvo em disco e recarregado na inicialização                         | `PersistenciaIndice` + `ConsoleUI`       |
| Busca acontece exclusivamente em memória                                     | `Buscador.buscar`                        |
| Uma palavra → docs que contêm ela                                            | `Buscador.buscar` (caso de 1 termo)      |
| Várias palavras → docs que contêm **todas** elas (AND)                       | `Buscador.intersecao`                    |
| **Não** usa classes de estruturas nativas do Java                            | Pacote `estruturas` próprio              |
| Reutiliza estruturas implementadas na disciplina                             | `Lista` e `MapaDispersao` próprios       |
| Diagrama UML                                                                 | `docs/diagrama-uml.svg`                  |

---

## 5. Pontos para a entrevista (defesa)

- **"E se duas chaves diferentes caírem no mesmo balde?"**
  → O balde é uma `Lista` de `EntradaMapa`. Percorremos a lista e
  comparamos as chaves com `equals` para distinguir entradas distintas.

- **"E se o índice ficar muito cheio?"**
  → Quando `tamanho / capacidade > 0,75`, `MapaDispersao.redimensionar`
  dobra a capacidade e refaz o hash de todas as entradas.

- **"Como o `Indice` evita registrar o mesmo documento duas vezes?"**
  → Em `Indice.adicionarOcorrencia`, antes de adicionar, chamamos
  `docs.contem(documento)`. `Documento.equals` compara pelos caminhos.

- **"Por que a busca AND usa interseção em vez de buscar tudo de novo?"**
  → A lista de documentos do primeiro termo já restringe o espaço.
  Para os demais termos, basta verificar quais daqueles documentos
  também aparecem na lista do termo seguinte (`Lista.contem`),
  evitando varrer o índice inteiro.

- **"Por que arrays podem, mas `ArrayList` não?"**
  → A restrição do enunciado é sobre *classes* de estruturas de dados
  nativas (`ArrayList`, `HashMap`, …). Arrays são uma construção
  primitiva da linguagem Java, o mesmo recurso que toda implementação
  de hash table à mão usa internamente.

- **"Os acentos são tratados?"**
  → As letras acentuadas são preservadas (são reconhecidas como letra
  por `Character.isLetter`) e mantidas no índice. A consulta precisa
  usar a mesma grafia da palavra original. O enunciado não pede
  normalização de acentos, portanto mantemos o comportamento literal
  exigido. Se desejado, a normalização poderia ser feita aplicando
  `Normalizer.normalize(token, Form.NFD).replaceAll("\\p{M}+", "")`
  tanto na indexação quanto na busca.

---

## 6. Autor(es)

Preencher antes de entregar:

- Aluno(a): _____________________________________________
- RA: __________________________________________________
- (Opcional, dupla) Aluno(a): ____________________________
- (Opcional, dupla) RA: ___________________________________

Entrega: até **09/06/2026**.
Demonstração e entrevista: **10/06/2026**.
