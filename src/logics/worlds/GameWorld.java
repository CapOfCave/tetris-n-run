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

	public GameWorld(Rectangle graphicClip, Level level, KeyHandler keyHandler, Frame frame) {
		super(graphicClip, level, keyHandler, frame);
	}

	public void drawInventory(Graphics2D g) {
		player.drawInventory(g);
	}

	public void inventoryClick(int x, int y) {
		player.inventoryClick(x, y);

	}

}
