package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import data.Level;
import data.TetroType;
import data.Tiles.SaveNLoadTile;
import input.KeyHandler;
import input.MouseHandler;
import logics.InHandHandler;
import logics.World;
import tools.Fonts;

/**
 * @author Lars Created on 05.08.2018
 */
public class GameWorldPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private InHandHandler inHandHandler;
	private MouseHandler mouseHandler;

	public final int removedTetroFocusTicks = 5;

	private BufferedImage emptyTetro;
	private BufferedImage backLevel;

	protected ArrayList<Rectangle> tetroDrawPositions;
	protected ArrayList<Rectangle> tetroAmountDrawPositions;

	private int currentFocusTicks = 0;
	private int focusedTetroType = -1;
	
	private double fontMultiplier;

	public GameWorldPanel(Level level, KeyHandler keyHandler, GameFrameHandler gameFrame,
			ArrayList<TetroType> tetroTypes) {
		super(level, keyHandler, gameFrame, tetroTypes);
		initTetroDrawPositions();
		initTetroAmountDrawPositions();
		world = new World(getGamePanelBounds(), level, keyHandler, gameFrame);

		inHandHandler = new InHandHandler(world, tetroDrawPositions, getTetroPreviewBlockSize());
		mouseHandler = new MouseHandler(inHandHandler, world, this);
		world.addInHandHandler(inHandHandler);
		addMouseListener(mouseHandler);
		addMouseMotionListener(mouseHandler);
		
		fontMultiplier = (gameFrame.getPanelWidth() / 1920.);

		emptyTetro = gameFrame.getImage("/res/tetros/empty.png");
		backLevel = gameFrame.getImage("/res/imgs/backLevel.png");
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, (int) (44 * fontMultiplier)));
		g.setColor(Color.BLACK);

		Graphics2D gameGraphics = (Graphics2D) g.create(getGamePanelBounds().x, getGamePanelBounds().y,
				getGamePanelBounds().width, getGamePanelBounds().height);
		world.draw(gameGraphics, interpolation, keyHandler.inDebugMode());

		g.drawImage(backLevel, 0, 0, gameFrame.getPanelWidth(), gameFrame.getPanelHeight(), null);
		
		Rectangle mapRect = getMapBounds();
		Graphics2D mapGraphics = (Graphics2D) g.create(mapRect.x, mapRect.y, mapRect.width, mapRect.height);
		world.drawMap(mapGraphics);
		world.drawPlayerPreview(g, getPreviewRect());
		drawTetroPreview(g);

		g.setColor(Color.BLACK);
		g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, (int) (44 * fontMultiplier)));
		Fonts.drawCenteredString("Overworld", getBackBounds(), g);

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
				tetroTypes.get(i).drawCenteredPreview(g, tetroDrawPositions.get(i), getTetroPreviewBlockSize(),
						emptyTetro);
			} else if (tetroTypes.get(i) == mouseHandler.getCurrentlyHoveredTetro()) {
				tetroTypes.get(i).drawCenteredPreview(g, tetroDrawPositions.get(i), getTetroPreviewHoverBlockSize(),
						img);
			} else if (getFocusedTetroType() == i) {
				tetroTypes.get(i).drawCenteredPreview(g, tetroDrawPositions.get(i), getTetroPreviewHoverBlockSize(),
						img);
			} else {
				tetroTypes.get(i).drawCenteredPreview(g, tetroDrawPositions.get(i), getTetroPreviewBlockSize(), img);
			}

			g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, (int) (25 * fontMultiplier)));
			Fonts.drawCenteredString(Integer.toString(world.getTetroAmount()[i]), tetroAmountDrawPositions.get(i), g);

		}
	}

	private void initTetroDrawPositions() {
		tetroDrawPositions = new ArrayList<>();
		tetroDrawPositions.add(new Rectangle((int) (1395. / 1920 * gameFrame.getPanelWidth()),
				(int) (65. / 1080 * gameFrame.getPanelHeight()), (int) (460. / 1920 * gameFrame.getPanelWidth()),
				(int) (134. / 1080 * gameFrame.getPanelHeight())));
		tetroDrawPositions.add(new Rectangle((int) (1395. / 1920 * gameFrame.getPanelWidth()),
				(int) (210. / 1080 * gameFrame.getPanelHeight()), (int) (225. / 1920 * gameFrame.getPanelWidth()),
				(int) (134. / 1080 * gameFrame.getPanelHeight())));
		tetroDrawPositions.add(new Rectangle((int) (1630. / 1920 * gameFrame.getPanelWidth()),
				(int) (210. / 1080 * gameFrame.getPanelHeight()), (int) (225. / 1920 * gameFrame.getPanelWidth()),
				(int) (134. / 1080 * gameFrame.getPanelHeight())));
		tetroDrawPositions.add(new Rectangle((int) (1395. / 1920 * gameFrame.getPanelWidth()),
				(int) (355. / 1080 * gameFrame.getPanelHeight()), (int) (225. / 1920 * gameFrame.getPanelWidth()),
				(int) (134. / 1080 * gameFrame.getPanelHeight())));
		tetroDrawPositions.add(new Rectangle((int) (1630. / 1920 * gameFrame.getPanelWidth()),
				(int) (355. / 1080 * gameFrame.getPanelHeight()), (int) (225. / 1920 * gameFrame.getPanelWidth()),
				(int) (134. / 1080 * gameFrame.getPanelHeight())));
		tetroDrawPositions.add(new Rectangle((int) (1395. / 1920 * gameFrame.getPanelWidth()),
				(int) (500. / 1080 * gameFrame.getPanelHeight()), (int) (225. / 1920 * gameFrame.getPanelWidth()),
				(int) (134. / 1080 * gameFrame.getPanelHeight())));
		tetroDrawPositions.add(new Rectangle((int) (1630. / 1920 * gameFrame.getPanelWidth()),
				(int) (500. / 1080 * gameFrame.getPanelHeight()), (int) (225. / 1920 * gameFrame.getPanelWidth()),
				(int) (134. / 1080 * gameFrame.getPanelHeight())));

	}

	private void initTetroAmountDrawPositions() {
		int x0 = (int) (1555. / 1920 * gameFrame.getPanelWidth());
		int x1 = (int) (1790. / 1920 * gameFrame.getPanelWidth());
		int y0 = (int) (174. / 1080 * gameFrame.getPanelHeight());
		int y1 = (int) (319. / 1080 * gameFrame.getPanelHeight());
		int y2 = (int) (464. / 1080 * gameFrame.getPanelHeight());
		int y3 = (int) (609. / 1080 * gameFrame.getPanelHeight());
		int width = (int) (65. / 1920 * gameFrame.getPanelWidth());
		int height = (int) (25. / 1080 * gameFrame.getPanelHeight());
		tetroAmountDrawPositions = new ArrayList<>();
		tetroAmountDrawPositions.add(new Rectangle(x1, y0, width, height));
		tetroAmountDrawPositions.add(new Rectangle(x0, y1, width, height));
		tetroAmountDrawPositions.add(new Rectangle(x1, y1, width, height));
		tetroAmountDrawPositions.add(new Rectangle(x0, y2, width, height));
		tetroAmountDrawPositions.add(new Rectangle(x1, y2, width, height));
		tetroAmountDrawPositions.add(new Rectangle(x0, y3, width, height));
		tetroAmountDrawPositions.add(new Rectangle(x1, y3, width, height));

	}

	public int getTetroPreviewBlockSize() {
		return 45; // TODo
	}

	private int getTetroPreviewHoverBlockSize() {
		return 50; // TODo
	}

	public Rectangle getBackBounds() {
		return new Rectangle((int) (1395. / 1920 * gameFrame.getPanelWidth()),
				(int) (644. / 1080 * gameFrame.getPanelHeight()), (int) (460. / 1920 * gameFrame.getPanelWidth()),
				(int) (91. / 1080 * gameFrame.getPanelHeight()));
	}

	public Rectangle getMapBounds() {
		return new Rectangle(1285 * gameFrame.getPanelWidth() / 1920, 795 * gameFrame.getPanelHeight() / 1080,
				570 * gameFrame.getPanelWidth() / 1920 + 1, 220 * gameFrame.getPanelHeight() / 1080 + 1);
	}
}
