package br.furb.buscador.servico;

import br.furb.buscador.estruturas.Lista;
import br.furb.buscador.estruturas.No;
import br.furb.buscador.modelo.Documento;
import br.furb.buscador.modelo.Indice;

/**
 * Executa buscas sobre um {@link Indice} já carregado em memória.
 *
 * <p>Quando vários termos são informados, é feita a interseção das
 * listas de documentos de cada termo (semântica "E" / AND). Quando
 * apenas um termo é informado, a lista de documentos correspondente
 * é retornada diretamente.</p>
 *
 * <p>O processamento ocorre exclusivamente em memória — o índice
 * já deve ter sido construído ou carregado do disco antes.</p>
 */
public class Buscador {

    private final Indice indice;

    public Buscador(Indice indice) {
        this.indice = indice;
    }

    /**
     * Executa a busca dos termos informados e retorna a lista de
     * documentos que contêm <b>todos</b> os termos.
     */
    public Lista<Documento> buscar(Lista<String> termos) {
        if (termos == null || termos.estaVazia()) {
            return new Lista<>();
        }

        // começa com uma cópia da lista de documentos do primeiro termo
        String primeira = termos.primeiro().getValor().toLowerCase();
        Lista<Documento> resultado = copiar(indice.documentosCom(primeira));

        // intersecta sucessivamente com cada termo seguinte
        for (No<String> n = termos.primeiro().getProximo(); n != null; n = n.getProximo()) {
            if (resultado.estaVazia()) break;
            String termo = n.getValor().toLowerCase();
            Lista<Documento> docs = indice.documentosCom(termo);
            resultado = intersecao(resultado, docs);
        }
        return resultado;
    }

    private Lista<Documento> copiar(Lista<Documento> origem) {
        Lista<Documento> copia = new Lista<>();
        for (No<Documento> n = origem.primeiro(); n != null; n = n.getProximo()) {
            copia.adicionar(n.getValor());
        }
        return copia;
    }

    /**
     * Interseção entre duas listas de documentos (ordem preservada
     * a partir da lista A).
     */
    private Lista<Documento> intersecao(Lista<Documento> a, Lista<Documento> b) {
        Lista<Documento> resultado = new Lista<>();
        for (No<Documento> n = a.primeiro(); n != null; n = n.getProximo()) {
            if (b.contem(n.getValor())) {
                resultado.adicionar(n.getValor());
            }
        }
        return resultado;
    }
}
