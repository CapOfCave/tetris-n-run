package data;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * @author Lars Created on 05.08.2018
 */
public class TetroType {
	protected BufferedImage img;
	private int blockSize;

	private String strMovepattern;
	private String strHitbox;
	private boolean[][] hitbox;

	public TetroType(String strMovepattern, String strHitbox, BufferedImage img, int blockSize) {
		this.strMovepattern = strMovepattern;
		this.strHitbox = strHitbox;
		this.img = img;
		this.blockSize = blockSize;

		hitbox = new boolean[2][4];
		for (int y = 0; y < hitbox.length; y++) {
			for (int x = 0; x < hitbox[y].length; x++) {
				hitbox[y][x] = strHitbox.charAt(y * hitbox[y].length + x) == '0' ? false : true;
			}
		}

	}

	public void draw(Graphics g, int x, int y, int rotation, boolean debugMode) {
		for (int j = 0; j < hitbox.length; j++) {
			for (int i = 0; i < hitbox[j].length; i++) {
				if (hitbox[j][i]) {
					drawBlock(g, i, j, x, y, rotation, debugMode);
				}
			}
		}
	}

	public void drawBlock(Graphics g, int i, int j, int x, int y, int rotation, boolean debugMode) {
		if (debugMode) {
			g.setColor(Color.PINK);
			g.fillOval(x, y, 5, 5);
		}
		switch (rotation % 4) {
		case 0:
			g.drawImage(img, i * blockSize + x, j * blockSize + y, null);
			break;
		case 1:
			g.drawImage(img, j * blockSize + x, -i * blockSize + y + 3 * blockSize, null);
			break;
		case 2:
			g.drawImage(img, -i * blockSize + x + 3 * blockSize, -j * blockSize + y + 1 * blockSize, null);
			break;
		case 3:
			g.drawImage(img, -j * blockSize + x + 1 * blockSize, i * blockSize + y, null);
			break;
		}
	}

	public boolean[][] getHitbox() {
		return hitbox;
	}

	public String getStrHitbox() {
		return strHitbox;
	}

	public String getStrMovepattern() {
		return strMovepattern;
	}


}
