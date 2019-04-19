package logics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import data.TetroType;
import graphics.GameFrame;
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
	private int drawSize;

	public InHandHandler(World world, ArrayList<Point> tetroTypeOffsets, int drawSize) {
		this.world = world;
		this.tetroTypeOffsets = tetroTypeOffsets;
		this.drawSize = drawSize;
	}

	public boolean isHoldingTetro() {
		return tetroInHand != null;
	}

	public boolean setInHand(int x, int y) {

		tetroInHand = getTetroTypeAt(x, y);
		return tetroInHand != null;
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
		offset_y = (rotation % 2 + 1) * 2 * GameFrame.BLOCKSIZE - offset_x_alt;
	}

	public void placeInHand() {
		if (tetroInHand != null) {
			TetroType t = tetroInHand;
			tetroInHand = null;
			world.addTetro(t, (mouse_x - offset_x) - world.getGameBoundsX(),
					(mouse_y - offset_y) - world.getGameBoundsY(), mouse_x, mouse_y, rotation);

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
			g.drawOval(mouse_x - offset_x, mouse_y - offset_y, 5, 5);
			g.setFont(new Font("helvetica", Font.PLAIN, 10));
			g.drawString(getCenterX() + "|" + getCenterY(), 400, 80);
			g.setColor(Color.GREEN);
			if (rotation % 2 == 0)
				g.drawOval(mouse_x - offset_x + 2 * GameFrame.BLOCKSIZE, mouse_y - offset_y + 1 * GameFrame.BLOCKSIZE,
						5, 5);
			else
				g.drawOval(mouse_x - offset_x + 1 * GameFrame.BLOCKSIZE, mouse_y - offset_y + 2 * GameFrame.BLOCKSIZE,
						5, 5);

		}
	}

	private TetroType getTetroTypeAt(int x, int y) {

		TetroType tetroApproximation = null;
		for (int i = 0; i < world.getTetroTypeCount(); i++) {
			Point p = tetroTypeOffsets.get(i);
			if (x > p.x && x < p.x + 4 * drawSize && y > p.y && y < p.y + 2 * drawSize) {
				tetroApproximation = world.getTetroType(i);
				offset_x = (x - p.x) * GameFrame.BLOCKSIZE / drawSize;
				offset_y = (y - p.y) * GameFrame.BLOCKSIZE / drawSize;
				rotation = 0;
				break;
			}
		}

		// TODO? exact tetroHitbox

		return tetroApproximation;
	}

	public double getCenterX() {
		if (rotation % 2 == 0)
			return mouse_x - offset_x + 2 * GameFrame.BLOCKSIZE - world.getGameBoundsX();
		else
			return mouse_x - offset_x + 1 * GameFrame.BLOCKSIZE - world.getGameBoundsX();
	}

	public double getCenterY() {
		if (rotation % 2 == 1)
			return mouse_y - offset_y + 2 * GameFrame.BLOCKSIZE - world.getGameBoundsY();
		else
			return mouse_y - offset_y + 1 * GameFrame.BLOCKSIZE - world.getGameBoundsY();
	}
}
