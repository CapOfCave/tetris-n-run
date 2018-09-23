package logics.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import logics.World;

/**
 * @author Lars Created on 18.09.2018
 */
public abstract class Entity implements Serializable{

	private static final long serialVersionUID = 6260418655041864529L;
	protected double x, y;
	protected transient BufferedImage img;
    protected int rotation = 90;
    protected transient World world;
	
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
	
	public void setY(double y) {
		this.y = y;
	}

	public void setX(double x) {
		this.x = x;
	}
	
	public void setWorld(World world) {
		this.world = world;
	}
}
