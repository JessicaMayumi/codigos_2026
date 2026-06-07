import ui.ConsoleUI;
import ui.SwingUI;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.GraphicsEnvironment;

// Sem argumentos abre a tela (Swing); se nao tiver tela, ou com --console/-c, cai no modo texto. --gui/-g forca a tela.
public class Main {
    public static void main(String[] args) {
        boolean forcaConsole = temFlag(args, "--console", "-c");
        boolean forcaGui     = temFlag(args, "--gui", "-g");

        boolean usarGui = forcaGui ||
                (!forcaConsole && !GraphicsEnvironment.isHeadless());

        if (usarGui) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignorado) {
            }
            SwingUtilities.invokeLater(() -> new SwingUI().setVisible(true));
        } else {
            new ConsoleUI().executar();
        }
    }

    private static boolean temFlag(String[] args, String longa, String curta) {
        for (String a : args) {
            if (a.equals(longa) || a.equals(curta)) return true;
        }
        return false;
    }
}
