public class MapaDispersao<K, T> {
    private NoMapa<K, T>[] mapa;
    private int quantidade;

    public MapaDispersao(int tamanho) {
        mapa = new NoMapa[tamanho];
        quantidade = 0;
    }

    public int calcularHash(K chave) {

        return Math.abs(chave.hashCode()) % mapa.length;
    }

    public void inserir(K chave, T valor) {

        int indice = calcularHash(chave);

        if (mapa[indice] == null) {
            quantidade++;
        }

        mapa[indice] = new NoMapa<>(chave, valor);
    }

    public void remover(K chave) {
        int indice = calcularHash(chave);

        if (mapa[indice] != null &&
                mapa[indice].getChave().equals(chave)) {

            mapa[indice] = null;
            quantidade--;
        }
    }

    public T buscar(K chave) {
        int indice = calcularHash(chave);

        if (mapa[indice] != null &&
                mapa[indice].getChave().equals(chave)) {

            return mapa[indice].getValor();
        }

        return null;
    }

    public double calcularFatorCarga() {
        return (double) quantidade / mapa.length;
    }
}