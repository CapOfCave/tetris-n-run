package data.Tiles;

import graphics.Frame;

public class SaveNLoadTile extends Tile{

	public SaveNLoadTile(char key, int posX, int posY, boolean walkable, boolean walkableWithTetro, Frame frame) {
		super(key, posX, posY, walkable, walkableWithTetro, frame);
		
	}

	boolean canLoad = false;
	
	public void SaveNLoadTile() {
		
	}
	
	
}
