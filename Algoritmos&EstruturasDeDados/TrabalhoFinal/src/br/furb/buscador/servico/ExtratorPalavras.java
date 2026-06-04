package br.furb.buscador.servico;

import br.furb.buscador.estruturas.ListaEncadeada;

// Quebra um texto em palavras validas seguindo as regras do enunciado:
// - nao distingue maiuscula/minuscula (tudo vira minusculo);
// - pontuacao e separador (so letra ou digito faz parte da palavra);
// - so entram palavras com 3 ou mais letras (tokens so de numeros caem fora).
public final class ExtratorPalavras {

    private ExtratorPalavras() {
    }

    public static ListaEncadeada<String> extrair(String texto) {
        ListaEncadeada<String> palavras = new ListaEncadeada<>();
        if (texto == null || texto.isEmpty()) {
            return palavras;
        }

        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                buffer.append(Character.toLowerCase(c));
            } else if (buffer.length() > 0) {
                adicionarSeValida(palavras, buffer.toString());
                buffer.setLength(0);
            }
        }
        if (buffer.length() > 0) {
            adicionarSeValida(palavras, buffer.toString());
        }
        return palavras;
    }

    // Conta quantas letras o token tem e so guarda se tiver pelo menos 3.
    private static void adicionarSeValida(ListaEncadeada<String> lista, String token) {
        int letras = 0;
        for (int i = 0; i < token.length(); i++) {
            if (Character.isLetter(token.charAt(i))) {
                letras++;
            }
        }
        if (letras >= 3) {
            lista.inserir(token);
        }
    }
}
