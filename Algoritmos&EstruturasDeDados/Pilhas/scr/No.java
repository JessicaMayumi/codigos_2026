class No<T> {
    T valor;
    No<T> prox;

    public No(T valor) {
        this.valor = valor;
        this.prox = null;
    }
}
