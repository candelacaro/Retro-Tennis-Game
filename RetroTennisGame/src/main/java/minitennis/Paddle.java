package minitennis;

import java.awt.*;
import java.awt.event.*;

public class Paddle {

    int x = 100;
    int xa = 0;

    Game game;

    public Paddle(Game game) {
        this.game = game;
    }

    public void move() {
        x += xa;

        if (x < 0) x = 0;
        if (x > game.getWidth() - 60)
            x = game.getWidth() - 60;
    }

    public void paint(Graphics2D g) {
        g.fillRect(x, 330, 60, 10);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, 330, 60, 10);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
            xa = -5;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            xa = 5;
    }

    public void keyReleased(KeyEvent e) {
        xa = 0;
    }
}
