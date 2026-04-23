package minitennis;

import javax.swing.*;
import java.awt.*;

public class Menu extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Menu() {
        setTitle("Mini Tennis - Menu Principal");
        setSize(300, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(3, 1));

        JButton play = new JButton("Jugar");
        JButton rules = new JButton("Reglas");
        JButton exit = new JButton("Salir");

        add(play);
        add(rules);
        add(exit);

        play.addActionListener(e -> {
            dispose();
            new LevelMenu();
        });

        rules.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Reglas:\n\n" +
                    "- Usa flechas izquierda/derecha\n" +
                    "- No dejes caer la pelota\n" +
                    "- Cada rebote aumenta velocidad\n" +
                    "- Evita obstáculos en niveles altos\n");
        });

        exit.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Menu::new);
    }
}