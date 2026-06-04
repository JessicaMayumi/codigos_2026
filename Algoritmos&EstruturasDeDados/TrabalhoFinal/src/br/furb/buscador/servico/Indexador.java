package br.furb.buscador.servico;

import br.furb.buscador.estruturas.ListaEncadeada;
import br.furb.buscador.estruturas.NoLista;
import br.furb.buscador.modelo.Documento;
import br.furb.buscador.modelo.Indice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Indexador responsável por percorrer recursivamente um diretório
 * e construir o {@link Indice} invertido a partir de todos os
 * arquivos com extensão {@code .txt} encontrados.
 *
 * Mantém estatísticas simples (quantos arquivos foram lidos e
 * quantas ocorrências de palavras foram processadas) para que a UI
 * possa exibir feedback ao usuário ao final da indexação.
 */
public class Indexador {

    private int arquivosIndexados;
    private int palavrasProcessadas;

    public Indice indexar(File caminho) throws IOException {
        return indexarEm(caminho, new Indice());
    }

    /**
     * Adiciona o conteúdo do caminho informado ao índice existente,
     * permitindo acumular múltiplas operações de indexação no mesmo
     * {@link Indice}. Os contadores são reiniciados para refletir
     * apenas o que foi processado nesta chamada.
     */
    public Indice indexarEm(File caminho, Indice indice) throws IOException {
        if (caminho == null || !caminho.exists()) {
            throw new IllegalArgumentException("Caminho inválido: " + caminho);
        }
        if (indice == null) {
            throw new IllegalArgumentException("Índice não pode ser nulo.");
        }

        arquivosIndexados = 0;
        palavrasProcessadas = 0;
        if (caminho.isDirectory()) {
            percorrer(caminho, indice);
        } else if (caminho.isFile() && caminho.getName().toLowerCase().endsWith(".txt")) {
            indexarArquivo(caminho, indice);
        } else {
            throw new IllegalArgumentException(
                    "Selecione uma pasta ou um arquivo .txt: " + caminho);
        }
        return indice;
    }

    /**
     * Percorre recursivamente o diretório procurando arquivos .txt.
     */
    private void percorrer(File diretorio, Indice indice) throws IOException {
        File[] filhos = diretorio.listFiles();
        if (filhos == null) return;
        for (File filho : filhos) {
            if (filho.isDirectory()) {
                percorrer(filho, indice);
            } else if (filho.isFile() && filho.getName().toLowerCase().endsWith(".txt")) {
                indexarArquivo(filho, indice);
            }
        }
    }

    /**
     * Lê o arquivo linha a linha (em UTF-8) e adiciona cada palavra
     * válida ao índice associada ao documento atual.
     */
    private void indexarArquivo(File arquivo, Indice indice) throws IOException {
        Documento documento = new Documento(arquivo.getAbsolutePath());
        try (BufferedReader leitor = new BufferedReader(
                new InputStreamReader(new FileInputStream(arquivo), StandardCharsets.UTF_8))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                ListaEncadeada<String> palavras = ExtratorPalavras.extrair(linha);
                for (NoLista<String> n = palavras.getPrimeiro(); n != null; n = n.getProximo()) {
                    indice.adicionarOcorrencia(n.getInfo(), documento);
                    palavrasProcessadas++;
                }
            }
        }
        arquivosIndexados++;
    }

    public int getArquivosIndexados() {
        return arquivosIndexados;
    }

    public int getPalavrasProcessadas() {
        return palavrasProcessadas;
    }
}
