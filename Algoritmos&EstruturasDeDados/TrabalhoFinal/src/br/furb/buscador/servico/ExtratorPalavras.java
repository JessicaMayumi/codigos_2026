package br.furb.buscador.servico;

import br.furb.buscador.estruturas.Lista;

/**
 * Serviço utilitário responsável por extrair palavras válidas
 * de um trecho de texto, aplicando as regras definidas no enunciado:
 *
 * <ul>
 *   <li>Não distingue letras maiúsculas e minúsculas (converte para minúsculo)</li>
 *   <li>Despreza pontuação (qualquer caractere que não seja letra ou dígito é separador)</li>
 *   <li>Descarta tokens compostos apenas por algarismos ou pontos</li>
 *   <li>Mantém apenas tokens com 3 ou mais letras</li>
 * </ul>
 *
 * Como essas regras são aplicadas dentro da mesma varredura, todos
 * os filtros se reduzem a uma única condição: "o token tem 3 ou
 * mais letras alfabéticas". Tokens que sejam apenas dígitos
 * têm zero letras e são naturalmente descartados.
 */
public final class ExtratorPalavras {

    private ExtratorPalavras() {
        // classe utilitária
    }

    /**
     * Extrai todas as palavras válidas de um texto.
     */
    public static Lista<String> extrair(String texto) {
        Lista<String> palavras = new Lista<>();
        if (texto == null || texto.isEmpty()) {
            return palavras;
        }

        StringBuilder buffer = new StringBuilder();
        int comprimento = texto.length();

        for (int i = 0; i < comprimento; i++) {
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

    /**
     * Conta o número de letras alfabéticas do token e adiciona
     * à lista somente se houver pelo menos 3.
     *
     * Isso já cobre as três regras de validação:
     *  - tokens só com dígitos têm 0 letras  → descartados;
     *  - tokens com menos de 3 letras        → descartados;
     *  - tokens longos com 3+ letras úteis   → mantidos.
     */
    private static void adicionarSeValida(Lista<String> lista, String token) {
        int letras = 0;
        for (int i = 0; i < token.length(); i++) {
            if (Character.isLetter(token.charAt(i))) {
                letras++;
            }
        }
        if (letras >= 3) {
            lista.adicionar(token);
        }
    }
}
