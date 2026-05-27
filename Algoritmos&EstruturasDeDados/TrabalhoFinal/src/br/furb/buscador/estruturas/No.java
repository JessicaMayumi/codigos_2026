package br.furb.buscador.estruturas;

/**
 * Nó genérico utilizado pelas listas encadeadas da aplicação.
 * Mantém um valor e a referência para o próximo nó da sequência.
 *
 * @param <T> tipo do valor armazenado no nó
 */
public class No<T> {

    private T valor;
    private No<T> proximo;

    public No(T valor) {
        this.valor = valor;
        this.proximo = null;
    }

    public T getValor() {
        return valor;
    }

    public void setValor(T valor) {
        this.valor = valor;
    }

    public No<T> getProximo() {
        return proximo;
    }

    public void setProximo(No<T> proximo) {
        this.proximo = proximo;
    }
}
