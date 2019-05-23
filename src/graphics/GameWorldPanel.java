package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import data.Level;
import data.RawPlayer;
import data.Tiles.SaveNLoadTile;
import input.KeyHandler;
import input.MouseHandler;
import loading.ImageLoader;
import logics.InHandHandler;
import logics.worlds.World;

/**
 * @author Lars Created on 05.08.2018
 */
public class GameWorldPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private InHandHandler inHandHandler;
	private MouseHandler mouseHandler;

	private final int tetrotypeDrawSize = 30;

	public GameWorldPanel(int width, int height, Level level, KeyHandler keyHandler, GameFrame frame,
			RawPlayer rawPlayer) {
		super(width, height, level, keyHandler, frame);

		world = new World(gamePanel, level, keyHandler, frame, rawPlayer);

		inHandHandler = new InHandHandler(world, tetroDrawPositions, tetrotypeDrawSize);
		mouseHandler = new MouseHandler(inHandHandler, world);
		world.addInHandHandler(inHandHandler);
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

		Graphics2D previewGraphics = (Graphics2D) g.create(54, 680, 1000, 1000);
		world.drawPlayerPreview(previewGraphics);

		Graphics2D gameGraphics = (Graphics2D) g.create(gamePanel.x, gamePanel.y, gamePanel.width, gamePanel.height);
		Graphics2D mapGraphics = (Graphics2D) g.create(337, 670, 384, 184);

		world.draw(gameGraphics, interpolation, debugMode);
		world.drawMap(mapGraphics);
		inHandHandler.drawPreview(g, debugMode);
		// world.drawPlayer(gameGraphics, interpolation);
		for (int i = 0; i < tetroTypes.size(); i++) {
			tetroTypes.get(i).draw(g, tetroDrawPositions.get(i).x, tetroDrawPositions.get(i).y, tetrotypeDrawSize, 0);
			g.setFont(new Font("TimesNewRoman", 1, 25));
			if (i % 2 == 0 && world.getTetroAmount().length > 0 && world.getTetroAmount().length - 1 >= i) {
				g.drawString("" + world.getTetroAmount()[i], 1212, 153 + 58 * i + i / 2);
			} else if (world.getTetroAmount().length > 0 && world.getTetroAmount().length - 1 >= i) {
				g.drawString("" + world.getTetroAmount()[i], 1085, 212 + 58 * i + i / 2);
			}
			g.setFont(new Font("TimesNewRoman", 1, 44));
		}

		g.drawImage(ImageLoader.loadImage("/res/backLevel.png"), 0, 0, 1300, 900, null);

		g.setColor(Color.BLACK);
		g.drawString("Overworld", 1020, 585);
		String[] text = frame.getText();
		g.setFont(new Font("Timesnewroman", 0, 18));
		if (text.length > 0) {
			for (int i = 0; i < text.length; i++) {
				g.drawString(text[i], 920, 710 + (i * 21));
			}
		}

		if (debugMode) {
			gameGraphics.setColor(Color.WHITE);
			gameGraphics.fillRect(0, 0, 170, 55);
			drawDebug(gameGraphics);
			g.setColor(Color.GREEN);
			g.drawRect(gamePanel.x, gamePanel.y, gamePanel.width, gamePanel.height);
		}
	}

	public void setLastUsedSALTile(SaveNLoadTile tile) {
		world.setLastUsedSALTile(tile);

	}

}
