
package minitennis.sound;
import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;
public class Sound {
	// Atributs per emmagatzemar els fitxers d'àudio en memòria
  private AudioClip fondoActual;
  private AudioClip golpe;
  private AudioClip gameover;
//Variables per a les diferents pistes de música segons la progressió del joc
   private AudioClip musicaNivell1;
  private AudioClip musicaNivell5;
  private AudioClip musicaNivell10;
  private AudioClip musicaNivell15;
  private AudioClip musicaNivell20;
  private AudioClip musicaNivell25;
  private AudioClip musicaNivell30;
  /**
   * Constructor de la classe. S'encarrega d'inicialitzar i carregar 
   * tots els recursos d'àudio des de la carpeta de recursos.
   */
  public Sound() {
      try {
    	// Càrrega de les músiques de fons assignades a diferents etapes
          musicaNivell1 = cargarSonido("musica1.wav");
          musicaNivell5 = cargarSonido("musica2.wav");
          musicaNivell10 = cargarSonido("musica3.wav");
          musicaNivell15 = cargarSonido("musica4.wav");
          musicaNivell20 = cargarSonido("musica5.wav");
          musicaNivell25 = cargarSonido("musica6.wav");
          musicaNivell30 = cargarSonido("Base_After_Base_(Full).wav");
       // Càrrega d'efectes de game over y cop de bola
          golpe = cargarSonido("edr-8-bit-jump-001-171817.wav");
          gameover = cargarSonido("gameOver.wav");
       // Establim la pista inicial per defecte
          fondoActual = musicaNivell1;
          
      } catch (Exception e) {
          System.out.println("Error carregant sons: " + e.getMessage());
      }
  }
  /**
   * Mètode privat per carregar un fitxer d'àudio des del classpath.
   * @param archivo Nom del fitxer amb la seva extensió.
   * @return Un objecte AudioClip o null si no es troba el fitxer.
   */
  private AudioClip cargarSonido(String archivo) {
	    try {
	    	// Busquem el fitxer dins de la carpeta "Sound" situada a resources
	        URL url = getClass().getResource("/Sound/" + archivo); 
	        if (url == null) {
	            System.out.println("No se encuentra el audio: /Sound/" + archivo);
	            return null;
	        }
	     // Retornem el clip d'àudio a punt per ser reproduït
	        return Applet.newAudioClip(url);
	    } catch (Exception e) {
	        return null;
	    }
	}
  /**
   * Gestiona el canvi de música de fons de manera dinàmica segons el nivell.
   * Atura la música actual i inicia la nova només si hi ha un canvi de pista.
   * @param nivell El nivell actual del joc per determinar quina pista toca.
   */
  public void canviarMusica(int nivell) {
      AudioClip novaMusica;
   // Lògica de selecció de pista. Canvia aproximadament cada 5 nivells
      if (nivell >= 30) {
          novaMusica = musicaNivell30;
      } else if (nivell >= 26) {
          novaMusica = musicaNivell25;
      } else if (nivell >= 21) {
          novaMusica = musicaNivell20;
      } else if (nivell >= 16) {
          novaMusica = musicaNivell15;
      } else if (nivell >= 11) {
          novaMusica = musicaNivell10;
      } else if (nivell >= 6) {
          novaMusica = musicaNivell5;
      } else {
          novaMusica = musicaNivell1;
      }
   // Si la música que toca és diferent a la que sona ara, fem el canvi
      if (novaMusica != fondoActual && novaMusica != null) {
          if (fondoActual != null) fondoActual.stop();// Aturem l'actual
          fondoActual = novaMusica; // Assignem la nova
          fondoActual.loop();// La reproduïm en bucle
      }
  }
  /**
   * Inicia la reproducció en bucle de la música de fons actual.
   */
  public void playFondo() {
      if (fondoActual != null) fondoActual.loop();
  }
  /**
   * Atura completament la reproducció de la música de fons.
   */
  public void stopFondo() {
      if (fondoActual != null) fondoActual.stop();
  }
  /**
   * Reprodueix l'efecte de so de col·lisió (cop) una sola vegada.
   */
  public void playGolpe() {
      if (golpe != null) golpe.play();
  }
  /**
   * Reprodueix el so de final de partida (Game Over).
   */
  public void playGameOver() {
      if (gameover != null) gameover.play();
  }
}
