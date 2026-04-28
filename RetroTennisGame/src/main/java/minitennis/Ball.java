package minitennis;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;

public class Ball {
    private int x = 100;
    private int y = 100;
    private int xVel = 1;
    private int yVel = 1;
    private static final int DIAMETER = 35; // Mida del Pac-Man
    private Game game;
    private double speed = 1.0;

    public Ball(Game game) {
        this.game = game;
    }

    public void increaseSpeed() { speed *= 1.15; }
    public void setSpeed(double speed) { this.speed = speed; }
    public double getSpeed() { return speed; }

    public Obstacle move(List<Obstacle> obstacles) {
        int nextX = x + (int)(xVel * speed);
        int nextY = y + (int)(yVel * speed);

        // Rebot perfecte amb les parets (sense offsets de fotos)
        if (nextX < 0) xVel = 1;
        if (nextX > game.getWidth() - DIAMETER) xVel = -1;
        if (nextY < 0) yVel = 1;
        if (nextY > game.getHeight() - DIAMETER) game.gameOver();

        Rectangle ballRect = getBounds();

        // Col·lisió Raqueta
        if (ballRect.intersects(game.racquet.getBounds())) {
            yVel = -1;
            nextY = game.racquet.getTopY() - DIAMETER;
            game.sonido.playGolpe();
        }

        // Col·lisió Obstacles (Lògica original de penetració)
        Obstacle objetoADestruir = null;
        for (Obstacle o : obstacles) {
            Rectangle obsRect = o.getBounds();
            if (ballRect.intersects(obsRect)) {
                int pDreta = ballRect.x + ballRect.width - obsRect.x;
                int pEsquerra = obsRect.x + obsRect.width - ballRect.x;
                int pTerra = ballRect.y + ballRect.height - obsRect.y;
                int pSostre = obsRect.y + obsRect.height - ballRect.y;

                int pMin = Math.min(Math.min(pDreta, pEsquerra), Math.min(pTerra, pSostre));

                if (pMin == pDreta) xVel = -1;
                else if (pMin == pEsquerra) xVel = 1;
                else if (pMin == pTerra) yVel = -1;
                else if (pMin == pSostre) yVel = 1;

                o.ferDany(); 
                if (o.getVida() <= 0) objetoADestruir = o;
                game.sonido.playGolpe();
                break;
            }
        }
        x = nextX;
        y = nextY;
        return objetoADestruir;
    }

    public void paint(Graphics2D g) {
        g.setColor(Color.YELLOW);
        
        // Calculem l'angle de la boca segons la direcció
        int angleInici = 0;
        if (xVel > 0 && yVel > 0) angleInici = 315; // Baix-Dreta
        else if (xVel > 0 && yVel < 0) angleInici = 45;  // Dalt-Dreta
        else if (xVel < 0 && yVel > 0) angleInici = 225; // Baix-Esquerra
        else if (xVel < 0 && yVel < 0) angleInici = 135; // Dalt-Esquerra
        else if (xVel > 0) angleInici = 30;
        else angleInici = 210;

        // Dibuixem el cos (un arc de 300 graus deixa 60 per la boca)
        g.fillArc(x, y, DIAMETER, DIAMETER, angleInici, 300);

        // Dibuixem l'ull
        g.setColor(Color.BLACK);
        g.fillOval(x + (DIAMETER/2), y + (DIAMETER/5), 5, 5);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, DIAMETER, DIAMETER);
    }
}