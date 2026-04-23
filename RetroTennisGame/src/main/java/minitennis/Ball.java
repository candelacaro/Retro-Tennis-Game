package minitennis;

import java.awt.*;

public class Ball {

    int x = 100, y = 100;
    int xa = 1, ya = 1;

    Game game;
    static final int SIZE = 30;

    public Ball(Game game) {
        this.game = game;
    }

    public void move() {

        if (x + xa < 0 || x + xa > game.getWidth() - SIZE)
            xa = -xa;

        if (y + ya < 0)
            ya = -ya;

        if (game.getHeight() > 0 && y + ya > game.getHeight() - SIZE) {
            game.loseLife();
        }

        Rectangle ballRect = getBounds();

        if (ballRect.intersects(game.paddle.getBounds())) {
            ya = -game.speed;
            y = game.paddle.getBounds().y - SIZE;

            game.speed++;

            Sound.BALL.play();
        }

        x += xa;
        y += ya;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, SIZE, SIZE);
    }

    public void paint(Graphics2D g) {
        g.fillOval(x, y, SIZE, SIZE);
    }

    // ✔ AHORA SÍ FUNCIONA
    public void reset() {
        x = 100;
        y = 100;
        xa = 1;
        ya = 1;
    }
}