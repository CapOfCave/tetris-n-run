package logics.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * @author Lars Created on 18.09.2018
 */
public abstract class Entity {
	protected double x, y;
	protected BufferedImage img;
	protected int blockSize;
    protected int rotation = 90;
	
	public Entity(BufferedImage img, int blockSize) {
		this.img = img;
		this.blockSize = blockSize;
	}

	public Entity(BufferedImage img, int blockSize, double x, double y) {
		this(img, blockSize);
		this.x = x;
		this.y = y;
	}

	public abstract void draw(Graphics g, float interpolation, boolean debugMode);

	public abstract void tick();

	public double getY() {
		return y;
	}

	public double getX() {
		return x;
	}
}
