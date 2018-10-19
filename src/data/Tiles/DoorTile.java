package data.Tiles;

import java.util.HashMap;

import data.Animation;
import graphics.Frame;
import loading.EntityLoader;

public class DoorTile extends Tile {

	private int color = -1;
	private HashMap<String, Animation> pictures;
	private int rotation;

	public DoorTile(int color, int x, int y, int rotation, boolean open, Frame frame) {
		super('D', x, y, open, open, frame);
		this.rotation = rotation;
		this.color = color;
		pictures = EntityLoader.loadAnimations("/res/anims/door.txt");
		img = pictures.get((open ? "opened" : "closed") + rotation).getImage();
	}

	public DoorTile() {
		super('D', -1, -1, false, false, null);
	}

	public void changeState() {
		walkable = !walkable;
		walkableWithTetro = !walkableWithTetro;
		
		img = pictures.get((walkable ? "opened" : "closed") + rotation).getImage();

	}

	@Override
	public void eventWhenEntering() {
		// do nothing

	}

	public int getColor() {
		return color;
	}

	@Override
	public String toString() {
		return "DoorTile(x=" + posX + ";y=" + posY + ";open=" + walkable + ";color=" + color;
	}

	public int getRotation() {
		return rotation;
	}

}
