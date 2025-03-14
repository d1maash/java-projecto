import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private static Map<String, Clip> clips = new HashMap<>();
    private static boolean soundEnabled = true;

    public static void loadSounds() {
        loadSound("engine", "sounds/engine.wav");
        loadSound("collision", "sounds/collision.wav");
        loadSound("lap", "sounds/lap.wav");
        loadSound("start", "sounds/start.wav");
        loadSound("finish", "sounds/finish.wav");
    }

    private static void loadSound(String name, String path) {
        try {
            File soundFile = new File(path);
            if (!soundFile.exists()) {
                System.out.println("Creating sound directory...");
                new File("sounds").mkdir();
                return;
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clips.put(name, clip);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Error loading sound: " + path);
            e.printStackTrace();
        }
    }

    public static void playSound(String name) {
        if (!soundEnabled)
            return;

        Clip clip = clips.get(name);
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public static void loopSound(String name) {
        if (!soundEnabled)
            return;

        Clip clip = clips.get(name);
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public static void stopSound(String name) {
        Clip clip = clips.get(name);
        if (clip != null) {
            clip.stop();
        }
    }

    public static void stopAllSounds() {
        for (Clip clip : clips.values()) {
            clip.stop();
        }
    }

    public static void setSoundEnabled(boolean enabled) {
        soundEnabled = enabled;
        if (!enabled) {
            stopAllSounds();
        }
    }

    public static boolean isSoundEnabled() {
        return soundEnabled;
    }
}