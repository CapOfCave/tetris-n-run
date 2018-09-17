package data.Tiles;

import graphics.Frame;

/**
 * @author Lars
 * Created on 14.09.2018
 */
public class WallTile extends Tile{

	public WallTile(char key, int posX, int posY, Frame frame) {
		super(key, posX, posY, false, frame);
		isBlockingTetro = true;
	}
	
}
