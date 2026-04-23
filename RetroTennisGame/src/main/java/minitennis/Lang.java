package minitennis;

public class Lang {

    public static String get(String key) {

        String lang = LanguageMenu.LANG;

        switch (key) {

            case "title":
                return "Mini Tennis";

            case "play":
                return "Jugar";

            case "rules_btn":
                return lang.equals("ca") ? "Regles" : "Reglas";

            case "exit":
                return lang.equals("ca") ? "Sortir" : "Salir";

            case "rules":
                return lang.equals("ca")
                        ? "- Usa les fletxes per moure\n- No deixis caure la pilota\n- Trenca els obstacles\n- Completa els nivells"
                        : "- Usa las flechas para moverte\n- No dejes caer la pelota\n- Rompe los obstáculos\n- Completa los niveles";

            case "select_language":
                return lang.equals("ca") ? "Selecciona idioma" : "Seleccionar idioma";

            case "cast":
                return "Castellano";

            case "cat":
                return "Català";

            case "level_select":
                return lang.equals("ca") ? "Selecciona nivell" : "Selecciona nivel";

            case "score":
                return lang.equals("ca") ? "Puntuació" : "Puntuación";

            case "lives":
                return lang.equals("ca") ? "Vides" : "Vidas";

            case "level":
                return lang.equals("ca") ? "Nivell" : "Nivel";

            case "game_over":
                return lang.equals("ca") ? "Fi del joc" : "Game Over";

            case "level_completed":
                return lang.equals("ca") ? "Nivell completat" : "Nivel completado";

            default:
                return key;
        }
    }
}