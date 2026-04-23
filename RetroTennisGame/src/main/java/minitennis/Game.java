package minitennis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game extends JPanel {

    private static final long serialVersionUID = 1L;

    Ball ball;
    Paddle paddle;
    Obstacle[] obstacles;
    Particle[] particles = new Particle[150];

    int score = 0;
    int ballSpeed = 3;
    int level;
    int lives = 3;

    boolean running = true;

    public Game(int level) {

        this.level = level;

        setFocusable(true);
        setBackground(new Color(20, 20, 30));

        ball = new Ball(this);
        paddle = new Paddle(this);
        obstacles = Obstacle.createLevel(level, this);

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                paddle.keyPressed(e);
            }

            public void keyReleased(KeyEvent e) {
                paddle.keyReleased(e);
            }
        });

        Sound.BACK.loop();

        // ✔ GAME LOOP CORRECTO (SIN THREAD)
        Timer timer = new Timer(10, e -> gameLoop());
        timer.start();
    }

    void gameLoop() {

        if (!running) return;

        move();
        updateParticles();
        repaint();
    }

    public void move() {

        ball.move();
        paddle.move();

        boolean anyLeft = false;

        for (Obstacle o : obstacles) {
            if (!o.isDestroyed()) {
                o.checkCollision(ball);
                anyLeft = true;
            }
        }

        if (!anyLeft) levelComplete();
    }

    public void loseLife() {

        lives--;

        if (lives <= 0) {
            gameOver();
        } else {
            ball.reset();
        }
    }

    public void addScore(int points) {
        score += points;
    }

    public void addParticles(int x, int y) {

        for (int i = 0; i < particles.length; i++) {
            if (particles[i] == null) {
                particles[i] = new Particle(x, y);
                break;
            }
        }
    }

    private void updateParticles() {

        for (int i = 0; i < particles.length; i++) {
            if (particles[i] != null) {
                particles[i].update();
                if (particles[i].isDead()) particles[i] = null;
            }
        }
    }

    public void gameOver() {

        running = false;

        Sound.BACK.stop();
        Sound.GAMEOVER.play();

        JOptionPane.showMessageDialog(this,
                Lang.get("game_over") + "\nScore: " + score);

        SwingUtilities.getWindowAncestor(this).dispose();
        new Menu();
    }

    public void levelComplete() {

        running = false;

        Sound.BACK.stop();

        JOptionPane.showMessageDialog(this,
                Lang.get("level_completed") + " " + level);

        SwingUtilities.getWindowAncestor(this).dispose();
        new LevelSelect();
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // 🎮 ENTIDADES
        ball.paint(g2);
        paddle.paint(g2);

        for (Obstacle o : obstacles) o.paint(g2);
        for (Particle p : particles) if (p != null) p.paint(g2);

        drawHUD(g2);
    }

    private void drawHUD(Graphics2D g2) {

        // fondo HUD
        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRoundRect(10, 10, getWidth() - 20, 40, 20, 20);

        g2.setFont(new Font("Consolas", Font.BOLD, 14));

        drawText(g2, "Score: " + score, 25, 35, Color.CYAN);
        drawText(g2, "Level: " + level, getWidth()/2 - 30, 35, Color.ORANGE);

        drawHearts(g2);
    }

    private void drawText(Graphics2D g, String text, int x, int y, Color color) {

        g.setColor(new Color(0,0,0,120));
        g.drawString(text, x + 1, y + 1);

        g.setColor(color);
        g.drawString(text, x, y);
    }

    private void drawHearts(Graphics2D g) {

        int x = getWidth() - 120;

        for (int i = 0; i < 3; i++) {

            if (i < lives) g.setColor(Color.RED);
            else g.setColor(new Color(80,80,80));

            g.fillOval(x + i * 20, 20, 12, 12);
        }
    }
}