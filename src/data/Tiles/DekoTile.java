package data.Tiles;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import graphics.GameFrame;
import loading.ImageLoader;

public class DekoTile extends Tile{
	
	private BufferedImage image3d;
	

	
	public DekoTile(char key, int posX, int posY, int xo, int yo, String name, GameFrame frame) {
		super(key, posX, posY, false, false, false, frame);
		img = ImageLoader.loadImage("/res/blocks/block0.png");
		this.name = name;

		image3d = ImageLoader.loadImage("/res/blocks/" + name + ".png");
		
		offset = new Point(xo, yo);
	}

	@Override
	public void draw(Graphics g, float interpolation) {
		g.drawImage(image3d, (int) (posX * GameFrame.BLOCKSIZE - world.cameraX() + offset.x),
				(int) (posY * GameFrame.BLOCKSIZE - world.cameraY() + offset.y), null);
	}

	@Override
	public void drawBackground(Graphics g, float interpolation) {
		g.drawImage(img, (int) (posX * GameFrame.BLOCKSIZE - world.cameraX()),
				(int) (posY * GameFrame.BLOCKSIZE - world.cameraY()), null);
	}

}
