package minitennis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LanguageSelectionMenu extends JPanel {

    private String[] idiomas = {"ENGLISH", "ESPAÑOL", "CATALÀ"};
    private int seleccion = 0;
    private Image fondo;

    public LanguageSelectionMenu() {
        try {
            fondo = new ImageIcon(getClass().getResource("/Imatge/fondoMenu.gif")).getImage();
        } catch (Exception e) { }

        setBackground(Color.BLACK);
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    seleccion = (seleccion - 1 + idiomas.length) % idiomas.length;
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    seleccion = (seleccion + 1) % idiomas.length;
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    confirmarIdioma();
                }
                repaint(); // Esto hará que el título cambie al mover la flecha
            }
        });
    }

    private void confirmarIdioma() {
        ControlLanguage cl = new ControlLanguage();
        if (seleccion == 0) cl.setIdiomaActual(ControlLanguage.ANGLES);
        else if (seleccion == 1) cl.setIdiomaActual(ControlLanguage.CASTELLA);
        else if (seleccion == 2) cl.setIdiomaActual(ControlLanguage.CATALA);

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            MenuRetro menuRetro = new MenuRetro(cl);
            frame.getContentPane().removeAll();
            frame.add(menuRetro);
            frame.revalidate();
            frame.repaint();
            menuRetro.requestFocusInWindow();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (fondo != null) {
            g2.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
            g2.setColor(new Color(0, 0, 0, 180)); 
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        // --- LÓGICA DEL TÍTULO DINÁMICO ---
        String titulo;
        if (seleccion == 0) titulo = "SELECT LANGUAGE";
        else if (seleccion == 1) titulo = "SELECCIONAR IDIOMA";
        else titulo = "SELECCIONAR IDIOMA"; // Catalán es igual en este caso, o "TRIA IDIOMA"

        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Monospaced", Font.BOLD, 26));
        g2.drawString(titulo, 25, 80);

        // Opciones
        g2.setFont(new Font("Monospaced", Font.BOLD, 20));
        for (int i = 0; i < idiomas.length; i++) {
            if (i == seleccion) {
                g2.setColor(Color.CYAN);
                g2.drawString("> " + idiomas[i], 60, 180 + i * 50);
            } else {
                g2.setColor(Color.WHITE);
                g2.drawString("  " + idiomas[i], 60, 180 + i * 50);
            }
        }
        
        // Guía inferior también dinámica
        g2.setFont(new Font("Monospaced", Font.PLAIN, 11));
        g2.setColor(Color.GRAY);
        String guia = (seleccion == 0) ? "UP/DOWN to move, ENTER to select" : 
                     (seleccion == 1) ? "Flechas para mover, ENTER para elegir" : 
                                       "Fletxes per moure, ENTER per triar";
        g2.drawString(guia, 20, 360);
    }
}