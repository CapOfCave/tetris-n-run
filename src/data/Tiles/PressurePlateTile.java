package data.Tiles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import data.Animation;
import graphics.GameFrame;
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

	public PressurePlateTile(char key, int posX, int posY, GameFrame frame) {
		super(key, posX, posY, false, true, true, frame);
		needsBackGround = true; //TODO adapt 3d graphics
		if (key == 'à') {
			color = 0;
			drawColor = new Color(209, 17, 65);
		} else if (key == 'è') {
			color = 1;
			drawColor = new Color(0, 177, 89);
		} else if (key == 'ì') {
			color = 2;
			drawColor = new Color(0, 174, 219);
		} else if (key == 'ò') {
			color = 3;
			drawColor = new Color(255, 196, 37);
		}else if (key == 'ù') {
			color = 4;
			drawColor = new Color(243, 119, 53);

		}
		pictures = AnimationLoader.loadAnimations("/res/anims/pressurePlate.txt");
		image3d = GraphicalTools.setColor(ImageLoader.loadImage("/res/blocks/PressurePlate.png"), drawColor);
	}

	@Override
	public void eventWhenEntering() {
		frame.playSound("ButtonSound", -15f);
		pressedByPlayer = true;
		if (moveBlocksOnTile == 0) {
			world.switchDoors(color);
			image3d = GraphicalTools.setColor(
					pictures.get(pressedByPlayer || moveBlocksOnTile > 0 ? "state0" : "state1").getImage(), drawColor);
		}
	}

	@Override
	public void eventWhenMoveBlockEntering() {
		frame.playSound("ButtonSound", -15f);
		moveBlocksOnTile++;
		if (!pressedByPlayer && moveBlocksOnTile == 1) {
			world.switchDoors(color);
			image3d = GraphicalTools.setColor(
					pictures.get(pressedByPlayer || moveBlocksOnTile > 0 ? "state0" : "state1").getImage(), drawColor);
		}
	}

	@Override
	public void eventWhenMoveBlockLeaving() {
		frame.playSound("ButtonSound", -15f);
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
		frame.playSound("ButtonSound", -15f);
		if (moveBlocksOnTile == 0) {
			world.switchDoors(color);
			pressedByPlayer = false;
			image3d = GraphicalTools.setColor(pictures.get(pressedByPlayer ? "state0" : "state1").getImage(), drawColor);
		}
	}

	@Override
	public void draw(Graphics g, float interpolation) {
		g.drawImage(image3d, (int) (posX * GameFrame.BLOCKSIZE - world.cameraX()),
				(int) (posY * GameFrame.BLOCKSIZE - world.cameraY()), null);
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
