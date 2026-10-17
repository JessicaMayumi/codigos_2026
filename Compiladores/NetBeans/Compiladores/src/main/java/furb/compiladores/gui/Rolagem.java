package furb.compiladores.gui;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.plaf.metal.MetalScrollBarUI;

/**
 * Deixa as barras de rolagem sempre visíveis e com o mesmo desenho em qualquer
 * sistema. Só a política não basta: no Windows 11 o visual nativo reduz a barra
 * a um filete quando não há o que rolar, e ela some de vista.
 */
final class Rolagem {

    private Rolagem() {
    }

    static void sempreVisivel(JScrollPane rolagem) {
        rolagem.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        rolagem.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        desenhoClassico(rolagem.getVerticalScrollBar());
        desenhoClassico(rolagem.getHorizontalScrollBar());
    }

    private static void desenhoClassico(JScrollBar barra) {
        try {
            barra.setUI(new MetalScrollBarUI());
        } catch (RuntimeException semMetal) {
            // mantém o desenho do sistema se o visual clássico não estiver disponível
        }
        barra.setOpaque(true);
    }
}
