package furb.compiladores;

import furb.compiladores.gui.TelaPrincipal;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Compiladores {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new TelaPrincipal().setVisible(true));
    }
}
