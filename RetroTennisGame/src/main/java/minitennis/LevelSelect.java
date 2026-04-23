package minitennis;

import javax.swing.*;
import java.awt.*;

public class LevelSelect extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LevelSelect() {
        setTitle("Selecciona Nivel");
        setSize(300, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(5, 1));

        JLabel title = new JLabel("Elige un nivel", SwingConstants.CENTER);
        add(title);

        for (int i = 1; i <= 4; i++) {
            int level = i;
            JButton btn = new JButton("Nivel " + i);

            btn.addActionListener(e -> {
                startGame(level);
            });

            add(btn);
        }

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