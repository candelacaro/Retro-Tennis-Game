package minitennis;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

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
		//Array de String on emmagatzemem el nom dels idiomes disponibles
		String[] idiomes = { 
				"Catala", "Castella", "Angles" 
		};
		
		/**
		 * Declaració i inicialització de variable que mostra la primera finestra de 
		 * selecció d'idiomes
		 */
		String triaIdioma = (String) JOptionPane.showInputDialog(null, 
				"Select Language / Selecciona Idioma:",
				"Language", JOptionPane.QUESTION_MESSAGE, null, idiomes, idiomes[0]);

		//Estructura condicional que si l'usuari tanca la finestra d'idioma, tanquem l'aplicació
		if (triaIdioma == null) {
			System.exit(0);
		}
		
		//Estructura condicional que configura el codi d'idioma intern segons la tria
		if (triaIdioma.equals("Catala")) {
			controlLang.setIdiomaActual("CAT");
		} else if (triaIdioma.equals("Castella")) {
			controlLang.setIdiomaActual("ES");
		} else {
			controlLang.setIdiomaActual("EN");
		}

		//Declaració i incialització de variable boolean per a la segona finestra (formulari de nom)
		boolean nomValid = false;
		//Camp de text per al nom
		JTextField fieldNom = new JTextField();

		//Generem un array d'enters de l'1 al 30 per al seletor de nivells 
		Integer[] nivells = new Integer[30];
		//Estructura iterativa que recorre els nivells
		for (int i = 0; i < 30; i++) {
			nivells[i] = i + 1;
		}
		
		JComboBox<Integer> comboNivell = new JComboBox<>(nivells);
		
		//Array que agrupa els elements visuals que apareixeran al formulari
		Object[] formulari = { 
				controlLang.get("nom_usuari"), fieldNom, controlLang.get("nivell"), comboNivell 
		};

		//Estructura iterativa que valida: No sortirà d'aquí fins que el nom sigui correcte o es cancel·li
		while (!nomValid) {
			//Declaració i inicialització de variable que mostra el formulari
			int result = JOptionPane.showConfirmDialog(null, formulari, controlLang.get("titol_menu"),
					JOptionPane.OK_CANCEL_OPTION);

			//Estrcutura condicional si prem acceptar
			if (result == JOptionPane.OK_OPTION) {
				/*Verifiquem si el nom està buit, .trim().isEmpty() comprova que el nom no 
				estigui buit ni contingui només espais*/
				if (fieldNom.getText().trim().isEmpty()) {
					// Si està buit, mostrem error segons l'idioma
					String msgError = controlLang.get("error_nom");
					JOptionPane.showMessageDialog(null, msgError, "Error", JOptionPane.ERROR_MESSAGE);
					// El bucle continuarà perquè nomValid segueix sent false
				} else {
					// Si el nom és correcte, sortim del bucle i demanem regles
					nomValid = true;

					//Declaració i inicialització de varuiable per la tercera finestra de confirmació de regles
					int acceptaRegles = JOptionPane.showConfirmDialog(null, controlLang.get("regles"), "Rules",
							JOptionPane.OK_CANCEL_OPTION);

					//Estructura condicional on si accepta les regles, arrenquem el motor del joc
					if (acceptaRegles == JOptionPane.OK_OPTION) {
						llançarJoc(fieldNom.getText(), (int) comboNivell.getSelectedItem());
					} else {
						//Si no sortim
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
	 * joc. 
	 * @param nom Identificador de l'usuari.
	 * @param nivell Valor sencer que determina la dificultat inicial.
	 */
	private void llançarJoc(String nom, int nivell) {
		// Instancia de la classe principal del motor del joc
		Game game = new Game(nom, nivell);
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
