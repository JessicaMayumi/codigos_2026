package furb.compiladores.gui;

import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

class Editor extends JScrollPane {

    private final JTextArea area = new JTextArea();

    Editor() {
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        area.setTabSize(4);
        area.setBorder(new NumberedBorder());   // numeração das linhas

        setViewportView(area);
        // as barras de rolagem ficam sempre visíveis, mesmo sem texto
        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_ALWAYS);
    }

    String getTexto() {
        return area.getText();
    }

    void setTexto(String texto) {
        area.setText(texto);
        area.setCaretPosition(0);
    }

    void limpar() {
        area.setText("");
    }

    void copiar() {
        area.copy();
    }

    void colar() {
        area.paste();
    }

    void recortar() {
        area.cut();
    }

    void focar() {
        area.requestFocusInWindow();
    }
}
