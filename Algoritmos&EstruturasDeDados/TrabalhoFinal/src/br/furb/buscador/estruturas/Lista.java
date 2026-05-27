package br.furb.buscador.estruturas;

/**
 * Lista encadeada simples genérica.
 *
 * Mantém referências para o primeiro e o último nó, o que permite
 * inserção no fim em tempo O(1). A travessia da lista é feita
 * externamente a partir do método {@link #primeiro()}, sem utilizar
 * qualquer estrutura nativa do Java (como Iterator de java.util).
 *
 * @param <T> tipo dos elementos da lista
 */
public class Lista<T> {

    private No<T> cabeca;
    private No<T> cauda;
    private int tamanho;

    public Lista() {
        this.cabeca = null;
        this.cauda = null;
        this.tamanho = 0;
    }

    /**
     * Adiciona um valor ao final da lista em O(1).
     */
    public void adicionar(T valor) {
        No<T> novo = new No<>(valor);
        if (cabeca == null) {
            cabeca = novo;
            cauda = novo;
        } else {
            cauda.setProximo(novo);
            cauda = novo;
        }
        tamanho++;
    }

    /**
     * Verifica se a lista contém o valor informado (comparação por equals).
     */
    public boolean contem(T valor) {
        No<T> atual = cabeca;
        while (atual != null) {
            if (atual.getValor().equals(valor)) {
                return true;
            }
            atual = atual.getProximo();
        }
        return false;
    }

    /**
     * Retorna o valor armazenado na posição informada.
     *
     * @throws IndexOutOfBoundsException se o índice for inválido
     */
    public T obter(int indice) {
        if (indice < 0 || indice >= tamanho) {
            throw new IndexOutOfBoundsException("Índice inválido: " + indice);
        }
        No<T> atual = cabeca;
        for (int i = 0; i < indice; i++) {
            atual = atual.getProximo();
        }
        return atual.getValor();
    }

    /**
     * Remove a primeira ocorrência do valor informado.
     *
     * @return true se um elemento foi removido, false caso contrário
     */
    public boolean remover(T valor) {
        if (cabeca == null) {
            return false;
        }
        if (cabeca.getValor().equals(valor)) {
            cabeca = cabeca.getProximo();
            if (cabeca == null) {
                cauda = null;
            }
            tamanho--;
            return true;
        }
        No<T> anterior = cabeca;
        No<T> atual = cabeca.getProximo();
        while (atual != null) {
            if (atual.getValor().equals(valor)) {
                anterior.setProximo(atual.getProximo());
                if (atual == cauda) {
                    cauda = anterior;
                }
                tamanho--;
                return true;
            }
            anterior = atual;
            atual = atual.getProximo();
        }
        return false;
    }

    public boolean estaVazia() {
        return tamanho == 0;
    }

    public int tamanho() {
        return tamanho;
    }

    /**
     * Retorna o primeiro nó da lista. Permite percorrer a lista
     * de forma encadeada por código externo, sem expor a estrutura
     * interna para modificação.
     */
    public No<T> primeiro() {
        return cabeca;
    }

    public void limpar() {
        cabeca = null;
        cauda = null;
        tamanho = 0;
    }
}
