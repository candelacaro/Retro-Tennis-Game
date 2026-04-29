package minitennis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class NombreUsuarioMenu extends JPanel {
    private ControlLanguage controlLang;
    private int nivel;
    private StringBuilder nombre = new StringBuilder();
    private Image fondo;

    public NombreUsuarioMenu(ControlLanguage controlLang, int nivel) {
        this.controlLang = controlLang;
        this.nivel = nivel;
        try {
            fondo = new ImageIcon(getClass().getResource("/Imatge/nombreUsuario.gif")).getImage();
        } catch (Exception e) {}

        setBackground(Color.BLACK);
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (nombre.length() > 0) {
                        confirmarYEmpezar();
                    }
                } 
                else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    volverAlMenuPrincipal();
                }
                else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    if (nombre.length() > 0) {
                        nombre.deleteCharAt(nombre.length() - 1);
                    }
                } 
                else if (nombre.length() < 12) {
                    char c = e.getKeyChar();
                    if (Character.isLetterOrDigit(c) || c == ' ') {
                        nombre.append(c);
                    }
                }
                repaint();
            }
        });
    }

    private void volverAlMenuPrincipal() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            MenuRetro menuRetro = new MenuRetro(controlLang);
            frame.getContentPane().removeAll();
            frame.add(menuRetro);
            frame.revalidate();
            frame.repaint();
            menuRetro.requestFocusInWindow();
        }
    }

    private void confirmarYEmpezar() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            lanzarJuego(frame, nombre.toString(), nivel);
        }
    }

    private void lanzarJuego(JFrame frame, String nom, int niv) {
        frame.dispose(); 
        JFrame gameFrame = new JFrame("Retro Tenis - " + nom);
        Game game = new Game(nom, niv, null);
        gameFrame.add(game);
        gameFrame.setSize(300, 400);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setVisible(true);

        new Timer(10, e -> {
            game.move();
            game.repaint();
        }).start();
        game.requestFocusInWindow();
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

        // --- TRADUCCIONES APLICADAS AQUÍ ---
        
        // Título: "Introduce tu nombre" / "Enter your name" / "Introdueix el teu nom"
        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Monospaced", Font.BOLD, 20));
        g2.drawString(controlLang.get("nom_usuari"), 25, 100);

        // Caja de entrada
        g2.setColor(Color.WHITE);
        g2.drawRect(30, 150, 240, 40);
        
        // Texto escrito + cursor
        g2.setFont(new Font("Monospaced", Font.BOLD, 20));
        String cursor = (System.currentTimeMillis() % 1000 < 500) ? "_" : "";
        g2.drawString(nombre.toString() + cursor, 45, 178);

        // Guía inferior traducida (Usamos la clave "guia_menu" o creamos nuevas)
        g2.setFont(new Font("Monospaced", Font.PLAIN, 11));
        g2.setColor(Color.GRAY);
        
        // Puedes usar etiquetas directas si las añadiste a ControlLanguage:
        // "ENTER: EMPEZAR" / "ENTER: START"
        g2.drawString("ENTER: " + controlLang.get("boto_acceptar"), 30, 340);
        
        // "ESC: CANCELAR" / "ESC: CANCEL"
        g2.setColor(new Color(255, 100, 100));
        // Aquí puedes usar la clave de salir o una nueva como "cancelar"
        g2.drawString("ESC: " + controlLang.get("sortir"), 30, 360);
    }
}