package minitennis;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * 
 * @author André Medinas, Candela Cabello, Daner Coria, Izan Perez i Adrià Chenovart
 */
public class Obstacle {

	//Declaració d'atributs privats
	//Posició horitzontal de la pilota (eix de les absisses)
    private int x; 
    //Posició vertical de la pilota (eix de les ordenades)
    private int y;
    
    /*Declaració i incialització d'atribut, aquest serveix per la velocitat i 
    a quants pixels es mou cada vegada*/
    private int dx = 2;
    
    //Declaració i incialització de finals
    //Amplada de la caixa de xoc
    private static final int WIDTH = 26;
    //Alçada de la caixa de xoc
    private static final int HEIGHT = 28;

    //Instància d'objecte de la classe Game per interactuar amb el joc
    private Game game;

    /**
     * Constructor que, assigna la posició inicial i vincula la instància al context global del joc.
     * @param x, coordenada inicial en l'eix d'abscisses
     * @param y, coordenada inicial en l'eix d'ordenades.
     * @param game, referència a la instància de la classe Game.
     */
    public Obstacle(int x, int y, Game game) {
        this.x = x;
        this.y = y;
        this.game = game;
    }

    /**
     * Mètode que, implementa la lògica de moviment horitzontal.
     * Aplica una validació de límits per realitzar el rebot lateral.
     */
    public void move() {
    		//Obtenció de l'amplada de l'escenari mitjançant el mètode getter de la instància game
        int width = game.getWidth();

        //Estructura condicional: només s'executa si la finestra ha estat inicialitzada
        if (width > 0) {
        		//Actualització de l'estat de la posició X segons el diferencial
            x += dx;

            /**
             * Estructura condicional que controla la lògica de rebot.
             * x <= 0: col·lisió amb el marge esquerre
             * x >= width - WIDTH: col·lisió amb el marge dret
             */
            if (x <= 0 || x >= width - WIDTH) {
            		//Invertim la velocitat horitzontal
                dx = -dx;
            }
        }
    }

    /**
     * Mètode ràfic
     * @param g, Objecte Graphics2D que actua com a interfície de renderitzat.
     */
    public void paint(Graphics2D g) {

        //Color del fantasma, mitjançant el mètode setColor
        g.setColor(new Color(180, 0, 255));

        // Cap
        g.fillOval(x, y, WIDTH, 18);

        // Cos rectangular
        g.fillRect(x, y + 9, WIDTH, 12);

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
     * Mètode accessor (Getter) per a la detecció de col·lisions.
     * Exporta l'àrea d'ocupació de la instància .
     * @return Una nova instància de Rectangle que representa els límits de l'obstacle.
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
}