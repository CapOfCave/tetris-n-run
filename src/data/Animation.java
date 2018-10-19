package data;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * @author Lars Created on 07.10.2018
 */
public class Animation {

	ArrayList<BufferedImage> frames = new ArrayList<>();
	int index = 0;
	private int animTicks;

	public Animation(int animTicks) {
		this.animTicks = animTicks;
	}

	public void addFrame(BufferedImage bufferedImage) {
		frames.add(bufferedImage);
	}

	public void next() {
		index++;
		index %= frames.size() * animTicks;
	}

	public BufferedImage getImage() {
		return frames.get((index % (frames.size() * animTicks)) / animTicks);
	}

	public void reset() {
		index = 0;
	}

	public void setFrame(int animFrame) {
		index = animFrame * animTicks;

	}

	@Override
	public String toString() {
		return "Animation" + frames.toString();
	}

	public int getAnimFrame() {
		return index;
	}

	public int getAnimTicks() {
		return animTicks;
	}

	public int getFrameNumber() {
		return frames.size();
	}
}
