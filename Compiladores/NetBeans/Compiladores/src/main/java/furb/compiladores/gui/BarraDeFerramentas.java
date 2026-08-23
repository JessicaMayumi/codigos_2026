package furb.compiladores.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

class BarraDeFerramentas extends JToolBar {

    private static final int LARGURA = 150;
    private static final Dimension TAMANHO_BOTAO = new Dimension(138, 58);

    private final JRootPane raiz;

    BarraDeFerramentas(JRootPane raiz) {
        super(VERTICAL);
        this.raiz = raiz;

        setFloatable(false);
        setPreferredSize(new Dimension(LARGURA, 0));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(0xB0B0B0)),
                BorderFactory.createEmptyBorder(4, 5, 4, 5)));
    }

    void adicionar(String nome, String atalho, Runnable acao) {
        JButton botao = new JButton(nome + " [" + atalho + "]", Icones.carregar(nome));
        botao.setVerticalTextPosition(SwingConstants.BOTTOM);
        botao.setHorizontalTextPosition(SwingConstants.CENTER);
        botao.setIconTextGap(3);
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
        botao.setPreferredSize(TAMANHO_BOTAO);
        botao.setMinimumSize(TAMANHO_BOTAO);
        botao.setMaximumSize(TAMANHO_BOTAO);
        botao.setFocusable(false);   // assim o foco continua no editor
        botao.addActionListener(e -> acao.run());

        add(botao);
        registrarAtalho(atalho, acao);
    }

    // o atalho vale com o foco em qualquer lugar da janela
    private void registrarAtalho(String atalho, Runnable acao) {
        Object identificador = "acao:" + atalho;
        raiz.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(tecla(atalho), identificador);
        raiz.getActionMap().put(identificador, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                acao.run();
            }
        });
    }

    private static KeyStroke tecla(String atalho) {
        if (atalho.startsWith("ctrl-")) {
            return KeyStroke.getKeyStroke("control " + atalho.substring("ctrl-".length()).toUpperCase());
        }
        return KeyStroke.getKeyStroke(atalho);
    }
}
