package br.furb.buscador.estruturas;

/**
 * Mapa de dispersão (tabela hash) com tratamento de colisões por
 * encadeamento separado, utilizando a {@link Lista} encadeada
 * desta mesma implementação.
 *
 * O array de baldes cresce dinamicamente quando o fator de carga
 * (tamanho / capacidade) ultrapassa {@link #FATOR_CARGA_MAXIMO},
 * mantendo o custo médio das operações em O(1).
 *
 * Nenhuma estrutura nativa do Java é utilizada (apenas o tipo
 * array, que é uma construção primitiva da linguagem, não uma classe
 * do pacote {@code java.util}).
 *
 * @param <K> tipo da chave
 * @param <V> tipo do valor
 */
public class MapaDispersao<K, V> {

    private static final int CAPACIDADE_INICIAL = 16;
    private static final double FATOR_CARGA_MAXIMO = 0.75;

    private Lista<EntradaMapa<K, V>>[] baldes;
    private int tamanho;

    @SuppressWarnings("unchecked")
    public MapaDispersao() {
        this.baldes = (Lista<EntradaMapa<K, V>>[]) new Lista[CAPACIDADE_INICIAL];
        this.tamanho = 0;
    }

    /**
     * Calcula o índice do balde para uma chave, usando o hashCode
     * da chave com uma mistura de bits para reduzir colisões em
     * hashCodes mal distribuídos.
     */
    private int indiceBalde(K chave, int capacidade) {
        int h = chave.hashCode();
        h ^= (h >>> 16);
        return (h & 0x7FFFFFFF) % capacidade;
    }

    /**
     * Insere ou atualiza o par chave-valor. Se a chave já existe,
     * o valor antigo é substituído.
     */
    public void colocar(K chave, V valor) {
        int idx = indiceBalde(chave, baldes.length);
        if (baldes[idx] == null) {
            baldes[idx] = new Lista<>();
        }
        Lista<EntradaMapa<K, V>> balde = baldes[idx];

        for (No<EntradaMapa<K, V>> n = balde.primeiro(); n != null; n = n.getProximo()) {
            EntradaMapa<K, V> e = n.getValor();
            if (e.getChave().equals(chave)) {
                e.setValor(valor);
                return;
            }
        }

        balde.adicionar(new EntradaMapa<>(chave, valor));
        tamanho++;

        if ((double) tamanho / baldes.length > FATOR_CARGA_MAXIMO) {
            redimensionar();
        }
    }

    /**
     * Retorna o valor associado à chave, ou {@code null} se não houver.
     */
    public V obter(K chave) {
        int idx = indiceBalde(chave, baldes.length);
        if (baldes[idx] == null) {
            return null;
        }
        for (No<EntradaMapa<K, V>> n = baldes[idx].primeiro(); n != null; n = n.getProximo()) {
            EntradaMapa<K, V> e = n.getValor();
            if (e.getChave().equals(chave)) {
                return e.getValor();
            }
        }
        return null;
    }

    public boolean contemChave(K chave) {
        return obter(chave) != null;
    }

    public int tamanho() {
        return tamanho;
    }

    public int capacidade() {
        return baldes.length;
    }

    /**
     * Retorna uma lista com todas as chaves do mapa, em ordem
     * arbitrária (a ordem depende da distribuição de hash).
     */
    public Lista<K> chaves() {
        Lista<K> resultado = new Lista<>();
        for (int i = 0; i < baldes.length; i++) {
            if (baldes[i] == null) continue;
            for (No<EntradaMapa<K, V>> n = baldes[i].primeiro(); n != null; n = n.getProximo()) {
                resultado.adicionar(n.getValor().getChave());
            }
        }
        return resultado;
    }

    /**
     * Retorna uma lista com todas as entradas (chave + valor) do mapa.
     * Útil para persistência em disco.
     */
    public Lista<EntradaMapa<K, V>> entradas() {
        Lista<EntradaMapa<K, V>> resultado = new Lista<>();
        for (int i = 0; i < baldes.length; i++) {
            if (baldes[i] == null) continue;
            for (No<EntradaMapa<K, V>> n = baldes[i].primeiro(); n != null; n = n.getProximo()) {
                resultado.adicionar(n.getValor());
            }
        }
        return resultado;
    }

    /**
     * Dobra a capacidade do array de baldes e reinsere todas as
     * entradas existentes. Operação O(n), executada apenas quando
     * o fator de carga é excedido.
     */
    @SuppressWarnings("unchecked")
    private void redimensionar() {
        Lista<EntradaMapa<K, V>>[] antigos = baldes;
        baldes = (Lista<EntradaMapa<K, V>>[]) new Lista[antigos.length * 2];
        tamanho = 0;
        for (int i = 0; i < antigos.length; i++) {
            if (antigos[i] == null) continue;
            for (No<EntradaMapa<K, V>> n = antigos[i].primeiro(); n != null; n = n.getProximo()) {
                EntradaMapa<K, V> e = n.getValor();
                colocar(e.getChave(), e.getValor());
            }
        }
    }
}
