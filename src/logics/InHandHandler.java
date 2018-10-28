package logics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import data.TetroType;
import graphics.Frame;
import logics.worlds.World;

/**
 * @author Lars Created on 08.08.2018
 */
public class InHandHandler {

	public TetroType tetroInHand;
	private int rotation;
	private int mouse_x;
	private int mouse_y;

	private int offset_x;
	private int offset_y;

	private World world;
	private ArrayList<Point> tetroTypeOffsets;

	public InHandHandler(World world, ArrayList<Point> tetroTypeOffsets) {
		this.world = world;
		this.tetroTypeOffsets = tetroTypeOffsets;
	}

	public boolean setInHand(int x, int y) {

		tetroInHand = getTetroTypeAt(x, y);
		if (tetroInHand == null) {
			return false;
		} else {
			return true;
		}
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
		offset_y = (rotation % 2 + 1) * 2 * Frame.BLOCKSIZE - offset_x_alt;
	}

	public void placeInHand() {
		if (tetroInHand != null) {
			TetroType t = tetroInHand;
			tetroInHand = null;
			world.addTetro(t, (mouse_x - offset_x) - world.getGameBoundsX(), (mouse_y - offset_y) - world.getGameBoundsY(), rotation);
			
		}
	}

	public void setInHandPosition(int x, int y) {
		mouse_x = x;
		mouse_y = y;
	}

	public void drawPreview(Graphics g, boolean debugMode) {
		if (tetroInHand != null) {
			tetroInHand.draw(g, mouse_x - offset_x, mouse_y - offset_y, rotation);
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
		for (int i = 0; i < world.getTetroTypeCount(); i++) {
			Point p = tetroTypeOffsets.get(i);
			if (x > p.x && x < p.x + 4 * Frame.BLOCKSIZE && y > p.y && y < p.y + 2 * Frame.BLOCKSIZE) {
				tetroApproximation = world.getTetroType(i);
				offset_x = x - p.x;
				offset_y = y - p.y;
				rotation = 0;
				break;
			}
		}

		if (tetroApproximation == null && x > mouse_x - offset_x && x < mouse_x - offset_x + 4 *Frame.BLOCKSIZE && y > mouse_y - offset_y
				&& y < mouse_y - offset_y + 2 * Frame.BLOCKSIZE) {
			tetroApproximation = tetroInHand;
			offset_x = x - (mouse_x - offset_x);
			offset_y = y - (mouse_y - offset_y);
		}

		// TODO exact tetroHitbox

		return tetroApproximation;
	}

}
