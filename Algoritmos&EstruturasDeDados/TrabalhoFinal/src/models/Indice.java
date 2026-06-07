package models;

import structures.ListaEncadeada;
import structures.MapaDispersao;

// Indice invertido: cada palavra (chave de busca) aponta para a lista de documentos em que ela aparece.
public class Indice {
    // Numero primo para a capacidade do mapa, ajuda a espalhar as chaves.
    private static final int capacidade = 997;

    private final MapaDispersao<ListaEncadeada<Documento>> mapa;

    public Indice() {
        this.mapa = new MapaDispersao<>(capacidade);
    }

    // Registra que a palavra aparece no documento. Se o documento ja estava na lista daquela palavra, nao adiciona de novo.
    public void adicionarOcorrencia(String palavra, Documento documento) {
        ListaEncadeada<Documento> docs = mapa.buscar(palavra);
        if (docs == null) {
            docs = new ListaEncadeada<>();
            mapa.inserir(palavra, docs);
        }
        if (!docs.contem(documento)) {
            docs.inserir(documento);
        }
    }

    public ListaEncadeada<Documento> documentosCom(String palavra) {
        ListaEncadeada<Documento> docs = mapa.buscar(palavra);
        return docs != null ? docs : new ListaEncadeada<>();
    }

    public int totalPalavras() {
        return mapa.getQuantidade();
    }

    public MapaDispersao<ListaEncadeada<Documento>> getMapa() {
        return mapa;
    }
}
