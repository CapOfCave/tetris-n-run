package data.Tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import graphics.Frame;
import logics.worlds.World;

/**
 * @author Lars Created on 11.09.2018
 */
public abstract class Tile {

	protected BufferedImage img;

	protected char key;
	protected int posX, posY;
	protected Frame frame;
	protected boolean walkable;
	protected boolean walkableWithTetro;

	protected boolean isBlockingTetro;
	protected World world;

	public Tile(char key, int posX, int posY, boolean walkable, boolean walkableWithTetro, Frame frame) {
		this.key = key;
		this.posX = posX;
		this.posY = posY;
		this.frame = frame;
		this.walkable = walkable;
		this.walkableWithTetro = walkableWithTetro;
		isBlockingTetro = false;
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

	public abstract void eventWhenEntering();

	public boolean isWalkable() {
		return walkable;
	}

	public void setBlockingTetro(boolean isBlockingTetro) {
		this.isBlockingTetro = isBlockingTetro;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public boolean isWalkableWithTetro() {
		return walkableWithTetro;
	}

	public void draw(Graphics g, int i, int j) {
		g.drawImage(img, (int) (i * Frame.BLOCKSIZE - world.cameraX()),
				(int) (j * Frame.BLOCKSIZE - world.cameraY()), null);
		
	}
}
