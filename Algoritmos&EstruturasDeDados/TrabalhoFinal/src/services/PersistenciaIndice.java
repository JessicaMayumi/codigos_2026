package services;

import structures.ListaEncadeada;
import structures.NoLista;
import structures.NoMapa;
import models.Documento;
import models.Indice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class PersistenciaIndice {
    // Salva/carrega o indice num arquivo de texto pra nao reindexar toda vez.
    // Formato (UTF-8): 1a linha o cabecalho, depois uma palavra por linha seguida dos caminhos dos arquivos, tudo separado por TAB

    private static final String cabecalhoEsperado = "INDICE_BUSCADOR_V1";
    private static final String separador = "\t";

    public void salvar(Indice indice, File arquivo) throws IOException {
        File pai = arquivo.getParentFile();
        if (pai != null && !pai.exists()) {
            pai.mkdirs();
        }
        try (BufferedWriter escritor = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(arquivo), StandardCharsets.UTF_8))) {
            escritor.write(cabecalhoEsperado);
            escritor.newLine();

            ListaEncadeada<NoMapa<ListaEncadeada<Documento>>> entradas = indice.getMapa().entradas();
            for (NoLista<NoMapa<ListaEncadeada<Documento>>> n = entradas.getPrimeiro();
                 n != null;
                 n = n.getProximo()) {
                NoMapa<ListaEncadeada<Documento>> entrada = n.getInfo();
                StringBuilder linha = new StringBuilder();
                linha.append(entrada.getChave());
                for (NoLista<Documento> d = entrada.getValor().getPrimeiro(); d != null; d = d.getProximo()) {
                    linha.append(separador).append(d.getInfo().getCaminho());
                }
                escritor.write(linha.toString());
                escritor.newLine();
            }
        }
    }

    // Lanca IOException se o arquivo nao tiver o cabecalho certo.
    public Indice carregar(File arquivo) throws IOException {
        Indice indice = new Indice();
        try (BufferedReader leitor = new BufferedReader(
                new InputStreamReader(new FileInputStream(arquivo), StandardCharsets.UTF_8))) {
            String cabecalho = leitor.readLine();
            if (cabecalho == null || !cabecalho.equals(cabecalhoEsperado)) {
                throw new IOException("Arquivo de índice inválido ou corrompido: " + arquivo);
            }
            String linha;
            while ((linha = leitor.readLine()) != null) {
                if (linha.isEmpty()) continue;
                String[] partes = linha.split(separador, -1);
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
