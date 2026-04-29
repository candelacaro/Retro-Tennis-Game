package minitennis;
import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;
public class Sound {
   private AudioClip fondoActual;
   private AudioClip golpe;
   private AudioClip gameover;
  
   private AudioClip musicaNivell1;
   private AudioClip musicaNivell5;
   private AudioClip musicaNivell10;
   private AudioClip musicaNivell15;
   private AudioClip musicaNivell20;
   private AudioClip musicaNivell25;
   private AudioClip musicaNivell30;
   public Sound() {
       try {
           musicaNivell1 = cargarSonido("musica1.wav");
           musicaNivell5 = cargarSonido("musica2.wav");
           musicaNivell10 = cargarSonido("musica3.wav");
           musicaNivell15 = cargarSonido("musica4.wav");
           musicaNivell20 = cargarSonido("musica5.wav");
           musicaNivell25 = cargarSonido("musica6.wav");
           musicaNivell30 = cargarSonido("Base_After_Base_(Full).wav");
           golpe = cargarSonido("edr-8-bit-jump-001-171817.wav");
           gameover = cargarSonido("gameOver.wav");
          
           fondoActual = musicaNivell1;
       } catch (Exception e) {
           System.out.println("Error carregant sons: " + e.getMessage());
       }
   }
   private AudioClip cargarSonido(String archivo) {
       try {
           URL url = getClass().getResource(archivo);
           if (url == null) return null;
           return Applet.newAudioClip(url);
       } catch (Exception e) {
           return null;
       }
   }
   public void canviarMusica(int nivell) {
       AudioClip novaMusica;
       if (nivell >= 30) {
           novaMusica = musicaNivell30;
       } else if (nivell >= 25) {
           novaMusica = musicaNivell25;
       } else if (nivell >= 20) {
           novaMusica = musicaNivell20;
       } else if (nivell >= 15) {
           novaMusica = musicaNivell15;
       } else if (nivell >= 10) {
           novaMusica = musicaNivell10;
       } else if (nivell >= 5) {
           novaMusica = musicaNivell5;
       } else {
           novaMusica = musicaNivell1;
       }
       if (novaMusica != fondoActual && novaMusica != null) {
           if (fondoActual != null) fondoActual.stop();
           fondoActual = novaMusica;
           fondoActual.loop();
       }
   }
   public void playFondo() {
       if (fondoActual != null) fondoActual.loop();
   }
   public void stopFondo() {
       if (fondoActual != null) fondoActual.stop();
   }
   public void playGolpe() {
       if (golpe != null) golpe.play();
   }
   public void playGameOver() {
       if (gameover != null) gameover.play();
   }
}




