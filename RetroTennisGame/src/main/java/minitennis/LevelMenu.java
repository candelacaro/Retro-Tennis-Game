package minitennis;

import javax.swing.*;
import java.awt.*;

public class LevelMenu extends JFrame {

    private static final long serialVersionUID = 1L;

    public LevelMenu() {

        setTitle(Lang.get("level_select"));
        setSize(300, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new GridLayout(5, 1));

        JLabel title = new JLabel(Lang.get("level_select"), SwingConstants.CENTER);
        add(title);

        for (int i = 1; i <= 4; i++) {

            int level = i;

            JButton btn = new JButton(Lang.get("level") + " " + i);

            btn.addActionListener(e -> startGame(level));

            add(btn);
        }

        setVisible(true);
    }

    private void startGame(int level) {

        dispose();

        JFrame frame = new JFrame("Mini Tennis - Level " + level);

        Game game = new Game(level);

        frame.setContentPane(game);
        frame.setSize(300, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        game.requestFocusInWindow();
    }
}