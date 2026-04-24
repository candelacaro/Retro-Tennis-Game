package minitennis;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Game extends JPanel {
	
	//Instància de la bola
	Ball ball = new Ball(this);
	
	//Instància de la raqueta
	Racquet racquet = new Racquet(this);
	
	
	/**
	 * Constructors de la classe. Configura els listeners per a les entrades 
     * de teclat i rató, i estableix el focus del component.
	 */
	public Game() {
		
		//Listener de teclat: Captura quan l'usuari prem una tecla.
		addKeyListener(new KeyAdapter(){
			
			/**
			 * 
			 * @param e
			 */
			@Override
			public void keyPressed (KeyEvent e) {
				//Delegació d'esdeveniments de teclat a la classe Racquet
				racquet.keyPressed(e);
			}
			
			/**
			 * 
			 * @param e
			 */
			public void keyReleased(KeyEvent e) {
				racquet.keyReleased(e);
			}
		});
		
		//Escoltador de moviment de rató
		addMouseMotionListener(new MouseMotionAdapter() {
			
			/**
			 * 
			 * @param e
			 */
			@Override
			public void mouseMoved(MouseEvent e) {
				//Sincronització de la X del ratolí amb la raqueta
				racquet.setMouse(e.getX());
			}
		});
		
		//Requerit per capturar esdeveniments de teclat
		setFocusable(true);
	}
	
	/**
	 * Mètode privat que coordina l'actualització de la lògica del moviment de tots 
     * els components del joc (bola i raqueta)
	 */
	private void move() {
	    /*
	     * Estructura condicional on es comprova si l'altura i l'amplada és major a 0
	     * (a la superfície de la consola) i d'aquesta manera es mou la bola i la raqueta
	     */
	    if (getWidth() > 0 && getHeight() > 0) {
	        ball.move();
	        racquet.move();
	    }
	}
	
	/**
	 * Sobreescriptura del mètode de renderitzat de Swing per dibuixar els frames.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//Càsting per accedir als gràfics 2d
		Graphics2D g2d = (Graphics2D) g;
		
		/**
		 * Filtre de qualitat gràfica que fa que les figures geomètriques (cercles i rectangles) 
		 * es vegin suaus en lloc de pixelades.
		 * 
		 * RenderingHints: 
		 * KEY_ANTIALIASING: 
		 * VALUE_ANTIALIAS_ON: 
		 */
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		ball.paint(g2d);
		racquet.paint(g2d);
	}
	
	public void gameOver() {
		JOptionPane.showMessageDialog(this, "GAME-OVER", null, JOptionPane.YES_NO_OPTION);
		System.exit(0);
	}
	
	public static void main (String[] args) {
		JFrame frame = new JFrame("Mini Tennis");
		Game game = new Game();
		
		final int SIZE_WIDTH_CONSOLE = 300;
		final int SIZE_HIGHT_CONSOLE = 400;
		final int PAUSA_MILISEGONS = 10;
		
		frame.add(game);
		frame.setSize(SIZE_WIDTH_CONSOLE, SIZE_HIGHT_CONSOLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		
		while(true) {
			game.move();
			game.repaint();
			
			/*
			 * Control de frames: try-catch, per intentar que la ball
			 * tingui el millor flux possible, ni massa ràpid ni massa lent
			 */
			try {
				//
				Thread.sleep(PAUSA_MILISEGONS);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
