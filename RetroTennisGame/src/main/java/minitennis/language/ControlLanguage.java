package minitennis.language;

/**
 * 
 */
public class ControlLanguage {
	// Defineixen els identificadors d'idioma suportats per l'aplicació
	private final String IDIOMA_PER_DEFECTE = "EN";
	final static String CATALA = "CAT";
	final static String CASTELLA = "ES";
	final static String ANGLES = "EN";
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
    * @param clau Identificador únic del text.
    * @return El text traduït o un missatge d'error si l'idioma no existeix.
    */
	public String get(String clau) {
		// Estructura de control per a la selecció de l'idioma
		switch (idiomaActual) {

		case CATALA:
            switch (clau) {
            case "titol_menu": return "RETRO TENNIS";
            case "regles": return "Regles: Mou la pala per evitar que la bola caigui.";
            case "nivell": return "NIVELL: ";
            case "boto_acceptar": return "JUGAR";
            case "nom_usuari": return "Introdueix el teu nom:";
            case "error_nom": return "Has d'introduir un nom!";
            case "controles": return "CONTROLS";
            case "info_controles": return "Fletxes o Ratolí per moure la pala.";
            case "cambiar_idioma": return "CANVIAR IDIOMA";
            case "sortir": return "SORTIR";
            case "guia_menu": return "Fletxes per navegar, ENTER per seleccionar";
            default: return clau;
            }
        case CASTELLA:
            switch (clau) {
            case "titol_menu": return "RETRO TENNIS";
            case "regles": return "Reglas: Mueve la pala para evitar que la bola caiga.";
            case "nivell": return "NIVEL: ";
            case "boto_acceptar": return "JUGAR";
            case "nom_usuari": return "Introduce tu nombre:";
            case "error_nom": return "¡Debes introducir un nombre!";
            case "controles": return "CONTROLES";
            case "info_controles": return "Flechas o Ratón para mover la pala.";
            case "cambiar_idioma": return "CAMBIAR IDIOMA";
            case "sortir": return "SALIR";
            case "guia_menu": return "Flechas para navegar, ENTER para seleccionar";
            default: return clau;
            }
        default: // English
            switch (clau) {
            case "titol_menu": return "RETRO TENNIS";
            case "regles": return "Rules: Move the paddle to prevent the ball from falling.";
            case "nivell": return "LEVEL: ";
            case "boto_acceptar": return "START";
            case "nom_usuari": return "Enter your name:";
            case "error_nom": return "You must enter a name!";
            case "controles": return "CONTROLS";
            case "info_controles": return "Arrows or Mouse to move the paddle.";
            case "cambiar_idioma": return "CHANGE LANGUAGE";
            case "sortir": return "EXIT";
            case "guia_menu": return "ARROWS to navigate, ENTER to select";
            default: return clau;
            }
        }
	}
}
