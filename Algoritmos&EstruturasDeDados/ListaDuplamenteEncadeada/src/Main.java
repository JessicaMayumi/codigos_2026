//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
//        ListaDupla lista = new ListaDupla();
//        lista.inserir(1);
//        System.out.println(lista.toString());
//        lista.inserir(2);
//        System.out.println(lista.toString());
//        lista.inserir(3);
//        System.out.println(lista.toString());
//
//        System.out.println(lista.buscar(1).getInfo());
//
//        lista.retirar(1);
//        System.out.println(lista.toString());
//
//        lista.retirar(1);
//        System.out.println(lista.toString());
//
//        lista.liberar();
//        System.out.println(lista.toString());


//        1. Inclusão de dados
//        Criar uma lista e inserir 5, 10, 15, 20.
//        Percorrer do primeiro ao último e depois do último
//        ao primeiro para verificar se as ligações proximo e
//        anterior estão corretas.
        System.out.println("Teste 1: ");
        ListaDupla<Integer> lista1 = new ListaDupla<Integer>();
        lista1.inserir(5);
        lista1.inserir(10);
        lista1.inserir(15);
        lista1.inserir(20);
        System.out.println("Início ao fim: ");
        System.out.println(lista1.toString());
        System.out.println();
        System.out.println("Fim ao início: ");
        lista1.exibirOrdemInversa();

//        2. Buscar elemento no início
//        Criar a lista com 5, 10, 15, 20.
//        Executar buscar(20).
        System.out.println("Teste 2: ");
        ListaDupla<Integer> lista2 = new ListaDupla<Integer>();
        lista2.inserir(5);
        lista2.inserir(10);
        lista2.inserir(15);
        lista2.inserir(20);
        System.out.println("Teste 2: " + lista2.buscar(20).getInfo());

//        3. Buscar elemento no meio
//        Criar a lista com 5, 10, 15, 20.
//        Executar buscar(10).
        System.out.println("Teste 3: ");
        ListaDupla<Integer> lista3 = new ListaDupla<Integer>();
        lista3.inserir(5);
        lista3.inserir(10);
        lista3.inserir(15);
        lista3.inserir(10);
        System.out.println("Teste 3: " + lista3.buscar(10).getInfo());

//        4. Remover elemento no início
//        Criar a lista com 5, 10, 15, 20.
//        Executar retirar(20) e percorrer a lista para verificar.
        System.out.println("Teste 4: ");
        ListaDupla<Integer> lista4 = new ListaDupla<Integer>();
        lista4.inserir(5);
        lista4.inserir(10);
        lista4.inserir(15);
        lista4.inserir(20);
        lista4.retirar(20);
        System.out.println(lista4.toString());

//        5. Remover elemento no meio
//        Criar a lista com 5, 10, 15, 20.
//        Executar retirar(10) e percorrer a lista para verificar.
        System.out.println("Teste 5: ");
        ListaDupla<Integer> lista5 = new ListaDupla<Integer>();
        lista5.inserir(5);
        lista5.inserir(10);
        lista5.inserir(15);
        lista5.inserir(20);
        lista5.retirar(10);
        System.out.println(lista5.toString());

//        6. Remover elemento no fim
//        Criar a lista com 5, 10, 15, 20.
//        Executar retirar(5) e percorrer a lista para verificar.
        System.out.println("Teste 6: ");
        ListaDupla<Integer> lista6 = new ListaDupla<Integer>();
        lista6.inserir(5);
        lista6.inserir(10);
        lista6.inserir(15);
        lista6.inserir(20);
        lista6.retirar(5);
        System.out.println(lista6.toString());

//        7. Liberar dados da lista
//        Criar a lista com 5, 10, 15, 20.
//        Buscar os nós 5, 10, 15, 20, depois executar liberar() e verificar se anterior e proximo ficaram null.
        System.out.println("Teste 7: ");
        ListaDupla<Integer> lista7= new ListaDupla<Integer>();
        lista7.inserir(5);
        lista7.inserir(10);
        lista7.inserir(15);
        lista7.inserir(20);
        lista7.liberar();
        System.out.println(lista7.toString());
    }
}