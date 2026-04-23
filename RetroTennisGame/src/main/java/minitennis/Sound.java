package minitennis;

import javax.sound.sampled.*;
import java.net.URL;

public class Sound {

    public static final Sound BACK = new Sound("adrihuwu-adrihuwu-join-my-server-209103.wav");
    public static final Sound BALL = new Sound("dragon-studio-tennis-ball-hit-386155.wav");
    public static final Sound GAMEOVER = new Sound("calango_fx_official-game-over-401236.wav");

    private Clip clip;

    public Sound(String file) {
        try {
            URL url = getClass().getResource("/minitennis/" + file);

            if (url == null) {
                System.out.println("NO ENCUENTRA AUDIO: " + file);
                return;
            }

            AudioInputStream audio =
                    AudioSystem.getAudioInputStream(url);

            clip = AudioSystem.getClip();
            clip.open(audio);

        } catch (Exception e) {
            System.out.println("Error sonido: " + file);
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip == null) return;
        clip.setFramePosition(0);
        clip.start();
    }

    public void loop() {
        if (clip == null) return;
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        if (clip == null) return;
        clip.stop();
    }
}