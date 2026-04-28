package minitennis;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.ImageIcon;

/**
 * Classe ball: Representa la pilota del joc. * @author André Medinas, Candela
 * Cabello, Daner Coria, Izan Perez i Adrià Chenovart
 */
public class Ball {

	private int x = 10;
	private int y = 10;
	private int xVel = 1;
	private int yVel = 1;

	private Game game;
	private double speed = 1.0;

	private Image imagenBall; // Variable per guardar la imatge
	private static final int DIAMETER = 50;

	/**
	 * Constructor, que inicialitza la pilota connectant-la amb la instància del joc
	 * actual.
	 */
	public Ball(Game game) {
		this.game = game;
		this.imagenBall = new ImageIcon(getClass().getResource("pacman-removebg-preview.png")).getImage();
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
		int nextX = x + (int) (xVel * speed);
		int nextY = y + (int) (yVel * speed);

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

		// Lògica de col·lisió amb la raqueta
		if (game.racquet.getBounds().intersects(new Rectangle(nextX, nextY, DIAMETER, DIAMETER))) {
			game.sonido.playGolpe();
			yVel = -Math.abs(yVel); // Rebot cap a dalt
			nextY = game.racquet.getTopY() - DIAMETER;
		}
		Obstacle objetoADestruir = null;
		// --- Lògica de col·lisió amb obstacles i DESTRUCCIÓ ---

		for (Obstacle o : obstacles) {
			if (o.getBounds().intersects(new Rectangle(nextX, nextY, DIAMETER, DIAMETER))) {

				o.recibirGolpe(); // El obstáculo pierde 1 vida

				// SOLO si la vida llega a 0, lo marcamos para destruir
				if (o.getVida() <= 0) {
					objetoADestruir = o;
				}
				Rectangle obsRect = o.getBounds();
				Rectangle ballRect = new Rectangle(nextX, nextY, DIAMETER, DIAMETER);

				// Càlcul de la penetració de l'objecte per cada costat per determinar la
				// direcció del rebot
				int penetracioDreta = ballRect.x + ballRect.width - obsRect.x;
				int penetracioEsquerra = obsRect.x + obsRect.width - ballRect.x;
				int penetracioTerra = ballRect.y + ballRect.height - obsRect.y;
				int penetracioSostre = obsRect.y + obsRect.height - ballRect.y;
				// Busquem la penetració mínima per saber quina cara de l'obstacle s'ha tocat
				int penetracioMinima = Math.min(Math.min(penetracioDreta, penetracioEsquerra),
						Math.min(penetracioSostre, penetracioTerra));
				// Inversió de velocitat i ajust de posició segons el punt de xoc
				if (penetracioMinima == penetracioDreta) {
					xVel = -Math.abs(xVel);
					nextX = obsRect.x - ballRect.width;
				} else if (penetracioMinima == penetracioEsquerra) {
					xVel = Math.abs(xVel);
					nextX = obsRect.x + obsRect.width;
				} else if (penetracioMinima == penetracioTerra) {
					yVel = -Math.abs(yVel);
					nextY = obsRect.y - ballRect.height;
				} else if (penetracioMinima == penetracioSostre) {
					yVel = Math.abs(yVel);
					nextY = obsRect.y + obsRect.height;
				}

				game.sonido.playGolpe(); // So del choque amb l'obstacle
				break; // Sortim del bucle per gestionar només una col·lisió
			}
		}

		// Actualitzem les coordenades reals
		x = nextX;
		y = nextY;

		return objetoADestruir; // Retornem l'objecte a Game
	}

	/**
	 * Mètode que s'encarrega de dibuixar la pilota en el context gràfic.
	 */
	public void paint(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(imagenBall, x, y, DIAMETER, DIAMETER, null);
	}

	/**
	 * Mètode accessor que exporta els límits de la pilota per a la detecció de
	 * xocs.
	 * 
	 * @return Rectangle amb la posició i mida actuals.
	 */
	public Rectangle getBounds() {
		return new Rectangle(x, y, DIAMETER, DIAMETER);
	}
}