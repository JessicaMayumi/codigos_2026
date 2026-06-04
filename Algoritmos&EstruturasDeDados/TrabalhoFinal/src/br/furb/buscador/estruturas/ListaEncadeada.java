package br.furb.buscador.estruturas;

// Lista encadeada simples e generica, como vista na disciplina.
// Guarda uma referencia para o primeiro no e insere sempre no inicio.
public class ListaEncadeada<T> {

    private NoLista<T> primeiro;
    private int quantidade;

    public ListaEncadeada() {
        this.primeiro = null;
        this.quantidade = 0;
    }

    // Insere um novo valor no inicio da lista.
    public void inserir(T info) {
        NoLista<T> novo = new NoLista<>(info);
        novo.setProximo(primeiro);
        primeiro = novo;
        quantidade++;
    }

    // Procura um valor na lista comparando com equals.
    public boolean contem(T valor) {
        NoLista<T> p = primeiro;
        while (p != null) {
            if (p.getInfo().equals(valor)) {
                return true;
            }
            p = p.getProximo();
        }
        return false;
    }

    // Retira a primeira ocorrencia do valor. Retorna true se removeu.
    public boolean retirar(T valor) {
        NoLista<T> p = primeiro;
        NoLista<T> anterior = null;
        while (p != null) {
            if (p.getInfo().equals(valor)) {
                if (anterior == null) {
                    primeiro = p.getProximo();
                } else {
                    anterior.setProximo(p.getProximo());
                }
                quantidade--;
                return true;
            }
            anterior = p;
            p = p.getProximo();
        }
        return false;
    }

    public boolean estaVazia() {
        return primeiro == null;
    }

    public int tamanho() {
        return quantidade;
    }

    public NoLista<T> getPrimeiro() {
        return primeiro;
    }
}
