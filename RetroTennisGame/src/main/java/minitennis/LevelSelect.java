package minitennis;

import javax.swing.*;
import java.awt.*;

public class LevelSelect extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LevelSelect() {

        setTitle("Level Select");
        setSize(300, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new GridLayout(5,1));

        JLabel title = new JLabel("Choose level", SwingConstants.CENTER);
        add(title);

        for (int i = 1; i <= 4; i++) {

            int level = i;

            JButton btn = new JButton("Level " + i);

            btn.addActionListener(e -> {
                dispose();
                JFrame frame = new JFrame("Game");
                Game game = new Game(level);

                frame.add(game);
                frame.setSize(300, 400);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            });

            add(btn);
        }

        setVisible(true);
    }
}