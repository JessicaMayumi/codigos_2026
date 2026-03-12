// --- Check List---
// [ X ] Classe: ListaEstatica
//
// [ X ] Atributos:
//- info : int[]
//- tamanho : int
//
//Métodos:
//[ X ] + ListaEstatica()
//[ X ] - redimensionar() : void
//[ X ] + inserir(valor : int) : void
//[ X ] + exibir() : void
//[ X ] + buscar(valor : int) : int
//[ X ] + retirar(valor : int) : void
//[ X ] + liberar() : void
//[ X ] + obterElemento(posicao : int) : int
//[ X ] + estaVazia() : boolean
//[ X ] + getTamanho() : int
//[ X ] + toString() : String

public class ListaEstatica {
    int[] info;
    int tamanho;

    public ListaEstatica(){
        info = new int[10];
        tamanho = 0; // todas as variaveis primitivas ja sao inicializadas com um valor. Ex: int já inicia como 0
    }

    private void redimensionar(){
        int[] old_info = info;
        info = new int [(int)(old_info.length * 1.5)]; // descarta a parte decimal
        for(int i = 0; i < old_info.length; i++){
            info[i] = old_info[i];
            //System.out.println(info);
        }
        //System.out.println("Antigo array: " + old_info.length);
        //System.out.println("Novo array: " + info.length);
    }

    public void inserir(int valor){
        if(estaCheia() == false){
            info[tamanho] = valor;
            tamanho++;
        } else{
            redimensionar(); // garantindo q irá ter espaço no vetor para inserir os dados, mesmo depois de ter finalizado o vetor de tamanho 10
            //System.out.println(info);
            info[tamanho] = valor;
            tamanho++;
        }
    }
    public boolean estaVazia(){
        if(tamanho == 0) return true;
        return false;
    }

    public boolean estaCheia() {
        if(tamanho >= info.length) return true;
        return false;
    }

    public String toString(){
        //if(estaVazia() == true) return "Está Vazia";
        String string = "";
        for(int i = 0; i< tamanho; i++){
            if(i == tamanho - 1){
                string += info[i];
                break;
            }
            string += info[i] + ", ";
        }
        return string;
    }

    public int getTamanho(){
        return tamanho;
    }

    public void exibir(){
        String string = "";
        for(int i = 0; i< tamanho; i++){
            if(i == tamanho - 1){
                string += info[i];
                break;
            }
            string += info[i] + ",";
        }
        System.out.println(string);
    }

    public int buscar(int valor){
        for(int i = 0; i < tamanho; i++){
            if (info[i] == valor) return i;
        }
        return -1;
    }

    public void liberar(){
        info = new int[10];
        tamanho = 0;
        //System.out.println(info.length);
    }

    public int obterElemento(int posicao){
        if (posicao < 0 || posicao >= tamanho) {
            throw new IndexOutOfBoundsException("Índice inválido: " + posicao);
        }
        return info[posicao];
    }

    public void retirar(int valor){
        // o numero q se quer remover existe?
        if(buscar(valor) > -1){ // nao precisa zerar
            int posicao = buscar(valor);
            for(int i = posicao; i < tamanho; i++){
                if (info[i] == valor){
                    for(int j = i; j< tamanho; j++){
                        if(j == (tamanho - 1)){
                            info[j] = 0;
                        } else {
                            info[j] = info[j+1];
                        }
                    }
                    tamanho--;
                    break;
                }
            }
        } else {
            System.out.println("Esse número não existe na lista!");
        }

    }
}
