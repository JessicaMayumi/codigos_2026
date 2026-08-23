package furb.compiladores.gui;

import furb.compiladores.io.ArquivoTexto;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class TelaPrincipal extends JFrame {

    private static final String[] EQUIPE = {
        "Jessica Mayumi Schuhmacher Kogake",
        "João Vitor Furlaneto Rodrigues",
        "Ruan Gustavo Molinari"
    };

    private static final String MENSAGEM_COMPILACAO =
        "compilação de programas ainda não foi implementada";

    private static final int LARGURA_JANELA = 1500;
    private static final int ALTURA_JANELA = 800;
    private static final int ALTURA_INICIAL_EDITOR = 590;
    private static final int ALTURA_INICIAL_MENSAGENS = 120;

    private final Editor editor = new Editor();
    private final AreaMensagens areaMensagens = new AreaMensagens();
    private final BarraDeStatus barraStatus = new BarraDeStatus();
    private final BarraDeFerramentas barraFerramentas;

    private JFileChooser seletorArquivos;
    private File arquivoAtual;

    public TelaPrincipal() {
        super("Compilador");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        barraFerramentas = new BarraDeFerramentas(getRootPane());
        barraFerramentas.adicionar("novo", "ctrl-n", this::novo);
        barraFerramentas.adicionar("abrir", "ctrl-o", this::abrir);
        barraFerramentas.adicionar("salvar", "ctrl-s", this::salvar);
        barraFerramentas.adicionar("copiar", "ctrl-c", editor::copiar);
        barraFerramentas.adicionar("colar", "ctrl-v", editor::colar);
        barraFerramentas.adicionar("recortar", "ctrl-x", editor::recortar);
        barraFerramentas.adicionar("compilar", "F7", this::compilar);
        barraFerramentas.adicionar("equipe", "F1", this::equipe);

        add(barraFerramentas, BorderLayout.WEST);
        add(montarDivisor(), BorderLayout.CENTER);
        add(barraStatus, BorderLayout.SOUTH);

        setSize(LARGURA_JANELA, ALTURA_JANELA);
        setResizable(false);
        setLocationRelativeTo(null);
        novo();
    }

    private JSplitPane montarDivisor() {
        editor.setPreferredSize(new Dimension(0, ALTURA_INICIAL_EDITOR));
        editor.setMinimumSize(new Dimension(0, 50));
        areaMensagens.setPreferredSize(new Dimension(0, ALTURA_INICIAL_MENSAGENS));
        areaMensagens.setMinimumSize(new Dimension(0, 50));

        JSplitPane divisor = new JSplitPane(JSplitPane.VERTICAL_SPLIT, editor, areaMensagens);
        divisor.setResizeWeight(1.0);
        divisor.setContinuousLayout(true);
        divisor.setDividerSize(8);
        divisor.setBorder(BorderFactory.createEmptyBorder(4, 4, 2, 4));
        return divisor;
    }

    private void novo() {
        editor.limpar();
        areaMensagens.limpar();
        definirArquivoAtual(null);
        editor.focar();
    }

    private void abrir() {
        JFileChooser seletor = seletorArquivos("Abrir");
        if (seletor.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;   // cancelou: mantém tudo como estava
        }
        File arquivo = seletor.getSelectedFile();
        String conteudo;
        try {
            conteudo = ArquivoTexto.ler(arquivo);
        } catch (IOException e) {
            avisarErro("Não foi possível abrir o arquivo:\n" + arquivo.getAbsolutePath());
            return;
        }
        editor.setTexto(conteudo);
        areaMensagens.limpar();
        definirArquivoAtual(arquivo);
        editor.focar();
    }

    private void salvar() {
        File destino = arquivoAtual;
        if (destino == null) {
            // arquivo novo: precisa perguntar a pasta e o nome
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
            ArquivoTexto.gravar(destino, editor.getTexto());
        } catch (IOException e) {
            avisarErro("Não foi possível salvar o arquivo:\n" + destino.getAbsolutePath());
            return;
        }
        areaMensagens.limpar();
        definirArquivoAtual(destino);
        editor.focar();
    }

    private void compilar() {
        areaMensagens.mostrar(MENSAGEM_COMPILACAO);
    }

    private void equipe() {
        areaMensagens.mostrar(String.join("\n", EQUIPE));
    }

    private void definirArquivoAtual(File arquivo) {
        arquivoAtual = arquivo;
        if (arquivo == null) {
            barraStatus.limpar();
        } else {
            barraStatus.mostrar(arquivo);
        }
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
