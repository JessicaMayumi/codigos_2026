// [ x ] ListaDupla():construtor da classe. Deve estabelecer que a lista está vazia;
// [ x ] getPrimeiro(): método getter da variável primeiro;
// [ x ] inserir(T): Deve armazenar o dado fornecido como argumento na estrutura de dados;
// [ x ] buscar(T): Deve procurar na lista encadeada se há um nó cujo conteúdo
// seja igual à variável valor (utilizar igualdade de valores). Caso seja localizado,
// deverá retornar este nó (objeto da classe NoListaDupla). Se não for localizado,
// deverá retornar null;

// [ ] retirar(T): Deve retirar um nó da lista que contenha o valor informado como parâmetro para este método;
// [ ] exibirOrdemInversa(): deve exibir o conteúdo armazenado nos nós da lista encadeada de forma que primeiro
//seja exibido o valor do último nó da lista e por último seja exibido o valor do primeiro nó da lista.
// [ ] liberar(): Deverá limpar a estrutura de dados. Ao invés de simplesmente atribuir null para a variável de
// instância primeiro, remova todos os encadeamentos dos nós, isto é, atribua null para a associação proximo e
//anterior em todos os nós da lista;
// [ x ] toString(): deve retornar os valores armazenados na lista, desde o primeiro nó até o último, separando-os por
//vírgula.

public class ListaDupla<T> {
    private NoListaDupla<T> primeiro;

    public ListaDupla() {
        this.primeiro = null;
    }

    public NoListaDupla<T> getPrimeiro(){
        return primeiro;
    }

    public void inserir(T valor){
        NoListaDupla<T> novoNo = new NoListaDupla<T>(valor);
        if(this.primeiro == null){
            primeiro = novoNo;
        }
        else{
            primeiro.setAnterior(novoNo);
            novoNo.setProximo(primeiro);
            primeiro = novoNo;
        }
    }

    public NoListaDupla<T> buscar(T valor){
        NoListaDupla<T> p = primeiro;
        while((p!=null) && (!p.getInfo().equals(valor))){
            if(p.getProximo() == null){
                return null;
            }
            p = p.getProximo();
        }
        return p;
    }

    public boolean estaVazia(){
        return primeiro == null;
    }

    @Override
    public String toString(){
        String string = "";
        if(estaVazia()){
            return "A lista está vazia!";
        }
        NoListaDupla<T> atual = primeiro;
        if(atual.getProximo() == null && atual.getAnterior() == null){
            return "Anterior: sem anterior | Nó: " + atual.getInfo() + " | Próximo: sem próximo";
        }
        else{
            string = "Anterior: sem anterior | Nó: " + atual.getInfo() + " | Próximo: " + (atual.getProximo()).getInfo() + ", ";
            atual = atual.getProximo();
            while(atual.getProximo() != null){
                string += " Anterior: " + atual.getAnterior().getInfo() + " | Nó: " + atual.getInfo() + " | Próximo: " + (atual.getProximo()).getInfo() + ", ";
                atual = atual.getProximo();
            }

            string += " Anterior: " + atual.getAnterior().getInfo() + " | Nó: " + atual.getInfo() + " | Próximo: sem próximo";

            return string;
        }
    }

    public void retirar(T valor){
        System.out.println("Lista atual: " + toString());
        NoListaDupla<T> noRetirar = buscar(valor);
        if (noRetirar == null){
            System.out.println("Nó não encontrado!");
            return;
        }

        if(noRetirar == primeiro){ // caso for o primeiro
            NoListaDupla<T> segundo = noRetirar.getProximo();

            if(segundo != null){
                segundo.setAnterior(null);
            }

            primeiro = segundo;
        } else { // qualquer outro diferente do primeiro
            NoListaDupla<T> anterior = noRetirar.getAnterior();
            NoListaDupla<T> proximo = noRetirar.getProximo();

            if(anterior != null){
                anterior.setProximo(proximo);
            }
            if(proximo != null){
                proximo.setAnterior(anterior);
            }

            noRetirar.setProximo(null);
            noRetirar.setAnterior(null);
            System.out.println("Lista após remoção: " + toString());
        }
    }

    public void liberar(){
        NoListaDupla<T> atual = primeiro;
        while(atual.getProximo() != null){
            NoListaDupla<T> proximo = atual.getProximo();
            atual.setAnterior(null);
            atual.setProximo(null);
            atual = proximo;
        }
        primeiro = null;
    }

    public void exibirOrdemInversa(){
        if(estaVazia()){
            System.out.println("A lista está vazia!");
        }

        NoListaDupla<T> atual = primeiro;
        // lógica: ir até ao último nó e ir percorrendo os anteriores
        while(atual.getProximo() != null){
            atual = atual.getProximo();
        }
        // agora estamos no último, e devemos percorrer até q anterior == null
        while(atual.getAnterior() != null) {
            System.out.println("Nó de info: " + atual.getInfo());
            atual = atual.getAnterior();
        }
        System.out.println("Nó de info: " + atual.getInfo());
    }

}
