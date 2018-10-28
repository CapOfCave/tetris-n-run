package data.Tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import data.DrawAndSortable;
import graphics.Frame;
import graphics.Renderer;
import loading.ImageLoader;
import logics.worlds.World;

/**
 * @author Lars Created on 11.09.2018
 */
public abstract class Tile implements DrawAndSortable {

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
		img = ImageLoader.loadImage("/res/blocks/block0.png");
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

	@Override
	public void draw(Graphics g, float interpolation) {
//		g.drawImage(img, (int) (posX * Frame.BLOCKSIZE - world.cameraX()),
//				(int) (posY * Frame.BLOCKSIZE - world.cameraY()), null);
	}

	@Override
	public double getHeight() {
		return posY * Frame.BLOCKSIZE;
	}

	@Override
	public int compareTo(DrawAndSortable o) {
		if (this.getHeight() == o.getHeight()) {
			return 0;
		} else if (this.getHeight() < o.getHeight()) {
			return -1;
		} else {
			return 1;
		}
	}

	public void drawBackground(Graphics g, float interpolation) {
		g.drawImage(img, (int) (posX * Frame.BLOCKSIZE - world.cameraX()),
				(int) (posY * Frame.BLOCKSIZE - world.cameraY()), null);

	}

	@Override
	public void addTo(Renderer renderer) {
		renderer.addDrawable(this);
	}
}
