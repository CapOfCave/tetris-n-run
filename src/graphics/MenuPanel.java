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
	private int highlighted = -1;
	int loadingx = 20;
	private BufferedImage openingScreen;
	private Animation loadingAnim;

	public MenuPanel(MenuFrameHandler frame) {
		this.menuFrame = frame;
		mouseHandler = new MenuMouseHandler(this);
		setPreferredSize(new Dimension(menuFrame.getScreenSize()));
		addMouseListener(mouseHandler);
		addMouseMotionListener(mouseHandler);
		setBackground(Color.BLACK);
		menu = frame.getImage("/res/menu.png");

		loadingAnim = frame.getAnimations("/res/anims/loading.txt").get("loading");
		openingScreen = frame.getImage("/res/loadingScreen.png");

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
			g.setColor(Color.BLACK);
			int sizeDifPlay = 16;
			int normSizePlay = 130;
			int sizeDifRest = 10;
			int normSizeRest = 100;

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
			Fonts.drawCenteredString("Tutorial", getTutorialBounds(), g);

			if (highlighted == 2) {
				g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, normSizeRest + sizeDifRest));
			} else {
				g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, normSizeRest));

			}
			Fonts.drawCenteredString("Settings", getSettingsBounds(), g);

		}
	}

	private Rectangle getPlayBounds() { // TODO bounz
		return new Rectangle(45 * menuFrame.getPanelWidth() / 1300, 357 * menuFrame.getPanelHeight() / 900,
				1210 * menuFrame.getPanelWidth() / 1300, 184 * menuFrame.getPanelHeight() / 900);
	}

	private Rectangle getTutorialBounds() { // TODO bounz
		return new Rectangle(45 * menuFrame.getPanelWidth() / 1300, 596 * menuFrame.getPanelHeight() / 900,
				600 * menuFrame.getPanelWidth() / 1300, 184 * menuFrame.getPanelHeight() / 900);
	}

	private Rectangle getSettingsBounds() { // TODO bounz
		return new Rectangle(655 * menuFrame.getPanelWidth() / 1300, 596 * menuFrame.getPanelHeight() / 900,
				600 * menuFrame.getPanelWidth() / 1300, 184 * menuFrame.getPanelHeight() / 900);
	}

	public void drawLoadingScreen(Graphics g) {
		g.drawImage(openingScreen, 0, 0, null);
		g.drawImage(loadingAnim.getImage(), 450, 250, null); // TODO bounz
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
		} else if (getSettingsBounds().contains(x, y)) {
			if (highlighted != 2) {
				menuFrame.playSound("menuHover", -6f);
				highlight(2);
			}
		} else {
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
