public class PilhaVetor<T> {
    private T[] info;
    private int topo;

    public PilhaVetor(int limite) {
        info = (T[]) new Object[limite];
        topo = -1;
    }

    public void push(T valor) {
        if (topo == info.length - 1) {
            throw new RuntimeException("Pilha cheia");
        }
        topo++;
        info[topo] = valor;
    }

    public T pop() {
        if (estaVazia()) {
            throw new RuntimeException("Pilha vazia");
        }
        T valor = info[topo];
        topo--;
        return valor;
    }

    public T peek() {
        if (estaVazia()) {
            throw new RuntimeException("Pilha vazia");
        }
        return info[topo];
    }

    public boolean estaVazia() {
        return topo == -1;
    }

    public void liberar() {
        topo = -1;
    }

    public String toString() {
        String s = "";
        for (int i = topo; i >= 0; i--) {
            s += info[i];
            if (i != 0) {
                s += ",";
            }
        }
        return s;
    }

    public void concatenar(PilhaVetor<T> p) {
        int quantidadeP = p.topo + 1;

        if ((topo + 1) + quantidadeP > info.length) {
            throw new RuntimeException("Sem espaço");
        }

        for (int i = 0; i <= p.topo; i++) {
            this.push(p.info[i]);
        }
    }
}