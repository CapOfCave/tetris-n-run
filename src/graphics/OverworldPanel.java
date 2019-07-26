package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import data.Animation;
import data.Level;
import data.TetroType;
import input.KeyHandler;
import input.OverworldMouseHandler;
import logics.World;
import tools.Fonts;

/**
 * @author Marius Created on 13.09.2018
 */

public class OverworldPanel extends Panel {

	private static final long serialVersionUID = 1L;
	private OverworldMouseHandler overworldMouseHandler;
	private BufferedImage backOverworld;
	private BufferedImage buttonImg;
	private BufferedImage buttonImgPressed;
	private int highlighted = -1;
	private int clicked = -1;
	private BufferedImage loadingScreen;
	private Animation loadingAnim;
	// feld auf dem man steht, sonst nichts
	private char nextLevel;
	private int lastLevelSolved = 0;
	private double fontMultiplier;

	private boolean loadPossible;

	public OverworldPanel(Level level, KeyHandler keyHandler, GameFrameHandler gameFrame,
			ArrayList<TetroType> tetroTypes, int lastLevelSolved) {
		super(level, keyHandler, gameFrame, tetroTypes);
		this.lastLevelSolved = lastLevelSolved;
		backOverworld = gameFrame.getImage("/res/imgs/backOverworld.png");
		buttonImg = gameFrame.getImage("/res/imgs/overworldButton.png");
		buttonImgPressed = gameFrame.getImage("/res/imgs/overworldButtonPressed.png");
		world = new World(getGamePanelBounds(), level, keyHandler, gameFrame);

		overworldMouseHandler = new OverworldMouseHandler(gameFrame, this, world);
		addMouseListener(overworldMouseHandler);
		addMouseMotionListener(overworldMouseHandler);

		loadingAnim = gameFrame.getAnimations("/res/anims/loading.txt").get("loading");
		loadingScreen = gameFrame.getImage("/res/LoadingScreen.png");

		fontMultiplier = (gameFrame.getPanelWidth() / 1920.);

		checkIfLoadPossible();

	}

	public void checkIfLoadPossible() {
		File file = new File(System.getenv("APPDATA") + "\\tetro-maze\\saves\\tmpSaves");
		if (!file.exists()) {
			loadPossible = false;
			return;
		}
		int folder_length = file.listFiles().length;
		String path = null;
		for (File f : file.listFiles()) {
			if (f.getName().startsWith(folder_length + "saveNLoad")) {
				path = f.getAbsolutePath();
			}
		}
		loadPossible = file.exists() && path != null;
	}

	public void startLevel() { // auf dem man steht
		if (nextLevelExists()) {
			save();
			gameFrame.initiateDeleteAllSALSaves();
			gameFrame.loadLevel("/res/levels/level" + nextLevel + ".txt");
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D gameGraphics = (Graphics2D) g.create(getGamePanelBounds().x, getGamePanelBounds().y,
				getGamePanelBounds().width, getGamePanelBounds().height);
		if (gameFrame.isLoading()) {
			drawLoadingScreen(gameGraphics);
		} else {
			drawOverworldScreen(gameGraphics);
		}

		g.drawImage(backOverworld, 0, 0, gameFrame.getPanelWidth(), gameFrame.getPanelHeight(), null);
		world.drawPlayerPreview(g, getPreviewRect());

		drawLevelCaption(g);
		// drawGuiButtons(g);
		drawGuiButtonCaptions(g);

		drawConsole(g);

	}

	private void drawOverworldScreen(Graphics gameGraphics) {

		world.draw(gameGraphics, interpolation, keyHandler.inDebugMode());
		if (keyHandler.inDebugMode()) {
			drawDebug(gameGraphics);
		}
	}

	public void drawLoadingScreen(Graphics g) {
		g.drawImage(loadingScreen, 0, 0, null);
		if (loadingAnim.getImage() != null)
			g.drawImage(loadingAnim.getImage(),
					700 * gameFrame.getPanelWidth() / 1920 - loadingAnim.getImage().getWidth() / 2,
					400 * gameFrame.getPanelHeight() / 1920 - loadingAnim.getImage().getHeight() / 2, null);
		loadingAnim.next();
		if (loadingAnim.getImage() == null)
			loadingAnim.next();
	}

	@Override
	public void secondPassed() {
	}

	public void save() {
		world.initiateSaving(System.getenv("APPDATA") + "\\tetro-maze\\saves\\overworldSave.txt");
	}

	public void drawLevelCaption(Graphics g) {
		if (nextLevelExists()) {
			g.setColor(Color.BLACK);

			int fontSizeSmall = (int) (40 * fontMultiplier);
			int fontSizeBig = (int) (44 * fontMultiplier);

			switch (getNextLevelAsInt()) {
			case 1:
				g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, fontSizeSmall));
				Fonts.drawCenteredString("A new", getTitelLineBounds(-.5, g), g);
				Fonts.drawCenteredString("beginning", getTitelLineBounds(.5, g), g);
				g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, fontSizeBig));
				break;

			case 2:
				g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, fontSizeSmall));
				Fonts.drawCenteredString("Doors and", getTitelLineBounds(-.5, g), g);
				Fonts.drawCenteredString("Switches", getTitelLineBounds(.5, g), g);
				g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, fontSizeBig));
				break;

			case 4:
				g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, fontSizeSmall));
				Fonts.drawCenteredString("Crossroad", getTitelLineBounds(0, g), g);
				g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, fontSizeBig));
				break;

			case 5:
				g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, fontSizeSmall));
				Fonts.drawCenteredString("Pass the", getTitelLineBounds(-.5, g), g);
				Fonts.drawCenteredString("destination!", getTitelLineBounds(.5, g), g);
				g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, fontSizeBig));
				break;

			case 6:
				g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, fontSizeSmall));
				Fonts.drawCenteredString("Keep track!", getTitelLineBounds(0, g), g);
				g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, fontSizeBig));
				break;

			case 7:
				g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, fontSizeSmall));
				Fonts.drawCenteredString("A lack of", getTitelLineBounds(-.5, g), g);
				Fonts.drawCenteredString("Tetros.", getTitelLineBounds(.5, g), g);
				g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, fontSizeBig));
				break;

			default:
				g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, fontSizeBig));
				Fonts.drawCenteredString("Level " + getNextLevelAsInt(), getTitelLineBounds(0, g), g);
				break;
			}

		} else {
			g.setColor(Color.GRAY);
			g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, (int) (34 * fontMultiplier)));
			Fonts.drawCenteredString("Kein Level", getTitelLineBounds(0, g), g);

			// g.drawString("Play", 1085, 360);
		}

	}

	@SuppressWarnings("unused")
	private void drawGuiButtons(Graphics g) {
		if (clicked == 0) {
			g.drawImage(buttonImgPressed, getStartBounds().x, getStartBounds().y, getStartBounds().width,
					getStartBounds().height, null);
		} else {
			g.drawImage(buttonImg, getStartBounds().x, getStartBounds().y, getStartBounds().width,
					getStartBounds().height, null);
		}
		if (clicked == 1) {
			g.drawImage(buttonImgPressed, getLoadBounds().x, getLoadBounds().y, getLoadBounds().width,
					getLoadBounds().height, null);
		} else {
			g.drawImage(buttonImg, getLoadBounds().x, getLoadBounds().y, getLoadBounds().width, getLoadBounds().height,
					null);
		}
		if (clicked == 2) {
			g.drawImage(buttonImgPressed, getMenuBounds().x, getMenuBounds().y, getMenuBounds().width,
					getMenuBounds().height, null);
		} else {
			g.drawImage(buttonImg, getMenuBounds().x, getMenuBounds().y, getMenuBounds().width, getMenuBounds().height,
					null);

		}

	}

	private void drawGuiButtonCaptions(Graphics g) {
		int size = (int) (50 * fontMultiplier);
		int sizeDif = (int) (6 * fontMultiplier);

		// Color stays gray if needed

		if (highlighted == 0 && nextLevelExists()) {
			g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, sizeDif + size));
		} else {
			g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, size));
		}
		Fonts.drawCenteredString("Start", getStartBounds(), g);

		if (loadPossible) {
			g.setColor(Color.BLACK);
		} else {
			g.setColor(Color.GRAY);
		}

		if (highlighted == 1 && loadPossible) {
			g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, sizeDif + size));
		} else {
			g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, size));
		}
		Fonts.drawCenteredString("Load", getLoadBounds(), g);

		g.setColor(Color.BLACK);
		if (highlighted == 2) {
			g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, sizeDif + size));
		} else {
			g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, size));

		}

		Fonts.drawCenteredString("Menu", getMenuBounds(), g);
	}

	public Rectangle getStartBounds() {// TODO bounz
		return new Rectangle(1395 * gameFrame.getPanelWidth() / 1920, 275 * gameFrame.getPanelHeight() / 1080,
				460 * gameFrame.getPanelWidth() / 1920, 146 * gameFrame.getPanelHeight() / 1080);

	}

	public Rectangle getLoadBounds() {// TODO bounz
		return new Rectangle(1395 * gameFrame.getPanelWidth() / 1920, 432 * gameFrame.getPanelHeight() / 1080,
				460 * gameFrame.getPanelWidth() / 1920, 146 * gameFrame.getPanelHeight() / 1080);
	}

	public Rectangle getMenuBounds() { // TODO bounz
		return new Rectangle(1395 * gameFrame.getPanelWidth() / 1920, 589 * gameFrame.getPanelHeight() / 1080,
				460 * gameFrame.getPanelWidth() / 1920, 146 * gameFrame.getPanelHeight() / 1080);
	}

	public Rectangle getTitelLineBounds(double line, Graphics g) { // TODO bounz
		return new Rectangle(1395 * gameFrame.getPanelWidth() / 1920,
				(65 * gameFrame.getPanelHeight() / 1080) + (int) (g.getFontMetrics().getHeight() * 1 * line),
				460 * gameFrame.getPanelWidth() / 1920, 200 * gameFrame.getPanelHeight() / 1080);
	}

	public boolean isHighlighted(int i) {
		return highlighted == i;
	}

	public void highlight(int i) {
		this.highlighted = i;
	}

	public void click(int i) {
		if (i == 0 && !nextLevelExists()) {
			gameFrame.playSound("error", -5f);
			return;
		} else if (i == 1 && !loadPossible) {
			gameFrame.playSound("error", -5f);
			return;
		}

		this.clicked = i;
		if (i == -1) {
			return;
		}
		gameFrame.playSound("ButtonKlick", -5f);
	}

	public boolean isClicked(int i) {
		return clicked == i;
	}

	public int getNextLevelAsInt() {
		return (int) nextLevel - 96;
	}

	public boolean nextLevelExists() {
		return Character.isLowerCase(nextLevel);
	}

	public void setNextLevel(char nextLevel) {
		this.nextLevel = nextLevel;
	}

	public boolean updateLastLevelSolved() {
		if (getNextLevelAsInt() > lastLevelSolved) {
			lastLevelSolved = getNextLevelAsInt();
			return true;

		}
		return false;
	}

	public int getLastLevelSolved() {
		return lastLevelSolved;
	}

}
