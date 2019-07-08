package data.Tiles;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import graphics.GameFrame;
import logics.World;

public class DekoTile extends Tile {

	private BufferedImage image3d;

	public DekoTile(char key, int posX, int posY, int xo, int yo, String name) {
		super(key, posX, posY, false, false, false);
		this.name = name;
		offset = new Point(xo, yo);
	}

	@Override
	public void draw(Graphics g, float interpolation) {
		g.drawImage(image3d, (int) (posX * GameFrame.BLOCKSIZE - world.cameraX() + offset.x),
				(int) (posY * GameFrame.BLOCKSIZE - world.cameraY() + offset.y), null);
	}

	@Override
	public void drawBackground(Graphics g, float interpolation) {
		g.drawImage(img, (int) (posX * GameFrame.BLOCKSIZE - world.cameraX()),
				(int) (posY * GameFrame.BLOCKSIZE - world.cameraY()), null);
	}

	@Override
	public void setWorld(World world) {
		super.setWorld(world);
//		img = world.getImage("/res/blocks/block0.png"); //dublicate
		try {
			image3d = world.getImage("/res/blocks/" + name + ".png");
		} catch (IllegalArgumentException e) {
			System.err.println("No Image found at \"/res/blocks/" + name + ".png\"");
			image3d = world.getImage("/res/blocks/block0.png");
		}
	}

}
