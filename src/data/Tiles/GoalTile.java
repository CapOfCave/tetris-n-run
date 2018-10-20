package data.Tiles;

import graphics.Frame;
import loading.ImageLoader;

public class GoalTile extends Tile {

	public GoalTile(int posX, int posY, Frame frame) {
		super('!', posX, posY, true, true, frame);
		img = ImageLoader.loadImage("/res/goal.png");
	}

	@Override
	public void eventWhenEntering() {
		frame.changeToOverworld(false);
		// TODO Ziel erreicht

	}

}
