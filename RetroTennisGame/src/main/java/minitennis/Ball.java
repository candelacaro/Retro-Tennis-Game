package minitennis;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;

public class Ball {

    private int x = 10;
    private int y = 10;

    private int xVel = 1;
    private int yVel = 1;

    private static final int DIAMETER = 30;

    private Game game;

    // multiplicador velocitat
    private double speed = 1.0;

    public Ball(Game game) {
        this.game = game;
    }

    // augment 10%
    public void increaseSpeed() {
        speed *= 1.10;
    }

    public void move(List<Obstacle> obstacles) {

        int nextX = x + (int)(xVel * speed);
        int nextY = y + (int)(yVel * speed);

        if (nextX < 0) {
            xVel = Math.abs(xVel); // Forcem que vagi cap a la dreta
            x = 0;                 // La col·loquem exactament al límit
        } else if (nextX > game.getWidth() - DIAMETER) {
            xVel = -Math.abs(xVel); // Forcem que vagi cap a l'esquerra
            x = game.getWidth() - DIAMETER; // La col·loquem al límit dret
        } else {
            x = nextX; // Si no toca paret, es mou normal
        }

        // Sostre
        if (nextY < 0) {
            yVel = Math.abs(yVel); // Forcem que vagi cap avall
            y = 0;
        } else if (nextY > game.getHeight() - DIAMETER) {
            game.gameOver();
        } else {
            y = nextY;
        }

        // game over
        if (nextY > game.getHeight() - DIAMETER) {
            game.gameOver();
        }

        // raqueta
        if (game.racquet.getBounds().intersects(getBounds())) {
            game.sonido.playGolpe(); // Reproduce el sonido cuando toca la raqueta

            yVel = -Math.abs(yVel);
        }

     // Dins de Ball.java, mètode move
        for (Obstacle o : obstacles) {
            if (o.getBounds().intersects(getBounds())) {
                Rectangle ballRect = getBounds();
                Rectangle obsRect = o.getBounds();

                // Calculem quant s'ha ficat la pilota dins de l'obstacle per cada costat
                int overlapLeft = ballRect.x + ballRect.width - obsRect.x;
                int overlapRight = obsRect.x + obsRect.width - ballRect.x;
                int overlapTop = ballRect.y + ballRect.height - obsRect.y;
                int overlapBottom = obsRect.y + obsRect.height - ballRect.y;

                // Busquem la penetració més petita per saber per quin costat ha xocat
                int minOverlap = Math.min(Math.min(overlapLeft, overlapRight), Math.min(overlapTop, overlapBottom));

                if (minOverlap == overlapLeft) {
                    xVel = -Math.abs(xVel); // Forcem rebot a l'esquerra
                    x = obsRect.x - ballRect.width; // Treiem la pilota de dins de l'obstacle
                } else if (minOverlap == overlapRight) {
                    xVel = Math.abs(xVel); // Forcem rebot a la dreta
                    x = obsRect.x + obsRect.width;
                } else if (minOverlap == overlapTop) {
                    yVel = -Math.abs(yVel); // Forcem rebot cap a dalt
                    y = obsRect.y - ballRect.height;
                } else if (minOverlap == overlapBottom) {
                    yVel = Math.abs(yVel); // Forcem rebot cap a baix
                    y = obsRect.y + obsRect.height;
                }
                
                // Opcional: reprodueix el so de cop
                game.sonido.playGolpe(); 
            }
        }

        x += (int)(xVel * speed);
        y += (int)(yVel * speed);
    }

    public void paint(Graphics2D g) {
        g.setColor(Color.YELLOW);
        g.fillOval(x, y, DIAMETER, DIAMETER);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, DIAMETER, DIAMETER);
    }
}