package minitennis;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Ball {
    
    //Declaració i inicialització d'atributs privats
	
	//Posició de la bola en el eix de les abscisses
    private int x = 10;
    //Posició de la bola en el eix de les ordenades
    private int y = 10;
    
    private int xVelocitat = 1;
    private int yVelocitat = 1;
    
    private static final int DIAMETRE = 30;
    private Game game;
    
    public Ball (Game game) {
        this.game = game;
    }
    
    public void move() {
        // Rebote paredes laterales
        if(x + xVelocitat < 0) {
            xVelocitat = Math.abs(xVelocitat); // Asegura que va a la derecha
        }
        
        if(x + xVelocitat > game.getWidth() - DIAMETRE) {
            xVelocitat = -Math.abs(xVelocitat); // Asegura que va a la izquierda
        }
        
        // Rebote techo
        if(y + yVelocitat < 0) {
            yVelocitat = Math.abs(yVelocitat);
        }
        
        // GAME-OVER (suelo)
        // Añadimos un margen para que no de error si el alto es 0 al inicio
        if(game.getHeight() > 0 && y + yVelocitat > game.getHeight() - DIAMETRE) {
            game.gameOver();
        }
        
        // Rebote con raqueta
        if(collision()) {
            yVelocitat = -Math.abs(yVelocitat); // Rebota hacia arriba
            y = game.racquet.getTopY() - DIAMETRE; // Posicionamiento de seguridad
            
            // Aumentar velocidad moderadamente
            // Solo aumentamos si la velocidad es baja para no perder el control
            if (Math.abs(xVelocitat) < 8) {
                if(xVelocitat > 0) xVelocitat++; else xVelocitat--;
                if(yVelocitat > 0) yVelocitat++; else yVelocitat--;
            }
        }
        
        x += xVelocitat;
        y += yVelocitat;
    }
    
    public void paint(Graphics2D g2d) {
        g2d.setColor(Color.RED); // Rojo para que se vea bien
        g2d.fillOval(x, y, DIAMETRE, DIAMETRE);
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, DIAMETRE, DIAMETRE);
    }
    
    private boolean collision() {
        return game.racquet.getBounds().intersects(getBounds());
    }
}