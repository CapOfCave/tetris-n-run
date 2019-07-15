package data.Tiles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import data.Animation;
import graphics.GameFrameHandler;
import logics.World;
import tools.GraphicalTools;

public class PressurePlateTile extends Tile {

	private int color;
	private HashMap<String, Animation> pictures;
	boolean pressedByPlayer = false;
	int moveBlocksOnTile = 0;
	Color drawColor = Color.BLACK;
	private BufferedImage image3d;

	public PressurePlateTile(char key, int posX, int posY) {
		super(key, posX, posY, false, true, true);
		needsBackGround = true;
		if (key == 'à') {
			color = 0;
			drawColor = new Color(209, 17, 65); // rot
		} else if (key == 'è') {
			color = 1;
			drawColor = new Color(0, 177, 89); // Grün
		} else if (key == 'ì') {
			color = 2;
			drawColor = new Color(0, 174, 219); // Cyan
		} else if (key == 'ò') {
			color = 3;
			drawColor = new Color(255, 196, 37); // gelb
		} else if (key == 'ù') {
			color = 4;
			drawColor = new Color(243, 119, 53); // orange
		} else if (key == 'À') {
			color = 5;
			drawColor = new Color(198, 76, 255); // pink

		}

	}

	public void setWorld(World world) {
		super.setWorld(world);
		pictures = world.loadAnimations("/res/anims/pressurePlate.txt");
		image3d = GraphicalTools.setColor(world.getImage("/res/blocks/pressurePlate.png"), drawColor);
	}

	@Override
	public void eventWhenEntering() {
		world.playSound("ButtonSound", -15f);
		pressedByPlayer = true;
		if (moveBlocksOnTile == 0) {
			world.switchDoorsTogglestateStays(color);
			image3d = GraphicalTools.setColor(
					pictures.get(pressedByPlayer || moveBlocksOnTile > 0 ? "state0" : "state1").getImage(), drawColor);
		}
	}

	@Override
	public void eventWhenMoveBlockEntering() {
		world.playSound("ButtonSound", -15f);
		moveBlocksOnTile++;
		if (!pressedByPlayer && moveBlocksOnTile == 1) {
			world.switchDoorsTogglestateStays(color);
			image3d = GraphicalTools.setColor(
					pictures.get(pressedByPlayer || moveBlocksOnTile > 0 ? "state0" : "state1").getImage(), drawColor);
		}
	}

	@Override
	public void eventWhenMoveBlockLeaving() {
		world.playSound("ButtonSound", -15f);
		moveBlocksOnTile--;
		if (!pressedByPlayer && moveBlocksOnTile == 0) {
			world.switchDoorsTogglestateStays(color);
			pressedByPlayer = false;
			image3d = GraphicalTools.setColor(
					pictures.get(pressedByPlayer || moveBlocksOnTile > 0 ? "state0" : "state1").getImage(), drawColor);
		}
	}

	@Override
	public void eventWhenLeaving() {
		world.playSound("ButtonSound", -15f);
		if (moveBlocksOnTile == 0) {
			world.switchDoorsTogglestateStays(color);
			pressedByPlayer = false;
			image3d = GraphicalTools.setColor(pictures.get(pressedByPlayer ? "state0" : "state1").getImage(),
					drawColor);
		}
	}

	@Override
	public void draw(Graphics g, float interpolation) {
		g.drawImage(image3d, (int) (posX * GameFrameHandler.BLOCKSIZE - world.cameraX()),
				(int) (posY * GameFrameHandler.BLOCKSIZE - world.cameraY()), null);
	}

	@Override
	public double getHeight() {
		return -1;
	}

	public Color getColor() {
		return drawColor;
	}

	public boolean isOccupiedByMoveblock() {
		return moveBlocksOnTile >= 1;
	}
}
