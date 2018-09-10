package logics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import data.TetroType;

/**
 * @author Lars Created on 08.08.2018
 */
public class InHandHandler {

	private TetroType tetroInHand;
	private int rotation;
	private int mouse_x;
	private int mouse_y;

	private int offset_x;
	private int offset_y;

	public ArrayList<TetroType> tetroTypes;
	public ArrayList<Point> tetroOffsets;
	private int blockSize;
	private Rectangle gameBounds;

	private GameWorld gameWorld;

	public InHandHandler(ArrayList<TetroType> tetroTypes, ArrayList<Point> tetroDrawPositions, int blockSize, Rectangle gameBounds, GameWorld gameWorld) {
		this.tetroTypes = tetroTypes;
		this.tetroOffsets = tetroDrawPositions;
		this.blockSize = blockSize;
		this.gameWorld = gameWorld;
		this.gameBounds = gameBounds;
	}

	public void setInHand(int x, int y) {

		tetroInHand = getTetroTypeAt(x, y);
	}

	public void rotateInHand(boolean turnLeft) {
		if (turnLeft) {
			applyRotation();
		} else {
			applyRotation();
			applyRotation();
			applyRotation();
		}
	}

	private void applyRotation() {
		rotation = (rotation + 1) % 4;
		int offset_x_alt = offset_x;
		offset_x = offset_y;
		offset_y = (rotation % 2 + 1) * 2 * blockSize - offset_x_alt;
	}

	public void placeInHand() {
		if (tetroInHand != null) {
			gameWorld.addTetro(tetroInHand, (mouse_x - offset_x) - gameBounds.x, (mouse_y - offset_y) - gameBounds.y, rotation);
			tetroInHand = null;
		}
	}

	public void setInHandPosition(int x, int y) {
		mouse_x = x;
		mouse_y = y;
	}

	public void drawPreview(Graphics g, boolean debugMode) {
		if (tetroInHand != null) {
			tetroInHand.draw(g, mouse_x - offset_x, mouse_y - offset_y, rotation, debugMode);
		}

		if (debugMode) {
			g.setColor(Color.RED);
			g.drawOval(mouse_x, mouse_y, 5, 5);
			g.setColor(Color.GREEN);
			g.drawOval(mouse_x - offset_x, mouse_y - offset_y, 5, 5);
		}
	}

	private TetroType getTetroTypeAt(int x, int y) {

		TetroType tetroApproximation = null;
		for (int i = 0; i < tetroOffsets.size(); i++) {
			Point p = tetroOffsets.get(i);
			if (x > p.x && x < p.x + 4 * blockSize && y > p.y && y < p.y + 2 * blockSize) {
				tetroApproximation = tetroTypes.get(i);
				offset_x = x - p.x;
				offset_y = y - p.y;
				rotation = 0;
				break;
			}
		}

		if (tetroApproximation == null && x > mouse_x - offset_x && x < mouse_x - offset_x + 4 * blockSize && y > mouse_y - offset_y
				&& y < mouse_y - offset_y + 2 * blockSize) {
			tetroApproximation = tetroInHand;
			offset_x = x - (mouse_x - offset_x);
			offset_y = y - (mouse_y - offset_y);
		}

		// TODO exact tetroHitbox

		return tetroApproximation;
	}

}
