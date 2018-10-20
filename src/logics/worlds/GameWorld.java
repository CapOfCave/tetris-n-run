package logics.worlds;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import data.Level;
import graphics.Frame;
import input.KeyHandler;

/**
 * @author Lars Created on 05.08.2018
 */
public class GameWorld extends World {

	public GameWorld(Rectangle graphicClip, int blockSize, Level level, KeyHandler keyHandler, Frame frame) {
		super(graphicClip, blockSize, level, keyHandler, frame);
		addSpawner(10, 10, 2, 2, 2, 2, 3, true, 5);
		addSpawner(3, 3, 1, 1, 1, 1, 2, true, 3);
	}

	public void drawInventory(Graphics2D g) {
		player.drawInventory(g);
	}

	public void inventoryClick(int x, int y) {
		player.inventoryClick(x, y);

	}

}
