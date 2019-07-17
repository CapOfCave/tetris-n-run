package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import data.Level;
import data.TetroType;
import data.Tiles.SaveNLoadTile;
import input.KeyHandler;
import input.MouseHandler;
import logics.InHandHandler;
import logics.World;

/**
 * @author Lars Created on 05.08.2018
 */
public class GameWorldPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private InHandHandler inHandHandler;
	private MouseHandler mouseHandler;

	private final int tetrotypeDrawSize = 30;
	private final int tetrotypeDrawSizeHovered = 34;
	public final int removedTetroFocusTicks = 5;

	private final int tetroHoverOffset = (tetrotypeDrawSizeHovered - tetrotypeDrawSize) / 2;

	private BufferedImage emptyTetro;
	private BufferedImage backLevel;

	private int seconds = 0;

	private int currentFocusTicks = 0;
	private int focusedTetroType = -1;

	public GameWorldPanel(Level level, KeyHandler keyHandler, GameFrameHandler frame, ArrayList<TetroType> tetroTypes) {
		super(level, keyHandler, frame, tetroTypes);

		world = new World(gamePanel, level, keyHandler, frame);

		inHandHandler = new InHandHandler(world, tetroDrawPositions, tetrotypeDrawSize);
		mouseHandler = new MouseHandler(inHandHandler, world);
		world.addInHandHandler(inHandHandler);
		addMouseListener(mouseHandler);
		addMouseMotionListener(mouseHandler);

		emptyTetro = frame.getImage("/res/tetros/empty.png");
		backLevel = frame.getImage("/res/imgs/backLevel.png");
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, GameFrameHandler.PANEL_WIDTH, GameFrameHandler.PANEL_HEIGHT);
		g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, 44));
		g.setColor(Color.BLACK);

		Graphics2D previewGraphics = (Graphics2D) g.create(54, 680, 1000, 1000);
		world.drawPlayerPreview(previewGraphics);

		Graphics2D gameGraphics = (Graphics2D) g.create(gamePanel.x, gamePanel.y, gamePanel.width, gamePanel.height);
		Graphics2D mapGraphics = (Graphics2D) g.create(870, 670, 384, 184);

		world.draw(gameGraphics, interpolation, keyHandler.inDebugMode());
		world.drawMap(mapGraphics);

		drawTetroPreview(g);

		g.drawImage(backLevel, 0, 0, 1300, 900, null);

		g.setColor(Color.BLACK);
		g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, 44));
		g.drawString("Overworld", 1020, 585);

		drawConsole(g);

		inHandHandler.drawPreview(g, keyHandler.inDebugMode());

		if (keyHandler.inDebugMode()) {
			drawDebug(gameGraphics);
		}

	}

	public void setLastUsedSALTile(SaveNLoadTile tile) {
		world.setLastUsedSALTile(tile);

	}

	public void updateTetros() {
		((SaveNLoadTile) world.getTileAt(world.getPlayer().getTileY(), world.getPlayer().getTileX())).refreshTetros();

	}

	@Override
	public void tick() {
		super.tick();
		if (currentFocusTicks > 0)
			currentFocusTicks--;
	}

	@Override
	public void secondPassed() {
		seconds++;
	}

	public int getSeconds() {
		return seconds;
	}

	public void focusTetroType(int color) {
		focusedTetroType = color;
		currentFocusTicks = removedTetroFocusTicks;

	}

	public int getFocusedTetroType() {
		if (currentFocusTicks > 0) {
			return focusedTetroType;
		} else {
			return -1;
		}
	}

	

	private void drawTetroPreview(Graphics g) {
		for (int i = 0; i < tetroTypes.size(); i++) {
			BufferedImage img = null;
			if (world.getTetroAmount()[i] <= 0) {
				img = emptyTetro;
			}

			if (tetroTypes.get(i) == inHandHandler.tetroInHand && world.getTetroAmount()[i] <= 1) {
				tetroTypes.get(i).draw(g, tetroDrawPositions.get(i).x, tetroDrawPositions.get(i).y, tetrotypeDrawSize,
						0, emptyTetro);
			} else if (tetroTypes.get(i) == mouseHandler.getCurrentlyHoveredTetro()) {
				tetroTypes.get(i).draw(g, tetroDrawPositions.get(i).x - tetroHoverOffset * 4,
						tetroDrawPositions.get(i).y - tetroHoverOffset * 2, tetrotypeDrawSizeHovered, 0, img);
			} else if (getFocusedTetroType() == i) {
				tetroTypes.get(i).draw(g, tetroDrawPositions.get(i).x - tetroHoverOffset * 4,
						tetroDrawPositions.get(i).y - tetroHoverOffset * 2, tetrotypeDrawSizeHovered, 0, img);
			} else {
				tetroTypes.get(i).draw(g, tetroDrawPositions.get(i).x, tetroDrawPositions.get(i).y, tetrotypeDrawSize,
						0, img);
			}

			g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, 25));
			if (i % 2 == 0 && world.getTetroAmount().length > 0 && world.getTetroAmount().length - 1 >= i) {
				g.drawString("" + world.getTetroAmount()[i], 1212, 153 + 58 * i + i / 2);
			} else if (world.getTetroAmount().length > 0 && world.getTetroAmount().length - 1 >= i) {
				g.drawString("" + world.getTetroAmount()[i], 1085, 212 + 58 * i + i / 2);
			}

		}
	}
}
