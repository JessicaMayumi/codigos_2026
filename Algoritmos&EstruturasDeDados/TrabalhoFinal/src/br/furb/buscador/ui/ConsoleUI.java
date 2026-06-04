package br.furb.buscador.ui;

import br.furb.buscador.estruturas.ListaEncadeada;
import br.furb.buscador.estruturas.NoLista;
import br.furb.buscador.modelo.Documento;
import br.furb.buscador.modelo.Indice;
import br.furb.buscador.servico.Buscador;
import br.furb.buscador.servico.ExtratorPalavras;
import br.furb.buscador.servico.Indexador;
import br.furb.buscador.servico.PersistenciaIndice;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Interface de linha de comando para o buscador.
 *
 * Oferece um menu com as operações de indexação, busca e
 * estatísticas. Ao iniciar, tenta carregar um índice previamente
 * salvo em disco (arquivo {@code indice.dat} no diretório corrente).
 */
public class ConsoleUI {

    private static final File ARQUIVO_INDICE = new File("indice.dat");

    private final BufferedReader entrada;
    private final PersistenciaIndice persistencia;
    private Indice indice;

    public ConsoleUI() {
        this.entrada = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        this.persistencia = new PersistenciaIndice();
        this.indice = null;
    }

    public void executar() {
        System.out.println("==============================================");
        System.out.println("  Buscador de Arquivos - FURB / AED");
        System.out.println("==============================================");

        carregarIndiceSeExistir();

        boolean continuar = true;
        while (continuar) {
            try {
                exibirMenu();
                String opcao = lerLinha().trim();
                switch (opcao) {
                    case "1":
                        opcaoIndexar();
                        break;
                    case "2":
                        opcaoBuscar();
                        break;
                    case "3":
                        opcaoEstatisticas();
                        break;
                    case "0":
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opção inválida.\n");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage() + "\n");
            }
        }
        System.out.println("Encerrando. Até mais!");
    }

    private void exibirMenu() {
        System.out.println("------------------------------------------------");
        System.out.println("1 - Indexar diretório");
        System.out.println("2 - Buscar palavras");
        System.out.println("3 - Estatísticas do índice");
        System.out.println("0 - Sair");
        System.out.print("Opção: ");
    }

    private void carregarIndiceSeExistir() {
        if (!ARQUIVO_INDICE.exists()) {
            System.out.println("Nenhum índice encontrado em disco. Use a opção 1 para indexar.\n");
            return;
        }
        try {
            indice = persistencia.carregar(ARQUIVO_INDICE);
            System.out.println("Índice carregado do disco: "
                    + indice.totalPalavras() + " palavras únicas.\n");
        } catch (Exception e) {
            System.out.println("Falha ao carregar o índice: " + e.getMessage() + "\n");
            indice = null;
        }
    }

    private void opcaoIndexar() throws Exception {
        System.out.print("Caminho do diretório raiz: ");
        String caminho = lerLinha().trim();
        File dir = new File(caminho);

        Indexador indexador = new Indexador();
        long inicio = System.currentTimeMillis();
        Indice novo = indexador.indexar(dir);
        long fim = System.currentTimeMillis();

        persistencia.salvar(novo, ARQUIVO_INDICE);
        this.indice = novo;

        System.out.println();
        System.out.println("Indexação concluída em " + (fim - inicio) + " ms.");
        System.out.println("Arquivos lidos        : " + indexador.getArquivosIndexados());
        System.out.println("Ocorrências processadas: " + indexador.getPalavrasProcessadas());
        System.out.println("Palavras únicas        : " + novo.totalPalavras());
        System.out.println("Índice salvo em        : " + ARQUIVO_INDICE.getAbsolutePath() + "\n");
    }

    private void opcaoBuscar() throws Exception {
        if (indice == null) {
            System.out.println("Nenhum índice disponível. Indexe um diretório antes.\n");
            return;
        }
        System.out.print("Palavras a buscar (separadas por espaço): ");
        String linha = lerLinha().trim();
        if (linha.isEmpty()) {
            System.out.println("Nenhuma palavra informada.\n");
            return;
        }

        // reaproveita o extrator para normalizar os termos da busca,
        // garantindo consistência com a forma como foram indexados
        ListaEncadeada<String> termos = ExtratorPalavras.extrair(linha);
        if (termos.estaVazia()) {
            System.out.println("Nenhum termo válido na consulta (mínimo 3 letras por termo).\n");
            return;
        }

        Buscador buscador = new Buscador(indice);
        long inicio = System.nanoTime();
        ListaEncadeada<Documento> resultado = buscador.buscar(termos);
        long fim = System.nanoTime();

        System.out.println();
        System.out.println("Termos buscados:");
        for (NoLista<String> n = termos.getPrimeiro(); n != null; n = n.getProximo()) {
            System.out.println("  - " + n.getInfo());
        }
        System.out.println();

        if (resultado.estaVazia()) {
            System.out.println("Nenhum documento contém todos os termos informados.");
        } else {
            System.out.println(resultado.tamanho() + " documento(s) encontrado(s):");
            int i = 1;
            for (NoLista<Documento> n = resultado.getPrimeiro(); n != null; n = n.getProximo()) {
                System.out.println("  " + (i++) + ". " + n.getInfo().getCaminho());
            }
        }
        System.out.printf("Busca executada em %.3f ms%n%n", (fim - inicio) / 1_000_000.0);
    }

    private void opcaoEstatisticas() {
        if (indice == null) {
            System.out.println("Nenhum índice carregado.\n");
            return;
        }
        System.out.println("Palavras únicas indexadas: " + indice.totalPalavras());
        System.out.println("Capacidade interna do mapa: " + indice.getMapa().getCapacidade());
        System.out.printf("Fator de carga do mapa: %.2f%n", indice.getMapa().calcularFatorCarga());
        if (ARQUIVO_INDICE.exists()) {
            System.out.println("Arquivo do índice: " + ARQUIVO_INDICE.getAbsolutePath()
                    + " (" + ARQUIVO_INDICE.length() + " bytes)");
        } else {
            System.out.println("Arquivo do índice ainda não foi salvo em disco.");
        }
        System.out.println();
    }

    private String lerLinha() throws java.io.IOException {
        String linha = entrada.readLine();
        return linha == null ? "" : linha;
    }
}
