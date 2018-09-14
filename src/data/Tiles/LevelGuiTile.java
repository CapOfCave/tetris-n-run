package data.Tiles;

import graphics.Frame;


public class LevelGuiTile extends Tile {

	public LevelGuiTile(char key, int posX, int posY, Frame frame) {
		super(key, posX, posY, true, frame);
		
	}

	public void eventWhenEntering() {
		
		frame.setNextLevel(key);
		
	}
}
