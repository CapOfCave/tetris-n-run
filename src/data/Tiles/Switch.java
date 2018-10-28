package data.Tiles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import data.Animation;
import graphics.Frame;
import loading.AnimationLoader;
import loading.ImageLoader;
import tools.Tools;

public class Switch extends Tile{
	
	private int color;
	private HashMap<String, Animation> pictures;
	boolean state0 = true;
	Color drawColor = Color.BLACK;
	private BufferedImage image3d;
	
	public Switch(char key, int posX, int posY, Frame frame) {
		super(key, posX, posY, false, true, frame);
		
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
		image3d = Tools.setColor(ImageLoader.loadImage("/res/blocks/switch.png"), drawColor);
	}

	@Override
	public void eventWhenEntering() {
		world.switchDoors(color);
		state0 = !state0;
		image3d = Tools.setColor(pictures.get(state0?"state0":"state1").getImage(), drawColor);
		
	}
	@Override
	public void draw(Graphics g, float interpolation) {
		g.drawImage(image3d, (int) (posX * Frame.BLOCKSIZE - world.cameraX()),
				(int) (posY * Frame.BLOCKSIZE - world.cameraY()), null);
	}

}
