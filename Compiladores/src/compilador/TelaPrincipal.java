package compilador;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Janela principal da interface do compilador: barra de ferramentas, editor com
 * numeração de linhas, área para mensagens e barra de status.
 */
public class TelaPrincipal extends JFrame {

    /** Nomes apresentados pelo botão "equipe" (item 15). */
    private static final String[] EQUIPE = {
        "Jessica Mayumi",
        "Segundo integrante da equipe",
        "Terceiro integrante da equipe"
    };

    /** Mensagem apresentada pelo botão "compilar" (item 14). */
    private static final String MENSAGEM_COMPILACAO =
        "compilação de programas ainda não foi implementada";

    private static final int LARGURA_JANELA = 1500;
    private static final int ALTURA_JANELA = 800;
    private static final int LARGURA_FERRAMENTAS = 150;
    private static final int ALTURA_STATUS = 25;
    private static final int ALTURA_INICIAL_EDITOR = 590;
    private static final int ALTURA_INICIAL_MENSAGENS = 120;

    private static final Dimension TAMANHO_BOTAO = new Dimension(138, 58);
    private static final Font FONTE = new Font(Font.MONOSPACED, Font.PLAIN, 14);
    private static final Color BORDA = new Color(0xB0B0B0);

    private final JTextArea editor = new JTextArea();
    private final JTextArea areaMensagens = new JTextArea();
    private final JLabel barraStatus = new JLabel();
    private final JToolBar barraFerramentas = new JToolBar(JToolBar.VERTICAL);

    private JFileChooser seletorArquivos;
    private File arquivoAtual;

    public TelaPrincipal() {
        super("Compilador");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        montarBarraFerramentas();
        add(barraFerramentas, BorderLayout.WEST);
        add(montarDivisor(), BorderLayout.CENTER);
        add(montarBarraStatus(), BorderLayout.SOUTH);

        setSize(LARGURA_JANELA, ALTURA_JANELA);
        setResizable(false);           // item 1: tamanho fixo, ainda permite minimizar e fechar
        setLocationRelativeTo(null);
        novo();
    }

    // ------------------------------------------------------------------
    // Construção da interface
    // ------------------------------------------------------------------

    /** Item 2 e item 9: barra de ferramentas de 150 x n, com os oito botões. */
    private void montarBarraFerramentas() {
        barraFerramentas.setFloatable(false);
        barraFerramentas.setPreferredSize(new Dimension(LARGURA_FERRAMENTAS, 0));
        barraFerramentas.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, BORDA),
                BorderFactory.createEmptyBorder(4, 5, 4, 5)));

        adicionarFerramenta("novo", "ctrl-n", Icones.novo(), this::novo);
        adicionarFerramenta("abrir", "ctrl-o", Icones.abrir(), this::abrir);
        adicionarFerramenta("salvar", "ctrl-s", Icones.salvar(), this::salvar);
        adicionarFerramenta("copiar", "ctrl-c", Icones.copiar(), editor::copy);
        adicionarFerramenta("colar", "ctrl-v", Icones.colar(), editor::paste);
        adicionarFerramenta("recortar", "ctrl-x", Icones.recortar(), editor::cut);
        adicionarFerramenta("compilar", "F7", Icones.compilar(), this::compilar);
        adicionarFerramenta("equipe", "F1", Icones.equipe(), this::equipe);
    }

    /** Cria um botão da barra de ferramentas e registra a tecla de atalho associada. */
    private void adicionarFerramenta(String nome, String atalho, Icon icone, Runnable acao) {
        JButton botao = new JButton(nome + " [" + atalho + "]", icone);
        botao.setVerticalTextPosition(SwingConstants.BOTTOM);
        botao.setHorizontalTextPosition(SwingConstants.CENTER);
        botao.setIconTextGap(3);
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
        botao.setPreferredSize(TAMANHO_BOTAO);
        botao.setMinimumSize(TAMANHO_BOTAO);
        botao.setMaximumSize(TAMANHO_BOTAO);
        botao.setFocusable(false);     // o foco permanece no editor
        botao.addActionListener(evento -> acao.run());

        barraFerramentas.add(botao);
        registrarAtalho(atalho, acao);
    }

    /** Item 3: editor e área para mensagens separados por uma barra de divisão. */
    private JSplitPane montarDivisor() {
        editor.setFont(FONTE);
        editor.setTabSize(4);
        editor.setMargin(new Insets(0, 6, 0, 6));

        JScrollPane rolagemEditor = criarRolagem(editor, ALTURA_INICIAL_EDITOR);
        rolagemEditor.setRowHeaderView(new NumeroDeLinhas(editor));   // item 4

        areaMensagens.setFont(FONTE);
        areaMensagens.setMargin(new Insets(0, 6, 0, 6));
        areaMensagens.setEditable(false);                             // item 6
        JScrollPane rolagemMensagens = criarRolagem(areaMensagens, ALTURA_INICIAL_MENSAGENS);

        JSplitPane divisor = new JSplitPane(JSplitPane.VERTICAL_SPLIT, rolagemEditor, rolagemMensagens);
        divisor.setResizeWeight(1.0);
        divisor.setContinuousLayout(true);
        divisor.setDividerSize(8);
        divisor.setBorder(BorderFactory.createEmptyBorder(4, 4, 2, 4));
        return divisor;
    }

    /** Itens 5 e 7: as barras de rolagem ficam sempre visíveis, mesmo sem texto. */
    private static JScrollPane criarRolagem(JTextArea area, int alturaInicial) {
        JScrollPane rolagem = new JScrollPane(area,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        rolagem.setPreferredSize(new Dimension(0, alturaInicial));
        rolagem.setMinimumSize(new Dimension(0, 50));
        return rolagem;
    }

    /** Item 2 e item 8: barra de status de m x 25. */
    private JComponent montarBarraStatus() {
        barraStatus.setPreferredSize(new Dimension(0, ALTURA_STATUS));
        barraStatus.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDA),
                BorderFactory.createEmptyBorder(0, 6, 0, 6)));
        return barraStatus;
    }

    /** Torna a tecla de atalho válida em toda a janela (itens 9 a 15). */
    private void registrarAtalho(String atalho, Runnable acao) {
        Object identificador = "acao:" + atalho;
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(tecla(atalho), identificador);
        getRootPane().getActionMap().put(identificador, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                acao.run();
            }
        });
    }

    /** Converte a indicação exibida no botão ("ctrl-n", "F7") em tecla de atalho. */
    private static KeyStroke tecla(String atalho) {
        if (atalho.startsWith("ctrl-")) {
            return KeyStroke.getKeyStroke("control " + atalho.substring("ctrl-".length()).toUpperCase());
        }
        return KeyStroke.getKeyStroke(atalho);
    }

    // ------------------------------------------------------------------
    // Ações dos botões
    // ------------------------------------------------------------------

    /** Item 10: limpa o editor, a área para mensagens e a barra de status. */
    private void novo() {
        editor.setText("");
        areaMensagens.setText("");
        definirArquivoAtual(null);
        editor.requestFocusInWindow();
    }

    /** Item 11: carrega um arquivo no editor; sem seleção, o estado é mantido. */
    private void abrir() {
        JFileChooser seletor = seletorArquivos("Abrir");
        if (seletor.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File arquivo = seletor.getSelectedFile();
        String conteudo;
        try {
            conteudo = ArquivoTexto.ler(arquivo);
        } catch (IOException erro) {
            avisarErro("Não foi possível abrir o arquivo:\n" + arquivo.getAbsolutePath());
            return;
        }
        editor.setText(conteudo);
        editor.setCaretPosition(0);
        areaMensagens.setText("");
        definirArquivoAtual(arquivo);
        editor.requestFocusInWindow();
    }

    /** Item 12: salva o texto do editor, pedindo pasta e nome apenas para arquivo novo. */
    private void salvar() {
        File destino = arquivoAtual;
        if (destino == null) {
            JFileChooser seletor = seletorArquivos("Salvar");
            if (seletor.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
                return;
            }
            destino = comExtensaoTxt(seletor.getSelectedFile());
            if (destino.exists() && !confirmarSubstituicao(destino)) {
                return;
            }
        }
        try {
            ArquivoTexto.gravar(destino, editor.getText());
        } catch (IOException erro) {
            avisarErro("Não foi possível salvar o arquivo:\n" + destino.getAbsolutePath());
            return;
        }
        areaMensagens.setText("");
        definirArquivoAtual(destino);
        editor.requestFocusInWindow();
    }

    /** Item 14. */
    private void compilar() {
        areaMensagens.setText(MENSAGEM_COMPILACAO);
    }

    /** Item 15. */
    private void equipe() {
        areaMensagens.setText(String.join("\n", EQUIPE));
    }

    // ------------------------------------------------------------------
    // Apoio
    // ------------------------------------------------------------------

    /** Item 8: a barra de status mostra a pasta e o nome do arquivo aberto. */
    private void definirArquivoAtual(File arquivo) {
        arquivoAtual = arquivo;
        barraStatus.setText(arquivo == null ? " " : arquivo.getAbsolutePath());
    }

    private JFileChooser seletorArquivos(String titulo) {
        if (seletorArquivos == null) {
            seletorArquivos = new JFileChooser();
            seletorArquivos.setAcceptAllFileFilterUsed(false);
            seletorArquivos.setFileFilter(new FileNameExtensionFilter("Arquivos texto (*.txt)", "txt"));
        }
        seletorArquivos.setDialogTitle(titulo);
        seletorArquivos.setSelectedFile(arquivoAtual == null ? new File("") : arquivoAtual);
        return seletorArquivos;
    }

    private static File comExtensaoTxt(File arquivo) {
        return arquivo.getName().toLowerCase().endsWith(".txt")
                ? arquivo
                : new File(arquivo.getPath() + ".txt");
    }

    private boolean confirmarSubstituicao(File arquivo) {
        int resposta = JOptionPane.showConfirmDialog(this,
                "O arquivo " + arquivo.getName() + " já existe. Deseja substituí-lo?",
                "Salvar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        return resposta == JOptionPane.YES_OPTION;
    }

    private void avisarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Compilador", JOptionPane.ERROR_MESSAGE);
    }
}
