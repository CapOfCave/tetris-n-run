package logics.entities;

import java.awt.Graphics;
import java.util.HashMap;

import data.Animation;
import logics.worlds.World;

public class Switch extends Entity {
	private static final long serialVersionUID = 1L;
	private int color;
	private boolean toggled = false;

	public Switch(World world, double x, double y, HashMap<String, Animation> anims, int color) {
		super(world, x, y, anims);
		this.color = color;
	}

	@Override
	public void draw(Graphics g, float interpolation) {
		g.drawImage(akt_animation.getImage(), (int)x - world.cameraX(), (int)y  - world.cameraY(), null);
	}

	@Override
	public void tick() {
		akt_animation.next();
	}
	
	@Override
	public void interact() {
		toggled = !toggled;
		world.switchDoors(color);
	}

}
