package minitennis;

import java.awt.*;

public class Particle {

    int x, y;
    int vx, vy;
    int life = 20;

    public Particle(int x, int y) {

        this.x = x;
        this.y = y;

        vx = (int)(Math.random() * 6 - 3);
        vy = (int)(Math.random() * 6 - 3);
    }

    public void update() {

        x += vx;
        y += vy;
        life--;
    }

    public void paint(Graphics2D g) {

        g.setColor(Color.YELLOW);
        g.fillOval(x, y, 4, 4);
    }

    public boolean isDead() {
        return life <= 0;
    }
}