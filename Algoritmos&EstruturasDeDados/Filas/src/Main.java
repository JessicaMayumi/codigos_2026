public class Main {
    public static void main(String[] args) {
        System.out.println("\nTestes 1:");

        // CASO 1
        System.out.println("Caso 1:");
        FilaVetor<Integer> f1 = new FilaVetor<>(5);
        System.out.println(f1.estaVazia()); // true

        // CASO 2
        System.out.println("\nCaso 2:");
        FilaVetor<Integer> f2 = new FilaVetor<>(5);
        f2.inserir(10);
        System.out.println(f2.estaVazia()); // false

        // CASO 3
        System.out.println("\nCaso 3:");
        FilaVetor<Integer> f3 = new FilaVetor<>(10);
        f3.inserir(10);
        f3.inserir(20);
        f3.inserir(30);

        System.out.println(f3.retirar()); // 10
        System.out.println(f3.retirar()); // 20
        System.out.println(f3.retirar()); // 30
        System.out.println(f3.estaVazia()); // true

        // CASO 4
        System.out.println("\nCaso 4:");
        try {
            FilaVetor<Integer> f4 = new FilaVetor<>(3);
            f4.inserir(10);
            f4.inserir(20);
            f4.inserir(30);
            f4.inserir(40); // deve lançar exceção
        } catch (FilaCheiaException e) {
            System.out.println("Exceção capturada: " + e.getMessage());
        }

        // CASO 5
        System.out.println("\nCaso 5:");
        try {
            FilaVetor<Integer> f5 = new FilaVetor<>(5);
            f5.retirar(); // deve lançar exceção
        } catch (FilaVaziaException e) {
            System.out.println("Exceção capturada: " + e.getMessage());
        }

        // CASO 6
        System.out.println("\nCaso 6:");
        FilaVetor<Integer> f6 = new FilaVetor<>(5);
        f6.inserir(10);
        f6.inserir(20);
        f6.inserir(30);

        System.out.println(f6.peek());   // 10
        System.out.println(f6.retirar()); // 10

        // CASO 7
        System.out.println("\nCaso 7:");
        FilaVetor<Integer> f7 = new FilaVetor<>(5);
        f7.inserir(10);
        f7.inserir(20);
        f7.inserir(30);

        f7.liberar();
        System.out.println(f7.estaVazia()); // true

        // CASO 8
        System.out.println("\nCaso 8:");
        FilaVetor<Integer> fa = new FilaVetor<>(5);
        fa.inserir(10);
        fa.inserir(20);
        fa.inserir(30);

        FilaVetor<Integer> fb = new FilaVetor<>(3);
        fb.inserir(40);
        fb.inserir(50);

        FilaVetor<Integer> fc = fa.criarFilaConcatenada(fb);

        System.out.println(fc.toString()); // 10, 20, 30, 40, 50
        System.out.println(fa.toString()); // 10, 20, 30
        System.out.println(fb.toString()); // 40, 50

        System.out.println("Capacidade fc: " + fc.getLimite()); // 5

        System.out.println("\nTestes 2:");

        // Caso 1
        System.out.println("\nCaso 1:");
        Fila<Integer> fila1 = new FilaLista<>();
        System.out.println(fila1.estaVazia()); // true

        // Caso 2
        System.out.println("\nCaso 2:");
        Fila<Integer> fila2 = new FilaLista<>();
        fila2.inserir(10);
        System.out.println(fila2.estaVazia()); // false

        // Caso 3
        System.out.println("\nCaso 3:");
        Fila<Integer> fila3 = new FilaLista<>();

        fila3.inserir(10);
        fila3.inserir(20);
        fila3.inserir(30);

        System.out.println(fila3.retirar()); // 10
        System.out.println(fila3.retirar()); // 20
        System.out.println(fila3.retirar()); // 30
        System.out.println(fila3.estaVazia()); // true


        // Caso 4
        System.out.println("\nCaso 4:");
        Fila<Integer> fila4 = new FilaLista<>();

        fila4.inserir(10);
        fila4.inserir(20);
        fila4.inserir(30);

        System.out.println(fila4.peek()); // 10
        System.out.println(fila4.retirar()); // 10


        // Caso 5
        System.out.println("\nCaso 5:");
        Fila<Integer> fila5 = new FilaLista<>();
        fila5.inserir(10);
        fila5.inserir(20);
        fila5.inserir(30);

        fila5.liberar();
        System.out.println(fila5.estaVazia()); // true

    }
}