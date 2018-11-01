package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import data.Level;
import data.RawPlayer;
import input.KeyHandler;
import input.MouseHandler;
import loading.ImageLoader;
import logics.InHandHandler;
import logics.worlds.GameWorld;

/**
 * @author Lars Created on 05.08.2018
 */
public class GameWorldPanel extends Panel {

	private static final long serialVersionUID = 1L;
	

	private InHandHandler inHandHandler;
	private MouseHandler mouseHandler;

	public GameWorldPanel(Level level, KeyHandler keyHandler, Frame frame, RawPlayer rawPlayer) {
		super(level, keyHandler, frame);

		world = new GameWorld(gamePanel, level, keyHandler, frame, rawPlayer);

		inHandHandler = new InHandHandler(world, tetroDrawPositions);
		mouseHandler = new MouseHandler(inHandHandler, (GameWorld) world);
		addMouseListener(mouseHandler);
		addMouseMotionListener(mouseHandler);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("TimesNewRoman", 1, 44));
		g.setColor(Color.BLACK);
		
		
		
		Graphics2D previewGraphics = (Graphics2D) g.create( 54, 680, 1000, 1000);
		world.drawPlayerPreview(previewGraphics);
		
		Graphics2D gameGraphics = (Graphics2D) g.create(gamePanel.x, gamePanel.y, gamePanel.width, gamePanel.height);
		Graphics2D inventoryGraphics = (Graphics2D) g.create(inventoryPanel.x, inventoryPanel.y, inventoryPanel.width,
				inventoryPanel.height);

		world.draw(gameGraphics, interpolation, debugMode);
		inHandHandler.drawPreview(g, debugMode);
//		world.drawPlayer(gameGraphics, interpolation);
		((GameWorld) world).drawInventory(inventoryGraphics);
		for (int i = 0; i < tetroTypes.size(); i++) {
			tetroTypes.get(i).draw(g, tetroDrawPositions.get(i).x, tetroDrawPositions.get(i).y, 0);
		}
		
		g.drawImage(ImageLoader.loadImage("/res/backLevel.png"), 0, 0, 1300, 900, null);

		g.drawString("Overworld", 1020, 585);
	}

}
