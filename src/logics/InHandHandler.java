package logics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import data.TetroType;
import graphics.GameFrameHandler;

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
	private ArrayList<Rectangle> tetroTypeOffsets;
	private int drawSize;
	private static String[] tetroPreviewString = { "empty200.png", "empty150.png", "empty100.png", "empty70.png",
			"empty35.png", "empty5.png" };
	private static BufferedImage[] tetroPreview;

	public InHandHandler(World world, ArrayList<Rectangle> tetroDrawPositions, int drawSize) {
		if (tetroPreview == null) {
			tetroPreview = new BufferedImage[tetroPreviewString.length];
			for (int i = 0; i < tetroPreviewString.length; i++) {
				tetroPreview[i] = world.getImage("/res/blocks/emptyTiles/" + tetroPreviewString[i]);
			}
		}
		this.world = world;
		this.tetroTypeOffsets = tetroDrawPositions;
		this.drawSize = drawSize;
	}

	public boolean isHoldingTetro() {
		return tetroInHand != null;
	}

	public void setInHand(int x, int y) {

		TetroType clickedOn = getTetroTypeAt(x, y);
		if (clickedOn == null || world.getTetroAmount()[clickedOn.getColor()] > 0 || world.noClip()) {
			tetroInHand = clickedOn;
		} else {
			world.playSound("error", -5f);
		}
//		
		
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
		offset_y = (rotation % 2 + 1) * 2 * GameFrameHandler.BLOCKSIZE - offset_x_alt;
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
				g.drawOval(mouse_x - offset_x + 2 * GameFrameHandler.BLOCKSIZE, mouse_y - offset_y + 1 * GameFrameHandler.BLOCKSIZE,
						5, 5);
			else
				g.drawOval(mouse_x - offset_x + 1 * GameFrameHandler.BLOCKSIZE, mouse_y - offset_y + 2 * GameFrameHandler.BLOCKSIZE,
						5, 5);

		}
	}

	public TetroType getTetroTypeAt(int x, int y) {

		TetroType tetroApproximation = null;
		for (int i = 0; i < world.getTetroTypeCount(); i++) {
			if (tetroTypeOffsets.get(i).contains(x, y)) {
				tetroApproximation = world.getTetroType(i);
				offset_x = (x - tetroApproximation.getPreviewStartX(tetroTypeOffsets.get(i), drawSize)) * GameFrameHandler.BLOCKSIZE / drawSize;
				offset_y = (y - tetroApproximation.getPreviewStartY(tetroTypeOffsets.get(i), drawSize)) * GameFrameHandler.BLOCKSIZE / drawSize;
				rotation = 0;
				break;
			}
		}

		return tetroApproximation;
	}

	public void drawFloorTiles(Graphics g) {
		if (tetroInHand != null) {
			int[][] hitbox = tetroInHand.getExpandedHitbox();
			for (int j = 0; j < hitbox.length; j++) {
				for (int i = 0; i < hitbox[j].length; i++) {
					if (hitbox[j][i] < 4 * TetroType.hitboxExpansion * TetroType.hitboxExpansion
							+ 4 * TetroType.hitboxExpansion + 1) {

						drawFloorTile(g, i, j,
								((int) ((mouse_x - offset_x - world.getGameBoundsX() + 0.5 * GameFrameHandler.BLOCKSIZE
										+ world.cameraX()) / GameFrameHandler.BLOCKSIZE) - TetroType.hitboxExpansion)
										* GameFrameHandler.BLOCKSIZE - world.cameraX(),
								((int) ((mouse_y - offset_y - world.getGameBoundsY() + 0.5 * GameFrameHandler.BLOCKSIZE
										+ world.cameraY()) / GameFrameHandler.BLOCKSIZE) - TetroType.hitboxExpansion)
										* GameFrameHandler.BLOCKSIZE - world.cameraY(),
								rotation, tetroPreview[Math.min(hitbox[j][i], tetroPreview.length - 1)]);
					}
				}
			}
		}
	}

	private void drawFloorTile(Graphics g, int dx, int dy, int x, int y, int rotation, BufferedImage img) {
		switch (rotation % 4) {
		case 0:
			g.drawImage(img, dx * GameFrameHandler.BLOCKSIZE + x, dy * GameFrameHandler.BLOCKSIZE + y, GameFrameHandler.BLOCKSIZE,
					GameFrameHandler.BLOCKSIZE, null);
			break;
		case 1:
			g.drawImage(img, dy * GameFrameHandler.BLOCKSIZE + x,
					-dx * GameFrameHandler.BLOCKSIZE + y + (4 + 2 * TetroType.hitboxExpansion - 1) * GameFrameHandler.BLOCKSIZE,
					GameFrameHandler.BLOCKSIZE, GameFrameHandler.BLOCKSIZE, null);
			break;
		case 2:
			g.drawImage(img,
					-dx * GameFrameHandler.BLOCKSIZE + x + (4 + 2 * TetroType.hitboxExpansion - 1) * GameFrameHandler.BLOCKSIZE,
					-dy * GameFrameHandler.BLOCKSIZE + y + (2 + 2 * TetroType.hitboxExpansion - 1) * GameFrameHandler.BLOCKSIZE,
					null);
			break;
		case 3:
			g.drawImage(img,
					-dy * GameFrameHandler.BLOCKSIZE + x + (2 + 2 * TetroType.hitboxExpansion - 1) * GameFrameHandler.BLOCKSIZE,
					dx * GameFrameHandler.BLOCKSIZE + y, GameFrameHandler.BLOCKSIZE, GameFrameHandler.BLOCKSIZE, null);
			break;
		}
	}

	public double getCenterX() {
		if (rotation % 2 == 0)
			return mouse_x - offset_x + 2 * GameFrameHandler.BLOCKSIZE - world.getGameBoundsX();
		else
			return mouse_x - offset_x + 1 * GameFrameHandler.BLOCKSIZE - world.getGameBoundsX();
	}

	public double getCenterY() {
		if (rotation % 2 == 1)
			return mouse_y - offset_y + 2 * GameFrameHandler.BLOCKSIZE - world.getGameBoundsY();
		else
			return mouse_y - offset_y + 1 * GameFrameHandler.BLOCKSIZE - world.getGameBoundsY();
	}
}
