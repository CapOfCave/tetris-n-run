package data.Tiles;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import graphics.GameFrame;
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
		img = world.getImage("/res/blocks/block0.png");
		image3d = world.getImage("/res/blocks/" + name + ".png");
	}
	
	@Override
	public int getPriorityInDrawQueue() {
		return priorityInDrawQueue;
	}

}
