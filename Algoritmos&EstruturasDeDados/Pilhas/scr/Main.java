public class Main {
    public static void main(String[] args) {

        PilhaVetor<Integer> p = new PilhaVetor<>(10);

        p.push(10);
        p.push(20);
        p.push(30);

        System.out.println(p); // 30,20,10

        System.out.println(p.pop()); // 30
        System.out.println(p.peek()); // 20

        System.out.println(p.estaVazia()); // false

        p.liberar();
        System.out.println(p.estaVazia()); // true
    }
}