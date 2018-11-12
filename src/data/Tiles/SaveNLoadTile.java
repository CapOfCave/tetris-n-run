package data.Tiles;

import java.awt.Graphics;

import java.awt.image.BufferedImage;
import java.io.File;

import graphics.Frame;
import loading.ImageLoader;


public class SaveNLoadTile extends Tile{
	
	
	BufferedImage image3d;

	public SaveNLoadTile(char key, int posX, int posY, boolean walkable, boolean walkableWithTetro, Frame frame) {
		super(key, posX, posY, walkable, walkableWithTetro, frame);
		
	
		image3d = ImageLoader.loadImage("/res/blocks/saveNLoad1.png");
		
	}

	@Override
	public void draw(Graphics g, float interpolation) {
		g.drawImage(image3d, (int) (posX * Frame.BLOCKSIZE - world.cameraX()),
				(int) (posY * Frame.BLOCKSIZE - world.cameraY()), null);
	}
	
	
	@Override
	public void interact() {
		super.interact();
		
		File file = new File(System.getenv("APPDATA") + "\\tetris-n-run\\levelSaves\\saveNLodeTile.txt");
		
		
		if(!file.exists()) {
			image3d = ImageLoader.loadImage("/res/blocks/saveNLoad2.png");
			frame.addLineToText("Spielstand wurde gespeichert.");
			world.save(System.getenv("APPDATA") + "\\tetris-n-run\\levelSaves", "saveNLodeTile.txt");

		}else {
			frame.addLineToText("Spielstand wurde geladen.");
			frame.swithLevel(System.getenv("APPDATA") + "\\tetris-n-run\\levelSaves\\saveNLodeTile.txt");
		}
	}
	
	
	
	
}
