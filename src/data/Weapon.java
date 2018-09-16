package data;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

/**
 * @author Lars
 * Created on 16.09.2018
 */
public class Weapon {

	private BufferedImage img;
	private BufferedImage imgHit;
	private Point imgOffset;
	private Point imgHitOffset;

	private int blockSize;
	
	private int hitTicks = 0;
	
	
	public Weapon(BufferedImage img, BufferedImage imgHit, Point imgOffset, Point imgHitOffset, int blockSize) {
		this.img = img;
		this.imgHit = imgHit;
		this.imgOffset = imgOffset;
		this.imgHitOffset = imgHitOffset;
		this.blockSize = blockSize;
	}
	
	public void draw(Graphics g, int rotation, int x, int y) {
		if (hitTicks != 0) {
			g.drawImage(imgHit, x + imgHitOffset.x, y + imgHitOffset.y, blockSize, blockSize, null);
			hitTicks--;
		} else {
			g.drawImage(img, x + imgOffset.x, y + imgOffset.y, blockSize, blockSize, null);

		}
	}

	public void hit() {
		hitTicks = 10;
	}

}
