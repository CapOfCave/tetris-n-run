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
		
			sounds.put("test", this.getClass().getResource("/res/sounds/test2.wav"));
			sounds.put("ButtonKlick", this.getClass().getResource("/res/sounds/klick.wav"));
			sounds.put("ButtonSound", this.getClass().getResource("/res/sounds/button2.wav"));
			sounds.put("SwitchSound", this.getClass().getResource("/res/sounds/button1.wav"));
			sounds.put("step", this.getClass().getResource("/res/sounds/metal_footsteps.wav"));
			sounds.put("metal0", this.getClass().getResource("/res/sounds/metal0.wav"));
			sounds.put("metal1", this.getClass().getResource("/res/sounds/metal1.wav"));
			sounds.put("metal2", this.getClass().getResource("/res/sounds/metal2.wav"));
			sounds.put("metal3", this.getClass().getResource("/res/sounds/metal3.wav"));
			sounds.put("synth", this.getClass().getResource("/res/sounds/synth.wav"));
			sounds.put("error", this.getClass().getResource("/res/sounds/error1.wav"));
			sounds.put("lever", this.getClass().getResource("/res/sounds/lever.wav"));
			sounds.put("save", this.getClass().getResource("/res/sounds/save.wav"));
			
		 


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
