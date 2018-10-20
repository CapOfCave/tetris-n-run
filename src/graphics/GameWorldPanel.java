package graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import data.Level;
import input.KeyHandler;
import input.MouseHandler;
import logics.InHandHandler;
import logics.worlds.GameWorld;

/**
 * @author Lars Created on 05.08.2018
 */
public class GameWorldPanel extends Panel {

	private static final long serialVersionUID = 1L;
	private final Rectangle inventoryPanel = new Rectangle(20, 641, 1260, 189);

	private InHandHandler inHandHandler;
	private MouseHandler mouseHandler;

	public GameWorldPanel(Level level, KeyHandler keyHandler, Frame frame) {
		super(level, keyHandler, frame);

		world = new GameWorld(gamePanel, blockSize, level, keyHandler, frame);

		inHandHandler = new InHandHandler(world, tetroDrawPositions);
		mouseHandler = new MouseHandler(inHandHandler, (GameWorld) world);
		addMouseListener(mouseHandler);
		addMouseMotionListener(mouseHandler);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D gameGraphics = (Graphics2D) g.create(gamePanel.x, gamePanel.y, gamePanel.width, gamePanel.height);
		Graphics2D inventoryGraphics = (Graphics2D) g.create(inventoryPanel.x, inventoryPanel.y, inventoryPanel.width,
				inventoryPanel.height);

		world.draw(gameGraphics, interpolation, debugMode);
		inHandHandler.drawPreview(g, debugMode);
		world.drawPlayer(gameGraphics, interpolation, debugMode);
		((GameWorld) world).drawInventory(inventoryGraphics);
		for (int i = 0; i < tetroTypes.size(); i++) {
			tetroTypes.get(i).draw(g, tetroDrawPositions.get(i).x, tetroDrawPositions.get(i).y, 0, debugMode);
		}

	}

}
