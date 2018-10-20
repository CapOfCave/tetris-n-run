package data.Tiles;

import java.awt.Color;
import java.awt.Image;
import java.util.HashMap;

import data.Animation;
import graphics.Frame;
import loading.AnimationLoader;
import tools.Tools;

public class DoorTile extends Tile {

	private int color = -1;
	private HashMap<String, Animation> pictures;
	private int rotation;
	Color drawColor = Color.BLACK;
	private Image backgroundImage;

	public DoorTile(int color, int x, int y, int rotation, boolean open, Frame frame) {
		super('D', x, y, open, open, frame);
		this.rotation = rotation;
		this.color = color;
		pictures = AnimationLoader.loadAnimations("/res/anims/door.txt");
		backgroundImage = pictures.get("background").getImage();
		
		if (color == 0) {
			drawColor = Color.RED;
		} else if (color == 1) {
			drawColor = Color.GREEN;
		} else if (color == 2) {
			drawColor = Color.BLUE;
		} else if (color == 3) {
			drawColor = Color.YELLOW;
		}
		img = Tools.setColor(pictures.get((open ? "opened" : "closed") + rotation).getImage(), drawColor);
	}

	public DoorTile() {
		super('D', -1, -1, false, false, null);
	}

	public void changeState() {
		walkable = !walkable;
		walkableWithTetro = !walkableWithTetro;
		
		img = Tools.setColor(pictures.get((walkable ? "opened" : "closed") + rotation).getImage(), drawColor);

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

	public Image getBackgroundImage() {
		return backgroundImage;
	}

}
