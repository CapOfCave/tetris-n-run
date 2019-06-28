package data.Tiles;

import java.awt.Graphics;

import graphics.GameFrame;
import logics.World;

/**
 * @author Lars Created on 14.09.2018
 */
public class WallTile extends Tile {

	
	public WallTile(char key, int posX, int posY) {
		super(key, posX, posY, false, false, false);
		isBlockingTetro = true;
		

	}
	
	public void setWorld(World world) {
		super.setWorld(world);
		img = world.getImage("/res/blocks/block0.png");
	}
	
	
	@Override
	public void draw(Graphics g, float interpolation) {
		
	}

	@Override
	public void drawBackground(Graphics g, float interpolation) {
		g.drawImage(img, (int) (posX * GameFrame.BLOCKSIZE - world.cameraX()),
				(int) (posY * GameFrame.BLOCKSIZE - world.cameraY()), null);
	}
}
