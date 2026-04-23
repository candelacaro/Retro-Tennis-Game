package minitennis;

import javax.swing.*;
import java.awt.*;

public class LanguageMenu extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static String LANG = "es";

    public LanguageMenu() {

        setTitle(Lang.get("select_language"));
        setSize(300, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new GridLayout(3, 1));

        JButton es = new JButton(Lang.get("cast"));
        JButton ca = new JButton(Lang.get("cat"));
        JButton back = new JButton("Atras");

        add(es);
        add(ca);
        add(back);

        es.addActionListener(e -> {
            LANG = "es";
            openLevels();
        });

        ca.addActionListener(e -> {
            LANG = "ca";
            openLevels();
        });

        back.addActionListener(e -> {
            dispose();
            new Menu();
        });

        setVisible(true);
    }

    private void openLevels() {
        dispose();
        new LevelMenu();
    }
}