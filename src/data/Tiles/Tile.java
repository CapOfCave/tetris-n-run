package data.Tiles;

import java.awt.image.BufferedImage;

import graphics.Frame;
import loading.ImageLoader;
import logics.Overworld;
import logics.World;

/**
 * @author Lars Created on 11.09.2018
 */
public class Tile {

	private BufferedImage img;

	protected char key;
	protected int posX, posY;
	protected Frame frame;
	protected boolean solid;
	

	public Tile(char key, int posX, int posY, boolean solid, Frame frame) {
		this.key = key;
		this.posX = posX;
		this.posY = posY;
		this.frame = frame;
		img = ImageLoader.loadImage("/res/block" + key + ".png");

	}

	public BufferedImage getImg() {
		return img;
	}

	public void setImg(BufferedImage img) {
		this.img = img;
	}

	public char getKey() {
		return key;
	}

	public void setKey(char key) {
		this.key = key;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public void eventWhenEntering() {
		
	}

	public boolean isSolid() {
		return solid;
	}

}
