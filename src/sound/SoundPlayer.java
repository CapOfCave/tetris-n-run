package sound;

import java.io.File;
import java.util.HashMap;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class SoundPlayer {

	Clip clip;
	HashMap<String, File> sounds;

	public SoundPlayer() {

		sounds = new HashMap<>();
		sounds.put("test", new File("src/res/sounds/test2.wav"));
		sounds.put("klick", new File("src/res/sounds/klick.wav"));

	}

	public void playSound(String sound) {
		if (sounds.containsKey(sound)) {
			try {
				clip = AudioSystem.getClip();
				System.out.println(clip.getFormat());
				clip.open(AudioSystem.getAudioInputStream(sounds.get(sound)));
				
				FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(1);
				
				clip.start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
