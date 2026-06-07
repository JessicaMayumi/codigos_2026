package structures;

// guarda o par chave/valor. A identidade e por valor, usando apenas a chave (chave de busca)
public class NoMapa<T> {
    private final String chave;
    private T valor;

    public NoMapa(String chave, T valor) {
        this.chave = chave;
        this.valor = valor;
    }

    public String getChave() {
        return chave;
    }

    public T getValor() {
        return valor;
    }

    public void setValor(T valor) {
        this.valor = valor;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        NoMapa<?> outro = (NoMapa<?>) obj;
        return chave.equals(outro.chave);
    }

    @Override
    public int hashCode() {
        return chave.hashCode();
    }
}
