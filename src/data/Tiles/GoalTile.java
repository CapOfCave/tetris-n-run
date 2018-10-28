package data.Tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import graphics.Frame;
import loading.ImageLoader;

public class GoalTile extends Tile {

	private BufferedImage image3d;

	public GoalTile(int posX, int posY, Frame frame) {
		super('!', posX, posY, true, true, frame);
		image3d = ImageLoader.loadImage("/res/goal.png");
	}

	@Override
	public void eventWhenEntering() {
		frame.changeToOverworld(false);

	}

	@Override
	public void draw(Graphics g, float interpolation) {
		g.drawImage(image3d, (int) (posX * Frame.BLOCKSIZE - world.cameraX()),
				(int) (posY * Frame.BLOCKSIZE - world.cameraY()), null);
	}

}
