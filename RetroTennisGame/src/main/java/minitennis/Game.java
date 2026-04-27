package minitennis;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Game extends JPanel {

    
    private static final long serialVersionUID = 1L;
    Ball ball = new Ball(this);
    Racquet racquet = new Racquet(this);
    Sound sonido = new Sound(); //Crea objeto sonido

    List<Obstacle> obstacles = new ArrayList<>();

    static int level;
    
    long startTime = System.currentTimeMillis();
	private static String playerName;

	/**
	 * 
	 * @param playerName
	 * @param selectedLevel
	 */
    public Game(String playerName, int selectedLevel) {

    	this.playerName = playerName;
        this.level = selectedLevel;

        for (int i = 1; i < selectedLevel; i++) {
            ball.increaseSpeed();
        }
        
        this.playerName = playerName;
        this.level = selectedLevel;

        for (int i = 1; i < selectedLevel; i++) {
            ball.increaseSpeed();
        }
        
        // Substituïm les 3 línies anteriors per la crida al nou mètode
        actualitzarObstacles(this.level);
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                racquet.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                racquet.keyReleased(e);
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                racquet.setMouse(e.getX());
            }
        });

        setFocusable(true);
        
        sonido.playFondo(); //reproduce la musica

    }

    /**
     * 
     */
    private void updateLevel() {
    	long now = System.currentTimeMillis();
        if (now - startTime >= 20000) {
            level++;
            startTime = now;
            ball.increaseSpeed(); // Augmenta velocitat 10% [cite: 29]
            actualitzarObstacles(level); // Crida aquí per refrescar obstacles
        }
    }

    /**
     * 
     */
    protected void move() {
        if (getWidth() > 0 && getHeight() > 0) {

            updateLevel();

            ball.move(obstacles);
            racquet.move();

            // moviment obstacles
            for (Obstacle o : obstacles) {
                o.move();
            }
        }
    }

    /*
     * 
     */
    @Override
    protected void paintComponent(Graphics g) {
    	super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Pintem els objectes del joc
        ball.paint(g2d);
        racquet.paint(g2d);
        for (Obstacle o : obstacles) {
            o.paint(g2d);
        }

        // --- NOVA PART: Dibuixar nivell i puntuació ---
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));

        // Mostrem el Nivell
        g2d.drawString("Level: " + level, 10, 20);

        // Calculem els punts (temps transcorregut des de l'inici en ms)
        long puntuacioActual = System.currentTimeMillis() - startTime;
        
        // Mostrem els Punts (els situem una mica més a la dreta o a sota)
        g2d.drawString("Points: " + puntuacioActual + " ms", 10, 40); 
    }

    /**
     * 
     */
    public void gameOver() {
        sonido.stopFondo();  //para la musica
        sonido.playGameOver(); //reproduce una vez el sonido

        JOptionPane.showMessageDialog(this, "GAME OVER");

        System.exit(0);
    }
    
    /**
     * 
     * @param nivellActual
     */
    private void actualitzarObstacles(int nivellActual) {
    	obstacles.clear(); // Netegem els obstacles del nivell anterior

        // Si el nivell és 1, no afegim res (numObstacles serà 0)
        // A partir del nivell 2, afegim (nivell - 1) obstacles, fins a un màxim de 10
        int numObstacles = Math.min(nivellActual - 1, 10);

        Random rand = new Random();

        for (int i = 0; i < numObstacles; i++) {
            // Generem coordenades aleatòries dins dels límits del joc (300x400)
            int xAleatori = rand.nextInt(250) + 10; // Entre 10 i 260
            int yAleatori = rand.nextInt(150) + 50;  // Entre 50 i 200 (part superior)

            obstacles.add(new Obstacle(xAleatori, yAleatori, this));
        }
    }
    
    
    /**
     * 
     * @param args
     */
    public static void main(String[] args) {

        JFrame frame = new JFrame("Mini Tennis");
        Game game = new Game(playerName, level);

        frame.add(game);
        frame.setSize(300, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        while (true) {
            game.move();
            game.repaint();

            try {
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
}