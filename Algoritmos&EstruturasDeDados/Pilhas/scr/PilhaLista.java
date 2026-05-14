public class PilhaLista<T> {
    private No<T> topo;

    public void push(T valor) {
        No<T> novo = new No<>(valor);
        novo.prox = topo;
        topo = novo;
    }

    public T pop() {
        if (estaVazia()) {
            throw new RuntimeException("Pilha vazia");
        }
        T valor = topo.valor;
        topo = topo.prox;
        return valor;
    }

    public T peek() {
        if (estaVazia()) {
            throw new RuntimeException("Pilha vazia");
        }
        return topo.valor;
    }

    public boolean estaVazia() {
        return topo == null;
    }

    public void liberar() {
        topo = null;
    }

    public String toString() {
        String s = "";
        No<T> atual = topo;

        while (atual != null) {
            s += atual.valor;
            if (atual.prox != null) s += ",";
            atual = atual.prox;
        }

        return s;
    }
}