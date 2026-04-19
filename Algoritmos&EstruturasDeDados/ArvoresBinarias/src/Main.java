//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
void main() {
    // TESTE 1
    ArvoreBinaria<Integer> arvore1 = new ArvoreBinaria<Integer>();
    System.out.println("Caso 1: " + arvore1.estaVazia());

    // TESTE 2
    ArvoreBinaria<Integer> arvore2 = new ArvoreBinaria<Integer>();
    NoArvoreBinaria<Integer> no = new NoArvoreBinaria<Integer>(5);
    arvore2.setRaiz(no);
    System.out.println("Caso 2: " + arvore2.estaVazia());

    // TESTE 3
    ArvoreBinaria<Integer> arvore3 = new ArvoreBinaria<Integer>();
    NoArvoreBinaria<Integer> no6 = new NoArvoreBinaria<Integer>(6);
    NoArvoreBinaria<Integer> no5 = new NoArvoreBinaria<Integer>(5);
    NoArvoreBinaria<Integer> no4 = new NoArvoreBinaria<Integer>(4);

    NoArvoreBinaria<Integer> no3 = new NoArvoreBinaria<Integer>(3, no5, no6);

    NoArvoreBinaria<Integer> no2 = new NoArvoreBinaria<Integer>(2, null, no4);

    NoArvoreBinaria<Integer> no1 = new NoArvoreBinaria<Integer>(1, no2, no3);

    arvore3.setRaiz(no1);

    System.out.println("Caso 3: " + arvore3.toString());

    // TESTE 4
    System.out.println("Caso 4: " + arvore3.pertence(1));

    // TESTE 5
    System.out.println("Caso 5: " + arvore3.pertence(3));

    // TESTE 6
    System.out.println("Caso 6: " + arvore3.pertence(6));

    // TESTE 7
    System.out.println("Caso 7: " + arvore3.pertence(10));

    // TESTE 8
    System.out.println("Caso 7: " + arvore3.contarNos());
}

