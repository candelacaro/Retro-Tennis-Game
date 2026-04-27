package minitennis;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class Sound {

    private AudioClip fondo; //variable per la musica de fons
    private AudioClip golpe; //variable per al so del rebot
    private AudioClip gameover; //variable per al so de game over

    public Sound() {
        try { //carrega la musica
            fondo = cargarSonido("musica1.wav");
            golpe = cargarSonido("edr-8-bit-jump-001-171817.wav");
            gameover = cargarSonido("gameOver.wav");
        } catch (Exception e) { // excepció per si falla al carregar algu arxiu.
            System.out.println("Error cargando sonidos.");
        }
    }

    private AudioClip cargarSonido(String archivo) {
        URL url = getClass().getResource(archivo); //obté la ruta del arxiu
        return Applet.newAudioClip(url); // Converteix el arxiu en so reproduible.

    }
   
    /**
     * Reprodueix musica de fons en un bucle infinit.
     */
    
    public void playFondo() {
        if (fondo != null) {
            fondo.loop();
        }
    }

    /**
     * Reprodueix el so de cop una sola vegada
     */
    public void playGolpe() {
        if (golpe != null) {
            golpe.play();
        }
    }

    /**
     * Reprodueix el so de Game Over una vegada.
     */
    public void playGameOver() {
        if (gameover != null) {
            gameover.play();
        }
    }
    /**
     * Atura la música de fons
     */
    public void stopFondo() {
        if (fondo != null) {
            fondo.stop();
        }
    }
}