package logics.entities;

import java.awt.Graphics;
import java.util.HashMap;

import data.Animation;
import logics.worlds.World;

public class Switch extends Entity {
	private static final long serialVersionUID = 1L;
	private int color;
	private boolean toggled = Math.random() < 0.5;

	public Switch(World world, double x, double y, HashMap<String, Animation> anims, int color) {
		super(world, x, y, anims);
		this.color = color;
		akt_animation = anims.get("state" + (toggled?"0":"1"));

	}

	@Override
	public void draw(Graphics g, float interpolation) {
		g.drawImage(akt_animation.getImage(), (int) x - world.cameraX(), (int) y - world.cameraY(), null);
	}

	@Override
	public void tick() {
		akt_animation.next();
	}

	@Override
	public void interact() {
		toggled = !toggled;
		world.switchDoors(color);
		akt_animation = anims.get("state" + (toggled?"0":"1"));
	}

}
