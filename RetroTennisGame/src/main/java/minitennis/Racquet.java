package minitennis;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class Racquet {

	private static final int Y = 330;
	private static final int WIDTH = 60;
	private static final int HEIGHT = 10;
	
	private int x = 0;
	private int xVelocitat = 0;
	
	private Game game;
	
	public Racquet(Game game) {
		this.game = game;
	}
	
	public void move() {
	    // Si el juego aún no tiene ancho (0), permitimos el movimiento inicial
	    int limit = (game.getWidth() <= 0) ? 300 : game.getWidth();
	    if (x + xVelocitat > 0 && x + xVelocitat < limit - WIDTH) {
	        x += xVelocitat;
	    }
	}

	public void setMouse(int mouseX) {
	    int newX = mouseX - (WIDTH / 2);
	    int limit = (game.getWidth() <= 0) ? 300 : game.getWidth();
	    
	    if (newX > 0 && newX < limit - WIDTH) {
	        this.x = newX;
	    }
	}
	
	public void paint(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		g2d.fillRect(x, Y, WIDTH, HEIGHT);
	}
	
	//CONTROL TECLADO
	public void keyReleased(KeyEvent e) {
		xVelocitat = 0;
	}
	
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			xVelocitat = -2;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			xVelocitat = 2;
		}
		
	}
	
	
	
	
	
	public Rectangle getBounds() {
		return new Rectangle(x, Y, WIDTH, HEIGHT);
	}
	
	public int getTopY() {
		return Y;
	}
	
}
