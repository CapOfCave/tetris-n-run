package data;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import graphics.Frame;

/**
 * @author Lars Created on 05.08.2018
 */
public class TetroType {
	protected BufferedImage img;

	private String strHitbox;
	private boolean[][] hitbox;

	public TetroType(String strHitbox, BufferedImage img) {
		this.strHitbox = strHitbox;
		this.img = img;

		hitbox = new boolean[2][4];
		for (int y = 0; y < hitbox.length; y++) {
			for (int x = 0; x < hitbox[y].length; x++) {
				hitbox[y][x] = strHitbox.charAt(y * hitbox[y].length + x) == '0' ? false : true;
			}
		}

	}

	public void draw(Graphics g, int x, int y, int rotation) {

		for (int j = 0; j < hitbox.length; j++) {
			for (int i = 0; i < hitbox[j].length; i++) {
				if (hitbox[j][i]) {
					drawBlock(g, i, j, x, y, rotation, Frame.BLOCKSIZE);
				}
			}
		}
	}

	public void draw(Graphics g, int x, int y, int blockWidth, int rotation) {
		for (int j = 0; j < hitbox.length; j++) {
			for (int i = 0; i < hitbox[j].length; i++) {
				if (hitbox[j][i]) {
					drawBlock(g, i, j, x, y, rotation, blockWidth);
				}
			}
		}
	}
	
	public void drawBlock(Graphics g, int i, int j, int x, int y, int rotation, int blockWidth) {
		switch (rotation % 4) {
		case 0:
			g.drawImage(img, i * blockWidth + x, j * blockWidth + y, blockWidth, blockWidth,  null);
			break;
		case 1:
			g.drawImage(img, j * blockWidth + x, -i * blockWidth + y + 3 * blockWidth, blockWidth, blockWidth, null);
			break;
		case 2:
			g.drawImage(img, -i * blockWidth + x + 3 * blockWidth,
					-j * blockWidth + y + 1 * blockWidth, null);
			break;
		case 3:
			g.drawImage(img, -j * blockWidth + x + 1 * blockWidth, i * blockWidth + y, blockWidth, blockWidth, null);
			break;
		}
	}
	
	public boolean[][] getHitbox() {
		return hitbox;
	}

	public String getStrHitbox() {
		return strHitbox;
	}

}
