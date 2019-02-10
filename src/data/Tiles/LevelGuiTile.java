package data.Tiles;

import graphics.GameFrame;
import loading.ImageLoader;

public class LevelGuiTile extends Tile {

	public LevelGuiTile(char key, int posX, int posY, GameFrame frame) {
		super(key, posX, posY, true, true, false, frame);
		img = ImageLoader.loadImage("/res/blocks/block" + key + ".png");

	}

	public void eventWhenEntering() {

		frame.setNextLevel(key);

	}
	
	public void eventWhenLeaving() {

		frame.setNextLevel(' ');

	}

	@Override
	public void interact() {
		frame.startLevel();
	}
}
