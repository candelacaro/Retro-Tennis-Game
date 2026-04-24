package minitennis;

public class ControlLanguage {

	private final String IDIOMA_PER_DEFECTE = "EN";
	final String CATALA = "CAT";
	final String CASTELLA = "ES";
	final String ANGLES = "EN";

	private String idiomaActual = "";

	public void setIdiomaActual(String idioma) {
		this.idiomaActual = idioma;
	}

	public String get(String clau) {

		switch (idiomaActual) {

		case CATALA:
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
				return clau;
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
