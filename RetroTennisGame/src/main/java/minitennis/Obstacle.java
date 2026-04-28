package minitennis;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.net.URL;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * Classe Obstacle que utilitza exclusivament imatges per al renderitzat.
 * @author André Medinas, Candela Cabello, Daner Coria, Izan Perez i Adrià Chenovart
 */
public class Obstacle {
	
	//Declaració d'atributs privats
	//Coordenada del vector X
    private int x;
    //Coordenada del vector Y
    private int y;
    //Declaració i inicialització d'atribut privat, per la velocitat lateral
    private int dx = 2;
    
    //Declaració i inicialització de finals per la mida de l'amplada de la caixa de xoc
    private static final int WIDTH = 26;
    //Declaració i inicialització de finals per la mida de l'alçada de la caixa de xoc
    private static final int HEIGHT = 28;
    
    //Declaració i inicialització de finals per la mida de l'amplada de l'imatge
    private static final int IMG_WIDTH = 30;
    //Declaració i inicialització de finals per la mida de l'alçada de l'imatge
    private static final int IMG_HEIGHT = 30;
   
    //Instància de la classe game
    private Game game;
    //Declaració i inicialització de variable, s'encarrega de la vida
    private int vida = 1;
   
    //Objectes per emmagatzemar les imatges dels fantasmes
    //Imatge quan té la vida màxima
    private Image imgNormal;
    //Imatge quan ha rebut un cop
    private Image imgDanyat;
   
    /**
     * Constructor del osbtacle
     * @param x
     * @param y
     * @param game
     * @param nivel
     */
    public Obstacle(int x, int y, Game game, int nivel) {
        this.x = x;
        this.y = y;
        this.game = game;
        
        try {
            // Intentem carregar les imatges des del mateix paquet on està la classe
            URL urlNormal = getClass().getResource("fantasmaAmarillo.png");
            URL urlDanyat = getClass().getResource("fantasmaRojo.png");
           
            if (urlNormal != null) {
                imgNormal = new ImageIcon(urlNormal).getImage();
            }
            if (urlDanyat != null) {
                imgDanyat = new ImageIcon(urlDanyat).getImage();
            }
        } catch (Exception e) {
            // No dibuixem res si falla
        }
        
        if (nivel >= 10) this.vida = 2;
    }

    public void move() {
        if (game.getWidth() > 0) {
            x += dx;
            if (x <= 0 || x >= game.getWidth() - WIDTH) dx *= -1;
        }
    }

    public void ferDany() { vida--; }
    public int getVida() { return vida; }

    public void paint(Graphics2D g) {
        // Triem la imatge segons la vida
        Image imgActual = (vida == 2) ? imgNormal : imgDanyat;
        
        // Calculem el centrat per a la imatge
        int offsetDrawX = x - (IMG_WIDTH - WIDTH) / 2;
        int offsetDrawY = y - (IMG_HEIGHT - HEIGHT) / 2;
       
        // NOMÉS dibuixem si la imatge existeix. Si no, no es veurà res.
        if (imgActual != null) {
            g.drawImage(imgActual, offsetDrawX, offsetDrawY, IMG_WIDTH, IMG_HEIGHT, null);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
}