package data;

import java.awt.Graphics2D;

import graphics.Frame;
import logics.Camera;

/**
 * @author Lars Created on 08.08.2018
 */
public class Tetro {

	private TetroType type;
	private int x, y;
	private int rotation;

	private Camera camera;

	public static Tetro NULL = new Tetro(null, 0, 0, 0, null);
	
	public Tetro(TetroType type, int x, int y, int rotation, Camera camera) {
		this.camera = camera;

		this.type = type;
		this.x = x;
		this.y = y;
		this.rotation = rotation;

	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;

	}

	public void setY(int y) {
		this.y = y;
	}

	public void draw(Graphics2D g, boolean debugMode) {
		type.draw(g, x * Frame.BLOCKSIZE - camera.getX(), y * Frame.BLOCKSIZE - camera.getY(), rotation, debugMode);
	}

	public TetroType getType() {
		return type;
	}

	public int getBlockAt(int x, int y) {
		int dx = x - this.x;
		int dy = y - this.y;
		switch (rotation % 4) {
		case 0:
			int index0 = dx + dy * 4;
			if (index0 >= 0 && index0 < 8) {
				return Character.digit(type.getStrHitbox().charAt(index0), 4);
			} else {
				return 0;
			}
		case 1:
			int index1 = 3 - dy + 4 * dx;
			if (index1 >= 0 && index1 < 8) {
				return Character.digit(type.getStrHitbox().charAt(index1), 4);
			} else {
				return 0;
			}
		case 2:
			int index2 = 7 - dx - dy * 4;
			if (index2 >= 0 && index2 < 8) {
				return Character.digit(type.getStrHitbox().charAt(index2), 4);
			} else {
				return 0;
			}
		case 3:
			int index3 = 4 - 4 * dx + dy;
			if (index3 >= 0 && index3 < 8) {
				return Character.digit(type.getStrHitbox().charAt(index3), 4);
			} else {
				return 0;
			}
		}
		return 0;
	}

	public int getRotation() {
		return rotation;
	}
	
	

}
