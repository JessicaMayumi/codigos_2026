# Buscador de Arquivos

Trabalho de Algoritmos e Estruturas de Dados - FURB.

Programa em Java que indexa arquivos `.txt` de uma pasta (e subpastas) e
deixa buscar palavras neles. O indice usa um mapa de dispersao feito a mao,
sem usar as estruturas prontas do Java.

## Como rodar

Pelo IntelliJ: abrir a pasta e rodar o `Main`.

Pelo terminal:

Linux / Mac:

```bash
./run.sh             # abre a tela
./run.sh --console   # abre no modo texto
```

Windows:

```bat
run.bat              :: abre a tela
run.bat --console    :: abre no modo texto
```

Sem argumentos abre a janela (Swing); com `--console` vai pro menu de texto.
O indice fica salvo em `indice.dat`, entao na proxima vez ele ja carrega
sem precisar indexar de novo.

## Pastas

- `src/structures` - lista encadeada e mapa de dispersao (proprios)
- `src/models` - Documento e Indice
- `src/services` - indexar, buscar, extrair palavras e salvar/carregar
- `src/ui` - tela (Swing) e menu de texto (console)
- `docs/Diagrama.png` - diagrama de classes
