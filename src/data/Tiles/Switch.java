package data.Tiles;

import java.awt.Color;
import java.awt.Image;
import java.util.HashMap;

import data.Animation;
import graphics.Frame;
import loading.AnimationLoader;
import loading.ImageLoader;
import tools.Tools;

public class Switch extends Tile{
	
	private int color;
	private HashMap<String, Animation> pictures;
	private Image backgroundImage;
	boolean state0 = true;
	Color drawColor = Color.BLACK;
	
	public Switch(char key, int posX, int posY, Frame frame) {
		super(key, posX, posY, true, true, frame);
		
		if (key == 'à') {
			color = 0;
			drawColor = Color.RED;
		} else if (key == 'è') {
			color = 1;
			drawColor = Color.GREEN;
		} else if (key == 'ì') {
			color = 2;
			drawColor = Color.BLUE;
		} else if (key == 'ò') {
			color = 3;
			drawColor = Color.YELLOW;
		}
		pictures = AnimationLoader.loadAnimations("/res/anims/switch.txt");
		backgroundImage = pictures.get("background").getImage();
		img = Tools.setColor(ImageLoader.loadImage("/res/blocks/switch.png"), drawColor);
	}

	@Override
	public void eventWhenEntering() {
		world.switchDoors(color);
		state0 = !state0;
		img = Tools.setColor(pictures.get(state0?"state0":"state1").getImage(), drawColor);
		
	}

}
