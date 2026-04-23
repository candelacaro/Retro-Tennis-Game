package minitennis;

import java.awt.*;
import java.util.Random;

public class Particle {

    int x, y;
    int vx, vy;
    int life = 20;

    Random r = new Random();

    public Particle(int x, int y) {
        this.x = x;
        this.y = y;

        vx = r.nextInt(6) - 3;
        vy = r.nextInt(6) - 3;
    }

    public void update() {
        x += vx;
        y += vy;
        life--;
    }

    public boolean isDead() {
        return life <= 0;
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.ORANGE);
        g.fillOval(x, y, 5, 5);
    }
}