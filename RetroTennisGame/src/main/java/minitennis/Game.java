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
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Game extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Ball ball = new Ball(this);
	Racquet racquet = new Racquet(this);
	Sound sonido = new Sound(); //Crea objeto sonido

	List<Obstacle> obstacles = new ArrayList<>();

	int level = 1;
	long startTime = System.currentTimeMillis();

	public Game() {

		obstacles.add(new Obstacle(50, 80));
		obstacles.add(new Obstacle(150, 150));
		obstacles.add(new Obstacle(220, 120));

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

	private void updateLevel() {
		long now = System.currentTimeMillis();

		if (now - startTime >= 20000) {
			level++;                 // puja nivell
			startTime = now;

			ball.increaseSpeed();    // +10% velocitat
		}
	}

	private void move() {
		if (getWidth() > 0 && getHeight() > 0) {
			updateLevel();
			ball.move(obstacles);
			racquet.move();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		ball.paint(g2d);
		racquet.paint(g2d);

		for (Obstacle o : obstacles) {
			o.paint(g2d);
		}

		// ⭐ NIVELL A PANTALLA (AIXÒ ET FALTAVA)
		g2d.setColor(Color.BLACK);
		g2d.setFont(new Font("Arial", Font.BOLD, 18));
		g2d.drawString("Level: " + level, 10, 20);
	}

	public void gameOver() {
	    sonido.stopFondo();  //para la musica
	    sonido.playGameOver(); //reproduce una vez el sonido

	    JOptionPane.showMessageDialog(this, "GAME OVER");

	    System.exit(0);
	}
	public static void main(String[] args) {

		JFrame frame = new JFrame("Mini Tennis");
		Game game = new Game();

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