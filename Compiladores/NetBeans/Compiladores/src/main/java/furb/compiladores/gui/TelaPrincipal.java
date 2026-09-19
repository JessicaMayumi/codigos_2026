package furb.compiladores.gui;

import furb.compiladores.io.ArquivoTexto;
import furb.compiladores.lexico.Constants;
import furb.compiladores.lexico.LexicalError;
import furb.compiladores.lexico.Lexico;
import furb.compiladores.lexico.Token;
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
    
    private static final java.util.Map<Integer, String> NOMES_CLASSES = new java.util.HashMap<>();
    static {
        NOMES_CLASSES.put(Constants.t_p_res, "p_res");
        NOMES_CLASSES.put(Constants.t_id_int, "id_int");
        NOMES_CLASSES.put(Constants.t_id_float, "id_float");
        NOMES_CLASSES.put(Constants.t_id_string, "id_string");
        NOMES_CLASSES.put(Constants.t_id_bool, "id_bool");
        NOMES_CLASSES.put(Constants.t_c_int, "c_int");
        NOMES_CLASSES.put(Constants.t_c_float, "c_float");
        NOMES_CLASSES.put(Constants.t_c_string, "c_string");
        NOMES_CLASSES.put(Constants.t_and, "and");
        NOMES_CLASSES.put(Constants.t_false, "false");
        NOMES_CLASSES.put(Constants.t_if, "if");
        NOMES_CLASSES.put(Constants.t_in, "in");
        NOMES_CLASSES.put(Constants.t_isfalsedo, "isfalsedo");
        NOMES_CLASSES.put(Constants.t_istruedo, "istruedo");
        NOMES_CLASSES.put(Constants.t_module, "module");
        NOMES_CLASSES.put(Constants.t_not, "not");
        NOMES_CLASSES.put(Constants.t_or, "or");
        NOMES_CLASSES.put(Constants.t_out, "out");
        NOMES_CLASSES.put(Constants.t_true, "true");
        NOMES_CLASSES.put(Constants.t_while, "while");
        NOMES_CLASSES.put(Constants.t_TOKEN_22, ",");
        NOMES_CLASSES.put(Constants.t_TOKEN_23, ":");
        NOMES_CLASSES.put(Constants.t_TOKEN_24, ";");
        NOMES_CLASSES.put(Constants.t_TOKEN_25, "[");
        NOMES_CLASSES.put(Constants.t_TOKEN_26, "]");
        NOMES_CLASSES.put(Constants.t_TOKEN_27, "(");
        NOMES_CLASSES.put(Constants.t_TOKEN_28, ")");
        NOMES_CLASSES.put(Constants.t_TOKEN_29, "{");
        NOMES_CLASSES.put(Constants.t_TOKEN_30, "}");
        NOMES_CLASSES.put(Constants.t_TOKEN_31, "+");
        NOMES_CLASSES.put(Constants.t_TOKEN_32, "-");
        NOMES_CLASSES.put(Constants.t_TOKEN_33, "*");
        NOMES_CLASSES.put(Constants.t_TOKEN_34, "/");
        NOMES_CLASSES.put(Constants.t_TOKEN_35, "<-");
        NOMES_CLASSES.put(Constants.t_TOKEN_36, "=");
        NOMES_CLASSES.put(Constants.t_TOKEN_37, "<");
        NOMES_CLASSES.put(Constants.t_TOKEN_38, ">");
        NOMES_CLASSES.put(Constants.t_TOKEN_39, "<>");
    }

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
        areaMensagens.limpar();
        String codigo = editor.getTexto();

        Lexico lexico = new Lexico();
        lexico.setInput(codigo);

        java.util.List<Token> tokens = new java.util.ArrayList<>();

        try {
            Token t;
            while ((t = lexico.nextToken()) != null) {
                tokens.add(t);
            }

            StringBuilder resultado = new StringBuilder();
            for (Token token : tokens) {
                int linha = calcularLinha(codigo, token.getPosition());
                String classe = obterNomeClasse(token.getId());
                resultado.append(String.format("linha: %d - classe: %s - lexema: %s%n",
                        linha, classe, token.getLexeme()));
            }
            resultado.append("programa compilado com sucesso");
            areaMensagens.mostrar(resultado.toString());

        } catch (LexicalError e) {
            int linha = calcularLinha(codigo, e.getPosition());
            areaMensagens.mostrar(String.format("Erro na linha %d - %s", linha, e.getMessage()));
        }
    }

    private int calcularLinha(String codigo, int posicao) {
        int linha = 1;
        for (int i = 0; i < posicao && i < codigo.length(); i++) {
            if (codigo.charAt(i) == '\n') {
                linha++;
            }
        }
        return linha;
    }


    private String obterNomeClasse(int id) {
        return NOMES_CLASSES.getOrDefault(id, "desconhecido(" + id + ")");
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
