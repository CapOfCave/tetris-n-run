package data.Tiles;

import graphics.GameFrame;

public class EmptyTile extends Tile {

	public EmptyTile(char key, int posX, int posY, GameFrame frame) {
		super(key, posX, posY, false, true, true, frame);
	}

	
}
