package minitennis;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;

public class Ball {

	private int x = 10;
	private int y = 10;

	private int xVel = 2;
	private int yVel = 2;

	private static final int DIAMETER = 30;

	private Game game;

	// multiplicador velocitat
	private double speed = 1.0;

	public Ball(Game game) {
		this.game = game;
	}

	// augment 10%
	public void increaseSpeed() {
		speed *= 1.10;
	}

	public void move(List<Obstacle> obstacles) {

		int nextX = x + (int)(xVel * speed);
		int nextY = y + (int)(yVel * speed);

		// parets laterals
		if (nextX < 0 || nextX > game.getWidth() - DIAMETER) {
			xVel = -xVel;
		}

		// sostre
		if (nextY < 0) {
			yVel = -yVel;
		}

		// game over
		if (nextY > game.getHeight() - DIAMETER) {
			game.gameOver();
		}

		// raqueta
		if (game.racquet.getBounds().intersects(getBounds())) {
			game.sonido.playGolpe(); // Reproduce el sonido cuando toca la raqueta

			yVel = -Math.abs(yVel);
		}

		// obstacles
		for (Obstacle o : obstacles) {
			if (o.getBounds().intersects(getBounds())) {
				xVel = -xVel;
				yVel = -yVel;
			}
		}

		x += (int)(xVel * speed);
		y += (int)(yVel * speed);
	}

	public void paint(Graphics2D g) {
		g.setColor(Color.RED);
		g.fillOval(x, y, DIAMETER, DIAMETER);
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, DIAMETER, DIAMETER);
	}
}