package minitennis;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class PowerUp {
	
	private int x, y;
    private int yVel = 2; // Velocitat de caiguda
    private static final int WIDTH = 20;
    private static final int HEIGHT = 20;
    private Game game;
    
    public PowerUp(int x, int y, Game game) {
        this.x = x;
        this.y = y;
        this.game = game;
    }
    
    public void move() {
        y = y + yVel;
    }
    
    public void paint(Graphics2D g) {
        g.setColor(Color.CYAN); // Color distintiu per al PowerUp
        g.fillOval(x, y, WIDTH, HEIGHT);
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
    
    public boolean isOut() {
        return y > game.getHeight();
    }
}
