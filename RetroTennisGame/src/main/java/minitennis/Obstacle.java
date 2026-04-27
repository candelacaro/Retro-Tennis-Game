package minitennis;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Obstacle {

    private int x, y;
    private int dx = 2;
    private static final int WIDTH = 26;
    private static final int HEIGHT = 28;

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
            if (x <= 0 || x >= width - WIDTH) {
                dx = -dx;
            }
        }
    }

    public void paint(Graphics2D g) {

        //Cos del fantasma
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

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
}