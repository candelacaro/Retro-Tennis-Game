package minitennis;

import java.awt.*;
import java.util.Random;

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
        g.setColor(Color.RED);
        g.fillRect(x, y, w, h);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, w, h);
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

        if (level == 1) {
            return createWall(0, game);
        }

        int count = level * 5;
        Obstacle[] obs = new Obstacle[count];

        Random r = new Random();

        for (int i = 0; i < count; i++) {
            obs[i] = new Obstacle(
                    20 + (i % 6) * 45,
                    60 + (i / 6) * 30,
                    game
            );
        }

        return obs;
    }

    // nivel 1 (simple)
    static Obstacle[] createWall(int level, Game game) {
        Obstacle[] obs = new Obstacle[6];

        for (int i = 0; i < obs.length; i++) {
            obs[i] = new Obstacle(20 + i * 45, 80, game);
        }

        return obs;
    }
}