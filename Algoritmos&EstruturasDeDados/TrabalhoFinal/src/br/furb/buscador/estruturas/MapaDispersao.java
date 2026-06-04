package br.furb.buscador.estruturas;

// Mapa de dispersao (tabela hash) com chave de busca do tipo texto.
// Trata colisoes por enderecamento separado: cada posicao do vetor
// guarda uma lista encadeada de NoMapa. A capacidade e fixa e definida
// no construtor (de preferencia um numero primo), como visto em aula.
public class MapaDispersao<T> {

    private final ListaEncadeada<NoMapa<T>>[] info;
    private int quantidade;

    @SuppressWarnings("unchecked")
    public MapaDispersao(int tamanho) {
        this.info = new ListaEncadeada[tamanho];
        this.quantidade = 0;
    }

    // Compacta a chave de texto para um indice do vetor.
    // O hashCode do Java pode ser negativo, por isso usamos o valor absoluto.
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

    // Busca o valor associado a chave. Retorna null se nao encontrar.
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

    // Remove a entrada com a chave informada. Retorna true se removeu.
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

    // Fator de carga = quantidade de itens / tamanho do vetor.
    public double calcularFatorCarga() {
        return (double) quantidade / info.length;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public int getCapacidade() {
        return info.length;
    }

    // Devolve todas as chaves do mapa (ordem qualquer).
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

    // Devolve todas as entradas (chave/valor) do mapa, util para salvar em disco.
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
