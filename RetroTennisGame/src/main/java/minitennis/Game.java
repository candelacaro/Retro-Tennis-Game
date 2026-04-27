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
import javax.swing.ImageIcon;
import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * * @author dam1
 */
public class Game extends JPanel {

	private static final long serialVersionUID = 1L; // identificació unic per a la serializacio de la classe
	Ball ball = new Ball(this); // membres d'instancia
	Racquet racquet = new Racquet(this);
	Sound sonido = new Sound(); // Crea el objecte "sonido"

	List<Obstacle> obstacles = new ArrayList<>(); // Una colecció dinamica per emmagatzemar instancias d'objectes "Obstacle"

	static int level; // Atributs d'estat globals del joc

	private long score = 0;
	private long startTime = System.currentTimeMillis();
	private long lastPointUpdate = System.currentTimeMillis();
	private static String playerName;
	private Image fondo; // referencia d'objecte per a la imatge de fons

	/**
	 * Constructor de la classe Game.
	 */
	public Game(String playerName, int selectedLevel) {

		this.playerName = playerName;
		this.level = selectedLevel;
		
		// Carrega d'actius (Assets)
		try {
			fondo = new ImageIcon(getClass().getResource("fondovideojuego.jpg")).getImage();
		} catch (Exception e) {
			System.out.println("No se pudo cargar la imagen de fondo.");
		}
		
		// Iteració de control per ajustar la velocitat segons el nivell seleccionat
		for (int i = 1; i < selectedLevel; i++) {
			ball.increaseSpeed();
		}

		// Inicialitzem obstacles
		actualitzarObstacles(this.level);
		
		// Classes Anònimes Internes per a la gestió d'esdeveniments de perifèrics
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				racquet.keyPressed(e); // Delegació de l'esdeveniment a l'objecte racquet
			}
			@Override
			public void keyReleased(KeyEvent e) {
				racquet.keyReleased(e);
			}
		});
		
		// Adaptador per a la gestió de moviment del ratolí
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				racquet.setMouse(e.getX());
			}
		});
		
		// Habilita el focus del component per rebre esdeveniments de teclat
		setFocusable(true);
		// Invocació de mètode per iniciar el flux d'àudio de fons
		sonido.playFondo();
	}

	/**
	 * Gestiona la progressió temporal del joc.
	 */
	private void updateLevel() {
		long now = System.currentTimeMillis();

		// --- LÒGICA D'ACUMULACIÓ DE PUNTS ---
		long elapsed = now - lastPointUpdate;
		score += elapsed; 
		lastPointUpdate = now; 

		// --- LÒGICA DE CANVI DE NIVELL (CADA 20 SEGONS) ---
		if (now - startTime >= 20000) {
			level++;
			startTime = now; 
			ball.increaseSpeed();
			actualitzarObstacles(level);
		}
	}

	/**
	 * Mètode de coordinació del moviment.
	 */
	protected void move() {
		if (getWidth() > 0 && getHeight() > 0) { // Verificació d'existència de context gràfic

			updateLevel();

			// --- NOU: Capturem l'obstacle que la pilota ens retorna si hi ha col·lisió ---
			Obstacle chocado = ball.move(obstacles); 
			
			// Si la pilota ha col·lidit amb un obstacle, l'eliminem de la llista
			if (chocado != null) {
				obstacles.remove(chocado);
			}

			racquet.move(); // Delegació del moviment de la raqueta

			// moviment obstacles restants
			for (int i = 0; i < obstacles.size(); i++) {
				obstacles.get(i).move();
			}
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Pintem els objectes
		ball.paint(g2d);
		racquet.paint(g2d);
		for (Obstacle o : obstacles) {
			o.paint(g2d);
		}

		// --- RENDERITZAT DE TEXT (UI) ---
		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("Retro", Font.BOLD, 12));
		g2d.drawString("Level: " + level, 10, 20);
		g2d.drawString("Points: " + score + " ms", 10, 40);
	}

	public void gameOver() {
		sonido.stopFondo();
		sonido.playGameOver();
		JOptionPane.showMessageDialog(this, "GAME OVER\nJugador: " + playerName + "\nPuntuació total: " + score);
		System.exit(0);
	}

	private void actualitzarObstacles(int nivellActual) {
		obstacles.clear(); // Netegem els obstacles del nivell anterior
		int numObstacles = Math.min(nivellActual - 1, 10);
		Random rand = new Random(); 

		for (int i = 0; i < numObstacles; i++) {
			int xAleatori = rand.nextInt(250) + 10; 
			int yAleatori = rand.nextInt(150) + 50; 
			obstacles.add(new Obstacle(xAleatori, yAleatori, this));
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Mini Tennis");
		// Assignem valors per defecte si les variables estan buides
		Game game = new Game(playerName != null ? playerName : "Invitado", level > 0 ? level : 1);

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