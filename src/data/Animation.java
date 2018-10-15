package data;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * @author Lars Created on 07.10.2018
 */
public class Animation {

	static int frame_ticks = 3;
	ArrayList<BufferedImage> frames = new ArrayList<>();
	int index = 0;

	public void addFrame(BufferedImage bufferedImage) {
		frames.add(bufferedImage);
	}

	public void next() {
		index++;
		index %= frames.size() * frame_ticks;
	}

	public BufferedImage getImage() {
		return frames.get((index % (frames.size() * frame_ticks)) / frame_ticks);
	}

	public void reset() {
		index = 0;
	}

	public void setFrame(int animFrame) {
		index = animFrame * frame_ticks;

	}

	@Override
	public String toString() {
		return "Animation" + frames.toString();
	}

	public int getAnimFrame() {
		return index;
	}
}
