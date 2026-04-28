package minitennis;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * * @author André Medinas, Candela Cabello, Daner Coria, Izan Perez i Adrià Chenovart
 */
public class Obstacle {
    private int x; 
    private int y;
    private int dx = 2;
    private static final int WIDTH = 26;
    private static final int HEIGHT = 28;
    private Game game;
    private int vida = 1;

    public Obstacle(int x, int y, Game game, int nivel) {
        this.x = x;
        this.y = y;
        this.game = game;
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
        if (vida == 2) g.setColor(Color.YELLOW);
        else g.setColor(new Color(180, 0, 255));

        g.fillOval(x, y, WIDTH, 18);
        g.fillRect(x, y + 9, WIDTH, 12);
        g.fillOval(x, y + 16, 8, 12);
        g.fillOval(x + 9, y + 16, 8, 12);
        g.fillOval(x + 18, y + 16, 8, 12);
        g.setColor(Color.WHITE);
        g.fillOval(x + 6, y + 7, 5, 7);
        g.fillOval(x + 15, y + 7, 5, 7);
        g.setColor(Color.BLACK);
        g.fillOval(x + 8, y + 10, 2, 3);
        g.fillOval(x + 17, y + 10, 2, 3);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
}