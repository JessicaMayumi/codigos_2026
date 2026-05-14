public class TestePilhaVetor {
    public static void main(String[] args) {
        teste1();
        teste2();
        teste3();
        teste4();
        teste5();
        teste6();
        teste7();
        teste8();
    }

    // 1) estaVazia() em pilha vazia
    public static void teste1() {
        System.out.println("Teste 1");

        PilhaVetor<Integer> p = new PilhaVetor<>(5);
        System.out.println("Pilha vazia? " + p.estaVazia());

        System.out.println();
    }

    // 2) estaVazia() em pilha não vazia
    public static void teste2() {
        System.out.println("Teste 2");

        PilhaVetor<Integer> p = new PilhaVetor<>(5);
        p.push(10);

        System.out.println("Depois de push(10):");
        System.out.println("Pilha vazia? " + p.estaVazia());

        System.out.println();
    }

    // 3) empilhar e desempilhar corretamente
    public static void teste3() {
        System.out.println("Teste 3");

        PilhaVetor<Integer> p = new PilhaVetor<>(10);

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

    // 4) pilha cheia
    public static void teste4() {
        System.out.println("Teste 4");

        PilhaVetor<Integer> p = new PilhaVetor<>(3);

        try {
            p.push(10);
            p.push(20);
            p.push(30);

            System.out.println("Pilha antes de estourar: " + p);

            p.push(40);
        } catch (RuntimeException e) {
            System.out.println("Excecao: " + e.getMessage());
        }

        System.out.println();
    }

    // 5) pilha vazia no pop
    public static void teste5() {
        System.out.println("Teste 5");

        PilhaVetor<Integer> p = new PilhaVetor<>(5);

        try {
            System.out.println("Tentando dar pop em pilha vazia...");
            int valor = p.pop();
            System.out.println("Valor removido: " + valor);
        } catch (RuntimeException e) {
            System.out.println("Excecao: " + e.getMessage());
        }

        System.out.println();
    }

    // 6) peek()
    public static void teste6() {
        System.out.println("Teste 6");

        PilhaVetor<Integer> p = new PilhaVetor<>(5);

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

    // 7) liberar()
    public static void teste7() {
        System.out.println("Teste 7");

        PilhaVetor<Integer> p = new PilhaVetor<>(5);

        p.push(10);
        p.push(20);
        p.push(30);

        System.out.println("Antes de liberar: " + p);

        p.liberar();

        System.out.println("Depois de liberar:");
        System.out.println("Pilha vazia? " + p.estaVazia());

        System.out.println();
    }

    // 8) concatenar()
    public static void teste8() {
        System.out.println("Teste 8");

        PilhaVetor<Integer> p1 = new PilhaVetor<>(10);
        PilhaVetor<Integer> p2 = new PilhaVetor<>(10);

        p1.push(10);
        p1.push(20);
        p1.push(30);

        p2.push(40);
        p2.push(50);

        System.out.println("Pilha 1 antes: " + p1);
        System.out.println("Pilha 2 antes: " + p2);

        p1.concatenar(p2);

        System.out.println("Pilha 1 depois da concatenacao: " + p1);
        System.out.println("Pilha 2 depois da concatenacao: " + p2);

        System.out.println();
    }
}