package sound;

import java.net.URL;
import java.util.HashMap;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class SoundPlayer {

	Clip clip;
	HashMap<String, URL> sounds;
	float generalVolume = 0.9f;

	public SoundPlayer() {

		sounds = new HashMap<>();

		sounds.put("ButtonSound", this.getClass().getResource("/res/sounds/pressurePlate.wav"));
		sounds.put("ButtonKlick", this.getClass().getResource("/res/sounds/klick.wav"));
		sounds.put("step", this.getClass().getResource("/res/sounds/metal_footsteps.wav"));
		sounds.put("metal0", this.getClass().getResource("/res/sounds/metal0.wav"));
		sounds.put("metal1", this.getClass().getResource("/res/sounds/metal1.wav"));
		sounds.put("metal2", this.getClass().getResource("/res/sounds/metal2.wav"));
		sounds.put("metal3", this.getClass().getResource("/res/sounds/metal3.wav"));
		sounds.put("synth", this.getClass().getResource("/res/sounds/synth.wav"));
		sounds.put("error", this.getClass().getResource("/res/sounds/error1.wav"));
		sounds.put("lever", this.getClass().getResource("/res/sounds/lever.wav"));
		sounds.put("save", this.getClass().getResource("/res/sounds/save.wav"));
		sounds.put("glassbreak", this.getClass().getResource("/res/sounds/glassbreak.wav"));
		sounds.put("menuHover", this.getClass().getResource("/res/sounds/menuHoverSound.wav"));

	}

	public void playSound(String sound, float volume) {
		if (sounds.containsKey(sound)) {
			try {
				clip = AudioSystem.getClip();

				clip.open(AudioSystem.getAudioInputStream(sounds.get(sound)));
				FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

				float min = gainControl.getMinimum();
				float max = gainControl.getMaximum();

				float normalisedVolume = (volume - min) / (max - min);
				float normalisedProduct = normalisedVolume * generalVolume;
				float product = normalisedProduct * (max - min) + min;
				gainControl.setValue(Math.min(product, 2.0206f));
				clip.start();
			} catch (Exception e) {
				System.err.println("Mal wieder ein Fehler im Sound-System");
			}
		} else {
			System.err.println("Sound \"" + sound + "\" not found. Sounds in database: " + sounds);
		}
	}

}
