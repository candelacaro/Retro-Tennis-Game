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

public class Game extends JPanel {
	private static final long serialVersionUID = 1L;

	// Ara fem servir llistes per permetre múltiples boles
	List<Ball> balls = new ArrayList<>();
	Racquet racquet = new Racquet(this);
	Sound sonido = new Sound();
	List<Obstacle> obstacles = new ArrayList<>();
	List<PowerUp> powerUps = new ArrayList<>();

	static int level;
	private long score = 0;
	private long startTime = System.currentTimeMillis();
	private long lastPointUpdate = System.currentTimeMillis();
	private static String playerName;
	private Image [] fondos = new Image[6];

	public Game(String playerName, int selectedLevel) {

		this.playerName = playerName;
		Game.level = selectedLevel;

		Ball primeraBola = new Ball(this);
		balls.add(primeraBola);

		// Si l'usuari selecciona un nivell alt al menú,
		// hem d'aplicar l'increment de velocitat tants cops com calgui
		for (int i = 1; i < selectedLevel; i++) {
			primeraBola.increaseSpeed();
		}

		try {
			fondos[0] = new ImageIcon(getClass().getResource("fondovideojuego.jpg")).getImage();
			fondos[1] = new ImageIcon(getClass().getResource("fondovideojuego2.jpg")).getImage();
			fondos[2] = new ImageIcon(getClass().getResource("fondovideojuego3.jpg")).getImage();
			fondos[3] = new ImageIcon(getClass().getResource("fondovideojuego4.jpg")).getImage();
			fondos[4] = new ImageIcon(getClass().getResource("fondovideojuego5.jpg")).getImage();
			fondos[5] = new ImageIcon(getClass().getResource("fondovideojuego6.jpg")).getImage();		} catch (Exception e) {
			System.out.println("No se pudo cargar la imagen de fondo.");
		}

		for (int i = 1; i < selectedLevel; i++) {
			balls.get(0).increaseSpeed();
		}
		
		actualitzarObstacles(Game.level);
		

		

		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				racquet.keyPressed(e);
			}

			public void keyReleased(KeyEvent e) {
				racquet.keyReleased(e);
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				racquet.setMouse(e.getX());
			}
		});

		setFocusable(true);
		sonido.playFondo();
	}

	private void updateLevel() {
		long now = System.currentTimeMillis();
		score += (now - lastPointUpdate);
		lastPointUpdate = now;

		if (now - startTime >= 20000) {
			level++;
			startTime = now;
			for (Ball b : balls)
				b.increaseSpeed();
			actualitzarObstacles(level);
		}
	}

	protected void move() {
		if (getWidth() <= 0 || getHeight() <= 0)
			return;
		updateLevel();
		racquet.move();

		// Nivell 16: Segona bola automàtica
		if (level >= 16 && balls.size() < 2) {
			Ball b2 = new Ball(this);
			b2.setSpeed(balls.get(0).getSpeed());
			balls.add(b2);
		}

		// Moure totes les boles (Lògica original)
		for (int i = 0; i < balls.size(); i++) {
			Obstacle chocado = balls.get(i).move(obstacles);
			if (chocado != null)
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
		g2d.drawImage(getFondoActual(), 0, 0, getWidth(), getHeight(), this);

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
	
	// Va cambiant el fons cada vegada que pasen 5 nivells
		private Image getFondoActual() {
			final int CADA_CINC_NIVELL = 5;
			
			int index = (level - 1)/CADA_CINC_NIVELL;
			
			if(index >= fondos.length) {
				
				index = fondos.length - 1;
			}
			
			return fondos[index];
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