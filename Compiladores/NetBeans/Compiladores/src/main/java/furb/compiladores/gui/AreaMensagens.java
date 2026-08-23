package furb.compiladores.gui;

import java.awt.Font;
import java.awt.Insets;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

class AreaMensagens extends JScrollPane {

    private final JTextArea area = new JTextArea();

    AreaMensagens() {
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        area.setMargin(new Insets(0, 6, 0, 6));
        area.setEditable(false);

        setViewportView(area);
        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_ALWAYS);
    }

    // troca o conteúdo, para ficar visível só a mensagem do último botão acionado
    void mostrar(String mensagem) {
        area.setText(mensagem);
    }

    void limpar() {
        area.setText("");
    }
}
