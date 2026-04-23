package minitennis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Ball ball;
    Paddle paddle;
    Obstacle[] obstacles;

    int speed = 1;
    int level;
    int lives = 3;
    boolean running = true;

    public Game(int level) {
        this.level = level;

        ball = new Ball(this);
        paddle = new Paddle(this);
        obstacles = Obstacle.createLevel(level, this);

        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                paddle.keyPressed(e);
            }

            public void keyReleased(KeyEvent e) {
                paddle.keyReleased(e);
            }
        });

        Sound.BACK.loop();

        new Thread(this::gameLoop).start();
    }

    void gameLoop() {
        while (running) {
            move();
            repaint();

            try {
                Thread.sleep(10);
            } catch (Exception ignored) {}
        }
    }

    public void move() {
        ball.move();
        paddle.move();

        for (Obstacle o : obstacles) {
            o.checkCollision(ball);
        }
    }
    public void loseLife() {
        lives--;

        if (lives <= 0) {
            gameOver();
        } else {
            ball.reset();
        }
    }

    public void gameOver() {
        running = false;

        Sound.BACK.stop();
        Sound.GAMEOVER.play();

        JOptionPane.showMessageDialog(this,
                "Game Over\nScore: " + speed);

        SwingUtilities.getWindowAncestor(this).dispose();

        new Menu();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        ball.paint(g2);
        paddle.paint(g2);

        if (obstacles != null) {
            for (Obstacle o : obstacles) {
                o.paint(g2);
            }
        }

        g2.drawString("Score: " + speed, 10, 20);
    }
}