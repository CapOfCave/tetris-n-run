package data;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import graphics.GameFrame;
import loading.ImageLoader;

/**
 * @author Lars Created on 05.08.2018
 */
public class TetroType {
	public static final int hitboxExpansion = 2;
	protected BufferedImage img;

	private String strHitbox;
	private boolean[][] hitbox;
	private int[][] expandedHitbox;

	private Pics sliced;
	private int colorInt;
	private ImageLoader imageLoader;

	public TetroType(String strHitbox, BufferedImage img, int slicedColor, ImageLoader imageLoader) {
		this.strHitbox = strHitbox;
		this.img = img;
		this.colorInt = slicedColor;
		this.imageLoader = imageLoader;

		hitbox = new boolean[2][4];
		expandedHitbox = new int[hitbox.length + 2 * hitboxExpansion][hitbox[0].length + 2 * hitboxExpansion];
		for (int i = 0; i < expandedHitbox.length; i++) {
			for (int j = 0; j < expandedHitbox[i].length; j++) {
				expandedHitbox[i][j] = 4 * hitboxExpansion * hitboxExpansion + 4 * hitboxExpansion + 1;
			}
		}
		for (int y = 0; y < hitbox.length; y++) {
			for (int x = 0; x < hitbox[y].length; x++) {
				hitbox[y][x] = strHitbox.charAt(y * hitbox[y].length + x) == '0' ? false : true;
				if (hitbox[y][x]) {
					for (int i = 0; i < 2 * hitboxExpansion + 1; i++) {
						for (int j = 0; j < 2 * hitboxExpansion + 1; j++) {
							expandedHitbox[y + i][x + j] = Math.min(expandedHitbox[y + i][x + j],
									Math.abs(i - hitboxExpansion) + Math.abs(j - hitboxExpansion));
						}
					}
				}
			}
		}

	}

	public void draw(Graphics g, int x, int y, int rotation) {

		for (int j = 0; j < hitbox.length; j++) {
			for (int i = 0; i < hitbox[j].length; i++) {
				if (hitbox[j][i]) {
					drawBlock(g, i, j, x, y, rotation, GameFrame.BLOCKSIZE, img);
				}
			}
		}
	}

	public void draw(Graphics g, int x, int y, int blockWidth, int rotation, BufferedImage img) {
		
		for (int j = 0; j < hitbox.length; j++) {
			for (int i = 0; i < hitbox[j].length; i++) {
				if (hitbox[j][i]) {
					if (img == null) {
						drawBlock(g, i, j, x, y, rotation, blockWidth, this.img);
					} else {
						drawBlock(g, i, j, x, y, rotation, blockWidth, img);
					}
					
				}
			}
		}
	}

	
	public void drawBlock(Graphics g, int i, int j, int x, int y, int rotation, int blockWidth, BufferedImage img) {
		switch (rotation % 4) {
		case 0:
			g.drawImage(img, i * blockWidth + x, j * blockWidth + y, blockWidth, blockWidth, null);
			break;
		case 1:
			g.drawImage(img, j * blockWidth + x, -i * blockWidth + y + 3 * blockWidth, blockWidth, blockWidth, null);
			break;
		case 2:
			g.drawImage(img, -i * blockWidth + x + 3 * blockWidth, -j * blockWidth + y + 1 * blockWidth, null);
			break;
		case 3:
			g.drawImage(img, -j * blockWidth + x + 1 * blockWidth, i * blockWidth + y, blockWidth, blockWidth, null);
			break;
		}
	}

	public boolean[][] getHitbox() {
		return hitbox;
	}

	public int[][] getExpandedHitbox() {
		return expandedHitbox;
	}

	public String getStrHitbox() {
		return strHitbox;
	}

	public Pics getSliced() {
		if (sliced == null) {
			sliced = new Pics("/res/slicedTetros/slicedPane" + colorInt + ".png", GameFrame.BLOCKSIZE, imageLoader);
		}
		return sliced;
	}
	
	public int getColor() {
		return colorInt;
	}

}
