package logics.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import logics.World;

/**
 * @author Lars Created on 18.09.2018
 */
public abstract class Entity {
	protected double x, y;
	protected BufferedImage img;
    protected int rotation = 90;
    protected World world;
	
	public Entity(World world, BufferedImage img) {
		this.world = world;
		this.img = img;
		
	}

	public Entity(World world, BufferedImage img, double x, double y) {
		this(world, img);
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
