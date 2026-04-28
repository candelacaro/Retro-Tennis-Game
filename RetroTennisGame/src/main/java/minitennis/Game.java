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
 * Classe Game que hereta de JPanel, funciona com a motor principal del joc.
 * @author Candela Cabello, André Medinas, Izan Perez, Daner Coria i Adrià Chenovart
 */
public class Game extends JPanel {
	private static final long serialVersionUID = 1L;

	//Instància dels elements del joc.
	//Instància d'objecte de la classe Racquet: La pala del jugador
	Racquet racquet = new Racquet(this);
	//Instància d'objecte de la classe Sound: Gestor de sons
	Sound sonido = new Sound();
	//Llista de boles actives (permet múltiples)
	List<Ball> balls = new ArrayList<>();
	//Llista d'obstacles a destruir
	List<Obstacle> obstacles = new ArrayList<>();
	//Llista de PowerUps
	List<PowerUp> powerUps = new ArrayList<>();

	//Declaració i inicialització de variable estàtica, que representa el nivell del joc
	static int level;
	//Declaració i inicialització de variable, puntuació basada en el temps
	private long score = 0;
	//Declaració i inicialització de variable, que controla la marca de temps per pujar de nivell
	private long startTime = System.currentTimeMillis();
	//Declaració i inicialització de variable, que emmagatzeman punts en el temps
	private long lastPointUpdate = System.currentTimeMillis();
	//Declaració i inicialització de variable, que serveix pel nom del jugador
	private static String playerName;
	//Declaració i inicialització de variable, per l'imatge de fons del joc
	private Image fons;

	/**
	 * Constructor del joc. Inicialitza components i escoltadors d'entrada.
	 * @param playerName, nom del jugador
	 * @param selectedLevel, nivell inicial seleccionat
	 */
	public Game(String playerName, int selectedLevel) {

		//Assignació del nom a l'atribut playerName
		this.playerName = playerName;
		//Assiganció de nivell seleccionat a la variable level
		Game.level = selectedLevel;

		//Instància d'objecte (bola) de la classe Ball
		Ball primeraBola = new Ball(this);
		//Afegim a la llista la primera bola instanciada
		balls.add(primeraBola);

		/*
		 * Estructura iterativa, que ajusta la velocitat segons 
		 * el nivell triat al menú
		 */
		for (int i = 1; i < selectedLevel; i++) {
			//Fem servir la funció increaseSpeed()
			primeraBola.increaseSpeed();
		}

		//Estructura de control d'errors TRY-CATCH, gestion el fons del videojoc
		try {
			
			/*Localitza, carrega i extreu la imatge del fitxer de fons 
			 per a la seva posterior renderització en el panell del joc. */
			fons = new ImageIcon(getClass().getResource("fondovideojuego.jpg")).getImage();
		} catch (Exception e) {
			//Si es produiex alguna mena d'excepció la capturem i mostrem el missatge
			System.out.println("No se pudo cargar la imagen de fondo.");
		}

		//Estructura iterativa que controla el nivell de dificultat
		for (int i = 1; i < selectedLevel; i++) {
			//Fem servir el mètode que incrementa la dificultat
			balls.get(0).increaseSpeed();
		}
		
		//Actualitzem els obstacles segons el nivell
		actualitzarObstacles(Game.level);
		

		//Mètode del JPanel que registra un "escoltador" per detectar quan l'usuari prem tecles.
		addKeyListener(new KeyAdapter() {
			
			/**
			 * Mètode que s'executa automàticament en el moment que es prem una tecla.
			 * Delega l'acció a la raqueta (racquet.keyPressed(e)) per iniciar el moviment.
			 */
			public void keyPressed(KeyEvent e) {
				racquet.keyPressed(e);
			}

			/**
			 * Mètode que s'executa quan l'usuari deixa anar la tecla. 
			 * Indica a la raqueta (racquet.keyReleased(e)) que s'ha d'aturar.
			 */
			public void keyReleased(KeyEvent e) {
				racquet.keyReleased(e);
			}
		});

		/*
		 * Registra un escoltador per detectar el moviment del cursor del ratolí dins 
		 * de la finestra del joc.
		 */
		addMouseMotionListener(new MouseMotionAdapter() {
			//Mètode que s'activa cada vegada que el ratolí es desplaça.
			public void mouseMoved(MouseEvent e) {
				/*
				 * Obté la coordenada horitzontal (X) del cursor i l'envia a la
				 *  raqueta per posicionar-la exactament en aquest punt.
				 */
				racquet.setMouse(e.getX());
			}
		});

		//Permet rebre focus per al teclat
		setFocusable(true);
		//Reproducció del so de fons
		sonido.playFondo();
	}

	/**
	 * Mètode que actualitza el nivell i la puntuació cada cert temps.
	 */
	private void updateLevel() {
		//Declaració i inicialització de variable que que controla la marca de temps per pujar de nivell
		long now = System.currentTimeMillis();
		//Suma de puntuació (temps en ms)
		score += (now - lastPointUpdate);
		//Actualització de temps
		lastPointUpdate = now;

		//Estructura condicional on avaluem si arribem als 20s
		if (now - startTime >= 20000) {
			//Incrementem el nivell
			level++;
			//S'actualitza el temps
			startTime = now;
			//Estructura i
			for (Ball b : balls) {
				//Incrementem la velocitat de la bola amb el mètode increaseSpeed()
				b.increaseSpeed();
			}
			//Actualitzem els obstacles segons el nivell
			actualitzarObstacles(level);
		}
	}

	/**
	 * Mètode que executa tota la lògica de moviment del frame actual
	 */
	protected void move() {
		//Estructura condicional que avalua si està fora de la pantalla
		if (getWidth() <= 0 || getHeight() <= 0) {
			return;
		}
		//Mètode updateLevel()
		updateLevel();
		//Fem servir el mètode .move() per moure la raqueta
		racquet.move();

		//Estructura condicional on si arriab al nivell 16 surt una bola nova
		if (level >= 16 && balls.size() < 2) {
			//Instància d'objecte (bola) de la classe Ball
			Ball b2 = new Ball(this);
			//Li posem velocitat a la bola
			b2.setSpeed(balls.get(level).getSpeed());
			//Afegim la nova bola a la llista
			balls.add(b2);
		}

		//Estructura iterativa que s'encarrega de moure les boles
		for (int i = 0; i < balls.size(); i++) {
			//Instància d'objecte que mira si hagut col·lisió amb un obstacle
			Obstacle chocado = balls.get(i).move(obstacles);
			//Si hagut col·lisió
			if (chocado != null)
				//Eliminem l'obstacle
				obstacles.remove(chocado);
		}

		for (Obstacle o : obstacles)
			o.move();

		// PowerUps (Nivell 16+)
		if (level >= 16 && Math.random() < 0.005) {
			powerUps.add(new PowerUp(new Random().nextInt(getWidth() - 20), 0, this));
		}

		for (int i = 0; i < powerUps.size(); i++) {
			PowerUp p = powerUps.get(i);
			p.move();
			if (p.getBounds().intersects(racquet.getBounds())) {
				multiplicarBoles();
				powerUps.remove(i);
			} else if (p.isOut()) {
				powerUps.remove(i);
			}
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(fons, 0, 0, getWidth(), getHeight(), this);

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		for (Ball b : balls)
			b.paint(g2d);
		for (PowerUp p : powerUps)
			p.paint(g2d);
		racquet.paint(g2d);
		for (Obstacle o : obstacles)
			o.paint(g2d);

		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("Arial", Font.BOLD, 12));
		g2d.drawString("Level: " + level, 10, 20);
		g2d.drawString("Points: " + score + " ms", 10, 40);
	}

	private void multiplicarBoles() {
		if (balls.size() < 6) {
			int totals = balls.size();
			double v = balls.get(0).getSpeed();
			for (int i = 0; i < totals; i++) {
				Ball n = new Ball(this);
				n.setSpeed(v);
				balls.add(n);
			}
		}
	}

	private void actualitzarObstacles(int nivellActual) {
		obstacles.clear();
		if (nivellActual < 2)
			return;
		int numObstacles = Math.min(nivellActual - 1, 10);
		Random rand = new Random();
		for (int i = 0; i < numObstacles; i++) {
			obstacles.add(new Obstacle(rand.nextInt(250) + 10, rand.nextInt(150) + 50, this, nivellActual));
		}
	}

	public void gameOver() {
		sonido.stopFondo();
		sonido.playGameOver();
		JOptionPane.showMessageDialog(this, "GAME OVER\n" + playerName + "\nScore: " + score);
		System.exit(0);
	}

	public static void main(String[] args) {
		// Aquest main és només per proves ràpides,
		// el flux real ha de venir de MainApp -> InitialWindow
		JFrame frame = new JFrame("Mini Tennis");
		Game game = new Game("Jugador", 1);
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
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}