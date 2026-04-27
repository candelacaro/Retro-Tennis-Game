package minitennis;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class Racquet {
// Parametres immutables de l'objecte
	private static final int Y = 330; // Posició fixa en l'eix vertical
	private static final int WIDTH = 60; // Dimensió horitzontal de la instància
	private static final int HEIGHT = 10; //Dimensió vertical de la instancia
	//Atribus d'instancia
	private int x = 0; //Coordenada actual en l'eix X
	private int xVelocitat = 0; //Vector de desplaçament horizontal
	//Referencia de dependencia cap a l'objecte contenidor
	private Game game;
	/**
     * Constructor de la classe Racquet.
     * @param game Instància de la classe principal Game per accedir al context del joc.
     */
	public Racquet(Game game) {
		this.game = game; // Injecció de dependència mitjançant el constructor
	}
	/**
     * Actualitza l'estat de la posició de la raqueta.
     * Aplica el vector de velocitat i valida que la nova posició estigui dins del 
     * rang permès.
     */
	public void move() {
		// Determinació del límit horitzontal del context gràfic
	    int limit = (game.getWidth() <= 0) ? 300 : game.getWidth();
	 // impedeix que l'objecte depassi els marges de la finestra
	    if (x + xVelocitat > 0 && x + xVelocitat < limit - WIDTH) {
	        x += xVelocitat; // Modificació de l'estat de la coordenada x
	    }
	}

	/**
     * Mètode mutador per al control mitjançant el ratolí.
     * @param mouseX Coordenada absoluta del punter del ratolí a la pantalla.
     */
	public void setMouse(int mouseX) {
		// Càlcul del centrat del cursor respecte a l'amplada de la raqueta
	    int newX = mouseX - (WIDTH / 2);
	    int limit = (game.getWidth() <= 0) ? 300 : game.getWidth();
	 // Validació de la posició predictiva abans de l'assignació
	    if (newX > 0 && newX < limit - WIDTH) {
	        this.x = newX;
	    }
	}
	/**
     * Renderitza la representació gràfica de la raqueta.
     * @param g2d Context gràfic Graphics2D per dibuixar primitives.
     */
	public void paint(Graphics2D g2d) {
		g2d.setColor(Color.MAGENTA); // Assignació del color del pinzell
		g2d.fillRect(x, Y, WIDTH, HEIGHT); //Dibuix del rectangle solid
	}
	
	/**
     * Manejador d'esdeveniment per a l'alliberament de tecles.
     * Reseteja el vector de velocitat per aturar el moviment inercial.
     * @param e Objecte KeyEvent que conté la informació de la tecla premuda.
     */
	public void keyReleased(KeyEvent e) {
		xVelocitat = 0;
	}
	/**
     * Manejador d'esdeveniment per a la pressió de tecles.
     * Assigna una direcció al vector de velocitat segons el codi de la tecla.
     * @param e Objecte KeyEvent subministrat pel Listener.
     */
	public void keyPressed(KeyEvent e) {
		// Logica de control de direcció
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			xVelocitat = -2; // Velocitat cap a l'esquerra
		}
		
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			xVelocitat = 2; // Velocitat cap a la dreta
		}
		
	}
	
	
	/**
     * Genera un rectangle de col·lisió basat en l'estat actual de l'objecte.
     * @return Una nova instància de Rectangle.
     */
	
	
	public Rectangle getBounds() {
		return new Rectangle(x, Y, WIDTH, HEIGHT);
	}
	/**
     * Mètode d'accés per a la coordenada superior de la raqueta.
     * @return Valor enter de la constant Y.
     */
	public int getTopY() {
		return Y;
	}
	
}
