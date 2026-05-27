package br.furb.buscador.estruturas;

/**
 * Par chave-valor armazenado em cada balde do mapa de dispersão.
 *
 * @param <K> tipo da chave (imutável)
 * @param <V> tipo do valor associado
 */
public class EntradaMapa<K, V> {

    private final K chave;
    private V valor;

    public EntradaMapa(K chave, V valor) {
        this.chave = chave;
        this.valor = valor;
    }

    public K getChave() {
        return chave;
    }

    public V getValor() {
        return valor;
    }

    public void setValor(V valor) {
        this.valor = valor;
    }
}
