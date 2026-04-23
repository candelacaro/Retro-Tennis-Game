package minitennis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.Window;

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
    int score = 0;

    boolean running = true;
    boolean finished = false;

    List<Particle> particles = new ArrayList<>();

    public Game(int level) {
        this.level = level;

        ball = new Ball(this);
        paddle = new Paddle(this);
        obstacles = Obstacle.createLevel(level, this);

        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                paddle.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                paddle.keyReleased(e);
            }
        });

        Sound.BACK.loop();

        new Thread(this::gameLoop).start();
    }

    // 🔁 LOOP PRINCIPAL
    void gameLoop() {
        while (running) {
            move();
            repaint();

            try {
                Thread.sleep(10);
            } catch (Exception ignored) {}
        }
    }

    // 🎮 MOVIMIENTO GENERAL
    public void move() {
        ball.move();
        paddle.move();

        for (Obstacle o : obstacles) {
            o.checkCollision(ball);
        }

        // partículas
        for (int i = 0; i < particles.size(); i++) {
            particles.get(i).update();
            if (particles.get(i).isDead()) {
                particles.remove(i);
                i--;
            }
        }

        // 🏁 comprobar nivel
        if (isLevelCompleted()) {
            levelCompleted();
        }
    }

    // 🧱 NIVEL COMPLETADO
    public void levelCompleted() {
        if (finished) return;
        finished = true;

        running = false;

        Sound.BACK.stop();

        JOptionPane.showMessageDialog(this,
                "Nivel " + level + " completado!\nScore: " + score);

        // ✔ RETORNO SEGURO AL MENU
        SwingUtilities.invokeLater(() -> {
            Window w = SwingUtilities.getWindowAncestor(this);
            if (w != null) {
                w.dispose();
            }

            new Menu();
        });
    }

    // 💀 VIDAS
    public void loseLife() {
        lives--;

        if (lives <= 0) {
            gameOver();
        } else {
            ball.reset();
        }
    }

    // ☠ GAME OVER
    public void gameOver() {
        running = false;

        Sound.BACK.stop();
        Sound.GAMEOVER.play();

        JOptionPane.showMessageDialog(this,
                "Game Over\nScore: " + score);

        SwingUtilities.invokeLater(() -> {
            Window w = SwingUtilities.getWindowAncestor(this);
            if (w != null) {
                w.dispose();
            }

            new Menu();
        });
    }

    // 🧮 NIVEL COMPLETADO CHECK
    public boolean isLevelCompleted() {
        for (Obstacle o : obstacles) {
            if (!o.destroyed) return false;
        }
        return true;
    }

    // 🧮 SCORE
    public void addScore(int value) {
        score += value;
    }

    // 💥 PARTICULAS
    public void addParticles(int x, int y) {
        for (int i = 0; i < 10; i++) {
            particles.add(new Particle(x, y));
        }
    }

    // 🎨 DIBUJO
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!running) return;

        Graphics2D g2 = (Graphics2D) g;

        ball.paint(g2);
        paddle.paint(g2);

        for (Obstacle o : obstacles) {
            if (!o.destroyed) {
                o.paint(g2);
            }
        }

        for (Particle p : particles) {
            p.draw(g2);
        }

        g2.setColor(Color.BLACK);
        g2.drawString("Score: " + score, 10, 20);
        g2.drawString("Lives: " + lives, 200, 20);
        g2.drawString("Level: " + level, 120, 20);
    }
}