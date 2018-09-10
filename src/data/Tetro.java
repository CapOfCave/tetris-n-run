package data;

import java.awt.Graphics2D;
import java.awt.Point;

import logics.Camera;

/**
 * @author Lars Created on 08.08.2018
 */
public class Tetro {

	private int blockSize;
	private TetroType type;
	private int x, y;
	private int rotation;
	private Point startP1 = new Point(-1, -1);
	private Point startP2 = new Point(-1, -1);

	private Camera camera;

	public Tetro(TetroType type, int x, int y, int rotation, int blockSize, Camera camera) {
		this.camera = camera;

		this.blockSize = blockSize;
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
		type.draw(g, x * blockSize - camera.getX(), y * blockSize - camera.getY(), rotation, debugMode);
	}

	public TetroType getType() {
		return type;
	}

	public Point getStartPoint1() {
		if (startP1.x >= 0 && startP1.y >= 0) {
			return startP1;
		}
		for (int i = x; i < x + 4; i++) {
			for (int j = y; j < y + 4; j++) {
				if (getBlockAt(i, j) == 2) {
					startP1 = new Point(i, j);
					return startP1;
				}
			}
		}
		System.out.println("Warning. Returning null @Tetro#getStartPoint1()");
		return null;
	}

	public Point getStartPoint2() {
		if (startP2.x >= 0 && startP2.y >= 0) {
			return startP2;
		}
		for (int i = x; i < x + 4; i++) {
			for (int j = y; j < y + 4; j++) {
				if (getBlockAt(i, j) == 3) {
					startP2 = new Point(i, j);
					return startP2;
				}
			}
		}
		System.out.println("Warning. Returning null @Tetro#getStartPoint2()");
		return null;
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
