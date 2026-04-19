public class NoArvore<T> {

    private T info;
    private NoArvore<T> primeiro; // primeiro filho
    private NoArvore<T> proximo; // próximo irmão

    public NoArvore(T info) {
        this.info = info;
        this.primeiro = null;
        this.proximo = null;
    }

    public T getInfo() {
        return info;
    }

    public void setInfo(T info) {
        this.info = info;
    }

    public NoArvore<T> getPrimeiro() {
        return primeiro;
    }

    public void setPrimeiro(NoArvore<T> primeiro) {
        this.primeiro = primeiro;
    }

    public NoArvore<T> getProximo() {
        return proximo;
    }

    public void setProximo(NoArvore<T> proximo) {
        this.proximo = proximo;
    }

    public void inserirFilho(NoArvore<T> sa) {
        if (primeiro == null) {
            primeiro = sa;
        } else {
            NoArvore<T> atual = primeiro;
            while (atual.getProximo() != null) {
                atual = atual.getProximo();
            }
            atual.setProximo(sa);
        }
    }
}