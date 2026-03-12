// --- Check List---
// [ X ] Classe: ListaEstatica<T>
//
// [ X ] Atributos:
//- info : Object[]
//- tamanho : int
//
//Métodos:
//[ X ] + ListaEstatica()
//[ X ] - redimensionar() : void
//[ X ] + inserir(valor : T) : void
//[ X ] + exibir() : void
//[ X ] + buscar(valor : T) : int            | Como fazer ele buscar por objetos distintos, mas com os mesmos valores
//[ X ] + retirar(valor : T) : void
//[ X ] + liberar() : void
//[ X ] + obterElemento(posicao : int) : int    | Não consegui fazer ele trocar de object para T
//[ X ] + estaVazia() : boolean
//[ X ] + getTamanho() : int
//[ X ] + toString() : String
//[ X ] + inverter(): void

public class ListaEstatica<T> { // identificador diamante <>
    Object[] info;
    int tamanho;

    public ListaEstatica(){
        info = new Object[10];
        tamanho = 0;
    }

    private void redimensionar(){
        Object[] old_info = info;
        info = new Object [(int)(old_info.length * 1.5)];
        for(int i = 0; i < old_info.length; i++){
            info[i] = old_info[i];
        }
        //System.out.println("Antigo array: " + old_info.length);
        //System.out.println("Novo array: " + info.length);
    }

    public void inserir(T valor){
        if(estaCheia() == false){
            info[tamanho] = valor;
            tamanho++;
        } else{
            redimensionar();
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
        // testar com info.lenght

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

    public int buscar(T valor){
        for(int i = 0; i < tamanho; i++){
            if (info[i].equals(valor)) return i;
        }
        return -1;
    }

    public void liberar(){
        info = new Object[10];
        tamanho = 0;
        //System.out.println(info.length);
    }

    public Object obterElemento(int posicao){
        if (posicao < 0 || posicao >= tamanho) {
            throw new IndexOutOfBoundsException("Índice inválido: " + posicao);
        }
        return info[posicao];
    }

    public void inverter() {
        // trocar primeiro com ultimo, segundo com penultimo...
        for (int i = 0; i < tamanho / 2; i++) {
            Object temp = info[i];
            info[i] = info[tamanho - 1 - i];
            info[tamanho - 1 - i] = temp;
        }
        //exibir();
    }

    public void retirar(T valor){
        if(buscar(valor) > -1){
            int posicao = buscar(valor);
            for(int i = posicao; i < tamanho; i++){
                if (info[i].equals(valor)){
                    for(int j = i; j< tamanho; j++){
                        if(j == (tamanho - 1)){
                            info[j] = null;
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
        exibir();
    }
}
