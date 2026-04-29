package minitennis.main;

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

import minitennis.db.Connexio;
import minitennis.objects.Ball;
import minitennis.objects.Obstacle;
import minitennis.objects.Racquet;
import minitennis.sound.Sound;

/**
 * Classe Game que hereta de JPanel, funciona com a motor principal del joc.
 * * @author Candela Cabello, André Medinas, Izan Perez, Daner Coria i Adrià
 * Chenovart
 */
public class Game extends JPanel {
	private static final long serialVersionUID = 1L;
	// Instància dels elements del joc.
	// Instància d'objecte de la classe Racquet: La pala del jugador
	private Racquet racquet = new Racquet(this);
	// Instància d'objecte de la classe Sound: Gestor de sons
	private Sound sonido = new Sound();
	// Llista de boles actives (permet múltiples)
	List<Ball> balls = new ArrayList<>();
	// Llista d'obstacles a destruir
	List<Obstacle> obstacles = new ArrayList<>();
	/*
	 * Declaració i inicialització de variable estàtica, que representa el nivell
	 * del joc
	 */
	public static int level;
	// Declaració i inicialització de variable, puntuació basada en el temps
	private long score = 0;
	/*
	 * Declaració i inicialització de variable, que controla la marca de temps per
	 * pujar de nivell
	 */
	private long startTime = System.currentTimeMillis();
	// Declaració i inicialització de variable, que emmagatzeman punts en el temps
	private long lastPointUpdate = System.currentTimeMillis();
	// Declaració i inicialització de variable, que serveix pel nom del jugador
	private String playerName;
	
	private String language;
	// Declaració i inicialització de variable, per l'imatge de fons del joc
	private Image fons;
	private Image[] fondos = new Image[6];

	/**
	 * Constructor del joc. Inicialitza components i escoltadors d'entrada.
	 *
	 * @param playerName,    nom del jugador
	 * @param selectedLevel, nivell inicial seleccionat
	 */
	public Game(String playerName, int selectedLevel, String language) {
		// Assignació del nom a l'atribut playerName
		this.playerName = playerName;
		
		this.language = language;
		// Assiganció de nivell seleccionat a la variable level
		Game.level = selectedLevel;
		// Instància d'objecte (bola) de la classe Ball
		Ball primeraBola = new Ball(this);
		// Afegim a la llista la primera bola instanciada
		balls.add(primeraBola);
		/*
		 * Estructura iterativa, que ajusta la velocitat segons el nivell triat al menú
		 */
		for (int i = 1; i < selectedLevel; i++) {
			// Fem servir la funció increaseSpeed()
			primeraBola.increaseSpeed();
		}
		// Estructura de control d'errors TRY-CATCH, gestion el fons del videojoc
		try {

			/*
			 * Localitza, carrega i extreu la imatge del fitxer de fons per a la seva
			 * posterior renderització en el panell del joc.
			 */
			fons = new ImageIcon(getClass().getResource("/Imatge/fondovideojuego.jpg")).getImage();
			// Càrrega de l'array de fondos
			fondos[0] = new ImageIcon(getClass().getResource("/Imatge/fondovideojuego.jpg")).getImage();
			fondos[1] = new ImageIcon(getClass().getResource("/Imatge/fondovideojuego2.jpg")).getImage();
			fondos[2] = new ImageIcon(getClass().getResource("/Imatge/fondovideojuego3.jpg")).getImage();
			fondos[3] = new ImageIcon(getClass().getResource("/Imatge/fondovideojuego4.jpg")).getImage();
			fondos[4] = new ImageIcon(getClass().getResource("/Imatge/fondovideojuego5.jpg")).getImage();
			fondos[5] = new ImageIcon(getClass().getResource("/Imatge/fondovideojuego6.jpg")).getImage();
		} catch (Exception e) {
			// Si es produiex alguna mena d'excepció la capturem i mostrem el missatge
			System.out.println("No se pudo cargar la imagen de fondo.");
		}
		// Actualitzem els obstacles segons el nivell
		actualitzarObstacles(Game.level);
		/*
		 * Mètode del JPanel que registra un "escoltador" per detectar quan l'usuari
		 * prem tecles.
		 */
		addKeyListener(new KeyAdapter() {
			/**
			 * Mètode que s'executa automàticament en el moment que es prem una tecla.
			 * Delega l'acció a la raqueta (racquet.keyPressed(e)) per iniciar el moviment.
			 */
			public void keyPressed(KeyEvent e) {
				racquet.keyPressed(e);
			}

			/**
			 * Mètode que s'executa quan l'usuari deixa anar la tecla. Indica a la raqueta
			 * (racquet.keyReleased(e)) que s'ha d'aturar.
			 */
			public void keyReleased(KeyEvent e) {
				racquet.keyReleased(e);
			}
		});
		/*
		 * Registra un escoltador per detectar el moviment del cursor del ratolí dins de
		 * la finestra del joc.
		 */
		addMouseMotionListener(new MouseMotionAdapter() {
			// Mètode que s'activa cada vegada que el ratolí es desplaça.
			public void mouseMoved(MouseEvent e) {
				/*
				 * Obté la coordenada horitzontal (X) del cursor i l'envia a la raqueta per
				 * posicionar-la exactament en aquest punt.
				 */
				racquet.setMouse(e.getX());
			}
		});
		// Permet rebre focus per al teclat
		setFocusable(true);
		// Reproducció del so de fons
		sonido.playFondo();
	}

	/**
	 * Mètode que actualitza el nivell i la puntuació cada cert temps.
	 */
	private void updateLevel() {
		/*
		 * Declaració i inicialització de variable que que controla la marca de temps
		 * per pujar de nivell
		 */
		long now = System.currentTimeMillis();
		// Suma de puntuació (temps en ms)
		score += (now - lastPointUpdate);
		// Actualització de temps
		lastPointUpdate = now;
		// Estructura condicional on avaluem si arribem als 20s
		if (now - startTime >= 20000) {
			// Incrementem el nivell
			level++;
			// S'actualitza el temps
			startTime = now;
			// Estructura iterativa que recorre la llista balls
			for (Ball b : balls) {
				double pre = b.getSpeed();
				// Incrementem la velocitat de la bola amb el mètode increaseSpeed()
				b.increaseSpeed();
				System.out.println("INCREASE SPEED: pre:" + pre + ", post:" + b.getSpeed());
			}
			// Actualitzem els obstacles segons el nivell
			actualitzarObstacles(level);
		}
		getSonido().canviarMusica(level);
	}

	/**
	 * Mètode que executa tota la lògica de moviment del frame actual
	 */
	public void move() {
		// Estructura condicional que avalua si està fora de la pantalla
		if (getWidth() <= 0 || getHeight() <= 0) {
			return;
		}
		// Mètode updateLevel()
		updateLevel();
		// Fem servir el mètode .move() per moure la raqueta
		racquet.move();
		// Estructura condicional on si arriab al nivell 16 surt una bola nova
		if (level >= 16 && balls.size() < 2) {
			// Instància d'objecte (bola) de la classe Ball
			Ball b2 = new Ball(this);
			// Li posem velocitat a la bola
			b2.setSpeed(balls.get(0).getSpeed());
			// Afegim la nova bola a la llista
			balls.add(b2);
		}
		// Estructura iterativa que s'encarrega de moure les boles
		for (int i = 0; i < balls.size(); i++) {
			// Instància d'objecte que mira si hagut col·lisió amb un obstacle
			Obstacle chocado = balls.get(i).move(obstacles);
			// Si hagut col·lisió
			if (chocado != null)
				// Eliminem l'obstacle
				obstacles.remove(chocado);
		}
		// Estructura iterativa que recorre la llista d'obstacles
		for (Obstacle o : obstacles) {
			// fem servir el mètode move() per moure el obstacle
			o.move();
		}
	}

	/**
	 * Mètode principal de renderització del component.
	 * 
	 * @param g, Objecte Graphics que permet realitzar operacions de dibuix.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		// Neteja el panell i prepara el dibuix base de la superclasse (JPanel)
		super.paintComponent(g);

		// Casting a Graphics2D per accedir a funcionalitats gràfiques
		Graphics2D g2d = (Graphics2D) g;
		// Mostra del fons mitjançant mètode drawImage, posem el fons i els límits
		g2d.drawImage(getFondoActual(), 0, 0, getWidth(), getHeight(), this);
		/*
		 * Fem servir el mètode setRenderingHint(), per renderitzar el fons i objectes i
		 * que es mostri net
		 */
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// Estructura iterativa que recorre la llista balls
		for (Ball b : balls) {
			// Fem servir el mètode .paint() per dibuixar les boles
			b.paint(g2d);
		}
		// Amb el mètode paint() pintem la raqueta
		racquet.paint(g2d);
		// Estructura iterativa que recorre la llista obstacles
		for (Obstacle o : obstacles) {
			// Amb el mètode paint(), pintem els obstacles
			o.paint(g2d);
		}
		// Posem color blanc amb el mètode setColor, per la lletra
		g2d.setColor(Color.WHITE);
		// Amb el mètode setFont posem una font Retro, la lletra en negreta i la mida
		g2d.setFont(new Font("SansSerif", Font.BOLD, 12));
		/*
		 * Dibuixem amb el mètode drawString perquè es mostri el nivell que s'està
		 * jugant
		 */
		g2d.drawString("Level: " + level, 10, 20);
		/*
		 * Dibuixem amb el mètode drawString perquè es mostri els punts que s'està
		 * jugant
		 */
		g2d.drawString("Score: " + score + " ms", 10, 40);
	}

	/**
	 * Mètode que serveix per multiplicar el número de boles
	 */
	private void multiplicarBoles() {
		// Estructura condicional que avalua si la llista balls té menys de 6
		if (balls.size() < 6) {
			/*
			 * Declaració i inicialització de variable que igualem al número d'elements de
			 * la llista
			 */
			int totals = balls.size();
			/*
			 * Declaració i inicialització de variable double que accedeix al primer element
			 * i afaga la seva velocitat
			 */
			double v = balls.get(0).getSpeed();
			// Estructura iterativa
			for (int i = 0; i < totals; i++) {
				// Instància d'objecte (bola) de la classe Ball
				Ball n = new Ball(this);
				// Modifiquem la velocitat al valor v
				n.setSpeed(v);
				// Afegim la bola a la llista ball
				balls.add(n);
			}
		}
	}

	/**
	 * Mètode que selecciona i retorna la imatge de fons corresponent al nivell
	 * actual del joc. El canvi de fons es produeix de forma escalonada cada 5
	 * nivells.
	 * 
	 * @return L'objecte Image que s'ha de renderitzar com a fons de pantalla
	 */
	private Image getFondoActual() {

		// Declaració i incialització de final, aquesta controla cada quants nivells ha
		// de canviar el fons
		final int CADA_CINC_NIVELL = 5;
		// Declaració i incialització de variable, aquesta calcula l'index de l'Array
		int index = (level - 1) / CADA_CINC_NIVELL;
		// Estructura condicional que controla que si el nivell és superior a la
		// quantitat de fons disponibles.
		if (index >= fondos.length) {
			index = fondos.length - 1;
		}
		// Estructura condicional que assegura que l'índex mai sigui negatiu.
		if (index < 0)
			index = 0;
		// Estructura condicional que comprova si existeix una imatge carregada en la
		// posició 'index'
		if (fondos[index] != null) {
			// Si la imatge existeix, la funció l'envia i s'acaba aquí
			return fondos[index];
		} else {
			// Si la posició està buida (null), enviem la imatge de reserva
			return fons;
		}
	}

	/**
	 * Mètode que actualitza els obstacles segons el nivell que s'està jugant
	 * * @param nivellActual, el nivell que s'està jugant
	 */
	private void actualitzarObstacles(int nivellActual) {
		// Amb el mètode .clear() netejem els obstacles
		obstacles.clear();
		// Estructura condicional avalua si hem arribat al nivell 2
		if (nivellActual < 2) {
			// No retornem res
			return;
		}
		/*
		 * Declaració i inicialització de variable que calcula el numObstacles, com a
		 * màxim 10. Ho controlem amb el mètode Math.min()
		 */
		int numObstacles = Math.min(nivellActual - 1, 10);
		// Declaració i incialització de random
		Random rand = new Random();
		/*
		 * Estructura iterativa que afegeix una instància d'objecte obstacle a la llista
		 * obstacles
		 */
		for (int i = 0; i < numObstacles; i++) {
			// Afegim un objecte obstacle a la llista obstacle amb coordenades aleatories
			obstacles.add(new Obstacle(rand.nextInt(250) + 10, rand.nextInt(150) + 50, this, nivellActual));
		}
	}

	/**
	 * Mètode que controla quan la bola ha caigut i perds
	 */
	public void gameOver() {
		// Reproducció de so amb mètode de la classe sound
		getSonido().stopFondo();
		// Reproducció de so gameOver
		getSonido().playGameOver();
		// Guardar partida en la BD
		Connexio c = new Connexio();
		c.guardarPartida(playerName, (int) score, language);

		JOptionPane.showMessageDialog(this, "GAME OVER\nJugador: " + playerName + "\nPuntuació total: " + score);

		System.exit(0);
	}

	public Racquet getRacquet () {
		return racquet;
	}

	public void setRacquet(Racquet racquet) {
		this.racquet = racquet;
	}

	public Sound getSonido() {
		return sonido;
	}

	public void setSonido(Sound sonido) {
		this.sonido = sonido;
	}

	

}
