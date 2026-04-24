package minitennis;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Obstacle {

	private int x, y;
	private static final int SIZE = 30;

	public Obstacle(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void paint(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fillRect(x, y, SIZE, SIZE);
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, SIZE, SIZE);
	}
}