package models;

public class Documento {
    private final String caminho;

    public Documento(String caminho) {
        if (caminho == null) {
            throw new IllegalArgumentException("Caminho do documento não pode ser nulo");
        }
        this.caminho = caminho;
    }

    public String getCaminho() {
        return caminho;
    }

    @Override
    public boolean equals(Object obj) {
        // Compara documentos pelo caminho. Assim o indice nao guarda o mesmo arquivo repetido na lista de uma palavra.
        if (this == obj) return true;
        if (!(obj instanceof Documento)) return false;
        Documento outro = (Documento) obj;
        return caminho.equals(outro.caminho);
    }

    @Override
    public int hashCode() {
        return caminho.hashCode();
    }

    @Override
    public String toString() {
        return caminho;
    }
}
