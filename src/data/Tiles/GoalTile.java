package data.Tiles;

import graphics.Frame;

public class GoalTile extends Tile {

	public GoalTile(int posX, int posY, Frame frame) {
		super('!', posX, posY, true, true, frame);
	}

	@Override
	public void eventWhenEntering() {
		// TODO Ziel erreicht

	}

}
