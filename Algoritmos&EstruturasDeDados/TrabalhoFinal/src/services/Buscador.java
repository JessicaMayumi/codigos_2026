package services;

import structures.ListaEncadeada;
import structures.NoLista;
import models.Documento;
import models.Indice;

// Faz a busca no indice que ja esta em memoria. Com varias palavras, retorna so os documentos que contem TODAS elas (E).
public class Buscador {
    private final Indice indice;

    public Buscador(Indice indice) {
        this.indice = indice;
    }

    public ListaEncadeada<Documento> buscar(ListaEncadeada<String> termos) {
        if (termos == null || termos.estaVazia()) {
            return new ListaEncadeada<>();
        }

        // Comeca com uma copia dos documentos do primeiro termo.
        String primeira = termos.getPrimeiro().getInfo().toLowerCase();
        ListaEncadeada<Documento> resultado = copiar(indice.documentosCom(primeira));

        // Vai cruzando (intersecao) com os documentos dos termos seguintes.
        for (NoLista<String> n = termos.getPrimeiro().getProximo(); n != null; n = n.getProximo()) {
            if (resultado.estaVazia()) {
                break;
            }
            String termo = n.getInfo().toLowerCase();
            ListaEncadeada<Documento> docs = indice.documentosCom(termo);
            resultado = intersecao(resultado, docs);
        }
        return resultado;
    }

    private ListaEncadeada<Documento> copiar(ListaEncadeada<Documento> origem) {
        ListaEncadeada<Documento> copia = new ListaEncadeada<>();
        for (NoLista<Documento> n = origem.getPrimeiro(); n != null; n = n.getProximo()) {
            copia.inserir(n.getInfo());
        }
        return copia;
    }

    private ListaEncadeada<Documento> intersecao(ListaEncadeada<Documento> a, ListaEncadeada<Documento> b) {
        ListaEncadeada<Documento> resultado = new ListaEncadeada<>();
        for (NoLista<Documento> n = a.getPrimeiro(); n != null; n = n.getProximo()) {
            if (b.contem(n.getInfo())) {
                resultado.inserir(n.getInfo());
            }
        }
        return resultado;
    }
}
