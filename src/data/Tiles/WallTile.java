package data.Tiles;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import graphics.Frame;
import loading.ImageLoader;

/**
 * @author Lars Created on 14.09.2018
 */
public class WallTile extends Tile {

	private static Point offset = new Point(0, -45);
	private BufferedImage image3d;

	public WallTile(char key, int posX, int posY, Frame frame) {
		super(key, posX, posY, false, false, frame);
		isBlockingTetro = true;
		img = ImageLoader.loadImage("/res/blocks/block0.png");
		image3d = ImageLoader.loadImage("/res/blocks/blockL.png");

	}

	@Override
	public void draw(Graphics g, float interpolation) {
		g.drawImage(image3d, (int) (posX * Frame.BLOCKSIZE - world.cameraX() + offset.x),
				(int) (posY * Frame.BLOCKSIZE - world.cameraY() + offset.y), null);

	}

	@Override
	public void drawBackground(Graphics g, float interpolation) {
		g.drawImage(img, (int) (posX * Frame.BLOCKSIZE - world.cameraX()),
				(int) (posY * Frame.BLOCKSIZE - world.cameraY()), null);
	}
}
