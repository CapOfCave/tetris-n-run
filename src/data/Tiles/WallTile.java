package data.Tiles;

import graphics.Frame;
import loading.ImageLoader;

/**
 * @author Lars
 * Created on 14.09.2018
 */
public class WallTile extends Tile{

	public WallTile(char key, int posX, int posY, Frame frame) {
		super(key, posX, posY, false, false, frame);
		isBlockingTetro = true;
		img = ImageLoader.loadImage("/res/blocks/block1.png");
	}

	@Override
	public void eventWhenEntering() {
		// do nothing
		
	}
	
}
