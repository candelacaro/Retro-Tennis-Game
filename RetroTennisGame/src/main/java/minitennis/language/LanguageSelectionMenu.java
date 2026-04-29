package minitennis.language;

import javax.swing.*;

import minitennis.menu.MenuRetro;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
//Definició de la classe que crea el panell de selecció d'idioma
public class LanguageSelectionMenu extends JPanel {
	// Llista d'opcions disponibles
    private String[] idiomas = {"ENGLISH", "ESPAÑOL", "CATALÀ"};
 // Índex per saber quina opció està ressaltada
    private int seleccion = 0;
 // Variable per guardar la imatge de fons del menu
    private Image fondo;
 // Constructor de la classe
    public LanguageSelectionMenu() {
        try {
        	// Intenta carregar la imatge des de la carpeta de recursos del projecte
            fondo = new ImageIcon(getClass().getResource("/Imatge/fondoMenu.gif")).getImage();
        } catch (Exception e) { }// Si hi ha un error en la càrrega, continua sense imatge
     // Estableix un color de fons negre per defecte
        setBackground(Color.BLACK);
     // Permet que aquest panell capti les pulsacions del teclat
        setFocusable(true);
     // Afegeix un "listener" per detectar quan l'usuari prem una tecla
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
            	// Si es prem la fletxa amunt, retrocedeix en la llista de forma circular
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    seleccion = (seleccion - 1 + idiomas.length) % idiomas.length;
                 // Si es prem la fletxa avall, avança en la llista de forma circular
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    seleccion = (seleccion + 1) % idiomas.length;
                 // Si es prem la tecla Enter, confirma l'idioma seleccionat
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    confirmarIdioma();
                }
                repaint(); // Actualitza visualment el panell per reflectir el canvi de selecció
            }
        });
    }
    // Mètode per aplicar l'idioma i canviar al menú principal
    private void confirmarIdioma() {
        ControlLanguage cl = new ControlLanguage();// Instància de l'objecte que controla l'idioma
     // Assigna l'idioma segons l'índex seleccionat
        if (seleccion == 0) cl.setIdiomaActual(ControlLanguage.ANGLES);
        else if (seleccion == 1) cl.setIdiomaActual(ControlLanguage.CASTELLA);
        else if (seleccion == 2) cl.setIdiomaActual(ControlLanguage.CATALA);
     // Busca la finestra principal (JFrame) que conté aquest panell
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            MenuRetro menuRetro = new MenuRetro(cl); // Crea el menú principal amb l'idioma escollit
            frame.getContentPane().removeAll();// Elimina el contingut actual de la finestra
            frame.add(menuRetro);// Afegeix el nou menú a la finestra
            frame.revalidate();// Refà el disseny dels components
            frame.repaint();// Torna a dibuixar la finestra
            menuRetro.requestFocusInWindow();// Demana el focus del teclat per al nou menú
        }
    }
 // Mètode que s'encarrega de dibuixar tots els elements gràfics
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Crida al mètode original de la superclasse
        Graphics2D g2 = (Graphics2D) g; // Converteix a Graphics2D per a funcions avançades
     // Activa el suavitzat de vores per a una millor qualitat visual
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
     // Si la imatge de fons s'ha carregat, la dibuixa i hi posa un filtre fosc
        if (fondo != null) {
            g2.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);// Dibuixa el fons
            g2.setColor(new Color(0, 0, 0, 180)); // Defineix un color negre semi-transparent
            g2.fillRect(0, 0, getWidth(), getHeight()); // Dibuixa un rectangle fosc a sobre
        }

     // Lògica per mostrar el títol segons l'idioma ressaltat (dinàmic)
        String titulo;
        if (seleccion == 0) titulo = "SELECT LANGUAGE";
        else if (seleccion == 1) titulo = "SELECCIONAR IDIOMA";
        else titulo = "SELECCIONAR IDIOMA"; // Catalán es igual en este caso, o "TRIA IDIOMA"

        g2.setColor(Color.YELLOW); // Color groc per al títol
        g2.setFont(new Font("Monospaced", Font.BOLD, 26));// Font tipus retro i mida	
        g2.drawString(titulo, 25, 80);// Dibuixa el títol a la posició indicada

     // Bucle per dibuixar les opcions d'idioma en pantalla
        g2.setFont(new Font("Monospaced", Font.BOLD, 20));
        for (int i = 0; i < idiomas.length; i++) {
            if (i == seleccion) {
                g2.setColor(Color.CYAN);// Color cian per a l'opció que estem marcant
                g2.drawString("> " + idiomas[i], 60, 180 + i * 50);// Indicador de selecció
            } else {
                g2.setColor(Color.WHITE);// Color blanc per a la resta d'opcions
                g2.drawString("  " + idiomas[i], 60, 180 + i * 50);
            }
        }
        
     // Dibuixa una petita guia d'ús a la part inferior
        g2.setFont(new Font("Monospaced", Font.PLAIN, 11));
        g2.setColor(Color.GRAY);
     // El text de la guia també canvia segons la selecció actual
        String guia = (seleccion == 0) ? "UP/DOWN to move, ENTER to select" : 
                     (seleccion == 1) ? "Flechas para mover, ENTER para elegir" : 
                                       "Fletxes per moure, ENTER per triar";
        g2.drawString(guia, 20, 360);// Dibuixa la guia inferior
    }
}