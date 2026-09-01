package compilador;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/** Ponto de entrada da interface do compilador. */
public final class Principal {

    private Principal() {
    }

    public static void main(String[] argumentos) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception semVisualDoSistema) {
            // segue com o visual padrão do Swing
        }
        SwingUtilities.invokeLater(() -> new TelaPrincipal().setVisible(true));
    }
}
