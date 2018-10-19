package data.Tiles;

import graphics.Frame;
import loading.ImageLoader;

public class Switch extends Tile{
	
	private int color;
	
	public Switch(char key, int posX, int posY, Frame frame) {
		super(key, posX, posY, true, true, frame);
		if (key == '�') {
			color = 0;
		} else if (key == '�') {
			color = 1;
		} else if (key == '�') {
			color = 2;
		} else if (key == '�') {
			color = 3;
		}
		img = ImageLoader.loadImage("/res/blocks/switch.png");
	}

	@Override
	public void eventWhenEntering() {
		world.switchDoors(color);
	}

}
