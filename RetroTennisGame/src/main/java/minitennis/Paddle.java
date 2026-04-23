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

        int width = Math.max(game.getWidth(), 300);

        if (x < 0) x = 0;
        if (x > width - 60) x = width - 60;
    }

    public void paint(Graphics2D g) {
        g.setColor(Color.GREEN);
        g.fillRoundRect(x, 330, 60, 10, 10, 10);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, 330, 60, 10);
    }

    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_LEFT) xa = -6;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) xa = 6;
    }

    public void keyReleased(KeyEvent e) {
        xa = 0;
    }
}
