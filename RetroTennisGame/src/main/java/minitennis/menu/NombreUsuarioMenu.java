package minitennis.menu;

import javax.swing.*;

import minitennis.language.ControlLanguage;
import minitennis.main.Game;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
//Classe que gestiona el menú per introduir el nom abans de jugar
public class NombreUsuarioMenu extends JPanel {
	// Referència al sistema de traducció
    private ControlLanguage controlLang;
 // Guarda el nivell seleccionat prèviament
    private int nivel;
 // Variable dinàmica per construir el nom
    private StringBuilder nombre = new StringBuilder();
 // Imatge de fons del menú
    private Image fondo;
 // Constructor que rep l'idioma i el nivell seleccionat
    public NombreUsuarioMenu(ControlLanguage controlLang, int nivel) {
        this.controlLang = controlLang;
        this.nivel = nivel;
        try {
        	// Carrega l'animació GIF de fons des dels recursos
            fondo = new ImageIcon(getClass().getResource("/Imatge/nombreUsuario.gif")).getImage();
        } catch (Exception e) {}

        setBackground(Color.BLACK);// Color de fons de seguretat
        setFocusable(true);// Permet que el panell rebi esdeveniments de teclat
     // Escoltador de tecles per gestionar l'escriptura
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
            	// Si es prem ENTER i s'ha escrit alguna cosa, es confirma el nom i comença el joc
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (nombre.length() > 0) {
                        confirmarYEmpezar();
                    }
                } 
             // Si es prem ESC, torna al menú principal
                else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    volverAlMenuPrincipal();
                }
             // Si es prem BACKSPACE, s'esborra l'últim caràcter
                else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    if (nombre.length() > 0) {
                        nombre.deleteCharAt(nombre.length() - 1);
                    }
                } 
             // Si el nom té menys de 12 caràcters, s'afegeix la lletra o número premut
                else if (nombre.length() < 12) {
                    char c = e.getKeyChar();
                 // Filtre per permetre només lletres, números o espais
                    if (Character.isLetterOrDigit(c) || c == ' ') {
                        nombre.append(c);
                    }
                }
             // Actualitza la pantalla per mostrar el nom mentre s'escriu
                repaint(); 
            }
        });
    }
 // Mètode per tornar al menú principal
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
 // Mètode per confirmar el nom i llançar la finestra del joc
    private void confirmarYEmpezar() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            lanzarJuego(frame, nombre.toString(), nivel);
        }
    }
 // Crea la finestra definitiva del joc i inicia el bucle
    private void lanzarJuego(JFrame frame, String nom, int niv) {
        frame.dispose(); // Tanca el menú de selecció
        JFrame gameFrame = new JFrame("Retro Tenis - " + nom);
        Game game = new Game(nom, niv, null); // Instància de la lògica del joc
        gameFrame.add(game);
        gameFrame.setSize(300, 400);
        gameFrame.setLocationRelativeTo(null); // Centra la finestra a la pantalla
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setVisible(true);
     // Bucle principal: cada 10ms mou els objectes i torna a dibuixar
        new Timer(10, e -> {
            game.move();
            game.repaint();
        }).start();
        game.requestFocusInWindow();// Posa el focus en el joc per poder moure la pala
    }
 // Mètode per dibuixar la interfície gràfica del menú
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
     // Activa el suavitzat per a un text més nítid
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
     // Dibuixa la imatge de fons amb una capa negra semi-transparent a sobre
        if (fondo != null) {
            g2.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
            g2.setColor(new Color(0, 0, 0, 180));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

     // Títol principal traduït dinàmicament
        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Monospaced", Font.BOLD, 20));
        g2.drawString(controlLang.get("nom_usuari"), 25, 100);

     // Dibuixa el rectangle blanc on s'escriu el nom
        g2.setColor(Color.WHITE);
        g2.drawRect(30, 150, 240, 40);
        
     // Dibuixa el nom escrit i un cursor que parpelleja segons el temps del sistema
        g2.setFont(new Font("Monospaced", Font.BOLD, 20));
        String cursor = (System.currentTimeMillis() % 1000 < 500) ? "_" : "";
        g2.drawString(nombre.toString() + cursor, 45, 178);

     // Guia inferior d'accions traduïda
        g2.setFont(new Font("Monospaced", Font.PLAIN, 11));
        g2.setColor(Color.GRAY);
        
     // Mostra l'opció per començar usant la traducció del botó acceptar
        g2.drawString("ENTER: " + controlLang.get("boto_acceptar"), 30, 340);
        
     // Mostra l'opció per cancel·lar en color vermellós
        g2.setColor(new Color(255, 100, 100));
        g2.drawString("ESC: " + controlLang.get("sortir"), 30, 360);
    }
}