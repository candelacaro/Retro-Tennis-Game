package minitennis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MenuRetro extends JPanel {

    private ControlLanguage controlLang;
    private String[] opciones;
    private int seleccion = 0;
    private int nivel = 1;
    private String nombre = "";
    private Image fondo;

    public MenuRetro(ControlLanguage controlLang) {
        this.controlLang = controlLang;
        try {
            fondo = new ImageIcon(getClass().getResource("/Imatge/fondoMenu.gif")).getImage();
        } catch (Exception e) { }

        actualizarTextos();
        setBackground(Color.BLACK);
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        seleccion = (seleccion - 1 + opciones.length) % opciones.length;
                        break;
                    case KeyEvent.VK_DOWN:
                        seleccion = (seleccion + 1) % opciones.length;
                        break;
                    case KeyEvent.VK_LEFT:
                        if (seleccion == 1 && nivel > 1) nivel--;
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (seleccion == 1 && nivel < 30) nivel++;
                        break;
                    case KeyEvent.VK_ENTER:
                        ejecutarOpcion();
                        break;
                }
                repaint();
            }
        });
    }

    private void actualizarTextos() {
        opciones = new String[]{
                controlLang.get("boto_acceptar"), 
                controlLang.get("nivell") + nivel,
                controlLang.get("controles"),
                controlLang.get("cambiar_idioma"), // <--- DINÁMICO
                controlLang.get("sortir")
        };
    }

    private void ejecutarOpcion() {
        switch (seleccion) {
            case 0: // JUGAR
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                if (frame != null) {
                    // Cambiamos el panel actual por el de pedir nombre
                    NombreUsuarioMenu nombreMenu = new NombreUsuarioMenu(controlLang, nivel);
                    frame.getContentPane().removeAll();
                    frame.add(nombreMenu);
                    frame.revalidate();
                    frame.repaint();
                    nombreMenu.requestFocusInWindow();
                }
                break;
                
            case 2: // CONTROLES
                // Puedes dejar este JOptionPane o crear otro panel similar si quieres 100% retro
                JOptionPane.showMessageDialog(this, controlLang.get("info_controles"), 
                        controlLang.get("controles"), JOptionPane.INFORMATION_MESSAGE);
                break;
                
            case 3: // CAMBIAR IDIOMA
                volverAIdiomas();
                break;
                
            case 4: // SALIR
                System.exit(0);
                break;
        }
    }

    private void volverAIdiomas() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            LanguageSelectionMenu langMenu = new LanguageSelectionMenu();
            frame.getContentPane().removeAll();
            frame.add(langMenu);
            frame.revalidate();
            frame.repaint();
            langMenu.requestFocusInWindow();
        }
    }

    private void iniciarFlujoJuego() {
        nombre = JOptionPane.showInputDialog(null, controlLang.get("nom_usuari"), "Mini Tennis", JOptionPane.QUESTION_MESSAGE);
        if (nombre != null && !nombre.trim().isEmpty()) {
            if (JOptionPane.showConfirmDialog(null, controlLang.get("regles"), "Rules", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                lanzarPantallaJuego();
            }
        }
    }

    private void lanzarPantallaJuego() {
        Window win = SwingUtilities.getWindowAncestor(this);
        if (win != null) win.dispose();
        JFrame gameFrame = new JFrame("Retro Tenis - " + nombre);
        Game game = new Game(nombre, nivel, null);
        gameFrame.add(game);
        gameFrame.setSize(300, 400);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);
        new Timer(10, e -> { game.move(); game.repaint(); }).start();
        game.requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        actualizarTextos();
        Graphics2D g2 = (Graphics2D) g;
        if (fondo != null) {
            g2.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
            g2.setColor(new Color(0, 0, 0, 180)); 
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Monospaced", Font.BOLD, 30));
        g2.drawString("RETRO TENNIS", 40, 70);

        g2.setFont(new Font("Monospaced", Font.BOLD, 18));
        for (int i = 0; i < opciones.length; i++) {
            if (i == seleccion) {
                g2.setColor(Color.CYAN);
                g2.drawString("> " + opciones[i], 40, 140 + i * 45);
            } else {
                g2.setColor(Color.WHITE);
                g2.drawString("  " + opciones[i], 40, 140 + i * 45);
            }
        }
        
        g2.setFont(new Font("Monospaced", Font.PLAIN, 11));
        g2.setColor(Color.DARK_GRAY);
        g2.drawString(controlLang.get("guia_menu"), 15, 370);
    }
}