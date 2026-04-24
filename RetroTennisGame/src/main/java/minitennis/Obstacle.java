package minitennis;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Obstacle {

    private int x, y;
    private int dx = 2;
    private static final int SIZE = 20;

    private Game game;

    public Obstacle(int x, int y, Game game) {
        this.x = x;
        this.y = y;
        this.game = game;
    }

    public void move() {
        int width = game.getWidth();

        if (width > 0) {
            x += dx;

            // rebot parets
            if (x <= 0 || x >= width - SIZE) {
                dx = -dx;
            }
        }
    }

    public void paint(Graphics2D g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, SIZE, SIZE);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, SIZE, SIZE);
    }
}