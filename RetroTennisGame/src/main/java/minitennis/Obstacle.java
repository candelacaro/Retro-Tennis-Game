package minitennis;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * * @author André Medinas, Candela Cabello, Daner Coria, Izan Perez i Adrià Chenovart
 */
public class Obstacle {

	//Declaració d'atributs privats
    private int x; 
    private int y;
    private int dx = 2;
    private static final int WIDTH = 26;
    private static final int HEIGHT = 28;
    private Game game;
    private int vida = 1; // Atribut de vida

    /**
     * Constructor que, assigna la posició inicial i vincula la instància al context global del joc.
     */
    public Obstacle(int x, int y, Game game, int nivel) {
        this.x = x;
        this.y = y;
        this.game = game;
        // Lògica de desafiament: a partir del nivell 10, 2 vides
        if (nivel >= 10) {
            this.vida = 2;
        }
    }

    /**
     * Mètode que, implementa la lògica de moviment horitzontal.
     */
    public void move() {
        int width = game.getWidth();
        if (width > 0) {
            x += dx;
            if (x <= 0 || x >= width - WIDTH) {
                dx = -dx;
            }
        }
    }

    /**
     * Mètode ràfic
     */
    public void paint(Graphics2D g) {

        // Lògica de color: Vermell si té 2 vides, Morat si en té 1
        if (vida == 2) {
            g.setColor(Color.YELLOW);
        } else {
            g.setColor(new Color(180, 0, 255));
        }

        // Dibuix del fantasma amb els colors originals
        g.fillOval(x, y, WIDTH, 18); // Cap
        g.fillRect(x, y + 9, WIDTH, 12); // Cos rectangular

        // ones inferiors
        g.fillOval(x, y + 16, 8, 12);
        g.fillOval(x + 9, y + 16, 8, 12);
        g.fillOval(x + 18, y + 16, 8, 12);

        // ojos
        g.setColor(Color.WHITE);
        g.fillOval(x + 6, y + 7, 5, 7);
        g.fillOval(x + 15, y + 7, 5, 7);

        // pupilas
        g.setColor(Color.BLACK);
        g.fillOval(x + 8, y + 10, 2, 3);
        g.fillOval(x + 17, y + 10, 2, 3);
    }
    /**
     * Mètode que redueix la resistència de l'obstacle quan és colpejat per la pilota.
     */
    public void recibirGolpe() {
        vida--;
    }
    /**
     * Mètode accessor per consultar les vides restants de l'obstacle.
     * @return El nombre de vides actuals.
     */
    public int getVida() {
        return vida;
    }
    /**
     * Mètode accessor  per a la detecció de col·lisions.
     * Exporta l'àrea d'ocupació de la instància per comprovar interseccions amb la pilota.
     * * @return Una nova instància de Rectangle que representa els límits físics de l'obstacle.
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
}