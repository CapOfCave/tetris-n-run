package data.Tiles;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import data.DrawAndSortable;
import graphics.GameFrame;
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
	protected GameFrame frame;
	protected boolean walkable;
	protected boolean walkableWithTetro;
	protected boolean tetroPlacable;
	protected Point offset = new Point(0, 0);
	protected String name;

	protected boolean isBlockingTetro;
	protected World world;
	protected int[] tetroAmount;

	protected boolean needsBackGround = false;;

	public Tile(char key, int posX, int posY, boolean walkable, boolean walkableWithTetro, boolean tetroPlacable,
			GameFrame frame) {
		this.key = key;
		this.posX = posX;
		this.posY = posY;
		this.frame = frame;
		this.walkable = walkable;
		this.walkableWithTetro = walkableWithTetro;
		this.tetroPlacable = tetroPlacable;

		isBlockingTetro = false;
		img = ImageLoader.loadImage("/res/blocks/block0.png");
	}

	public void tick() {

	}

	public double getX() {
		return posX * GameFrame.BLOCKSIZE;
	}

	public double getY() {
		return posY * GameFrame.BLOCKSIZE;
	}

	public BufferedImage getImg() {
		return img;
	}

	public void setImg(BufferedImage img) {
		this.img = img;
	}

	public int[] getTetroAmount() {
		return tetroAmount;
	}

	public void setTetroAmount(int[] tetroAmount) {
		this.tetroAmount = tetroAmount;
	}

	public String getName() {
		return name;
	}

	public char getKey() {
		return key;
	}

	public void setKey(char key) {
		this.key = key;
	}

	public Point getOffSet() {
		return offset;
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
		// do nothing
	}

	public void eventWhenLeaving() {
		// do nothing
	}

	public void eventWhenKeyPressed() {
		// do nothing
	}

	public boolean isWalkable() {
		return walkable;
	}

	// TODO remove
	public void setBlockingTetro(boolean isBlockingTetro) {
		this.isBlockingTetro = isBlockingTetro;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public boolean isWalkableWithTetro() {
		return walkableWithTetro;
	}

	public boolean isTetroPlacable() {
		return tetroPlacable;
	}

	@Override
	public void draw(Graphics g, float interpolation) {

	}

	@Override
	public double getHeight() {
		return posY * GameFrame.BLOCKSIZE;
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
		if (needsBackGround) {
			g.drawImage(img, (int) (posX * GameFrame.BLOCKSIZE - world.cameraX()),
					(int) (posY * GameFrame.BLOCKSIZE - world.cameraY()), null);
		}
	}

	@Override
	public void addTo(Renderer renderer) {
		renderer.addDrawable(this);
	}

	public void interact() {
	}

	public void eventWhenMoveBlockEntering() {
		// do nothing

	}

	public void eventWhenMoveBlockLeaving() {
		// do nothing
	}
}
