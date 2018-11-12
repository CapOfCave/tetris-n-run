package logics.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import data.Animation;
import logics.worlds.World;
import tools.Tools;

public class Switch extends Entity {
	private static final long serialVersionUID = 1L;
	private int color;
	private boolean toggled = Math.random() < 0.5;
	private BufferedImage akt_image;
	private Color drawColor = Color.PINK;

	public Switch(World world, double x, double y, HashMap<String, Animation> anims, int color) {
		super(world, x, y, anims);
		this.color = color;
		if (color == 0) {
			drawColor = Color.RED;
		} else if (color == 1) {
			drawColor = Color.GREEN;
		} else if (color == 2) {
			drawColor = Color.BLUE;
		} else if (color == 3) {
			drawColor = Color.YELLOW;
		}
		
		akt_animation = anims.get("state" + (toggled ? "0" : "1"));
		akt_image = Tools.setColor(akt_animation.getImage(), drawColor);
	}

	@Override
	public void draw(Graphics g, float interpolation) {
		g.drawImage(akt_image, (int) x - world.cameraX(), (int) y - world.cameraY(), null);
	}

	@Override
	public void tick() {
		akt_animation.next();
		akt_image = Tools.setColor(akt_animation.getImage(), drawColor);
	}

	@Override
	public void interact() {
		toggled = !toggled;
		world.switchDoors(color);
		akt_animation = anims.get("state" + (toggled ? "0" : "1"));
		akt_image = Tools.setColor(akt_animation.getImage(), drawColor);
	}

}
