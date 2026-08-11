package compilador;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.function.Consumer;
import javax.swing.Icon;

/**
 * Ícones dos botões da barra de ferramentas (item 9).
 *
 * <p>São desenhados com Java2D em vez de carregados de imagens, para que o
 * arquivo .jar não dependa de nenhum recurso externo.</p>
 */
final class Icones {

    private static final int TAMANHO = 26;

    private static final Color CONTORNO = new Color(0x5A5A5A);
    private static final Color PAPEL = Color.WHITE;
    private static final Color TEXTO_DO_PAPEL = new Color(0xB4B4B4);
    private static final Color PASTA = new Color(0xEDB04A);
    private static final Color PASTA_ESCURA = new Color(0xB57B18);
    private static final Color DISQUETE = new Color(0x4A6FA5);
    private static final Color DISQUETE_ESCURO = new Color(0x2E4C73);
    private static final Color PRANCHETA = new Color(0xC49A6C);
    private static final Color PRANCHETA_ESCURA = new Color(0x8A6A44);
    private static final Color VERMELHO = new Color(0xD1495B);
    private static final Color CINZA = new Color(0x9A9A9A);
    private static final Color AZUL = new Color(0x3E7CB1);
    private static final Color AMARELO = new Color(0xF2C037);

    private Icones() {
    }

    /** Folha em branco com um brilho, indicando um programa novo. */
    static Icon novo() {
        return new IconeVetorial(grafico -> {
            desenharFolha(grafico, 5, 4, 13, 18);
            grafico.setColor(AMARELO);
            grafico.fill(brilho(19.5, 6.5, 5));
        });
    }

    /** Pasta aberta. */
    static Icon abrir() {
        return new IconeVetorial(grafico -> {
            Path2D fundo = new Path2D.Double();
            fundo.moveTo(2, 21);
            fundo.lineTo(2, 6);
            fundo.lineTo(9, 6);
            fundo.lineTo(11.5, 9);
            fundo.lineTo(20, 9);
            fundo.lineTo(20, 21);
            fundo.closePath();
            grafico.setColor(PASTA_ESCURA);
            grafico.fill(fundo);

            Path2D frente = new Path2D.Double();
            frente.moveTo(2, 21);
            frente.lineTo(6.5, 12);
            frente.lineTo(24, 12);
            frente.lineTo(20, 21);
            frente.closePath();
            grafico.setColor(PASTA);
            grafico.fill(frente);
            grafico.setColor(PASTA_ESCURA);
            grafico.draw(frente);
        });
    }

    /** Disquete. */
    static Icon salvar() {
        return new IconeVetorial(grafico -> {
            RoundRectangle2D corpo = new RoundRectangle2D.Double(3, 3, 20, 20, 3, 3);
            grafico.setColor(DISQUETE);
            grafico.fill(corpo);
            grafico.setColor(DISQUETE_ESCURO);
            grafico.draw(corpo);

            grafico.setColor(new Color(0xDDE4EE));
            grafico.fill(new Rectangle2D.Double(8, 4, 10, 7));
            grafico.setColor(DISQUETE_ESCURO);
            grafico.fill(new Rectangle2D.Double(14, 5, 3, 5));

            grafico.setColor(PAPEL);
            grafico.fill(new Rectangle2D.Double(7, 14, 12, 9));
            grafico.setColor(DISQUETE_ESCURO);
            grafico.draw(new Rectangle2D.Double(7, 14, 12, 9));
            grafico.setColor(TEXTO_DO_PAPEL);
            grafico.draw(new Line2D.Double(9, 17, 17, 17));
            grafico.draw(new Line2D.Double(9, 20, 17, 20));
        });
    }

    /** Duas folhas sobrepostas. */
    static Icon copiar() {
        return new IconeVetorial(grafico -> {
            desenharFolha(grafico, 3, 2, 12, 16);
            desenharFolha(grafico, 9, 8, 13, 16);
        });
    }

    /** Prancheta com uma folha. */
    static Icon colar() {
        return new IconeVetorial(grafico -> {
            RoundRectangle2D prancheta = new RoundRectangle2D.Double(3, 4, 19, 20, 3, 3);
            grafico.setColor(PRANCHETA);
            grafico.fill(prancheta);
            grafico.setColor(PRANCHETA_ESCURA);
            grafico.draw(prancheta);

            grafico.setColor(PAPEL);
            grafico.fill(new Rectangle2D.Double(6, 9, 13, 13));
            grafico.setColor(CONTORNO);
            grafico.draw(new Rectangle2D.Double(6, 9, 13, 13));
            grafico.setColor(TEXTO_DO_PAPEL);
            for (int altura = 13; altura <= 19; altura += 3) {
                grafico.draw(new Line2D.Double(8, altura, 17, altura));
            }

            RoundRectangle2D clipe = new RoundRectangle2D.Double(9, 2, 7, 5, 2, 2);
            grafico.setColor(PRANCHETA_ESCURA);
            grafico.fill(clipe);
        });
    }

    /** Tesoura. */
    static Icon recortar() {
        return new IconeVetorial(grafico -> {
            grafico.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            grafico.setColor(CINZA);
            grafico.draw(new Line2D.Double(7, 3, 16.5, 18));
            grafico.draw(new Line2D.Double(19, 3, 9.5, 18));

            grafico.setStroke(new BasicStroke(1.8f));
            grafico.setColor(VERMELHO);
            grafico.draw(new Ellipse2D.Double(5, 17, 6, 6));
            grafico.draw(new Ellipse2D.Double(15, 17, 6, 6));
        });
    }

    /** Esfera verde, como no modelo da interface. */
    static Icon compilar() {
        return new IconeVetorial(grafico -> {
            Ellipse2D esfera = new Ellipse2D.Double(3, 3, 20, 20);
            grafico.setPaint(new GradientPaint(6, 5, new Color(0x8BDF7A), 20, 22, new Color(0x2E7D32)));
            grafico.fill(esfera);
            grafico.setPaint(new Color(0x1B5E20));
            grafico.draw(esfera);
            grafico.setColor(new Color(255, 255, 255, 140));
            grafico.fill(new Ellipse2D.Double(7.5, 6.5, 7, 5));
        });
    }

    /** Duas pessoas. */
    static Icon equipe() {
        return new IconeVetorial(grafico -> {
            grafico.setColor(PASTA);
            grafico.fill(new Ellipse2D.Double(13, 4, 8, 8));
            grafico.fill(new Arc2D.Double(11, 13, 14, 13, 0, 180, Arc2D.CHORD));

            grafico.setColor(AZUL);
            grafico.fill(new Ellipse2D.Double(4, 7, 9, 9));
            grafico.fill(new Arc2D.Double(1, 17, 15, 14, 0, 180, Arc2D.CHORD));
        });
    }

    /** Folha de papel com a ponta superior direita dobrada. */
    private static void desenharFolha(Graphics2D grafico, double x, double y, double largura, double altura) {
        double dobra = 5;
        Path2D folha = new Path2D.Double();
        folha.moveTo(x, y);
        folha.lineTo(x + largura - dobra, y);
        folha.lineTo(x + largura, y + dobra);
        folha.lineTo(x + largura, y + altura);
        folha.lineTo(x, y + altura);
        folha.closePath();

        grafico.setColor(PAPEL);
        grafico.fill(folha);
        grafico.setColor(CONTORNO);
        grafico.draw(folha);
        grafico.draw(new Line2D.Double(x + largura - dobra, y, x + largura - dobra, y + dobra));
        grafico.draw(new Line2D.Double(x + largura - dobra, y + dobra, x + largura, y + dobra));

        grafico.setColor(TEXTO_DO_PAPEL);
        for (double linha = y + dobra + 4; linha < y + altura - 1; linha += 3) {
            grafico.draw(new Line2D.Double(x + 2, linha, x + largura - 2, linha));
        }
    }

    /** Brilho de quatro pontas. */
    private static Path2D brilho(double centroX, double centroY, double raio) {
        double meio = raio / 3.5;
        Path2D estrela = new Path2D.Double();
        estrela.moveTo(centroX, centroY - raio);
        estrela.quadTo(centroX + meio, centroY - meio, centroX + raio, centroY);
        estrela.quadTo(centroX + meio, centroY + meio, centroX, centroY + raio);
        estrela.quadTo(centroX - meio, centroY + meio, centroX - raio, centroY);
        estrela.quadTo(centroX - meio, centroY - meio, centroX, centroY - raio);
        estrela.closePath();
        return estrela;
    }

    private record IconeVetorial(Consumer<Graphics2D> desenho) implements Icon {

        @Override
        public int getIconWidth() {
            return TAMANHO;
        }

        @Override
        public int getIconHeight() {
            return TAMANHO;
        }

        @Override
        public void paintIcon(Component componente, Graphics grafico, int x, int y) {
            Graphics2D copia = (Graphics2D) grafico.create(x, y, TAMANHO, TAMANHO);
            copia.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            copia.setStroke(new BasicStroke(1.2f));
            desenho.accept(copia);
            copia.dispose();
        }
    }
}
