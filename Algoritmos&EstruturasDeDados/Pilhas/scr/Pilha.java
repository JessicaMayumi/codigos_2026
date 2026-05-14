public interface Pilha<T> {

    void push(T info) throws PilhaCheiaException;

    T pop() throws PilhaVaziaException;

    T peek() throws PilhaVaziaException;

    boolean estaVazia();

    void liberar();
}