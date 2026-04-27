package minitennis;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
/**
 * Definició de la classe InitialWindow.
 * Aquesta classe gestiona la configuració prèvia a l'execució del joc (Input de l'usuari).
 * Actua com a pont entre la lògica de configuració de llenguatge i la instanciació del joc.
 * * @author André Medinas, Candela Cabello, Daner Coria, Izan Perez i Adrià Chenovart
 * @version 1.0
 */
public class InitialWindow {
	// Atribut de referència per a la gestió de la internacionalització
	private ControlLanguage controlLang;
	/**
	 * Constructor de la classe.
	 * Instancia l'objecte ControlLanguage per gestionar els recursos de text.
	 */
	public InitialWindow() {
		this.controlLang = new ControlLanguage();
	}
	/**
	 * Mètode de control principal per mostrar la interfície de configuració.
	 * Gestiona el flux de selecció d'idioma, dades de l'usuari i nivell inicial.
	 */
	public void mostrarMenu() {
		// Definició d'un array de cadenes per a les opcions del selector
		String[] idiomes = {"Catala", "Castella", "Angles"};
		// Invocació d'un mètode estàtic de JOptionPane per a l'entrada de dades
		String triaIdioma = (String) JOptionPane.showInputDialog(null, 
                "Select Language / Selecciona Idioma:", "Language",
                JOptionPane.QUESTION_MESSAGE, null, idiomes, idiomes[0]);
		// Gestió de la terminació del procés si l'usuari cancel·la la finestra
		if (triaIdioma == null) {
			System.exit(0);
		}
		// Estructura de control condicional per configurar l'estat de l'objecte controlLang
		if(triaIdioma.equals("Catala")) {
			controlLang.setIdiomaActual("CAT");
		} else if(triaIdioma.equals("Castella")) {
			controlLang.setIdiomaActual("ES");
		} else {
			controlLang.setIdiomaActual("EN");
		}
		// Instanciació d'un component de Swing per a l'entrada de text
		JTextField fieldNom = new JTextField();
		// Creació d'una estructura de dades tipus Array per emmagatzemar objectes Integer
		Integer[] nivells = new Integer[100];
		for (int i = 0; i < 100; i++) {
		    nivells[i] = i + 1; // Omplim l'array amb el rang de dificultat
		}
		
		/// Instanciació d'un component JComboBox	
		JComboBox<Integer> comboNivell = new JComboBox<>(nivells);
		// Definició d'un array d'objectes per construir el formulari dinàmicament
		Object[] formulari = {
			    controlLang.get("nom_usuari"), fieldNom,
			    controlLang.get("nivell"), comboNivell
		};
		// Invocació del diàleg de confirmació per validar l'entrada de l'usuari
		int result = JOptionPane.showConfirmDialog(null, formulari, 
				controlLang.get("titol_menu"), JOptionPane.OK_CANCEL_OPTION);
		// Validació de l'opció OK i comprovació que el camp de text no estigui buit
        if (result == JOptionPane.OK_OPTION && !fieldNom.getText().isEmpty()) {
            
            //Mostrar regles i acceptar per començar 
            int acceptaRegles = JOptionPane.showConfirmDialog(null, 
            		controlLang.get("regles"), "Rules", JOptionPane.OK_CANCEL_OPTION);

            if (acceptaRegles == JOptionPane.OK_OPTION) {
            	// Invocació del mètode d'instanciació del joc amb els paràmetres recollits
                llançarJoc(fieldNom.getText(), (int) comboNivell.getSelectedItem());
            }
        } else {
        	// Sortida forçosa del sistema si no es compleixen els requisits
            System.exit(0);
        }
    }
	/**
	 * Mètode privat  per a la inicialització del contenidor principal i el bucle del joc.
	 * * @param nom Identificador de l'usuari.
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

