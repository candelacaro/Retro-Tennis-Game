package minitennis;

import javax.swing.*;
import java.awt.*;

public class LevelMenu extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LevelMenu() {

        setTitle("Seleccionar Nivel");
        setSize(300, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new GridLayout(5, 1));

        JLabel title = new JLabel("ELIGE NIVEL", SwingConstants.CENTER);

        JButton b1 = new JButton("Nivel 1");
        JButton b2 = new JButton("Nivel 2");
        JButton b3 = new JButton("Nivel 3");
        JButton b4 = new JButton("Nivel 4");

        add(title);
        add(b1);
        add(b2);
        add(b3);
        add(b4);

        b1.addActionListener(e -> startGame(1));
        b2.addActionListener(e -> startGame(2));
        b3.addActionListener(e -> startGame(3));
        b4.addActionListener(e -> startGame(4));

        setVisible(true);
    }

    private void startGame(int level) {

        dispose();

        JFrame frame = new JFrame("Mini Tennis - Nivel " + level);

        Game game = new Game(level);

        frame.add(game);
        frame.setSize(300, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}