package data.Tiles;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import graphics.GameFrameHandler;
import logics.World;

public class DekoTile extends Tile {

	private BufferedImage image3d;
	private int priorityInDrawQueue;

	public DekoTile(char key, int posX, int posY, int xo, int yo, String name, int priorityInDrawQueue) {
		super(key, posX, posY, false, false, false);
		this.name = name;
		this.priorityInDrawQueue = priorityInDrawQueue;
		offset = new Point(xo, yo);
	}

	@Override
	public void draw(Graphics g, float interpolation) {
		g.drawImage(image3d, (int) (posX * GameFrameHandler.BLOCKSIZE - world.cameraX() + offset.x),
				(int) (posY * GameFrameHandler.BLOCKSIZE - world.cameraY() + offset.y), null);
	}

	@Override
	public void drawBackground(Graphics g, float interpolation) {
		g.drawImage(img, (int) (posX * GameFrameHandler.BLOCKSIZE - world.cameraX()),
				(int) (posY * GameFrameHandler.BLOCKSIZE - world.cameraY()), null);
	}

	@Override
	public void setWorld(World world) {
		super.setWorld(world);
//		img = world.getImage("/res/blocks/block0.png"); //dublicate
		try {
			image3d = world.getImage("/res/blocks/deko/" + name + ".png");
		} catch (IllegalArgumentException e) {
			System.err.println("No Image found at \"/res/blocks/deko/" + name + ".png\"");
			image3d = world.getImage("/res/blocks/block0.png");
		}
	}
	
	@Override
	public int getPriorityInDrawQueue() {
		return priorityInDrawQueue;
	}

}
