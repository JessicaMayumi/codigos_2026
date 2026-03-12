public class ListaEncadeada<T> {
    NoLista<T> primeiro;

    public void ListaEncadeada(NoLista<T> primeiro){
        this.primeiro = null;
    }

    public NoLista<T> getPrimeiro(){
        return primeiro;
    }

    public void inserir(T valor){

        NoLista<T> novoNo = new NoLista<>(valor);

        novoNo.setProximo(primeiro);
        primeiro = novoNo;

//        NoLista<T> novoNo = new NoLista<>(valor);
//        if(estaVazia()){
//            primeiro = novoNo;
//        } else {
//            NoLista<T> atual = primeiro;
//            while(atual.getProximo() != null){
//                atual = atual.getProximo();
//            }
//            atual.setProximo(novoNo);
//        }
    }

    public boolean estaVazia(){
        if (primeiro == null) return true;
        return false;
    }

    public int obterComprimento(){
        int tamanho = 0;
        if (!estaVazia()) {
            NoLista<T> atual = primeiro;
            while (atual.getProximo() != null) {
                atual = atual.getProximo();
                tamanho++;
            }
            tamanho++;
        }
        return tamanho;
    }

    @Override
    public String toString(){
        NoLista<T> atual = primeiro;
        String string = "";
        while(atual.getProximo() != null){
            string += "Nó: " + atual.getInfo() + " | Próximo: " + (atual.getProximo()).getInfo() + ", ";
            atual = atual.getProximo();
        }
        string += "Nó: " + atual.getInfo() + " | Próximo: sem próximo";

        return string;
    }

    public NoLista<T> obterNo(int idx){
        NoLista<T> atual = primeiro;
        int contador = 0;

        while (atual != null) {
            if (contador == idx) {
                return atual;
            }

            contador++;
            atual = atual.getProximo();
        }

        throw new IndexOutOfBoundsException("Nó no encontrado: " + idx );
    }

    public NoLista<T> buscar(T valor){
        NoLista<T> atual = primeiro;

        while (atual != null) {
            if (atual.getInfo().equals(valor)) { // atual.equals(valor) está errado pois busca se o nó é igual ao valor, e não compara o valor dentro do nó
                return atual;
            }
            atual = atual.getProximo();
        }

        return null;
    }

    public void retirar(T valor){
        NoLista<T> atual = primeiro;
        NoLista<T> anterior = null;

        while (atual != null) { // while((p!=null) && (!p.getInfo().equals(valor))
            if (atual.getInfo().equals(valor)) {

                if (anterior == null) {
                    primeiro = atual.getProximo();
                } else {
                    anterior.setProximo(atual.getProximo());
                }

                break;
            }

            anterior = atual;
            atual = atual.getProximo();
        }
    }
}
