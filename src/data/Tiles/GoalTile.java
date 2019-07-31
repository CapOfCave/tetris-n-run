package data.Tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import graphics.GameFrameHandler;
import logics.World;

public class GoalTile extends Tile {

	private BufferedImage image, imageActiv;
	private boolean isActiv = false;

	public GoalTile(int posX, int posY) {
		super('!', posX, posY, false, true, true);
		needsBackGround = false;

	}

	@Override
	public void setWorld(World world) {
		super.setWorld(world);
		image = world.getImage("/res/imgs/goal.png");
		imageActiv = world.getImage("/res/imgs/goalActiv.png");
	}

	@Override
	public void interact() {

	}

	@Override
	public void eventWhenEntering() {
		world.startExitingSequence(this);
		isActiv = true;
	}

	@Override
	public void draw(Graphics g, float interpolation) {
		if (isActiv)
			g.drawImage(imageActiv, (int) (posX * GameFrameHandler.BLOCKSIZE - world.cameraX()),
					(int) (posY * GameFrameHandler.BLOCKSIZE - world.cameraY()), null);
		else
			g.drawImage(image, (int) (posX * GameFrameHandler.BLOCKSIZE - world.cameraX()),
					(int) (posY * GameFrameHandler.BLOCKSIZE - world.cameraY()), null);
	}

	@Override
	public double getHeight() {
		return -1000;
	}

}