package minitennis;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;

/**
 * Classe ball que gestiona el comportament de la pilota, incloent el moviment,
 * el renderitzat i la lògica de col·lisió amb parets i obstacles.
 * @author Candela Cabello, André Medinas, Izan Perez, Daner Coria i Adrià Chenovart
 */
public class Ball {

	// Declaració i inicialització d'atributs privats
	// Coordenades actuals de la pilota en el eix de les abcisses
	private int x = 100;
	// Coordenades actuals de la pilota en el eix de les ordenades
	private int y = 100;
	// Direcció del vector de moviment en el eix de les abcisses
	private int xVel = 1;
	// Direcció del vector de moviment en el eix de les ordenades
	private int yVel = 1;

	// Declaració i inicialització de final, mida fixa de la pilota
	private static final int DIAMETER = 35;
	// Declaració i inicialització de final, velocitat que aumenta per cada nivell
	private static final double INCREMENT_VELOCITAT = 1.10;
	// Connexió amb la instància principal per accedir a l'estat del joc.
	private Game game;
	/*
	 * Declaració i inicialització d'atribut privat, multiplicador de velocitat que
	 * augmenta amb el nivell
	 */
	private double speed = 1.0;

	/**
	 * Constructor Ball
	 * 
	 * @param game, Instància del joc actual
	 */
	public Ball(Game game) {
		this.game = game;
	}

	/**
	 * Mètode que incrementa la velocitat de la pilota
	 */
	public void increaseSpeed() {
		speed *= INCREMENT_VELOCITAT;
	}

	/**
	 * Mètode que defineix la velocitat de la pilota.
	 * @param speed, valor de la velocitat
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	/**
	 * Mètode GETTER, obté la velocitat actual.
	 * @return valor de la velocitat
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * Mètode que calcula el moviment i gestiona les col·lisions amb 
	 * parets, raqueta i obstacles.
	 * @param obstacles, llista d'obstacles presents al joc.
	 * @return L'objecte Obstacle que s'ha de destruir, si n'hi ha.
	 */
	public Obstacle move(List<Obstacle> obstacles) {
		
		/*Declaració i inicialització de variable, aquest calcula la següent 
		posició segons la velocitat actual en el eix de les abcisses*/
		int nextX = x + (int) (xVel * speed);
		/*Declaració i inicialització de variable, aquest calcula la següent 
		posició segons la velocitat actual en el eix de les abcisses*/
		int nextY = y + (int) (yVel * speed);

		/*Estructura condicional on avalua la lògica de rebot amb els marges 
		 * de la finestra*/
		if (nextX < 0) {
			// Rebot paret esquerra
			xVel = 1;
		}
		if (nextX > game.getWidth() - DIAMETER) {
			// Rebot paret dreta
			xVel = -1;
		}
		if (nextY < 0) {
			// Rebot sostre
			yVel = 1;
		}
		if (nextY > game.getHeight() - DIAMETER) {
			// Si toca el terra, acaba la partida
			game.gameOver();
		}

		//Instància de Rectangle, a l'àrea de col·lisió de la pilota
		Rectangle ballRect = getBounds();

		/*Estructura condicional, on es controla lògica de 
		 * col·lisió de la raqueta */
		if (ballRect.intersects(game.racquet.getBounds())) {
			//Canvia direcció cap amunt
			yVel = -1;
			//Reposiciona per evitar solapament
			nextY = game.racquet.getTopY() - DIAMETER;
			//Reprodueix so de rebot
			game.sonido.playGolpe();
		}

		//Instància de classe Obstacle, lògica de col·lisió detallada amb obstacles
		Obstacle objetoADestruir = null;
		
		//Estructura iterativa FOR-EACH, que recorre tota l'Array
		for (Obstacle o : obstacles) {
			//Instància de Rectangle, a l'àrea de col·lisió dels obstacles
			Rectangle obsRect = o.getBounds();
			//Estructura condicional que avalua si la pilota intersecciona amb l'obstacle
			if (ballRect.intersects(obsRect)) {
				
				/*Declaració i inicialització de variable que calcula la distància entre 
				 * el marge dret de la pilota i el marge esquerre de l'obstacle. 
				 * Indica penetració des de la dreta.*/
				int pDreta = ballRect.x + ballRect.width - obsRect.x;
				
				/*Declaració i inicialització de variable que calcula la distància entre 
				 * el marge dret de l'obstacle i el marge esquerre de la pilota. 
				 * Indica penetració des de l'esquerra.*/
				int pEsquerra = obsRect.x + obsRect.width - ballRect.x;
				
				/*Declaració i inicialització de variable que calcula la penetració de la 
				 * pilota des de la part superior de l'obstacle (el "terra" per a la pilota).*/
				int pTerra = ballRect.y + ballRect.height - obsRect.y;
				
				/*Declaració i inicialització de variable que calcula la penetració de la 
				 * pilota des de la part inferior de l'obstacle (el "sostre" per a la pilota).*/
				int pSostre = obsRect.y + obsRect.height - ballRect.y;

				/*Utilitza Math.min per trobar quina de les quatre distàncies és la més petita. 
				 * La distància més petita indica el costat real on s'ha produït el xoc 
				 * (el punt de contacte inicial).
				 */
				int pMin = Math.min(Math.min(pDreta, pEsquerra), Math.min(pTerra, pSostre));

				/*Estructura condicional que segons la situació es canvia el vector de 
				 * velocitat de la pilota*/
				if (pMin == pDreta) {
					//Invertim la velocitat
					xVel = -1;
				}
				else if (pMin == pEsquerra) {
					//Invertim la velocitat
					xVel = 1;
				}
				else if (pMin == pTerra) {
					//Invertim la velocitat
					yVel = -1;
				}
				else if (pMin == pSostre) {
					//Invertim la velocitat
					yVel = 1;
				}

				//Crida al mètode de l'obstacle per reduir la seva vida.
				o.ferDany();
				
				/*Estructura condicional que si la vida de l'obstacle arriba a zero, 
				 * s'assigna a una variable per eliminar-lo de la llista del joc
				 */
				if (o.getVida() <= 0)
					//Indiquem el osbtacle
					objetoADestruir = o;
				//Reproducció de so de col·lisió
				game.sonido.playGolpe();
			}
		}
		//Corregim la posició de x
		x = nextX;
		//Corregim la posició de y
		y = nextY;
		
		//Retornem l'objecte que hem de destruir
		return objetoADestruir;
	}

	/**
	 * Mètode que dibuixa la representació gràfica de la pilota (Pac-Man) a la pantalla.
	 * L'orientació de la boca canvia dinàmicament en funció del vector de moviment.
	 * @param g, El context gràfic 2D utilitzat per pintar el component
	 */
	public void paint(Graphics2D g) {
		//Mitjançant el mètode setColor() definim el color groc 
		g.setColor(Color.YELLOW);

		/*Declaració i inicialització de variable per Lògica per calcular 
		l'orientació de la boca segons la direcció (xVel, yVel) */
		int angleInici = 0;
		
		/*Estructura condicional que realitza combinacions de direccions per determinar 
		cap on mira el personatge*/
		if (xVel > 0 && yVel > 0) {
			//Moviment diagonal cap a baix-dreta
			angleInici = 315; 
		}
		else if (xVel > 0 && yVel < 0) {
			//Moviment diagonal cap a Dalt-Dreta
			angleInici = 45; 
		}
		else if (xVel < 0 && yVel > 0) {
			//Moviment diagonal cap a Baix-Esquerra
			angleInici = 225;
		}
		else if (xVel < 0 && yVel < 0) {
			//Moviment diagonal cap a Dalt-Esquerra
			angleInici = 135; 
		}
		else if (xVel > 0) {
			//Moviment horitzontal a la dreta
			angleInici = 30;
		}
		else {
			//Moviment horitzontal a l'esquerra
			angleInici = 210;
		}

		/**
	     * Dibuixem el cos del personatge:
	     * fillArc rep: (x, y, ample, alt, angle inici, extensió de l'angle)
	     * Utilitzem 300 graus d'extensió per deixar un espai buit de 60 graus per la boca.
	     */
		g.fillArc(x, y, DIAMETER, DIAMETER, angleInici, 300);

		// Canviem el color a negre per dibuixar els detalls
		g.setColor(Color.BLACK);
		/**
	     * Dibuixem l'ull del personatge:
	     * El posicionem de forma relativa a la 'x' i 'y' actuals perquè es mogui amb el cos.
	     * Les mides de l'ull són fixes (5x5 píxels).
	     */
		g.fillOval(x + (DIAMETER / 2), y + (DIAMETER / 5), 5, 5);
	}

	/**
	 * Mètode que retorna l'àrea rectangular de la pilota per a càlculs de col·lisió.
	 * @return Objecte Rectangle.
	 */
	public Rectangle getBounds() {
		return new Rectangle(x, y, DIAMETER, DIAMETER);
	}
}