package data;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * @author Lars Created on 07.10.2018
 */
public class Animation {

	ArrayList<BufferedImage> frames = new ArrayList<>();
	ArrayList<Point> offsets = new ArrayList<>();
	int index = 0;
	private int animTicks;

	public Animation(int animTicks) {
		this.animTicks = animTicks;
	}

	public void addFrame(BufferedImage bufferedImage, Point offset) {
		frames.add(bufferedImage);
		offsets.add(offset);
	}

	public void next() {
		index++;
		index %= frames.size() * animTicks;
	}

	public BufferedImage getImage() {
		return frames.get((index % (frames.size() * animTicks)) / animTicks);
	}
	public int getOffsetX() {
		return offsets.get((index % (frames.size() * animTicks)) / animTicks).x;
	}
	public int getOffsetY() {
		return offsets.get((index % (frames.size() * animTicks)) / animTicks).y;
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
