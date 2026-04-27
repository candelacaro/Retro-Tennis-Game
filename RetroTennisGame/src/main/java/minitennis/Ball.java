package minitennis;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;

/**
 * Classe ball: Representa la pilota del joc.
 * * @author André Medinas, Candela Cabello, Daner Coria, Izan Perez i Adrià Chenovart
 */
public class Ball {

    private int x = 10;
    private int y = 10;
    private int xVel = 1;
    private int yVel = 1;

    private static final int DIAMETER = 30;
    private Game game;
    private double speed = 1.0;

    /**
     * Constructor, que inicialitza la pilota connectant-la amb la instància del joc actual.
     */
    public Ball(Game game) {
        this.game = game;
    }

    /**
     * Mètode que incrementa la velocitat de la pilota en un 10%.
     */
    public void increaseSpeed() {
        speed *= 1.10;
    }

    /**
     * Gestiona el moviment i retorna l'obstacle col·lisionat per borrar-lo.
     */
    public Obstacle move(List<Obstacle> obstacles) {
        // Càlcul de la posició predictiva
        int nextX = x + (int)(xVel * speed);
        int nextY = y + (int)(yVel * speed);

        // --- Lògica de col·lisió amb parets (Eix X) ---
        if (nextX < 0) {
            xVel = Math.abs(xVel);
            nextX = 0;
        } else if (nextX > game.getWidth() - DIAMETER) {
            xVel = -Math.abs(xVel); 
            nextX = game.getWidth() - DIAMETER; 
        }

        // --- Lògica de col·lisió amb sostre i terra (Eix Y) ---
        if (nextY < 0) {
            yVel = Math.abs(yVel); 
            nextY = 0;
        } else if (nextY > game.getHeight() - DIAMETER) {
            game.gameOver(); // Si toca el terra, s'acaba el joc
        }

        // --- Lògica de col·lisió amb la raqueta ---
        if (game.racquet.getBounds().intersects(new Rectangle(nextX, nextY, DIAMETER, DIAMETER))) {
            game.sonido.playGolpe(); 
            yVel = -Math.abs(yVel); // Rebot cap a dalt
            nextY = game.racquet.getTopY() - DIAMETER; 
        }

        // --- Lògica de col·lisió amb obstacles i DESTRUCCIÓ ---
        Obstacle objetoADestruir = null;	

        for (Obstacle o : obstacles) {
            if (o.getBounds().intersects(new Rectangle(nextX, nextY, DIAMETER, DIAMETER))) {
                
                objetoADestruir = o; // --- NOU: Assignem l'obstacle per retornar-lo a Game ---

                Rectangle obsRect = o.getBounds();
                Rectangle ballRect = new Rectangle(nextX, nextY, DIAMETER, DIAMETER);

                // Càlcul de la penetració de l'objecte
                int penetracioDreta = ballRect.x + ballRect.width - obsRect.x;
                int penetracioEsquerra = obsRect.x + obsRect.width - ballRect.x;
                int penetracioTerra = ballRect.y + ballRect.height - obsRect.y;
                int penetracioSostre = obsRect.y + obsRect.height - ballRect.y;

                int penetracioMinima = Math.min(Math.min(penetracioDreta, penetracioEsquerra), Math.min(penetracioSostre, penetracioTerra));

                if (penetracioMinima == penetracioDreta) {
                    xVel = -Math.abs(xVel); 
                    nextX = obsRect.x - ballRect.width; 
                } 
                else if (penetracioMinima == penetracioEsquerra) {
                    xVel = Math.abs(xVel); 
                    nextX = obsRect.x + obsRect.width;
                } 
                else if (penetracioMinima == penetracioTerra) {
                    yVel = -Math.abs(yVel); 
                    nextY = obsRect.y - ballRect.height;
                } 
                else if (penetracioMinima == penetracioSostre) {
                    yVel = Math.abs(yVel); 
                    nextY = obsRect.y + obsRect.height;
                }
                
                game.sonido.playGolpe(); 
                break; // Sortim del bucle per gestionar només una col·lisió
            }
        }

        // Actualitzem les coordenades reals
        x = nextX;
        y = nextY;

        return objetoADestruir; // Retornem l'objecte a Game
    }

    public void paint(Graphics2D g) {
        g.setColor(Color.YELLOW);
        g.fillOval(x, y, DIAMETER, DIAMETER);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, DIAMETER, DIAMETER);
    }
}