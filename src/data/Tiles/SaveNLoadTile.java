package data.Tiles;

import java.awt.Graphics;

import java.awt.image.BufferedImage;

import graphics.Frame;
import loading.ImageLoader;


public class SaveNLoadTile extends Tile{
	
	
	boolean canLoad = false;
	BufferedImage image3d;

	public SaveNLoadTile(char key, int posX, int posY, boolean walkable, boolean walkableWithTetro, Frame frame) {
		super(key, posX, posY, walkable, walkableWithTetro, frame);
		
	
		image3d = ImageLoader.loadImage("/res/blocks/block3.png");
		
	}

	@Override
	public void draw(Graphics g, float interpolation) {
		g.drawImage(image3d, (int) (posX * Frame.BLOCKSIZE - world.cameraX()),
				(int) (posY * Frame.BLOCKSIZE - world.cameraY()), null);
	}
	
	@Override
	public void eventWhenEntering() {
		super.eventWhenEntering();
		
		world.save("C:\\Users\\Marius\\AppData\\Roaming\\tetris-n-run\\levelSaves", "saveNLodeTile.txt");
	}
	
	
	
	
}
