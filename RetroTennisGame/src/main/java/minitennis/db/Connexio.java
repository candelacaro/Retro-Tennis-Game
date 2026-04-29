package minitennis.db;

import java.sql.*;

public class Connexio {

    private Connection cn = null;

    public Connexio() {
        connectar();
    }

    // Mètode separat per gestionar la connexió i reobrir-la si cal
    private void connectar() {
        try {
            if (cn == null || cn.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/retro_tennis", "root", "7022");
            }
        } catch (Exception ex) {
            System.out.println("Error al connectar: " + ex.getMessage());
        }
    }

    public String guardarPartida(String nombre, int puntuacion, String idioma) {
    	connectar(); 
        StringBuilder sb = new StringBuilder();
        String sql = "{CALL ranking10millors(?, ?, ?)}";

        try (CallableStatement cs = cn.prepareCall(sql)) {
            cs.setString(1, nombre);
            cs.setInt(2, puntuacion);
            cs.setString(3, idioma);

            boolean resultados = cs.execute();

            if (resultados) {
                try (ResultSet rs = cs.getResultSet()) {
                    int pos = 1;
                    while (rs.next()) {
                        sb.append(pos).append(". ")
                          .append(rs.getString("name")).append(" - ")
                          .append(rs.getInt("score")).append(" pts\n");
                        pos++;
                    }
                }
            } else {
                return "Partida guardada (sense rànquing disponible).";
            }
            return sb.toString();
        } catch (SQLException e) {
            return "Error al desar la partida: " + e.getMessage();
        }
    }

    public void consultarRanking() {
        connectar();
        String sql = "SELECT name, score, date, language FROM PARTIDES ORDER BY score DESC LIMIT 10";

        // El try-with-resources tancarà el Statement i el ResultSet, però NO 'cn'
        try (Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\n--- RANKING ACTUAL ---");
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
    
    
    public static void main (String[] args) {

    	Connexio c = new Connexio();



    	c.guardarPartida("JugadorTest", 5000, "ES");



    	c.consultarRanking();

    	}

    	}
