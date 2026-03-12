

public class Main {

    public static void main(String[] args) {
        // Plano de Testes

        //[X] CASO 1 - Testar método de inclusão de dados na lista
        //Descrição: Criar uma lista. Adicionar os dados 5, 10, 15 e 20.
        //Saída esperada: toString() deve resultar em "5,10,15,20"

        ListaEstatica array1 = new ListaEstatica();
        array1.inserir(5);
        array1.inserir(10);
        array1.inserir(15);
        array1.inserir(20);

        System.out.println("Caso 1: " + array1.toString());

        System.out.println("---------------");

        //[X] CASO 2 - Testar método de obtenção de tamanho da lista
        //Descrição: Criar uma lista. Adicionar os dados 5, 10, 15 e 20.
        //Saída esperada: getTamanho() deve resultar em 4

        ListaEstatica array2 = new ListaEstatica();
        array2.inserir(5);
        array2.inserir(10);
        array2.inserir(15);
        array2.inserir(20);

        System.out.println("Caso 2: " + array2.getTamanho());

        System.out.println("---------------");

        //[X] CASO 3 - Testar método buscar() com elemento existente
        //Descrição: Criar uma lista. Adicionar os dados 5, 10, 15 e 20, nesta ordem.
        //Saída esperada: buscar(15) deve resultar em 2

        ListaEstatica array3 = new ListaEstatica();
        array3.inserir(5);
        array3.inserir(10);
        array3.inserir(15);
        array3.inserir(20);

        System.out.println("Caso 3: " + array3.buscar(15));

        System.out.println("---------------");

        //[X] CASO 4 - Testar método buscar() com elemento inexistente
        //Descrição: Criar uma lista. Adicionar os dados 5, 10, 15 e 20.
        //Saída esperada: buscar(30) deve resultar em -1

        ListaEstatica array4 = new ListaEstatica();
        array4.inserir(5);
        array4.inserir(10);
        array4.inserir(15);
        array4.inserir(20);

        System.out.println("Caso 4: " + array4.buscar(0));

        System.out.println("---------------");

        //[X] CASO 5 - Testar método retirar()
        //Descrição: Criar uma lista. Adicionar os dados 5, 10, 15 e 20, nesta ordem. Em seguida, retirar o elemento 10 – retirar(10).
        //Saída esperada: toString() deve resultar em "5 15 20"
        //                getTamanho() deve resultar em 3

        ListaEstatica array5 = new ListaEstatica();
        array5.inserir(5);
        array5.inserir(10);
        array5.inserir(15);
        array5.inserir(20);

        array5.retirar(15);
        System.out.println("Caso 5 (toString): " + array5.toString());
        System.out.println("Caso 5 (Tamanho): " + array5.getTamanho());

        System.out.println("---------------");

        //[X] CASO 6 - Testar inclusão que provoque redimensionamento
        //Descrição: Criar uma lista. Adicionar 15 números na lista (de 1 à 15).
        //Saída esperada: toString() deve resultar em "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15"
        //                getTamanho() deve resultar em 15

        ListaEstatica array6 = new ListaEstatica();
        for(int i = 0; i < 15; i++){
            array6.inserir(i+1);
        }

        System.out.println("Caso 6 (toString): " + array6.toString());
        System.out.println("Caso 6 (Tamanho): " + array6.getTamanho());

        System.out.println("---------------");

        //[X] CASO 7 - Testar método obterElemento()
        //Descrição: Criar uma lista. Adicionar os dados 5, 10, 15 e 20.
        //Saída esperada: obterElemento(3) deve resultar em 20

        ListaEstatica array7 = new ListaEstatica();
        array7.inserir(5);
        array7.inserir(10);
        array7.inserir(15);
        array7.inserir(20);

        System.out.println("Caso 7: " + array7.obterElemento(3));

        System.out.println("---------------");

        //[X] CASO 8 - Testar lançamento de exceção no método obterElemento()
        //Descrição: Criar uma lista. Adicionar os dados 5, 10, 15 e 20.
        //Saída esperada: obterElemento(5) deve lançar IndexOutOfBoundsException

        ListaEstatica array8 = new ListaEstatica();
        array8.inserir(5);
        array8.inserir(10);
        array8.inserir(15);
        array8.inserir(20);

        //System.out.println("Caso 8: " + array8.obterElemento(5));

        //Exception in thread "main" java.lang.IndexOutOfBoundsException: �ndice inv�lido: 5
        //	at ListaEstatica.obterElemento(ListaEstatica.java:106)
        //	at Main.main(Main.java:120)

        System.out.println("---------------");

        //[X] CASO 9 - Certificar que liberar() remove todos os elementos
        //Descrição: Criar uma lista. Adicionar os dados 5, 10, 15 e 20. Invocar o método liberar().
        //Saída esperada: estaVazia() deve resultar em true

        ListaEstatica array9 = new ListaEstatica();
        array9.inserir(5);
        array9.inserir(10);
        array9.inserir(15);
        array9.inserir(20);

        array9.liberar();
        System.out.println("Caso 9: " + array9.estaVazia());
    }


}