package services;

import structures.ListaEncadeada;
import structures.NoLista;
import models.Documento;
import models.Indice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

// Percorre um diretorio (e subpastas) e monta o indice invertido a partir dos arquivos .txt. Vai contando arquivos e palavras pra UI mostrar no fim
public class Indexador {
    private int arquivosIndexados;
    private int palavrasProcessadas;

    public Indice indexar(File caminho) throws IOException {
        return indexarEm(caminho, new Indice());
    }

    // Adiciona o conteudo do caminho a um indice ja existente. Os contadores zeram para contar so o que foi feito nesta chamada.
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
        } else if (ehTxt(caminho)) {
            indexarArquivo(caminho, indice);
        } else {
            throw new IllegalArgumentException(
                    "Selecione uma pasta ou um arquivo .txt: " + caminho);
        }
        return indice;
    }

    private static boolean ehTxt(File arquivo) {
        return arquivo.isFile() && arquivo.getName().toLowerCase().endsWith(".txt");
    }

    private void percorrer(File diretorio, Indice indice) throws IOException {
        File[] filhos = diretorio.listFiles();
        if (filhos == null) return;
        for (File filho : filhos) {
            if (filho.isDirectory()) {
                percorrer(filho, indice);
            } else if (ehTxt(filho)) {
                indexarArquivo(filho, indice);
            }
        }
    }

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
