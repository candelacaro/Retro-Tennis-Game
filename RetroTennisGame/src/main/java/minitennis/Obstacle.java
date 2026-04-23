package minitennis;

import java.awt.*;

public class Obstacle {

    int x, y, w = 40, h = 20;
    boolean destroyed = false;

    Game game;

    public Obstacle(int x, int y, Game game) {
        this.x = x;
        this.y = y;
        this.game = game;
    }

    public void paint(Graphics2D g) {

        if (destroyed) return;

        g.setColor(Color.RED);
        g.fillRoundRect(x, y, w, h, 8, 8);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, w, h);
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void checkCollision(Ball ball) {

        if (destroyed) return;

        if (getBounds().intersects(ball.getBounds())) {

            destroyed = true;

            ball.ya = -ball.ya;

            game.addScore(10);
            game.addParticles(x, y);

            Sound.BALL.play();
        }
    }

    public static Obstacle[] createLevel(int level, Game game) {

        int cols = 5;
        int rows = level + 1;

        Obstacle[] obs = new Obstacle[cols * rows];

        int index = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                obs[index++] = new Obstacle(
                        30 + c * 50,
                        60 + r * 30,
                        game
                );
            }
        }

        return obs;
    }
}