package br.furb.buscador.modelo;

import br.furb.buscador.estruturas.Lista;
import br.furb.buscador.estruturas.MapaDispersao;

/**
 * Índice invertido construído sobre um {@link MapaDispersao}.
 *
 * Cada palavra (chave) é associada a uma lista encadeada de documentos
 * em que essa palavra ocorre, formando o índice invertido clássico
 * usado por sistemas de busca textual.
 *
 * A inclusão de um documento na lista de ocorrências é idempotente:
 * mesmo que a palavra apareça várias vezes no mesmo arquivo, o
 * documento é gravado uma única vez.
 */
public class Indice {

    private final MapaDispersao<String, Lista<Documento>> mapa;

    public Indice() {
        this.mapa = new MapaDispersao<>();
    }

    /**
     * Registra que a palavra informada ocorre no documento informado.
     * Se a palavra ainda não estava no índice, cria a lista de documentos.
     */
    public void adicionarOcorrencia(String palavra, Documento documento) {
        Lista<Documento> docs = mapa.obter(palavra);
        if (docs == null) {
            docs = new Lista<>();
            mapa.colocar(palavra, docs);
        }
        if (!docs.contem(documento)) {
            docs.adicionar(documento);
        }
    }

    /**
     * Retorna a lista de documentos em que a palavra ocorre.
     * Retorna lista vazia (nunca {@code null}) quando a palavra
     * não está indexada.
     */
    public Lista<Documento> documentosCom(String palavra) {
        Lista<Documento> docs = mapa.obter(palavra);
        return docs != null ? docs : new Lista<>();
    }

    public int totalPalavras() {
        return mapa.tamanho();
    }

    public MapaDispersao<String, Lista<Documento>> getMapa() {
        return mapa;
    }
}
