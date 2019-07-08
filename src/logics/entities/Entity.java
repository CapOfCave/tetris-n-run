package logics.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.HashMap;

import data.Animation;
import data.DrawAndSortable;
import graphics.GameFrame;
import graphics.Renderer;
import logics.World;

/**
 * @author Lars Created on 18.09.2018
 */
public abstract class Entity implements Serializable, DrawAndSortable {

	private static final long serialVersionUID = 1L;
	protected double x, y;
	protected int rotation = 90;
	protected transient World world;

	protected transient HashMap<String, Animation> anims;

	protected transient Animation akt_animation;
	protected transient String animation_key;
	protected String type;

	protected Rectangle relCollisionsRect;
	private boolean hasCollisions;
	private String animPath;

	public Entity(World world, String animPath, Rectangle relCollisionsRect) {
		this.world = world;
		this.animPath = animPath;
		if (relCollisionsRect != null) {
			this.relCollisionsRect = relCollisionsRect;
			hasCollisions = true;
		} else {
			hasCollisions = false;
		}
		
	}

	public Entity(World world, double x, double y, String animPath, Rectangle relCollisionsRect) {
		this(world, animPath, relCollisionsRect);
		this.x = x;
		this.y = y;

	}

	@Override
	public abstract void draw(Graphics g, float interpolation);

	public abstract void tick();

	public void interact() {

	}

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
		if (animPath != null) {
			this.anims = world.loadAnimations(animPath);
		}
		if (anims != null)
			akt_animation = anims.get(anims.keySet().toArray()[0]);
		else
			akt_animation = null;
	}


	@Override
	public double getHeight() {
		return y + (double) (hashCode()) / Integer.MAX_VALUE / 1000;
	}

	@Override
	public int compareTo(DrawAndSortable o) {
		if (this.getHeight() == o.getHeight()) {
			return 0;
		} else if (this.getHeight() < o.getHeight()) {
			return -1;
		} else {
			return 1;
		}
	}

	@Override
	public void addTo(Renderer renderer) {
		renderer.addDrawable(this);
	}

	public void drawDebug(Graphics g, float interpolation) {

	}

	public String getType() {
		return type;
	}

	public boolean collidesWith(double colliderY, double colliderX) {
		if (!hasCollisions) {
			return false;
		} else {
			return relCollisionsRect.contains(colliderX - x, colliderY - y);
		}
	}
	
	protected int getTileX(double dx) { //dx vom Zentrum aus
		return (int) ((x + dx + GameFrame.BLOCKSIZE / 2) / GameFrame.BLOCKSIZE);
	}

	protected int getTileY(double dy) {
		return (int) ((y + dy + GameFrame.BLOCKSIZE / 2) / GameFrame.BLOCKSIZE);
	}

	public int getTileX() {
		return (int) ((x + GameFrame.BLOCKSIZE / 2) / GameFrame.BLOCKSIZE);
	}
	
	public int getTileY() {
		return (int) ((y + GameFrame.BLOCKSIZE / 2) / GameFrame.BLOCKSIZE);
	}
	@Override
	public int getPriorityInDrawQueue() {
		return 0;
	}

}
