package minitennis;

public class ControlLanguage {
	// Defineixen els identificadors d'idioma suportats per l'aplicació
	private final String IDIOMA_PER_DEFECTE = "EN";
	final String CATALA = "CAT";
	final String CASTELLA = "ES";
	final String ANGLES = "EN";
	//Emmagatzema el codi de l'idioma actiu en la instància
	private String idiomaActual = "";
	/**
     * Mètode mutador per definir l'idioma de treball.
     * @param idioma Codi de l'idioma
     */
	public void setIdiomaActual(String idioma) {
		this.idiomaActual = idioma; // Assignació de la referència a l'atribut d'instància
	}
	/**
     * Mètode d'accés per obtenir la traducció d'una etiqueta.
     * Implementa una lògica de selecció múltiple per resoldre
     * la cadena de text corresponent a la clau i l'idioma actual.
     * * @param clau Identificador únic del text.
     * @return El text traduït o un missatge d'error si l'idioma no existeix.
     */
	public String get(String clau) {
		// Estructura de control per a la selecció de l'idioma
		switch (idiomaActual) {

		case CATALA:
			// Estructura de control per a la resolució de la clau
			switch (clau) {
			case "titol_menu":
				return "Configuració de la Partida";
			case "regles":
				return "Regles: Mou la pala per evitar que la bola caigui a terra.";
			case "puntuacio":
				return "Puntuació: ";
			case "nivell":
				return "Nivell: ";
			case "boto_acceptar":
				return "Acceptar";
			case "nom_usuari":
				return "Introdueix el teu nom:";
			default:
				return clau; // si la clau no existeix, retorna el nom de la clau
			}

		case CASTELLA:
			switch (clau) {
			case "titol_menu":
				return "Configuración de la Partida";
			case "regles":
				return "Reglas: Mueve la pala para evitar que la bola caiga al suelo.";
			case "puntuacio":
				return "Puntuación:";
			case "nivell":
				return "Nivel: ";
			case "boto_acceptar":
				return "Aceptar";
			case "nom_usuari":
				return "Introduce tu nombre:";
			default:
				return clau;
			}

		case ANGLES:
			switch (clau) {
			case "titol_menu":
				return "Match Configuration";
			case "regles":
				return "Rules: Move the paddle to prevent the ball from hitting the ground.";
			case "puntuacio":
				return "Score:";
			case "nivell":
				return "Level: ";
			case "boto_acceptar":
				return "Aceptar";
			case "nom_usuari":
				return "Enter your name:";
			default:
				return clau;
			}
			
		default:
	        // Si per algun motiu demanem una clau que no existeix, retorna la mateixa clau
	        // per adonar-nos de l'error ràpidament.
	        return clau + ": Idioma no disponible";
		}
	
	}
}
