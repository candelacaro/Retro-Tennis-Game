package minitennis;

import java.awt.*;
import java.util.Random;

public class Obstacle {

    int x, y, w = 40, h = 20;
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

        if (getBounds().intersects(ball.getBounds())) {

            ball.ya = -ball.ya;

            if (ball.y < y) {
                ball.y = y - 30;
            } else {
                ball.y = y + h + 1;
            }

            Sound.BALL.play();
        }
    }

    public static Obstacle[] createLevel(int level, Game game) {

        if (level == 1) {
            return new Obstacle[0]; 
        }

        int count;

        if (level == 2) count = 3;
        else if (level == 3) count = 6;
        else count = 12; 

        Obstacle[] obs = new Obstacle[count];
        Random r = new Random();

        for (int i = 0; i < count; i++) {

            obs[i] = new Obstacle(
                    30 + r.nextInt(200),
                    60 + r.nextInt(200),
                    game
            );
        }

        return obs;
    }
}