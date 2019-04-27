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
	private boolean finished = false;

	public Animation(int animTicks) {
		this.animTicks = animTicks;
	}

	public void addFrame(BufferedImage bufferedImage, Point offset) {
		frames.add(bufferedImage);
		offsets.add(offset);
	}

	public void next() {
		index++;
		if (index >= frames.size() * animTicks) {
			index %= frames.size() * animTicks;
			finished = true;
		}
		
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
		finished = false;
	}

	public void setFrame(int animFrame) {
		index = animFrame * animTicks;
	}

	@Override
	public String toString() {
		return "Animation" + frames.toString();
	}

	public int getAktIndex() {
		return index / animTicks;
	}

	public int getAnimLengthTicks() {
		return animTicks;
	}

	public int getFrameAmount() {
		return frames.size();
	}
	
	public boolean isFinished() {
		return finished;
	}
}
