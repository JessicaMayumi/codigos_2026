public class Main {

    public static void main(String[] args) {

//        1. Verificar se é reconhecida lista vazia
//        Entrada: Apenas construir a lista
//        Saída esperada: estaVazia() = true

        ListaEncadeada<Integer> lista1 = new ListaEncadeada<>();
        System.out.println("Teste 1: " + lista1.estaVazia());
        System.out.println("--------------------------------");

//        2. Verificar se é reconhecida lista não vazia
//        Entrada: Adicionar o número 5 na lista
//        Saída esperada: estaVazia() = false

        ListaEncadeada<Integer> lista2 = new ListaEncadeada<>();
        lista2.inserir(5);
        System.out.println("Teste 2: " + lista2.estaVazia());
        System.out.println("Lista atual: " + lista2.toString());
        System.out.println("--------------------------------");

//        3. Validar inclusão de um número
//        Entrada: Adicionar o número 5 na lista
//        Saída esperada: Obter o primeiro objeto da lista. Conferir que tenha sido retornado nó
//        e o nó contenha 5. Certificar-se que não haja mais nós

        System.out.println("Teste 3:");
        ListaEncadeada<Integer> lista3 = new ListaEncadeada<>();
        lista3.inserir(5);
        System.out.println("Lista atual: " + lista3.toString());
        lista3.inserir(6);
        System.out.println("Lista atual: " + lista3.toString());
        System.out.println("--------------------------------");

//        4. Validar inclusão de 3 números
//        Entrada: Adicionar os números 5, 10, 15 – nesta ordem
//        Saída esperada: Obter os objetos da lista e certificar-se que hajam apenas 3 nós e os
//        valores devem ser 15, 10 e 5 (nesta ordem)

        System.out.println("Teste 4:");
        ListaEncadeada<Integer> lista4 = new ListaEncadeada<>();
        lista4.inserir(5);
        System.out.println("Lista atual: " + lista4.toString());
        lista4.inserir(10);
        System.out.println("Lista atual: " + lista4.toString());
        lista4.inserir(15);
        System.out.println("Lista atual: " + lista4.toString());
        System.out.println("--------------------------------");

//        5. Validar busca de dados na lista na primeira posição
//        Entrada: Adicionar os números 5, 10, 15 e 20 – nesta ordem. Buscar o número 20
//        Saída esperada: Certificar-se que o método buscar() retorne um nó contendo o número 20

        ListaEncadeada<Integer> lista5 = new ListaEncadeada<>();
        lista5.inserir(5);
        System.out.println("Lista atual: " + lista5.toString());
        lista5.inserir(10);
        System.out.println("Lista atual: " + lista5.toString());
        lista5.inserir(15);
        System.out.println("Lista atual: " + lista5.toString());
        lista5.inserir(20);
        System.out.println("Lista atual: " + lista5.toString());

        NoLista<Integer> noEncontrado = lista5.buscar(20);
        System.out.println("Teste 5: " + noEncontrado.getInfo() + " | Próximo: " + (noEncontrado.getProximo()).getInfo());
        System.out.println("--------------------------------");

//        6. Validar busca de dados no meio da lista
//        Entrada: Adicionar os números 5, 10, 15 e 20 – nesta ordem. Buscar o número 15
//        Saída esperada: Certificar-se que o método buscar() retorne um nó contendo o número 15

        ListaEncadeada<Integer> lista6 = new ListaEncadeada<>();
        lista6.inserir(5);
        System.out.println("Lista atual: " + lista6.toString());
        lista6.inserir(10);
        System.out.println("Lista atual: " + lista6.toString());
        lista6.inserir(15);
        System.out.println("Lista atual: " + lista6.toString());
        lista6.inserir(20);
        System.out.println("Lista atual: " + lista6.toString());

        NoLista<Integer> noEncontrado2 = lista6.buscar(15);
        System.out.println("Teste 6: " + noEncontrado2.getInfo() + " | Próximo: " + (noEncontrado2.getProximo()).getInfo());
        System.out.println("--------------------------------");

//        7. Validar busca de dado inexistente
//        Entrada: Adicionar os números 5, 10, 15 e 20 – nesta ordem. Buscar o número 50
//        Saída esperada: buscar() deve resultar null

        ListaEncadeada<Integer> lista7 = new ListaEncadeada<>();
        lista7.inserir(5);
        System.out.println("Lista atual: " + lista7.toString());
        lista7.inserir(10);
        System.out.println("Lista atual: " + lista7.toString());
        lista7.inserir(15);
        System.out.println("Lista atual: " + lista7.toString());
        lista7.inserir(20);
        System.out.println("Lista atual: " + lista7.toString());

        System.out.println("Teste 7: " + lista7.buscar(50));
        System.out.println("--------------------------------");

//        8. Validar exclusão de primeiro elemento da lista
//        Entrada: Adicionar os números 5, 10, 15 e 20 – nesta ordem. Solicitar exclusão de número 20
//        Saída esperada: Após o algoritmo de remoção, navegar na lista e certificar-se que a lista
//        contenha exclusivamente os números 5, 10 e 15
        System.out.println("Teste 8: ");
        ListaEncadeada<Integer> lista8 = new ListaEncadeada<>();
        lista8.inserir(5);
        System.out.println("Lista atual: " + lista8.toString());
        lista8.inserir(10);
        System.out.println("Lista atual: " + lista8.toString());
        lista8.inserir(15);
        System.out.println("Lista atual: " + lista8.toString());
        lista8.inserir(20);
        System.out.println("Lista atual: " + lista8.toString());
        lista8.retirar(20);
        System.out.println("Lista atual: " + lista8.toString());
        System.out.println("--------------------------------");

//        9. Validar exclusão de elemento do meio da lista
//        Entrada: Adicionar os números 5, 10, 15 e 20 – nesta ordem. Solicitar exclusão de número 15
//        Saída esperada: Após o algoritmo de remoção, navegar na lista e certificar-se que a lista
//        contenha exclusivamente os números 5, 10 e 20

        System.out.println("Teste 9: ");
        ListaEncadeada<Integer> lista9 = new ListaEncadeada<>();
        lista9.inserir(5);
        System.out.println("Lista atual: " + lista9.toString());
        lista9.inserir(10);
        System.out.println("Lista atual: " + lista9.toString());
        lista9.inserir(15);
        System.out.println("Lista atual: " + lista9.toString());
        lista9.inserir(20);
        System.out.println("Lista atual: " + lista9.toString());
        lista9.retirar(15);
        System.out.println("Lista atual: " + lista9.toString());
        System.out.println("--------------------------------");

//        10. Validar que obterNo() retorna nó da posição 0
//        Entrada: Criar lista e adicionar os números 5, 10, 15, 20 – nesta ordem
//        Saída esperada: obterNo(0) deve resultar no nó que armazena 20


        ListaEncadeada<Integer> lista10 = new ListaEncadeada<>();
        lista10.inserir(5);
        System.out.println("Lista atual: " + lista10.toString());
        lista10.inserir(10);
        System.out.println("Lista atual: " + lista10.toString());
        lista10.inserir(15);
        System.out.println("Lista atual: " + lista10.toString());
        lista10.inserir(20);
        System.out.println("Lista atual: " + lista10.toString());
        System.out.println("Teste 10: " + lista10.obterNo(0).getInfo());
        System.out.println("--------------------------------");

//        11. Validar que obterNo() retorna nó da última posição
//        Entrada: Criar lista e adicionar os números 5, 10, 15, 20 – nesta ordem
//        Saída esperada: obterNo(3) deve resultar no nó que armazena 5

        ListaEncadeada<Integer> lista11 = new ListaEncadeada<>();
        lista11.inserir(5);
        System.out.println("Lista atual: " + lista11.toString());
        lista11.inserir(10);
        System.out.println("Lista atual: " + lista11.toString());
        lista11.inserir(15);
        System.out.println("Lista atual: " + lista11.toString());
        lista11.inserir(20);
        System.out.println("Lista atual: " + lista11.toString());
        System.out.println("Teste 11: " + lista11.obterNo(3).getInfo());
        System.out.println("--------------------------------");

//        12. Validar que obterNo() recusa tentativa de ler posição inválida de nó
//        Entrada: Criar lista e adicionar os números 5, 10, 15, 20
//        Saída esperada: obterNo(10) deve lançar a exceção IndexOutOfBoundsException

        ListaEncadeada<Integer> lista12 = new ListaEncadeada<>();
        lista12.inserir(5);
        System.out.println("Lista atual: " + lista12.toString());
        lista12.inserir(10);
        System.out.println("Lista atual: " + lista12.toString());
        lista12.inserir(15);
        System.out.println("Lista atual: " + lista12.toString());
        lista12.inserir(20);
        System.out.println("Lista atual: " + lista12.toString());
//        System.out.println("Teste 12: " + lista12.obterNo(10).getInfo());
        System.out.println("--------------------------------");

//        13. Validar método obterComprimento() para lista vazia
//        Entrada: Criar lista vazia
//        Saída esperada: obterComprimento() deve resultar em 0

        ListaEncadeada<Integer> lista13 = new ListaEncadeada<>();
        System.out.println("Teste 13: " + lista13.obterComprimento());

//        14. Validar método obterComprimento() para lista não vazia
//        Entrada: Criar lista e adicionar os números 5, 10, 15, 20
//        Saída esperada: obterComprimento() deve resultar em 4

        ListaEncadeada<Integer> lista14 = new ListaEncadeada<>();
        lista14.inserir(5);
        System.out.println("Lista atual: " + lista14.toString());
        lista14.inserir(10);
        System.out.println("Lista atual: " + lista14.toString());
        lista14.inserir(15);
        System.out.println("Lista atual: " + lista14.toString());
        lista14.inserir(20);
        System.out.println("Lista atual: " + lista14.toString());
        System.out.println("Teste 14: " + lista14.obterComprimento());

    }
}