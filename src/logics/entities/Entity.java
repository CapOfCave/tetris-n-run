package logics.entities;

import java.awt.Graphics;
import java.io.Serializable;
import java.util.HashMap;

import data.Animation;
import logics.worlds.World;

/**
 * @author Lars Created on 18.09.2018
 */
public abstract class Entity implements Serializable {

	private static final long serialVersionUID = 1L;
	protected double x, y;
	protected int rotation = 90;
	protected transient World world;

	protected transient HashMap<String, Animation> anims;
	
	protected transient Animation akt_animation;
	protected transient String animation_key;

	public Entity(World world, HashMap<String, Animation> anims) {
		this.world = world;
		this.anims = anims;

		if (anims != null)
			akt_animation = anims.get(anims.keySet().toArray()[0]);
		else
			akt_animation = null;
	}

	public Entity(World world, double x, double y, HashMap<String, Animation> anims) {
		this(world, anims);
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
