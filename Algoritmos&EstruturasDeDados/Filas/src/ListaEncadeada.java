public class ListaEncadeada<T> {
    private NoLista<T> primeiro;
    private NoLista<T> ultimo;

    public ListaEncadeada() {
        this.primeiro = null;
        this.ultimo = null;
    }

    public boolean estaVazia() {
        return primeiro == null;
    }

    public void inserirNoFinal(T valor) {
        NoLista<T> novo = new NoLista<>(valor);

        if (estaVazia()) {
            primeiro = novo;
            ultimo = novo;
        } else {
            ultimo.setProximo(novo);
            ultimo = novo;
        }
    }

    public T retirarDoInicio() {
        if (estaVazia()) {
            throw new RuntimeException("Lista vazia");
        }

        T valor = primeiro.getInfo();
        primeiro = primeiro.getProximo();

        if (primeiro == null) {
            ultimo = null;
        }

        return valor;
    }

    public T getPrimeiro() {
        if (estaVazia()) {
            throw new RuntimeException("Lista vazia");
        }
        return primeiro.getInfo();
    }

    public void liberar() {
        primeiro = null;
        ultimo = null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        NoLista<T> atual = primeiro;

        while (atual != null) {
            sb.append(atual.getInfo());
            if (atual.getProximo() != null) {
                sb.append(", ");
            }
            atual = atual.getProximo();
        }

        return sb.toString();
    }
}