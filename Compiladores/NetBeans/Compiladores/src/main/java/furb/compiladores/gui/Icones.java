package furb.compiladores.gui;

import java.awt.Image;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;

final class Icones {

    private static final int TAMANHO = 26;
    private static final String PASTA = "/furb/compiladores/gui/icones/";

    private Icones() {
    }

    static Icon carregar(String nome) {
        URL endereco = Icones.class.getResource(PASTA + nome + ".png");
        if (endereco == null) {
            System.err.println("ícone não encontrado: " + PASTA + nome + ".png");
            return null;
        }
        ImageIcon icone = new ImageIcon(endereco);
        if (icone.getIconWidth() > TAMANHO || icone.getIconHeight() > TAMANHO) {
            return reduzir(icone);
        }
        return icone;
    }

    // reduz mantendo a proporção, para a imagem não deformar nem estourar o botão
    private static Icon reduzir(ImageIcon icone) {
        double escala = Math.min(
                TAMANHO / (double) icone.getIconWidth(),
                TAMANHO / (double) icone.getIconHeight());
        int largura = Math.max(1, (int) Math.round(icone.getIconWidth() * escala));
        int altura = Math.max(1, (int) Math.round(icone.getIconHeight() * escala));
        return new ImageIcon(icone.getImage().getScaledInstance(largura, altura, Image.SCALE_SMOOTH));
    }
}
