package br.furb.buscador;

import br.furb.buscador.ui.ConsoleUI;
import br.furb.buscador.ui.SwingUI;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.GraphicsEnvironment;

/**
 * Ponto de entrada da aplicação.
 *
 * <p>Sem argumentos: tenta abrir a interface gráfica (Swing).
 * Se o ambiente não tiver display (modo headless, ex.: SSH sem
 * X11), faz fallback automático para a interface de console.</p>
 *
 * <p>Flags reconhecidas:
 * <ul>
 *   <li>{@code --console} ou {@code -c} — força modo console</li>
 *   <li>{@code --gui} ou {@code -g}      — força modo gráfico</li>
 * </ul>
 * </p>
 */
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
                // se o L&F do sistema falhar, mantém o padrão Metal
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
