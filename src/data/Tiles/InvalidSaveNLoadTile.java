package data.Tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import graphics.GameFrameHandler;
import logics.World;

public class InvalidSaveNLoadTile extends Tile {

	private BufferedImage image3d;
	public InvalidSaveNLoadTile(int posX, int posY, World world, GameFrameHandler gameFrame) {
		this(posX, posY);
		setWorld(world);
		setFrame(gameFrame);
		
	}
	@Override
	public void setWorld(World world) {
		super.setWorld(world);
		image3d = world.getImage("/res/blocks/saveNLoadBroken.png");
	}
	
	public InvalidSaveNLoadTile(int posX, int posY) {
		super('3', posX, posY, false, true, true);
		
	}

	@Override
	public void draw(Graphics g, float interpolation) {
		g.drawImage(image3d, (int) (posX * GameFrameHandler.BLOCKSIZE - world.cameraX() + offset.x),
				(int) (posY * GameFrameHandler.BLOCKSIZE - world.cameraY() + offset.y), null);
	}

}
