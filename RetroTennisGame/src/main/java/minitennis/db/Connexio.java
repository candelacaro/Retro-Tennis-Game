package minitennis.db;

import java.sql.*;
/**
 * Classe per gestionar la connexió amb MySQL i les operacions de lectura i escriptura de partides.
 */
public class Connexio {// Objecte que manté la connexió activa amb la BD
	// Constructor: s'executa en crear l'objecte i estableix el primer intent de connexió
    private Connection cn = null;

    public Connexio() {
        connectar();
    }

    /**
     * Mètode privat per carregar el controlador (driver) i obrir la connexió.
     * Gestiona la reobertura si la connexió s'ha perdut o tancat.
     */
    private void connectar() {
        try {
        	// Comprova si la connexió és nul·la o s'ha tancat per seguretat
            if (cn == null || cn.isClosed()) {
            	// Carrega dinàmicament el Driver de MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");
             // Defineix la ruta de la BD, l'usuari i la contrasenya
                cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/retro_tennis", "root", "7022");
            }
        } catch (Exception ex) {
        	// Captura qualsevol error (Driver no trobat o error de xarxa)
            System.out.println("Error al connectar: " + ex.getMessage());
        }
    }
    /**
     * Guarda una partida a la base de dades i retorna el rànquing actualitzat.
     * @param nombre Nom del jugador
     * @param puntuacion Punts aconseguits
     * @param idioma Codi d'idioma utilitzat
     * @return Cadena de text amb el rànquing dels 10 millors
     */
    public String guardarPartida(String nombre, int puntuacion, String idioma) {
    	connectar(); // Assegurem que estem connectats abans d'operar
        StringBuilder sb = new StringBuilder();// Per construir la llista de millors puntuacions
        String sql = "{CALL ranking10millors(?, ?, ?)}";// Crida al Procediment Emmagatzemat de MySQL
     // Utilitzem CallableStatement per executar el procediment de la base de dades
        try (CallableStatement cs = cn.prepareCall(sql)) {
            cs.setString(1, nombre);// Assignem el primer paràmetre
            cs.setInt(2, puntuacion);// Assignem el segon paràmetre
            cs.setString(3, idioma);// Assignem el tercer paràmetre
         // Executa la crida
            boolean resultados = cs.execute();
         // Si el procediment retorna dades (el rànquing), les processem
            if (resultados) {
                try (ResultSet rs = cs.getResultSet()) {
                    int pos = 1;
                    while (rs.next()) {
                    	// Afegim cada fila del rànquing al StringBuilder
                        sb.append(pos).append(". ")
                          .append(rs.getString("name")).append(" - ")
                          .append(rs.getInt("score")).append(" pts\n");
                        pos++;
                    }
                }
            } else {
                return "Partida guardada (sense rànquing disponible).";
            }// Retornem el rànquing complet com a text
            return sb.toString();// Retornem el rànquing complet com a text
        } catch (SQLException e) {
            return "Error al desar la partida: " + e.getMessage();
        }
    }
    /**
     * Consulta el top 10 de partides directament mitjançant una sentència SELECT.
     * Els resultats es mostren per la consola del sistema.
     */
    public void consultarRanking() {
        connectar();// Verifiquem la connexió
        String sql = "SELECT name, score, date, language FROM PARTIDES ORDER BY score DESC LIMIT 10";

     // El try-with-resources garanteix que Statement i ResultSet es tanquin automàticament
        try (Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\n--- RANKING ACTUAL ---");
         // Iterem per cada fila retornada per la consulta
            while (rs.next()) {
                System.out.println("Nom: " + rs.getString("name") +
                        " | Punts: " + rs.getInt("score") +
                        " | Data: " + rs.getTimestamp("date") +
                        " | Idioma: " + rs.getString("language"));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar la BD: " + e.getMessage());
        }
    }
    /**
     * Mètode principal per fer proves ràpides de la connexió.
     */
    
    public static void main (String[] args) {
    	// Creem la instància de connexió
    	Connexio c = new Connexio();


    	// Prova: Guardar una partida de test
    	c.guardarPartida("JugadorTest", 5000, "ES");


    	// Prova: Mostrar el rànquing per consola
    	c.consultarRanking();

    	}

    	}
