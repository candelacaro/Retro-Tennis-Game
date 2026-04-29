package minitennis.db;

import java.sql.*;

public class Connexio {

	private Connection cn = null;

	// Connexió a la base de dades
	public Connexio() {
		cn = null;
		try {

			if (cn == null || cn.isClosed()) {

				Class.forName("com.mysql.cj.jdbc.Driver");

				cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/retro_tennis", "root", "1234");
			}

		} catch (Exception ex) {

			System.out.println("Error al connectar: " + ex.getMessage());
		}

	}

	/**
	 * Guarda una partida executant el procediment emmagatzemat i mostra el TOP 10.
	 */
	public void guardarPartida(String nombre, int puntuacion, String idioma) {

		String sql = "{CALL ranking10millors(?, ?, ?)}";

		try (Connection conn = cn; CallableStatement cs = conn.prepareCall(sql)) {

			// Passem els paràmetres al procediment
			cs.setString(1, nombre);
			cs.setInt(2, puntuacion);
			cs.setString(3, idioma);

			// Executem el procediment
			boolean resultados = cs.execute();

			// Mostrem el ranking retornat pel procediment
			if (resultados) {

				ResultSet rs = cs.getResultSet();

				System.out.println("\n--- TOP 10 ---");

				while (rs.next()) {

					System.out.println("Nom: " + rs.getString("name") + " | Punts: " + rs.getInt("score") + " | Data: "
							+ rs.getTimestamp("date") + " | Idioma: " + rs.getString("language"));
				}

				rs.close();
			}

			System.out.println("Partida guardada correctament.");

		} catch (SQLException e) {

			System.out.println("Error al guardar la partida: " + e.getMessage());
		}
	}

	/**
	 * Consulta manual del TOP 10 sense guardar partida.
	 */
	public void consultarRanking() {

		String sql = "SELECT name, score, date, language " + "FROM PARTIDES " + "ORDER BY score DESC " + "LIMIT 10";

		try (Connection conn = cn; Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {

			System.out.println("\n--- RANKING ACTUAL ---");

			while (rs.next()) {

				System.out.println("Nom: " + rs.getString("name") + " | Punts: " + rs.getInt("score") + " | Data: "
						+ rs.getTimestamp("date") + " | Idioma: " + rs.getString("language"));
			}

		} catch (SQLException e) {

			System.out.println("Error al consultar la BD: " + e.getMessage());
		}
	}
}