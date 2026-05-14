public class TestePilhaLista {
    public static void main(String[] args) {
        teste1();
        teste2();
        teste3();
        teste4();
        teste5();
    }

    // 1) estaVazia() em pilha vazia
    public static void teste1() {
        System.out.println("Teste 1");

        PilhaLista<Integer> p = new PilhaLista<>();
        System.out.println("Pilha vazia? " + p.estaVazia());

        System.out.println();
    }

    // 2) estaVazia() em pilha não vazia
    public static void teste2() {
        System.out.println("Teste 2");

        PilhaLista<Integer> p = new PilhaLista<>();
        p.push(10);

        System.out.println("Depois de push(10):");
        System.out.println("Pilha vazia? " + p.estaVazia());

        System.out.println();
    }

    // 3) empilhar e desempilhar corretamente
    public static void teste3() {
        System.out.println("Teste 3");

        PilhaLista<Integer> p = new PilhaLista<>();

        p.push(10);
        p.push(20);
        p.push(30);

        System.out.println("Pilha: " + p);

        int a = p.pop();
        int b = p.pop();
        int c = p.pop();

        System.out.println("Pop 1: " + a);
        System.out.println("Pop 2: " + b);
        System.out.println("Pop 3: " + c);

        System.out.println("Pilha vazia? " + p.estaVazia());

        System.out.println();
    }

    // 4) peek()
    public static void teste4() {
        System.out.println("Teste 4");

        PilhaLista<Integer> p = new PilhaLista<>();

        p.push(10);
        p.push(20);
        p.push(30);

        System.out.println("Pilha: " + p);

        int topo = p.peek();
        System.out.println("Peek (topo): " + topo);

        int removido = p.pop();
        System.out.println("Pop: " + removido);

        System.out.println("Pilha agora: " + p);

        System.out.println();
    }

    // 5) liberar()
    public static void teste5() {
        System.out.println("Teste 5");

        PilhaLista<Integer> p = new PilhaLista<>();

        p.push(10);
        p.push(20);
        p.push(30);

        System.out.println("Antes de liberar: " + p);

        p.liberar();

        System.out.println("Depois de liberar:");
        System.out.println("Pilha vazia? " + p.estaVazia());

        System.out.println();
    }
}