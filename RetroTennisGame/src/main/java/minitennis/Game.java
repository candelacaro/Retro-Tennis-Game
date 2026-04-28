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

	List<Obstacle> obstacles = new ArrayList<>(); // Una colecció dinamica per emmagatzemar instancias d'objectes
													// "Obstacle"

	static int level; // Atributs d'estat globals del joc

	private long score = 0;
	private long startTime = System.currentTimeMillis();
	private long lastPointUpdate = System.currentTimeMillis();
	private static String playerName;

	private Image fondo; // referencia d'objecte per a la imatge de fons

	/**
	 * Constructor de la classe Game. Configura l'estat inicial del joc, els
	 * manejadors d'esdeveniments i l'àudio.
	 * * @param playerName    Nom identificador del jugador.
	 * @param selectedLevel Nivell de dificultat inicial escollit.
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

		// Substituïm les 3 línies anteriors per la crida al nou mètode
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
	 * Gestiona la progressió temporal del joc. Actualitza el nivell i la dificultat
	 * cada interval de 20 segons.
	 */
	private void updateLevel() {
		long now = System.currentTimeMillis();

		// LÒGICA D'ACUMULACIÓ DE PUNTS
		// Calculem quants mil·lisegons han passat des de l'últim "update"
		long elapsed = now - lastPointUpdate;
		score += elapsed; // Acumulem el temps a la puntuació total (no es reinicia mai)
		lastPointUpdate = now; // Actualitzem la marca de temps per al pròxim càlcul

		// LÒGICA DE CANVI DE NIVELL
		// Controla si han passat 20 segons des de l'últim canvi de nivell
		if (now - startTime >= 20000) {
			level++;
			startTime = now; 
			ball.increaseSpeed();
			actualitzarObstacles(level);
		}
	}

	/**
	 * Mètode de coordinació del moviment. Invoca els mètodes move() de totes les
	 * instàncies que formen part del joc.
	 */
	protected void move() {
		if (getWidth() > 0 && getHeight() > 0) { // Verificació d'existència de context gràfic

			updateLevel();

			// Capturem l'obstacle per si s'ha de borrar 
			Obstacle chocado = ball.move(obstacles); 
			
			if (chocado != null) {
				obstacles.remove(chocado);
			}

			racquet.move(); // Delegació del moviment de la raqueta

			// moviment obstacles
			for (int i = 0; i < obstacles.size(); i++) {
				obstacles.get(i).move();
			}
		}
	}

	/**
	 * Sobreescriptura del mètode de renderitzat de Swing. Dibuixa tots els elements
	 * visuals en cada frame. * @param g Objecte de context gràfic.
	 */
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

		// renderizat de text
		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("Retro", Font.BOLD, 12));

		// Mostrem el Nivell actual
		g2d.drawString("Level: " + level, 10, 20);

		// Mostrem els Punts Acumulats 
		g2d.drawString("Points: " + score + " ms", 10, 40);
	}

	/**
	 * Finalitza l'execució del programa. Tanca els fluxos d'àudio i mostra la
	 * notificació final a l'usuari.
	 */
	public void gameOver() {
		sonido.stopFondo();
		sonido.playGameOver();

		// Modifiquem el missatge perquè mostri la puntuació total abans de tancar
		JOptionPane.showMessageDialog(this, "GAME OVER\nJugador: " + playerName + "\nPuntuació total: " + score);
		// Sortida forçada de la Maquina Virtual de Java
		System.exit(0);
	}

	/**
	 * Mètode auxiliar per a la instanciació d'obstacles basada en el nivell.
	 * Gestiona el cicle de vida de la llista d'obstacles. * @param nivellActual
	 * l'estat actual de la variable level per calcular la quantitat d'entitats.
	 */
	private void actualitzarObstacles(int nivellActual) {
		obstacles.clear(); // Netegem els obstacles del nivell anterior

		// Si el nivell és 1, no afegim res (numObstacles serà 0)
		// A partir del nivell 2, afegim (nivell - 1) obstacles, fins a un màxim de 10
		int numObstacles = Math.min(nivellActual - 1, 10);

		Random rand = new Random(); // Instanciació de generador de nombres pseudo-aleatoris

		for (int i = 0; i < numObstacles; i++) {
			// Generem coordenades aleatòries dins dels límits del joc (300x400)
			int xAleatori = rand.nextInt(250) + 10; // Entre 10 i 260
			int yAleatori = rand.nextInt(150) + 50; // Entre 50 i 200 (part superior)
			
			// Passem el nivell per saber si ha de tenir 2 vides 
			obstacles.add(new Obstacle(xAleatori, yAleatori, this, nivellActual));
		}
	}

	/**
	 * Punt d'entrada de l'aplicació.
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame("Mini Tennis");
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