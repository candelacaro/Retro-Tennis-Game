package minitennis;

import javax.swing.*;
import java.awt.*;

public class Menu extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Menu() {

        setTitle(Lang.get("title"));
        setSize(300, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(3,1));

        JButton play = new JButton(Lang.get("play"));
        JButton rules = new JButton(Lang.get("rules_btn"));
        JButton exit = new JButton(Lang.get("exit"));

        add(play);
        add(rules);
        add(exit);

        play.addActionListener(e -> {
            dispose();
            new LanguageMenu();
        });

        rules.addActionListener(e ->
                JOptionPane.showMessageDialog(this, Lang.get("rules")));

        exit.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Menu::new);
    }
}