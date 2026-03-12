public class NoLista<T> {
    T info;
    NoLista<T> proximo;

    public NoLista(T info) {
        this.info = info;
        this.proximo = null;
    }

    public void setInfo(T info) {
        this.info = info;
    }

    public T getInfo() {
        return info;
    }

    public void setProximo(NoLista<T> proximo){
        this.proximo = proximo;
    }

    public NoLista<T> getProximo() {
        return proximo;
    }
}
