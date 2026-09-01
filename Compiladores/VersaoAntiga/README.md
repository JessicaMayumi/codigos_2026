# Compiladores — Trabalho Final, parte 1: interface do compilador

Interface do compilador implementada em **Java (Swing)**, conforme a Figura 1 do enunciado.

## Como gerar e executar

```bash
./build.sh                     # compila e gera dist/interface.jar
java -jar dist/interface.jar   # executa
```

Só é necessário ter o JDK instalado (foi usado o JDK 21; o `build.sh` compila com
`--release 17`, então o `.jar` roda em qualquer Java 17 ou superior). O projeto não
usa nenhuma biblioteca externa e os ícones são desenhados com Java2D, portanto o
`.jar` funciona sozinho, sem arquivos de imagem ao lado.

## Organização

| Arquivo | Responsabilidade |
| --- | --- |
| `src/compilador/Principal.java` | Ponto de entrada; aplica o visual do sistema e abre a janela. |
| `src/compilador/TelaPrincipal.java` | Janela, layout dos componentes, botões, atalhos e ações. |
| `src/compilador/NumeroDeLinhas.java` | Régua com o número das linhas, à esquerda do editor. |
| `src/compilador/ArquivoTexto.java` | Leitura e gravação dos arquivos `.txt`. |
| `src/compilador/Icones.java` | Ícones dos botões, desenhados com Java2D. |

O layout usa `BorderLayout`: a barra de ferramentas fica a oeste (150 x n), a barra de
status ao sul (m x 25) e, no centro, um `JSplitPane` vertical com o editor em cima e a
área para mensagens embaixo.

## Itens do enunciado

| Item | Onde está |
| --- | --- |
| 1. Janela 1500x800, fixa, com minimizar e fechar | `TelaPrincipal` — `setSize` + `setResizable(false)` |
| 2. Barra de ferramentas 150 x n, editor, mensagens e barra de status 25 | `TelaPrincipal.montarBarraFerramentas / montarDivisor / montarBarraStatus` |
| 3. Barra de divisão entre editor e mensagens | `JSplitPane` vertical em `montarDivisor` |
| 4. Número da linha à esquerda, a partir de 1, não editável | `NumeroDeLinhas` (componente só de desenho, usado como `setRowHeaderView`) |
| 5. Editor com as duas barras de rolagem sempre visíveis | `criarRolagem` — políticas `*_SCROLLBAR_ALWAYS` |
| 6. Área para mensagens não editável | `areaMensagens.setEditable(false)` |
| 7. Mensagens com as duas barras de rolagem sempre visíveis | `criarRolagem` |
| 8. Barra de status com pasta e nome do arquivo | `definirArquivoAtual` |
| 9. Oito botões de mesmo tamanho, com ícone, nome e atalho | `adicionarFerramenta` + `registrarAtalho` |
| 10. `novo` limpa editor, mensagens e barra de status | `TelaPrincipal.novo` |
| 11. `abrir` carrega o arquivo, limpa mensagens e atualiza a barra de status | `TelaPrincipal.abrir` |
| 12. `salvar` grava o arquivo (pedindo pasta/nome só quando é novo) | `TelaPrincipal.salvar` |
| 13. `copiar`, `colar` e `recortar` como nos editores convencionais | `editor::copy`, `editor::paste`, `editor::cut` |
| 14. `compilar` mostra a mensagem prevista | `TelaPrincipal.compilar` |
| 15. `equipe` mostra os nomes da equipe | `TelaPrincipal.equipe` |

As teclas de atalho (`ctrl-n`, `ctrl-o`, `ctrl-s`, `ctrl-c`, `ctrl-v`, `ctrl-x`, `F7`, `F1`)
são registradas no `JRootPane` com `WHEN_IN_FOCUSED_WINDOW`, então funcionam com o foco em
qualquer ponto da janela. Quando o foco está no editor, `ctrl-c`, `ctrl-v` e `ctrl-x` são
tratados primeiro pelo próprio editor — o comportamento é o mesmo nos dois caminhos.

## Arquivos texto

Os arquivos são gravados em `.txt` com quebra de linha do Windows (CRLF) e codificação
UTF-8 sem BOM, para abrirem corretamente no Notepad. Na leitura, arquivos em UTF-8 e em
ANSI (windows-1252) são aceitos, e o BOM, se existir, é descartado.

## Antes de entregar

1. **Trocar os nomes da equipe** na constante `EQUIPE`, em
   `src/compilador/TelaPrincipal.java` — hoje só o primeiro nome está preenchido.
2. Rodar `./build.sh` e conferir se o `dist/interface.jar` abre com
   `java -jar dist/interface.jar`.
3. Compactar o projeto completo (código fonte + `.jar`) em um arquivo chamado
   `interface` seguido do número da equipe e postar no AVA, na tarefa
   "parte 1 - interface", até 29/08/2026, 23h.
