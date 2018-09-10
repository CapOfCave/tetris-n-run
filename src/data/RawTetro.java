package data;

import java.util.ArrayList;

import logics.Camera;

/**
 * @author Lars Created on 27.08.2018
 */
public class RawTetro {
	private int type;
	private int x;
	private int y;
	private int rotation;

	public RawTetro(int type, int x, int y, int rotation) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.rotation = rotation;
	}

	public int getType() {
		return type;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getRotation() {
		return rotation;
	}

	public Tetro createTetro(ArrayList<TetroType> tetroTypes, int blockSize, Camera camera) {
		return new Tetro(tetroTypes.get(type), x, y, rotation, blockSize, camera);
	}
}
