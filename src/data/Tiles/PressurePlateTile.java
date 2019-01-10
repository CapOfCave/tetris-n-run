package data.Tiles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import data.Animation;
import graphics.Frame;
import loading.AnimationLoader;
import loading.ImageLoader;
import tools.GraphicalTools;

public class PressurePlateTile extends Tile {

	private int color;
	private HashMap<String, Animation> pictures;
	boolean pressedByPlayer = false;
	int moveBlocksOnTile = 0;
	Color drawColor = Color.BLACK;
	private BufferedImage image3d;

	public PressurePlateTile(char key, int posX, int posY, Frame frame) {
		super(key, posX, posY, false, true, true, frame);

		if (key == 'à') {
			color = 0;
			drawColor = Color.RED;
		} else if (key == 'è') {
			color = 1;
			drawColor = Color.GREEN;
		} else if (key == 'ì') {
			color = 2;
			drawColor = Color.BLUE;
		} else if (key == 'ò') {
			color = 3;
			drawColor = Color.YELLOW;
		}else if (key == 'ù') {
			color = 4;
			drawColor = Color.gray;
		}
		pictures = AnimationLoader.loadAnimations("/res/anims/PressurePlate.txt");
		image3d = GraphicalTools.setColor(ImageLoader.loadImage("/res/blocks/PressurePlate.png"), drawColor);
	}

	@Override
	public void eventWhenEntering() {
		pressedByPlayer = true;
		if (moveBlocksOnTile == 0) {
			world.switchDoors(color);
			image3d = GraphicalTools.setColor(
					pictures.get(pressedByPlayer || moveBlocksOnTile > 0 ? "state0" : "state1").getImage(), drawColor);
		}
	}

	@Override
	public void eventWhenMoveBlockEntering() {
		moveBlocksOnTile++;
		if (!pressedByPlayer && moveBlocksOnTile == 1) {
			world.switchDoors(color);
			image3d = GraphicalTools.setColor(
					pictures.get(pressedByPlayer || moveBlocksOnTile > 0 ? "state0" : "state1").getImage(), drawColor);
		}
	}

	@Override
	public void eventWhenMoveBlockLeaving() {
		moveBlocksOnTile--;
		if (!pressedByPlayer && moveBlocksOnTile == 0) {
			world.switchDoors(color);
			 pressedByPlayer = false;
			image3d = GraphicalTools.setColor(
					pictures.get(pressedByPlayer || moveBlocksOnTile > 0 ? "state0" : "state1").getImage(), drawColor);
		}
	}

	@Override
	public void eventWhenLeaving() {
		if (moveBlocksOnTile == 0) {
			world.switchDoors(color);
			pressedByPlayer = false;
			image3d = GraphicalTools.setColor(pictures.get(pressedByPlayer ? "state0" : "state1").getImage(), drawColor);
		}
	}

	@Override
	public void draw(Graphics g, float interpolation) {
		g.drawImage(image3d, (int) (posX * Frame.BLOCKSIZE - world.cameraX()),
				(int) (posY * Frame.BLOCKSIZE - world.cameraY()), null);
	}

	@Override
	public double getHeight() {
		return -1;
	}
}
