package minitennis;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
/**
 * Definició de la classe Racquet.
 * Aquesta entitat representa l'element controlable per l'usuari.
 * Implementa la lògica de moviment horitzontal i la gestió d'entrada per esdeveniments.
 * * @author André Medinas, Candela Cabello, Daner Coria, Izan Perez i Adrià Chenovart
 * @version 1.0
 */
public class Racquet {
	// Defineixen l'estat immutable de les dimensions i la posició vertical de la raqueta
	private static final int Y = 330; // Posició fixa en l'eix d'ordenades
	private static final int WIDTH = 60; // Amplitud horitzontal de l'objecte
	private static final int HEIGHT = 10; // Altura vertical de l'objecte
	// Atributs d'instància
	private int x = 0; // Coordenada d'abscisses
	private int xVelocitat = 0; // Vector de velocitat lineal horitzontal
	// Referència de composició cap a l'objecte contenidor
	private Game game;
	/**
     * Constructor de la classe Racquet.
     * Realitza l'assignació de la instància de control del joc.
     * * @param game Instància de l'objecte Game per accedir al context global.
     */
	public Racquet(Game game) {
		this.game = game; // Injecció de dependència de l'objecte Game
	}
	/**
     * Actualitza l'estat de la posició de l'objecte.
     * Implementa una validació de límits per evitar que 
     * l'objecte surti del marc del component gràfic.
     */
	public void move() {
		// Determinació del límit del canvas mitjançant un operador ternari
	    int limit = (game.getWidth() <= 0) ? 300 : game.getWidth();
	 // Condicional lògic per validar si el següent pas es manté dins del rang permès
	    if (x + xVelocitat > 0 && x + xVelocitat < limit - WIDTH) {
	        x += xVelocitat; // Actualització de la variable d'estat x
	    }
	}
	/**
     * Mètode mutador per al control mitjançant dispositiu apuntador.
     * Calcula la posició centrada de l'objecte respecte a la coordenada del cursor.
     * * @param mouseX Coordenada horitzontal obtinguda de l'esdeveniment del ratolí.
     */
	public void setMouse(int mouseX) {
		// Càlcul per centrar l'objecte: es resta la meitat de l'amplitud a la posició del cursor
	    int newX = mouseX - (WIDTH / 2);
	    int limit = (game.getWidth() <= 0) ? 300 : game.getWidth();
	 // Validació de seguretat per mantenir l'objecte dins de l'espai renderitzable
	    if (newX > 0 && newX < limit - WIDTH) {
	        this.x = newX;
	    }
	}
	/**
     * Renderitza la representació gràfica de la raqueta.
     * * @param g2d Context gràfic Graphics2D per a l'execució d'operacions de dibuix.
     */
	public void paint(Graphics2D g2d) {
		g2d.setColor(Color.MAGENTA);
		g2d.fillRect(x, Y, WIDTH, HEIGHT);
	}
	
	/**
     * Controlador d'esdeveniments per a l'alliberament de tecles.
     * Reinicia el vector de velocitat per aturar el moviment inercial.
     * * @param e Objecte KeyEvent que conté la informació de l'esdeveniment de teclat.
     */
	public void keyReleased(KeyEvent e) {
		xVelocitat = 0; // Aturada del moviment
	}
	/**
     * Controlador d'esdeveniments per a la pulsació de tecles.
     * Modifica el vector de velocitat segons l'entrada de l'usuari.
     * * @param e Objecte KeyEvent que identifica la tecla premuda.
     */
	public void keyPressed(KeyEvent e) {
		// Verificació del codi de tecla mitjançant constants de la classe KeyEvent
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			xVelocitat = -2;// decrement vector cap a l'esquerra
		}
		
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			xVelocitat = 2; // increment vector cap a la dreta
		}
		
	}
	
	/**
     * Retorna el volum d'ocupació de l'objecte per a càlculs de col·lisió externs.
     * * @return Una nova instància de Rectangle que defineix el Bounding Box de la raqueta.
     */
	public Rectangle getBounds() {
		return new Rectangle(x, Y, WIDTH, HEIGHT);
	}
	/**
     * Mètode d'accés per a la coordenada superior de l'objecte.
     * * @return El valor de la constant Y.
     */
	public int getTopY() {
		return Y;
	}
	
}
