package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JPanel;

import data.Animation;
import input.MenuMouseHandler;
import logics.Playable;
import tools.Fonts;

public class MenuPanel extends JPanel implements Playable {
	private static final long serialVersionUID = 1L;

	private MenuFrameHandler menuFrame;
	private MenuMouseHandler mouseHandler;
	private BufferedImage menu;
	private BufferedImage menuTitel;
	private int highlighted = -1;
	int loadingx = 20;
	private BufferedImage openingScreen;
	private Animation loadingAnim;
	private final int WQHDWIDTH = 2560;
	private double fontMultiplier;

	public MenuPanel(MenuFrameHandler frame) {
		this.menuFrame = frame;
		mouseHandler = new MenuMouseHandler(this);
		setPreferredSize(new Dimension(menuFrame.getScreenSize()));
		addMouseListener(mouseHandler);
		addMouseMotionListener(mouseHandler);
		setBackground(Color.BLACK);
		menu = frame.getImage("/res/menu16x9.png");
		if (frame.getPanelWidth() == WQHDWIDTH)
			menuTitel = frame.getImage("/res/titel2560.png");
		else
			menuTitel = frame.getImage("/res/titel1920.png");
		loadingAnim = frame.getAnimations("/res/anims/loading.txt").get("loading");
		openingScreen = frame.getImage("/res/loadingScreen.png");

		fontMultiplier = (frame.getPanelWidth() / 1920.);

		repaint();

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.translate(menuFrame.getPanelOffsetX(), menuFrame.getPanelOffsetY());
		if (menuFrame.isLoading()) {
			drawLoadingScreen(g);
		} else {
			g.drawImage(menu, 0, 0, menuFrame.getPanelWidth(), menuFrame.getPanelHeight(), null);
			g.drawImage(menuTitel, 245 * menuFrame.getPanelWidth() / 1920, 81 * menuFrame.getPanelHeight() / 1080,
					null);
			g.setColor(Color.BLACK);
			int sizeDifPlay = (int) (16 * fontMultiplier);
			int normSizePlay = (int) (130 * fontMultiplier);
			int sizeDifRest = (int) (10 * fontMultiplier);
			int normSizeRest = (int) (100 * fontMultiplier);

			if (highlighted == 0) {
				g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, sizeDifPlay + normSizePlay));
			} else {
				g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, normSizePlay));
			}
			Fonts.drawCenteredString("Play", getPlayBounds(), g);

			if (highlighted == 1) {
				g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, normSizeRest + sizeDifRest));
			} else {
				g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, normSizeRest));
			}
			Fonts.drawCenteredString("Instructions", getTutorialBounds(), g);

			if (highlighted == 2) {
				g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, normSizeRest + sizeDifRest));
			} else {
				g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, normSizeRest));

			}
			Fonts.drawCenteredString("Credits", getCreditsBounds(), g);

			if (highlighted == 3) {
				g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, normSizeRest + sizeDifRest));
			} else {
				g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, normSizeRest));

			}
			Fonts.drawCenteredString("Settings", getSettingsBounds(), g);

			if (highlighted == 4) {
				g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, normSizeRest + sizeDifRest));
			} else {
				g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, normSizeRest));

			}
			Fonts.drawCenteredString("Exit", getExitBounds(), g);

		}
	}

	private Rectangle getPlayBounds() { // TODO bounz
		return new Rectangle(235 * menuFrame.getPanelWidth() / 1920, 357 * menuFrame.getPanelHeight() / 900,
				720 * menuFrame.getPanelWidth() / 1920, 184 * menuFrame.getPanelHeight() / 900);
	}

	private Rectangle getTutorialBounds() { // TODO bounz
		return new Rectangle(966 * menuFrame.getPanelWidth() / 1920, 429 * menuFrame.getPanelHeight() / 1080,
				720 * menuFrame.getPanelWidth() / 1920, 220 * menuFrame.getPanelHeight() / 1080);
	}

	private Rectangle getSettingsBounds() { // TODO bounz
		return new Rectangle(723 * menuFrame.getPanelWidth() / 1920, 715 * menuFrame.getPanelHeight() / 1080,
				473 * menuFrame.getPanelWidth() / 1920, 221 * menuFrame.getPanelHeight() / 1080);
	}

	private Rectangle getExitBounds() { // TODO bounz
		return new Rectangle(1207 * menuFrame.getPanelWidth() / 1920, 715 * menuFrame.getPanelHeight() / 1080,
				479 * menuFrame.getPanelWidth() / 1920, 221 * menuFrame.getPanelHeight() / 1080);
	}

	private Rectangle getCreditsBounds() { // TODO bounz
		return new Rectangle(234 * menuFrame.getPanelWidth() / 1920, 715 * menuFrame.getPanelHeight() / 1080,
				478 * menuFrame.getPanelWidth() / 1920, 221 * menuFrame.getPanelHeight() / 1080);
	}

	public void drawLoadingScreen(Graphics g) {
		g.drawImage(openingScreen, 0, 0, menuFrame.getPanelWidth(), menuFrame.getPanelHeight(), null);
		g.drawImage(loadingAnim.getImage(), menuFrame.getPanelWidth() / 2 - loadingAnim.getImage().getWidth() / 2,
				menuFrame.getPanelHeight() / 2 - loadingAnim.getImage().getHeight() / 2, null); // TODO bounz
		loadingAnim.next();
		if (loadingAnim.getImage() == null)
			loadingAnim.next();
	}

	public void mouseReleased(int x, int y) {

		if (!menuFrame.isLoading()) {
			if (getPlayBounds().contains(x, y)) {
				File akt_Overworld = new File(System.getenv("APPDATA") + "\\tetro-maze\\saves\\overworldSave.txt");
				if (akt_Overworld.exists()) {
					menuFrame.loadLevel(akt_Overworld.getPath());
				} else {
					menuFrame.loadLevel("/res/levels/overworld" + menuFrame.getLevelSolved() + ".txt");
				}

			} else if (getTutorialBounds().contains(x, y)) {
				menuFrame.startTutorial();
			} else if (getSettingsBounds().contains(x, y)) {
				menuFrame.startOption();
			} else if (getExitBounds().contains(x, y)) {
				menuFrame.closeGame();
			} else if (getCreditsBounds().contains(x, y)) {
				menuFrame.startCredits();
			}
		}
	}

	public void mousePressed(int x, int y) {

		if (!menuFrame.isLoading()) {
			if (getPlayBounds().contains(x, y)) {
				menuFrame.playSound("ButtonKlick", -5f);
			} else if (getTutorialBounds().contains(x, y)) {
				menuFrame.playSound("ButtonKlick", -5f);
			} else if (getSettingsBounds().contains(x, y)) {
				menuFrame.playSound("ButtonKlick", -5f);
			} else if (getExitBounds().contains(x, y)) {
				menuFrame.playSound("ButtonKlick", -5f);
			} else if (getCreditsBounds().contains(x, y)) {
				menuFrame.playSound("ButtonKlick", -5f);
			}
		}
	}

	public void mouseMoved(int x, int y) {
		if (getPlayBounds().contains(x, y)) {
			if (highlighted != 0) {
				menuFrame.playSound("menuHover", -6f);
				highlight(0);
			}
		} else if (getTutorialBounds().contains(x, y)) {
			if (highlighted != 1) {
				menuFrame.playSound("menuHover", -6f);
				highlight(1);
			}
		} else if (getCreditsBounds().contains(x, y)) {
			if (highlighted != 2) {
				menuFrame.playSound("menuHover", -6f);
				highlight(2);
			}
		} else if (getSettingsBounds().contains(x, y)) {
			if (highlighted != 3) {
				menuFrame.playSound("menuHover", -6f);
				highlight(3);
			}
		} else if (getExitBounds().contains(x, y)) {
			if (highlighted != 4) {
				menuFrame.playSound("menuHover", -6f);
				highlight(4);
			}
		}

		else {
			highlight(-1);
		}
	}

	private void highlight(int i) {
		this.highlighted = i;

	}

	@Override
	public void tick() {
		menuFrame.checkIfLoading();
		if (menuFrame.isLoading()) {
			loadingx = (loadingx + 3290) % 2300 - 1000;
			menuFrame.increaseLoadingTicks();
		}

	}

	@Override
	public void render(float interpolation, int fps, int ups) {
		repaint();
	}

	@Override
	public void secondPassed() {

	}

	public int getPanelOffsetX() {
		return menuFrame.getPanelOffsetX();
	}

	public int getPanelOffsetY() {
		return menuFrame.getPanelOffsetY();
	}

}
