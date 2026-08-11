package compilador;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Item 4: régua com o número das linhas, exibida à esquerda do editor.
 *
 * <p>É um componente apenas de desenho, portanto o número da linha não pode ser
 * alterado pelo usuário. As linhas são numeradas a partir de 1 e acompanham a
 * rolagem do editor, pois a régua é usada como cabeçalho de linha
 * ({@code setRowHeaderView}) da mesma barra de rolagem.</p>
 */
class NumeroDeLinhas extends JPanel {

    private static final int MARGEM = 8;
    private static final int MINIMO_DE_DIGITOS = 3;

    private final JTextArea editor;

    NumeroDeLinhas(JTextArea editor) {
        this.editor = editor;
        setFont(editor.getFont());

        // cores derivadas do editor, para acompanhar o visual do sistema
        Color fundo = editor.getBackground();
        Color texto = editor.getForeground();
        setBackground(mesclar(fundo, texto, 0.08));
        setForeground(mesclar(texto, fundo, 0.40));
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, mesclar(fundo, texto, 0.25)));

        editor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent evento) {
                atualizar();
            }

            @Override
            public void removeUpdate(DocumentEvent evento) {
                atualizar();
            }

            @Override
            public void changedUpdate(DocumentEvent evento) {
                atualizar();
            }
        });
    }

    private void atualizar() {
        revalidate();
        repaint();
    }

    /** Combina duas cores, com {@code peso} indicando quanto da segunda cor é usado. */
    private static Color mesclar(Color primeira, Color segunda, double peso) {
        return new Color(
                (int) Math.round(primeira.getRed() * (1 - peso) + segunda.getRed() * peso),
                (int) Math.round(primeira.getGreen() * (1 - peso) + segunda.getGreen() * peso),
                (int) Math.round(primeira.getBlue() * (1 - peso) + segunda.getBlue() * peso));
    }

    @Override
    public Dimension getPreferredSize() {
        FontMetrics medidas = editor.getFontMetrics(editor.getFont());
        int digitos = Math.max(MINIMO_DE_DIGITOS, String.valueOf(editor.getLineCount()).length());
        int largura = medidas.charWidth('0') * digitos + MARGEM * 2;

        int altura = editor.getLineCount() * medidas.getHeight();
        if (getParent() instanceof JViewport visor) {
            // numera também as linhas vazias visíveis abaixo do texto
            altura = Math.max(altura, visor.getExtentSize().height + medidas.getHeight());
        }
        return new Dimension(largura, altura);
    }

    @Override
    protected void paintComponent(Graphics grafico) {
        super.paintComponent(grafico);

        FontMetrics medidas = editor.getFontMetrics(editor.getFont());
        int alturaDaLinha = medidas.getHeight();
        Rectangle recorte = grafico.getClipBounds();
        int primeira = Math.max(0, recorte.y / alturaDaLinha);
        int ultima = (recorte.y + recorte.height) / alturaDaLinha;
        int direita = getWidth() - MARGEM;

        grafico.setColor(getForeground());
        for (int linha = primeira; linha <= ultima; linha++) {
            String numero = String.valueOf(linha + 1);
            grafico.drawString(numero,
                    direita - medidas.stringWidth(numero),
                    linha * alturaDaLinha + medidas.getAscent());
        }
    }
}
