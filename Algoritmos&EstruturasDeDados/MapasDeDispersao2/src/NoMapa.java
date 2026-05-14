import java.util.Objects;

public class NoMapa <K,T>{
    private K chave;
    private T valor;

    public NoMapa(K chave, T valor) {
        this.chave = chave;
        this.valor = valor;
    }


    public K getChave() {
        return chave;
    }

    public void setChave(K chave) {
        this.chave = chave;
    }

    public T getValor() {
        return valor;
    }

    public void setValor(T valor) {
        this.valor = valor;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        NoMapa<?, ?> noMapa = (NoMapa<?, ?>) o;
        return Objects.equals(chave, noMapa.chave);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(chave);
    }
}
