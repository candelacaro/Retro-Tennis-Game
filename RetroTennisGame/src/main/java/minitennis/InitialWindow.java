package minitennis;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Definició de la classe InitialWindow. Aquesta classe gestiona la configuració
 * prèvia a l'execució del joc (Input de l'usuari). Actua com a pont entre la
 * lògica de configuració de llenguatge i la instanciació del joc. * @author
 * André Medinas, Candela Cabello, Daner Coria, Izan Perez i Adrià Chenovart
 * 
 * @version 1.0
 */
public class InitialWindow {
	// Atribut de referència per a la gestió de la internacionalització
	private ControlLanguage controlLang;

	/**
	 * Constructor de la classe. Instancia l'objecte ControlLanguage per gestionar
	 * els recursos de text.
	 */
	public InitialWindow() {
		this.controlLang = new ControlLanguage();
	}

	/**
	 * Mètode de control principal per mostrar la interfície de configuració.
	 * Gestiona el flux de selecció d'idioma, dades de l'usuari i nivell inicial.
	 */
	public void mostrarMenu() {
		String[] idiomes = { "Catala", "Castella", "Angles" };
		String triaIdioma = (String) JOptionPane.showInputDialog(null, "Select Language / Selecciona Idioma:",
				"Language", JOptionPane.QUESTION_MESSAGE, null, idiomes, idiomes[0]);

		if (triaIdioma == null)
			System.exit(0);

		if (triaIdioma.equals("Catala")) {
			controlLang.setIdiomaActual("CAT");
		} else if (triaIdioma.equals("Castella")) {
			controlLang.setIdiomaActual("ES");
		} else {
			controlLang.setIdiomaActual("EN");
		}

		// --- NOVA LÒGICA AMB BUCLE ---
		boolean nomValid = false;
		JTextField fieldNom = new JTextField();

		// Creem el selector de nivells (com ja tenies)
		Integer[] nivells = new Integer[30];
		for (int i = 0; i < 30; i++)
			nivells[i] = i + 1;
		JComboBox<Integer> comboNivell = new JComboBox<>(nivells);

		Object[] formulari = { controlLang.get("nom_usuari"), fieldNom, controlLang.get("nivell"), comboNivell };

		while (!nomValid) {
			int result = JOptionPane.showConfirmDialog(null, formulari, controlLang.get("titol_menu"),
					JOptionPane.OK_CANCEL_OPTION);

			if (result == JOptionPane.OK_OPTION) {
				// Verifiquem si el nom està buit (treient espais en blanc)
				if (fieldNom.getText().trim().isEmpty()) {
					// Si està buit, mostrem error segons l'idioma
					String msgError = controlLang.get("error_nom");
					JOptionPane.showMessageDialog(null, msgError, "Error", JOptionPane.ERROR_MESSAGE);
					// El bucle continuarà perquè nomValid segueix sent false
				} else {
					// Si el nom és correcte, sortim del bucle i demanem regles
					nomValid = true;

					int acceptaRegles = JOptionPane.showConfirmDialog(null, controlLang.get("regles"), "Rules",
							JOptionPane.OK_CANCEL_OPTION);

					if (acceptaRegles == JOptionPane.OK_OPTION) {
						llançarJoc(fieldNom.getText(), (int) comboNivell.getSelectedItem());
					} else {
						System.exit(0);
					}
				}
			} else {
				// Si prem Cancel·lar o tanca la finestra, sortim del programa
				System.exit(0);
			}
		}
	}

	/**
	 * Mètode privat per a la inicialització del contenidor principal i el bucle del
	 * joc. * @param nom Identificador de l'usuari.
	 * 
	 * @param nivell Valor sencer que determina la dificultat inicial.
	 */
	private void llançarJoc(String nom, int nivell) {
		// Instanciació de la classe principal del motor del joc
		Game game = new Game(nom, nivell);
		// Instanciació del contenidor de finestra
		JFrame frame = new JFrame("Retro Tenis - " + nom);
		// Addició de l'objecte 'game'
		frame.add(game);
		frame.setSize(300, 400);// Definició de les dimensions del frame
		frame.setVisible(true);// Activació de la visibilitat de la finestra
		frame.setLocationRelativeTo(null);// Centrat de la finestra a la pantalla
		// S'utilitza un javax.swing.Timer per executar tasques cada 10 mil·lisegons
		new javax.swing.Timer(10, e -> {
			game.move();// Actualització de la lògica de posicions
			game.repaint(); // Invocació del renderitzat gràfic
		}).start(); // Arrencada del fil de temps
	}
}
