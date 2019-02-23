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
