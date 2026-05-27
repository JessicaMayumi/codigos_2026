package br.furb.buscador.servico;

import br.furb.buscador.estruturas.EntradaMapa;
import br.furb.buscador.estruturas.Lista;
import br.furb.buscador.estruturas.No;
import br.furb.buscador.modelo.Documento;
import br.furb.buscador.modelo.Indice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

/**
 * Salva e carrega o {@link Indice} em um arquivo texto simples,
 * permitindo reuso entre execuções sem reindexar os arquivos.
 *
 * <p><b>Formato do arquivo de índice:</b></p>
 * <pre>
 *   INDICE_BUSCADOR_V1
 *   palavra1\tcaminho1\tcaminho2\t...
 *   palavra2\tcaminho1\t...
 * </pre>
 *
 * <p>Cada linha representa uma palavra seguida da lista de caminhos
 * absolutos dos documentos em que ela ocorre, separados por
 * tabulação. O arquivo é lido e escrito em UTF-8.</p>
 */
public class PersistenciaIndice {

    private static final String CABECALHO = "INDICE_BUSCADOR_V1";
    private static final String SEPARADOR = "\t";

    /**
     * Grava o índice no arquivo indicado, criando os diretórios pai
     * se necessário.
     */
    public void salvar(Indice indice, File arquivo) throws IOException {
        File pai = arquivo.getParentFile();
        if (pai != null && !pai.exists()) {
            pai.mkdirs();
        }
        try (BufferedWriter escritor = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(arquivo), StandardCharsets.UTF_8))) {
            escritor.write(CABECALHO);
            escritor.newLine();

            Lista<EntradaMapa<String, Lista<Documento>>> entradas = indice.getMapa().entradas();
            for (No<EntradaMapa<String, Lista<Documento>>> n = entradas.primeiro();
                 n != null;
                 n = n.getProximo()) {
                EntradaMapa<String, Lista<Documento>> entrada = n.getValor();
                StringBuilder linha = new StringBuilder();
                linha.append(entrada.getChave());
                for (No<Documento> d = entrada.getValor().primeiro(); d != null; d = d.getProximo()) {
                    linha.append(SEPARADOR).append(d.getValor().getCaminho());
                }
                escritor.write(linha.toString());
                escritor.newLine();
            }
        }
    }

    /**
     * Lê um índice previamente gravado e devolve um novo {@link Indice}
     * já com todas as palavras e documentos carregados.
     *
     * @throws IOException se o arquivo não tiver o cabeçalho esperado
     *                     ou estiver corrompido
     */
    public Indice carregar(File arquivo) throws IOException {
        Indice indice = new Indice();
        try (BufferedReader leitor = new BufferedReader(
                new InputStreamReader(new FileInputStream(arquivo), StandardCharsets.UTF_8))) {
            String cabecalho = leitor.readLine();
            if (cabecalho == null || !cabecalho.equals(CABECALHO)) {
                throw new IOException("Arquivo de índice inválido ou corrompido: " + arquivo);
            }
            String linha;
            while ((linha = leitor.readLine()) != null) {
                if (linha.isEmpty()) continue;
                String[] partes = linha.split(SEPARADOR, -1);
                if (partes.length < 2) continue;
                String palavra = partes[0];
                for (int i = 1; i < partes.length; i++) {
                    indice.adicionarOcorrencia(palavra, new Documento(partes[i]));
                }
            }
        }
        return indice;
    }
}
