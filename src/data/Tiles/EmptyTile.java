package data.Tiles;

import graphics.Frame;
import loading.ImageLoader;

public class EmptyTile extends Tile {

	public EmptyTile(char key, int posX, int posY, Frame frame) {
		super(key, posX, posY, false, true, frame);
		img = ImageLoader.loadImage("/res/blocks/block0.png");
	}

	@Override
	public void eventWhenEntering() {
		//do nothing
	}

}
