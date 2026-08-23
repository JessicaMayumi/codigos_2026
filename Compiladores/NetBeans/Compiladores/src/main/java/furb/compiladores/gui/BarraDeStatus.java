package furb.compiladores.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

class BarraDeStatus extends JLabel {

    private static final int ALTURA = 25;

    BarraDeStatus() {
        setPreferredSize(new Dimension(0, ALTURA));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0xB0B0B0)),
                BorderFactory.createEmptyBorder(0, 6, 0, 6)));
        limpar();
    }

    // mostra a pasta e o nome do arquivo aberto
    void mostrar(File arquivo) {
        setText(arquivo.getAbsolutePath());
    }

    void limpar() {
        setText(" ");
    }
}
