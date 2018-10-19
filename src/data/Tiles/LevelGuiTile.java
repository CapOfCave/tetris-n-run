package data.Tiles;

import graphics.Frame;
import loading.ImageLoader;


public class LevelGuiTile extends Tile {

	private char key;
	public LevelGuiTile(char key, int posX, int posY, Frame frame) {
		super(key, posX, posY, true, true, frame);
		img = ImageLoader.loadImage("/res/blocks/block" + key + ".png");
		
	}

	public void eventWhenEntering() {
		
		frame.setNextLevel(key);
		
	}
}
