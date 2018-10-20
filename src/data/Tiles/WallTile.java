package data.Tiles;

import java.awt.Graphics;
import java.awt.Point;

import graphics.Frame;
import loading.ImageLoader;

/**
 * @author Lars Created on 14.09.2018
 */
public class WallTile extends Tile {

	private static Point offset = new Point(0, -45);

	public WallTile(char key, int posX, int posY, Frame frame) {
		super(key, posX, posY, false, false, frame);
		isBlockingTetro = true;
		img = ImageLoader.loadImage("/res/blocks/block1.png");
	}

	@Override
	public void eventWhenEntering() {
		// do nothing

	}

	@Override
	public void draw(Graphics g, int i, int j) {
		g.drawImage(img, (int) (i * Frame.BLOCKSIZE - world.cameraX() + offset.x),
				(int) (j * Frame.BLOCKSIZE - world.cameraY() + offset.y), null);

	}
}
