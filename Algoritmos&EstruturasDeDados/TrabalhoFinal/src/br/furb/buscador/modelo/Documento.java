package br.furb.buscador.modelo;

/**
 * Representa um documento de texto indexado pela aplicação.
 *
 * Dois documentos são considerados iguais quando possuem o mesmo
 * caminho absoluto. Essa noção de igualdade é usada pelo índice
 * para evitar inserir o mesmo documento duas vezes na lista de
 * ocorrências de uma palavra.
 */
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
