package minitennis;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class Sound {

    private AudioClip fondo; //variable para la musica de fondo
    private AudioClip golpe; //variable para el sonido de rebote
    private AudioClip gameover; //variable de game over

    public Sound() {
        try { //carga musica la musica
            fondo = cargarSonido("musica1.wav");
            golpe = cargarSonido("minitennis.sound/edr-8-bit-jump-001-171817.wav");
            gameover = cargarSonido("gameOver.wav");
        } catch (Exception e) { // excepcion para que si falla algun archivo da error
            System.out.println("Error cargando sonidos.");
        }
    }

    private AudioClip cargarSonido(String archivo) {
        URL url = getClass().getResource(archivo); //obtiene la ruta de archivo
        return Applet.newAudioClip(url); // Convierte archivo en sonido reproducible

    }
   
    /**
     * Reproduce música de fondo en bucle infinito
     */
    
    public void playFondo() {
        if (fondo != null) {
            fondo.loop();
        }
    }

    /**
     * Reproduce sonido de golpe una sola vez
     */
    public void playGolpe() {
        if (golpe != null) {
            golpe.play();
        }
    }

    /**
     * Reproduce sonido de Game Over una vez
     */
    public void playGameOver() {
        if (gameover != null) {
            gameover.play();
        }
    }
    /**
     * Detiene la música de fondo
     */
    public void stopFondo() {
        if (fondo != null) {
            fondo.stop();
        }
    }
}