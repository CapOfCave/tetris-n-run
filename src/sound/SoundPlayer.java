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
		sounds.put("step", new File("src/res/sounds/metal_footsteps.wav"));
		sounds.put("metal0", new File("src/res/sounds/metal0.wav"));
		sounds.put("metal1", new File("src/res/sounds/metal1.wav"));
		sounds.put("metal2", new File("src/res/sounds/metal2.wav"));
		sounds.put("metal3", new File("src/res/sounds/metal3.wav"));
		sounds.put("metal3", new File("src/res/sounds/metal3.wav"));
		sounds.put("synth", new File("src/res/sounds/synth.wav"));
		sounds.put("error", new File("src/res/sounds/error1.wav"));
		sounds.put("save", new File("src/res/sounds/hardlysavethequeen.wav"));
		sounds.put("lever", new File("src/res/sounds/lever.wav"));
		

		

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
		} else {
			System.out.println("Sound \"" + sound + "\" not found. Sounds in database: " + sounds);
		}
	}

}
