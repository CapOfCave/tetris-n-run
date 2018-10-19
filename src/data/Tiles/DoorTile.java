package data.Tiles;

import graphics.Frame;
import loading.ImageLoader;

public class DoorTile extends Tile {

	private int color = -1;
	public DoorTile(char key, int posX, int posY, Frame frame) {
		super(key, posX, posY, Character.isUpperCase(key), Character.isUpperCase(key), frame);
		img = ImageLoader.loadImage("/res/blocks/door.png");
		if (key == 'i' || key == 'I') {
			color = 0;
		} else if (key == 'j' || key == 'J') {
			color = 1;
		} else if (key == 'k' || key == 'K') {
			color = 2;
		} else if (key == 'l' || key == 'L') {
			color = 3;
		}
	}
	
	public void changeState() {
		walkable = !walkable;
		walkableWithTetro = !walkableWithTetro;
	}

	@Override
	public void eventWhenEntering() {
		// do nothing
		
	}

	public int getColor() {
		return color;
	}

}
