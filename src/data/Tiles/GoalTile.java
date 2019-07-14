package data.Tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import graphics.GameFrame;
import logics.World;

public class GoalTile extends Tile {

	private BufferedImage image3d;

	public GoalTile(int posX, int posY) {
		super('!', posX, posY, false, true, true);
		needsBackGround = true;
		
	}
	@Override
	public void setWorld(World world) {
		super.setWorld(world);
		image3d = world.getImage("/res/imgs/goal.png");
	}
	

	@Override
	public void interact() {
		
	}
	
	@Override
	public void eventWhenEntering() {
		world.startExitingSequence(this);
	}

	@Override
	public void draw(Graphics g, float interpolation) {
		g.drawImage(image3d, (int) (posX * GameFrame.BLOCKSIZE - world.cameraX()),
				(int) (posY * GameFrame.BLOCKSIZE - world.cameraY()), null);
	}

	@Override
	public double getHeight() {
		return -1;
	}

}