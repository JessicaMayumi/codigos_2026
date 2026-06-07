package structures;

public class MapaDispersao<T> {
    // Tabela hash com chave de texto. Cada posicao do vetor guarda uma lista encadeada de NoMapa pra tratar colisoes (enderecamento separado).
    private final ListaEncadeada<NoMapa<T>>[] info;
    private int quantidade;

    @SuppressWarnings("unchecked")
    public MapaDispersao(int tamanho) {
        this.info = new ListaEncadeada[tamanho];
        this.quantidade = 0;
    }

    // hashCode do Java pode vir negativo, por isso o Math.abs.
    public int calcularHash(String chave) {
        return Math.abs(chave.hashCode()) % info.length;
    }

    // Insere o par chave/valor. Se a chave ja existe, atualiza o valor.
    public void inserir(String chave, T valor) {
        int indice = calcularHash(chave);
        if (info[indice] == null) {
            info[indice] = new ListaEncadeada<>();
        }
        NoLista<NoMapa<T>> p = info[indice].getPrimeiro();
        while (p != null) {
            if (p.getInfo().getChave().equals(chave)) {
                p.getInfo().setValor(valor);
                return;
            }
            p = p.getProximo();
        }
        info[indice].inserir(new NoMapa<>(chave, valor));
        quantidade++;
    }

    public T buscar(String chave) {
        int indice = calcularHash(chave);
        if (info[indice] == null) {
            return null;
        }
        NoLista<NoMapa<T>> p = info[indice].getPrimeiro();
        while (p != null) {
            if (p.getInfo().getChave().equals(chave)) {
                return p.getInfo().getValor();
            }
            p = p.getProximo();
        }
        return null;
    }

    public boolean remover(String chave) {
        int indice = calcularHash(chave);
        if (info[indice] == null) {
            return false;
        }
        boolean removeu = info[indice].retirar(new NoMapa<>(chave, null));
        if (removeu) {
            quantidade--;
        }
        return removeu;
    }

    public double calcularFatorCarga() {
        return (double) quantidade / info.length;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public int getCapacidade() {
        return info.length;
    }

    public ListaEncadeada<String> chaves() {
        ListaEncadeada<String> resultado = new ListaEncadeada<>();
        for (int i = 0; i < info.length; i++) {
            if (info[i] == null) {
                continue;
            }
            NoLista<NoMapa<T>> p = info[i].getPrimeiro();
            while (p != null) {
                resultado.inserir(p.getInfo().getChave());
                p = p.getProximo();
            }
        }
        return resultado;
    }

    public ListaEncadeada<NoMapa<T>> entradas() {
        ListaEncadeada<NoMapa<T>> resultado = new ListaEncadeada<>();
        for (int i = 0; i < info.length; i++) {
            if (info[i] == null) {
                continue;
            }
            NoLista<NoMapa<T>> p = info[i].getPrimeiro();
            while (p != null) {
                resultado.inserir(p.getInfo());
                p = p.getProximo();
            }
        }
        return resultado;
    }
}
