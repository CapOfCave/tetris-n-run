package data;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * @author Lars
 * Created on 16.09.2018
 */
public class Weapon {

	protected BufferedImage img;
	protected int blockSize;
	
	public Weapon(BufferedImage img, int blockSize) {
		this.img = img;
		this.blockSize = blockSize;
	}
	
	public void draw(Graphics g, int rotation, int x, int y) {
		g.drawImage(img, x, y, blockSize, blockSize, null);
	}

}
