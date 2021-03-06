package logics.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import logics.World;
import tools.GraphicalTools;

public class Switch extends Entity {
	private static final long serialVersionUID = 1L;
	private static final String animPath = "/res/anims/switch.txt";
	private int color;
	private boolean toggled = Math.random() < 0.5;
	private BufferedImage akt_image;
	private BufferedImage akt_imageOutline;
	private Color drawColor = Color.PINK;

	public Switch(World world, double x, double y, int color) {
		super(world, x, y, animPath, null);
		this.color = color;
		if (color == 0) {
			drawColor = new Color(209, 17, 65);
		} else if (color == 1) {
			drawColor = new Color(0, 177, 89);
		} else if (color == 2) {
			drawColor = new Color(0, 174, 219);
		} else if (color == 3) {
			drawColor = new Color(255, 196, 37);
		}else if (color == 4) {
			drawColor = new Color(243, 119, 53);
		}else if (color == 5) {
			drawColor = new Color(210, 114, 255);
		}

		type = "switch";
		
	}
	
	@Override
	public void setWorld(World world) {
		super.setWorld(world);
		akt_animation = anims.get("state" + (toggled ? "0" : "1"));
		akt_imageOutline = anims.get("stateOut" + (toggled ? "0" : "1")).getImage();
		akt_image = GraphicalTools.setColor(akt_animation.getImage(), drawColor);
	}

	@Override
	public void draw(Graphics g, float interpolation) {
		g.drawImage(akt_image, (int) x - world.cameraX(), (int) y - world.cameraY(), null);
		g.drawImage(akt_imageOutline, (int) x - world.cameraX(), (int) y - world.cameraY(), null);
	}

	@Override
	public void tick() {
		akt_animation.next();
		akt_image = GraphicalTools.setColor(akt_animation.getImage(), drawColor);
	}

	@Override
	public void interact() {
		toggled = !toggled;
		world.switchDoors(color);
		akt_animation = anims.get("state" + (toggled ? "0" : "1"));
		akt_image = GraphicalTools.setColor(akt_animation.getImage(), drawColor);
		akt_imageOutline = anims.get("stateOut" + (toggled ? "0" : "1")).getImage();
		world.playSound("lever", -5f);
	}

	@Override
	public double getHeight() {
		return -0.5;
	}

	public Color getColor() {
		return drawColor;
	}
	
	public int getColorAsInt() {
		return color;
	}
	
	public boolean isToggled() {
		return toggled;
	}

}
