package minitennis;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class InitialWindow {
	
	private ControlLanguage controlLang;
	
	public InitialWindow() {
		this.controlLang = new ControlLanguage();
	}
	
	public void mostrarMenu() {
		
		String[] idiomes = {"Catala", "Castella", "Angles"};
		
		String triaIdioma = (String) JOptionPane.showInputDialog(null, 
                "Select Language / Selecciona Idioma:", "Language",
                JOptionPane.QUESTION_MESSAGE, null, idiomes, idiomes[0]);
		
		if (triaIdioma == null) {
			System.exit(0);
		}
		
		if(triaIdioma.equals("Catala")) {
			controlLang.setIdiomaActual("CAT");
		} else if(triaIdioma.equals("Castella")) {
			controlLang.setIdiomaActual("ES");
		} else {
			controlLang.setIdiomaActual("EN");
		}
		
		JTextField fieldNom = new JTextField();
		
		Integer[] nivells = new Integer[100];
		for (int i = 0; i < 100; i++) {
		    nivells[i] = i + 1; 
		}
		
		// Creem el selector amb els 100 nivells
		JComboBox<Integer> comboNivell = new JComboBox<>(nivells);
		
		Object[] formulari = {
			    controlLang.get("nom_usuari"), fieldNom,
			    controlLang.get("nivell"), comboNivell
		};
		
		int result = JOptionPane.showConfirmDialog(null, formulari, 
				controlLang.get("titol_menu"), JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION && !fieldNom.getText().isEmpty()) {
            
            //Mostrar regles i acceptar per començar 
            int acceptaRegles = JOptionPane.showConfirmDialog(null, 
            		controlLang.get("regles"), "Rules", JOptionPane.OK_CANCEL_OPTION);

            if (acceptaRegles == JOptionPane.OK_OPTION) {
                llançarJoc(fieldNom.getText(), (int) comboNivell.getSelectedItem());
            }
        } else {
            System.exit(0);
        }
    }

	private void llançarJoc(String nom, int nivell) {
        JFrame frame = new JFrame("Retro Tenis - " + nom);
        Game game = new Game();
        //game.iniciarPartida(nom, nivell, controlLang); // Injectem dades i idioma
        frame.add(game);
        frame.setSize(300, 400);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        //game.arrencarBucle(); // Inicia el fil d'execució
    }
}

