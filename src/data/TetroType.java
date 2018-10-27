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
					drawBlock(g, i, j, x, y, rotation);
				}
			}
		}
	}

	public void drawBlock(Graphics g, int i, int j, int x, int y, int rotation) {
		switch (rotation % 4) {
		case 0:
			g.drawImage(img, i * Frame.BLOCKSIZE + x, j * Frame.BLOCKSIZE + y, null);
			break;
		case 1:
			g.drawImage(img, j * Frame.BLOCKSIZE + x, -i * Frame.BLOCKSIZE + y + 3 * Frame.BLOCKSIZE, null);
			break;
		case 2:
			g.drawImage(img, -i * Frame.BLOCKSIZE + x + 3 * Frame.BLOCKSIZE, -j * Frame.BLOCKSIZE + y + 1 * Frame.BLOCKSIZE, null);
			break;
		case 3:
			g.drawImage(img, -j * Frame.BLOCKSIZE + x + 1 * Frame.BLOCKSIZE, i * Frame.BLOCKSIZE + y, null);
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
