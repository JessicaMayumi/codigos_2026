public class Main {
    private static Arvore<Integer> criarArvore() {
        // nós folha
        NoArvore<Integer> no5  = new NoArvore<>(5);
        NoArvore<Integer> no6  = new NoArvore<>(6);
        NoArvore<Integer> no7  = new NoArvore<>(7);
        NoArvore<Integer> no8  = new NoArvore<>(8);
        NoArvore<Integer> no9  = new NoArvore<>(9);
        NoArvore<Integer> no10 = new NoArvore<>(10);

        // nível intermediário
        NoArvore<Integer> no2 = new NoArvore<>(2);
        no2.inserirFilho(no5);
        no2.inserirFilho(no6);
        no2.inserirFilho(no7);

        NoArvore<Integer> no3 = new NoArvore<>(3);
        no3.inserirFilho(no8);

        NoArvore<Integer> no4 = new NoArvore<>(4);
        no4.inserirFilho(no9);
        no4.inserirFilho(no10);

        // raiz
        NoArvore<Integer> no1 = new NoArvore<>(1);
        no1.inserirFilho(no2);
        no1.inserirFilho(no3);
        no1.inserirFilho(no4);

        Arvore<Integer> arvore = new Arvore<>();
        arvore.setRaiz(no1);
        System.out.println(arvore.getRaiz().getInfo());
        return arvore;
    }

    public static void main(String[] args) {

        // Caso 1 – representação textual
        Arvore<Integer> arvore = criarArvore();
        String representacao = arvore.toString();
        String esperado = "<1<2<5><6><7>><3<8>><4<9><10>>>";
        System.out.println("=== Caso 1: toString() ===");
        System.out.println("Resultado : " + representacao);
        System.out.println("Esperado  : " + esperado);
        System.out.println("PASSOU    : " + representacao.equals(esperado));
        System.out.println();

        // Caso 2 – pertence() com valor existente (7)
        arvore = criarArvore();
        boolean pertence7 = arvore.pertence(7);
        System.out.println("=== Caso 2: pertence(7) ===");
        System.out.println("Resultado : " + pertence7);
        System.out.println("Esperado  : true");
        System.out.println("PASSOU    : " + (pertence7 == true));
        System.out.println();

        // Caso 3 – pertence() com valor inexistente (55)
        arvore = criarArvore();
        boolean pertence55 = arvore.pertence(55);
        System.out.println("=== Caso 3: pertence(55) ===");
        System.out.println("Resultado : " + pertence55);
        System.out.println("Esperado  : false");
        System.out.println("PASSOU    : " + (pertence55 == false));
        System.out.println();

        // Caso 4 – contarNos()
        arvore = criarArvore();
        int total = arvore.contarNos();
        System.out.println("=== Caso 4: contarNos() ===");
        System.out.println("Resultado : " + total);
        System.out.println("Esperado  : 10");
        System.out.println("PASSOU    : " + (total == 10));
    }
}