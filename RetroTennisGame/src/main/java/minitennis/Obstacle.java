package minitennis;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.net.URL;
import java.awt.Image;
import javax.swing.ImageIcon;
/**
* * @author André Medinas, Candela Cabello, Daner Coria, Izan Perez i Adrià Chenovart
*/
public class Obstacle {
   private int x;
   private int y;
   private int dx = 2;
   private static final int WIDTH = 26;
   private static final int HEIGHT = 28;
 
   private static final int IMG_WIDTH = 30;
   private static final int IMG_HEIGHT = 30;
  
   private Game game;
   private int vida = 1;
  
   //atributs imatges
   private Image imgNormal;
   private Image imgDanyat;
  
   public Obstacle(int x, int y, Game game, int nivel) {
       this.x = x;
       this.y = y;
       this.game = game;
       try {
           // Intenta obtenir la URL del fitxer dins del paquet
           URL urlNormal = getClass().getResource("fantasmaAmarillo.png");
           URL urlDanyat = getClass().getResource("fantasmaRojo.png");
          
           if (urlNormal != null) {
               imgNormal = new ImageIcon(urlNormal).getImage();
           } else {
               System.out.println("Error: No s'ha trobat l'arxiu fantasma_lila.png al paquet minitennis");
           }
          
           if (urlDanyat != null) {
               imgDanyat = new ImageIcon(urlDanyat).getImage();
           } else {
               System.out.println("Error: No s'ha trobat l'arxiu fantasma_blau.png al paquet minitennis");
           }
          
       } catch (Exception e) {
           System.out.println("Excepció carregant imatges d'obstacles: " + e.getMessage());
       }
       if (nivel >= 10) this.vida = 2;
   }
   public void move() {
       if (game.getWidth() > 0) {
           x += dx;
           if (x <= 0 || x >= game.getWidth() - WIDTH) dx *= -1;
       }
   }
   public void ferDany() { vida--; }
   public int getVida() { return vida; }
   public void paint(Graphics2D g) {
   	Image imgActual = (vida == 2) ? imgNormal : imgDanyat;
   	
   	int offsetDrawX = x - (IMG_WIDTH - WIDTH) / 2;
       int offsetDrawY = y - (IMG_HEIGHT - HEIGHT) / 2;
      
       if (imgActual != null) {
           g.drawImage(imgActual, offsetDrawX, offsetDrawY, IMG_WIDTH, IMG_HEIGHT, null);
       } else {
           // Dibuix d'emergència si la foto falla ---
           // Així no sortiran invisibles si no troba l'arxiu
           g.setColor(vida == 2 ? Color.YELLOW : new Color(180, 0, 255));
           g.fillOval(x, y, WIDTH, HEIGHT);
           g.setColor(Color.WHITE);
           g.fillOval(x + 5, y + 5, 5, 5);
           g.fillOval(x + 15, y + 5, 5, 5);
       }
   }
  
   
   public Rectangle getBounds() {
       return new Rectangle(x, y, WIDTH, HEIGHT);
   }
}

