package sound;

import java.io.File;
import java.util.HashMap;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class SoundPlayer {

	Clip clip;
	HashMap<String, File> sounds;
	float generalVolume = 0.9f;

	public SoundPlayer() {

		sounds = new HashMap<>();
		sounds.put("test", new File("src/res/sounds/test2.wav"));
		sounds.put("klick", new File("src/res/sounds/klick.wav"));

	}

	public void playSound(String sound, float volume) {
		if (sounds.containsKey(sound)) {
			try {
				clip = AudioSystem.getClip();
				clip.open(AudioSystem.getAudioInputStream(sounds.get(sound)));
				
				FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				float min = gainControl.getMinimum();
				float max = gainControl.getMaximum();
				
				float normalisedVolume = (volume - min) / ( max - min);
				float normalisedProduct = normalisedVolume * generalVolume;
				float product = normalisedProduct * (max - min) + min;
				gainControl.setValue(product);
				clip.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
