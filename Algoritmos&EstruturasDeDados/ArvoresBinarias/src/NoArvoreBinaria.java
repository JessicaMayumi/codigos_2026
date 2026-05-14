public class NoArvoreBinaria<T> {
    private NoArvoreBinaria<T> esquerda;
    private NoArvoreBinaria<T> direita;
    private T info;

    public NoArvoreBinaria(T info){
        this.info = info;
        this.direita = null;
        this.esquerda = null;
    }

    public NoArvoreBinaria(T info, NoArvoreBinaria<T> esquerda, NoArvoreBinaria<T> direita){
        this.info = info;
        this.direita = direita;
        this.esquerda = esquerda;
    }

    public NoArvoreBinaria<T> getEsquerda() {
        return esquerda;
    }

    public void setEsquerda(NoArvoreBinaria<T> esquerda) {
        this.esquerda = esquerda;
    }

    public NoArvoreBinaria<T> getDireita() {
        return direita;
    }

    public void setDireita(NoArvoreBinaria<T> direita) {
        this.direita = direita;
    }

    public T getInfo() {
        return info;
    }

    public void setInfo(T info) {
        this.info = info;
    }
}
