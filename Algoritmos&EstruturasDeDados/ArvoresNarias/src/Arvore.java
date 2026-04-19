public class Arvore<T> {

    private NoArvore<T> raiz;

    public Arvore() {
        this.raiz = null;
    }

    public void setRaiz(NoArvore<T> raiz) {
        this.raiz = raiz;
    }

    public NoArvore<T> getRaiz() {
        return raiz;
    }

    @Override
    public String toString() {
        return obterRepresentacaoTextual(raiz);
    }

    private String obterRepresentacaoTextual(NoArvore<T> no) {
        if (no == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<");
        sb.append(no.getInfo());

        // percorre os filhos (irmãos via proximo)
        NoArvore<T> filho = no.getPrimeiro();
        while (filho != null) {
            sb.append(obterRepresentacaoTextual(filho));
            filho = filho.getProximo();
        }

        sb.append(">");
        return sb.toString();
    }

    public boolean pertence(T info) {
        return pertence(raiz, info);
    }

    private boolean pertence(NoArvore<T> no, T info) {
        if (no == null) {
            return false;
        }
        if (no.getInfo().equals(info)) {
            return true;
        }
        // verifica filhos
        NoArvore<T> filho = no.getPrimeiro();
        while (filho != null) {
            if (pertence(filho, info)) {
                return true;
            }
            filho = filho.getProximo();
        }
        return false;
    }

    public int contarNos() {
        return contarNos(raiz);
    }

    private int contarNos(NoArvore<T> no) {
        if (no == null) {
            return 0;
        }
        int count = 1; // conta o nó atual
        NoArvore<T> filho = no.getPrimeiro();
        while (filho != null) {
            count += contarNos(filho);
            filho = filho.getProximo();
        }
        return count;
    }
}