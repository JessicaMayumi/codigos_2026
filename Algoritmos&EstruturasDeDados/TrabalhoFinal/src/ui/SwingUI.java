package ui;

import structures.ListaEncadeada;
import structures.NoLista;
import models.Documento;
import models.Indice;
import services.Indexador;
import services.PersistenciaIndice;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;

// Tela do buscador com Swing. Usa os mesmos servicos da ConsoleUI, so muda a apresentacao.
// A indexacao roda numa SwingWorker pra nao travar a janela.
public class SwingUI extends JFrame {
    private static final File arquivoIndice = new File("indice.dat");

    private final PersistenciaIndice persistencia;
    private Indice indice;

    private JTextField campoDiretorio;
    private JButton botaoEscolher;
    private JButton botaoIndexar;
    private JTextField campoBusca;
    private JTextArea areaResultado;
    private JLabel rotuloStatus;

    public SwingUI() {
        super("Buscador de Arquivos — FURB / AED");
        this.persistencia = new PersistenciaIndice();
        construirInterface();
        carregarIndiceSeExistir();
    }

    private void construirInterface() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(760, 540);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new BorderLayout(8, 8));
        painel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel topo = new JPanel();
        topo.setLayout(new BoxLayout(topo, BoxLayout.Y_AXIS));
        topo.add(criarPainelIndexacao());
        topo.add(Box.createVerticalStrut(8));
        topo.add(criarPainelBusca());
        painel.add(topo, BorderLayout.NORTH);

        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        areaResultado.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        areaResultado.setLineWrap(false);
        JScrollPane scroll = new JScrollPane(areaResultado);
        scroll.setBorder(BorderFactory.createTitledBorder("Resultados"));
        painel.add(scroll, BorderLayout.CENTER);

        rotuloStatus = new JLabel("Pronto.");
        rotuloStatus.setBorder(new EmptyBorder(4, 6, 4, 6));
        painel.add(rotuloStatus, BorderLayout.SOUTH);

        setContentPane(painel);
    }

    private JPanel criarPainelIndexacao() {
        JPanel painel = new JPanel(new BorderLayout(6, 0));
        painel.setBorder(BorderFactory.createTitledBorder("Indexação"));

        campoDiretorio = new JTextField();
        botaoEscolher = new JButton("Escolher…");
        botaoIndexar = new JButton("Indexar");

        botaoEscolher.addActionListener(e -> escolherDiretorio());
        botaoIndexar.addActionListener(e -> indexar());

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        botoes.add(botaoEscolher);
        botoes.add(botaoIndexar);

        JLabel rotulo = new JLabel("  Pasta ou arquivo: ");
        painel.add(rotulo, BorderLayout.WEST);
        painel.add(campoDiretorio, BorderLayout.CENTER);
        painel.add(botoes, BorderLayout.EAST);
        return painel;
    }

    private JPanel criarPainelBusca() {
        JPanel painel = new JPanel(new BorderLayout(6, 0));
        painel.setBorder(BorderFactory.createTitledBorder("Busca (instantânea, por caractere)"));

        campoBusca = new JTextField();
        campoBusca.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e)  { buscar(); }
            @Override public void removeUpdate(DocumentEvent e)  { buscar(); }
            @Override public void changedUpdate(DocumentEvent e) { buscar(); }
        });

        JLabel rotulo = new JLabel("  Digite: ");
        painel.add(rotulo, BorderLayout.WEST);
        painel.add(campoBusca, BorderLayout.CENTER);
        return painel;
    }

    private void escolherDiretorio() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Selecione uma pasta ou um arquivo .txt");
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.addChoosableFileFilter(
                new FileNameExtensionFilter("Pastas ou arquivos .txt", "txt"));
        String atual = campoDiretorio.getText().trim();
        if (!atual.isEmpty()) {
            File f = new File(atual);
            if (f.exists()) {
                chooser.setCurrentDirectory(f.isDirectory() ? f : f.getParentFile());
            }
        }
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File escolhido = chooser.getSelectedFile();
            // Se entrou na pasta por duplo-clique, o seletor repete o nome (pasta/pasta).
            // Nesse caso o caminho nao existe, entao uso a pasta atual do seletor.
            if (escolhido == null || !escolhido.exists()) {
                escolhido = chooser.getCurrentDirectory();
            }
            campoDiretorio.setText(escolhido.getAbsolutePath());
        }
    }

    private void indexar() {
        String caminho = campoDiretorio.getText().trim();
        if (caminho.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe uma pasta ou um arquivo .txt.",
                    "Caminho vazio", JOptionPane.WARNING_MESSAGE);
            return;
        }
        final File alvo = new File(caminho);
        if (!alvo.exists()) {
            JOptionPane.showMessageDialog(this, "Caminho inválido: " + caminho,
                    "Caminho inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (alvo.isFile() && !alvo.getName().toLowerCase().endsWith(".txt")) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma pasta ou um arquivo .txt.",
                    "Arquivo não suportado", JOptionPane.ERROR_MESSAGE);
            return;
        }

        setBotoesHabilitados(false);
        rotuloStatus.setText("Indexando, aguarde…");
        areaResultado.setText("");

        // roda fora da EDT pra nao travar a janela
        SwingWorker<Indice, Void> worker = new SwingWorker<Indice, Void>() {
            final Indexador indexador = new Indexador();
            long ms;

            @Override
            protected Indice doInBackground() throws Exception {
                long inicio = System.currentTimeMillis();
                Indice idx = (indice != null) ? indice : new Indice();
                indexador.indexarEm(alvo, idx);
                persistencia.salvar(idx, arquivoIndice);
                ms = System.currentTimeMillis() - inicio;
                return idx;
            }

            @Override
            protected void done() {
                try {
                    indice = get();
                    areaResultado.setText(String.format(
                            "Indexação concluída em %d ms%n%n" +
                            "Arquivos lidos          : %d%n" +
                            "Ocorrências processadas : %d%n" +
                            "Palavras únicas         : %d%n" +
                            "Índice salvo em         : %s%n",
                            ms,
                            indexador.getArquivosIndexados(),
                            indexador.getPalavrasProcessadas(),
                            indice.totalPalavras(),
                            arquivoIndice.getAbsolutePath()));
                    atualizarStatus();
                } catch (Exception ex) {
                    Throwable causa = ex.getCause() != null ? ex.getCause() : ex;
                    JOptionPane.showMessageDialog(SwingUI.this,
                            "Erro ao indexar: " + causa.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                    rotuloStatus.setText("Erro na indexação.");
                } finally {
                    setBotoesHabilitados(true);
                }
            }
        };
        worker.execute();
    }

    private void buscar() {
        if (indice == null) {
            areaResultado.setText("Indexe uma pasta ou arquivo .txt para começar a buscar.");
            return;
        }
        String entrada = campoBusca.getText();
        if (entrada == null || entrada.isEmpty()) {
            areaResultado.setText("");
            return;
        }
        String alvo = entrada.toLowerCase();

        long inicio = System.nanoTime();

        ListaEncadeada<String> palavrasCasadas = new ListaEncadeada<>();
        ListaEncadeada<Documento> docsUnicos = new ListaEncadeada<>();

        ListaEncadeada<String> todasPalavras = indice.getMapa().chaves();
        for (NoLista<String> n = todasPalavras.getPrimeiro(); n != null; n = n.getProximo()) {
            String palavra = n.getInfo();
            if (palavra.contains(alvo)) {
                palavrasCasadas.inserir(palavra);
                ListaEncadeada<Documento> docs = indice.documentosCom(palavra);
                for (NoLista<Documento> d = docs.getPrimeiro(); d != null; d = d.getProximo()) {
                    if (!docsUnicos.contem(d.getInfo())) {
                        docsUnicos.inserir(d.getInfo());
                    }
                }
            }
        }

        long fim = System.nanoTime();

        StringBuilder sb = new StringBuilder();
        sb.append("Trecho buscado: \"").append(entrada).append("\"\n\n");

        if (palavrasCasadas.estaVazia()) {
            sb.append("Nenhuma palavra do índice contém esse trecho.\n");
        } else {
            sb.append(palavrasCasadas.tamanho()).append(" palavra(s) com o trecho:\n");
            int i = 1;
            for (NoLista<String> n = palavrasCasadas.getPrimeiro(); n != null; n = n.getProximo()) {
                sb.append("  ").append(i++).append(". ").append(n.getInfo()).append('\n');
            }
            sb.append('\n').append(docsUnicos.tamanho()).append(" documento(s):\n");
            int j = 1;
            for (NoLista<Documento> n = docsUnicos.getPrimeiro(); n != null; n = n.getProximo()) {
                sb.append("  ").append(j++).append(". ")
                  .append(n.getInfo().getCaminho()).append('\n');
            }
        }
        sb.append(String.format("%nBusca executada em %.3f ms%n", (fim - inicio) / 1_000_000.0));

        areaResultado.setText(sb.toString());
        areaResultado.setCaretPosition(0);
    }

    private void carregarIndiceSeExistir() {
        if (!arquivoIndice.exists()) {
            rotuloStatus.setText("Nenhum índice em disco — escolha um diretório para indexar.");
            return;
        }
        try {
            indice = persistencia.carregar(arquivoIndice);
            atualizarStatus();
        } catch (Exception e) {
            rotuloStatus.setText("Falha ao carregar o índice: " + e.getMessage());
            indice = null;
        }
    }

    private void atualizarStatus() {
        if (indice == null) {
            rotuloStatus.setText("Nenhum índice carregado.");
        } else {
            rotuloStatus.setText(String.format(
                    "Índice carregado em memória: %d palavras únicas (capacidade do mapa: %d).",
                    indice.totalPalavras(), indice.getMapa().getCapacidade()));
        }
    }

    private void setBotoesHabilitados(boolean habilitado) {
        botaoEscolher.setEnabled(habilitado);
        botaoIndexar.setEnabled(habilitado);
        campoBusca.setEnabled(habilitado);
    }
}
