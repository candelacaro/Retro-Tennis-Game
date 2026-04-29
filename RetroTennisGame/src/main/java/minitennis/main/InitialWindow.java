package minitennis.main;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import minitennis.language.ControlLanguage;
import minitennis.language.LanguageSelectionMenu;

/**
 * Classe InitialWindow. Aquesta classe gestiona la configuració
 * prèvia a l'execució del joc (Input de l'usuari). Actua com a pont entre la
 * lògica de configuració de llenguatge i la instanciació del joc. 
 * 
 * * @author André Medinas, Candela Cabello, Daner Coria, Izan Perez i Adrià Chenovart
 * 
 */
public class InitialWindow {
	//Declaració i inicialització d'atribut de la classe controlLang
	private ControlLanguage controlLang;

	/**
	 * Constructor de la classe. Instancia l'objecte ControlLanguage per gestionar
	 * els recursos de text.
	 */
	public InitialWindow() {
		this.controlLang = new ControlLanguage();
	}

	/**
	 * Mètode per mostrar la interfície de configuració.
	 * Gestiona el flux de selecció d'idioma, dades de l'usuari i nivell inicial.
	 */
	public void mostrarMenu() {
		// Creem el frame principal del joc
		JFrame frame = new JFrame("Retro Tennis");
		
		// Instanciem el nostre nou panell de selecció d'idioma retro
		LanguageSelectionMenu selectionMenu = new LanguageSelectionMenu();
		
		// Configurem el frame
		frame.add(selectionMenu);
		frame.setSize(300, 400); // Mida estàndard per als teus menús
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null); // Centrat a la pantalla
		frame.setVisible(true);
		
		// Molt important: demanar el focus perquè el teclat funcioni al moment
		selectionMenu.requestFocusInWindow();
	}

	/**
	 * Mètode privat per a la inicialització del contenidor principal i el bucle del
	 * joc. 
	 * @param nom Identificador de l'usuari.
	 * @param nivell Valor sencer que determina la dificultat inicial.
	 */
	private void llançarJoc(String nom, int nivell, String language) {
		// Instancia de la classe principal del motor del joc
		Game game = new Game(nom, nivell, language);
		// Instancia del contenidor de finestra
		JFrame frame = new JFrame("Retro Tenis - " + nom);
		// Addició de l'objecte 'game' (panell del joc)
		frame.add(game);
		// Definició de les dimensions del frame
		frame.setSize(300, 400);
		// Activació de la visibilitat de la finestra
		frame.setVisible(true);
		// Centrat de la finestra a la pantalla
		frame.setLocationRelativeTo(null);
		
		// S'utilitza un javax.swing.Timer per executar tasques cada 10 mil·lisegons
		new javax.swing.Timer(10, e -> {
			// Actualització de la lògica de posicions
			game.move();
			// Invocació del renderitzat gràfic
			game.repaint(); 
		}).start(); // Arrencada del fil de temps
	}
}
