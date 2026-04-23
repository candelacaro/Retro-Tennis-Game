package minitennis;

import java.awt.*;

public class Ball {

    int x = 100, y = 100;
    int xa = 2, ya = 2;

    Game game;
    static final int SIZE = 20;

    public Ball(Game game) {
        this.game = game;
    }

    public void move() {

        int width = Math.max(game.getWidth(), 300);
        int height = Math.max(game.getHeight(), 400);

        if (x + xa < 0 || x + xa > width - SIZE) xa = -xa;
        if (y + ya < 0) ya = -ya;

        if (y + ya > height - SIZE) {
            game.loseLife();
        }

        if (getBounds().intersects(game.paddle.getBounds())) {
            ya = -Math.abs(ya);
            Sound.BALL.play();
        }

        x += xa;
        y += ya;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, SIZE, SIZE);
    }

    public void paint(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillOval(x, y, SIZE, SIZE);
    }

    public void reset() {
        x = 100;
        y = 100;
        xa = 2;
        ya = 2;
    }
}