package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JPanel;

import data.Animation;
import input.MenuMouseHandler;
import logics.Playable;
import tools.Fonts;

public class MenuPanel extends JPanel implements Playable {
	private static final long serialVersionUID = 1L;

	private MenuFrameHandler frame;
	private MenuMouseHandler mouseHandler;
	private BufferedImage menu;
	private int highlighted = -1;
	int loadingx = 20;
	private BufferedImage openingScreen;
	private Animation loadingAnim;
	
	

	public MenuPanel(MenuFrameHandler frame) {

		this.frame = frame;
		mouseHandler = new MenuMouseHandler(this);
		setPreferredSize(new Dimension(GameFrameHandler.PANEL_WIDTH, GameFrameHandler.PANEL_HEIGHT));
		addMouseListener(mouseHandler);
		addMouseMotionListener(mouseHandler);
		setBackground(Color.WHITE);
		menu = frame.getImage("/res/Menu.png");
		
		loadingAnim = frame.getAnimations("/res/anims/loading.txt").get("loading");
		openingScreen = frame.getImage("/res/LoadingScreen.png");
		
		

		repaint();

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (frame.isLoading()) {
			drawLoadingScreen(g);
		} else {
			g.drawImage(menu, 0, 0, null);
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
			Fonts.drawCenteredString("Play", 45, 357, 1210, 184, g);

			if (highlighted == 1) {
				g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, normSizeRest + sizeDifRest));
			} else {
				g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, normSizeRest));
			}
			Fonts.drawCenteredString("Tutorial", 45, 596, 600, 184, g);

			if (highlighted == 2) {
				g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, normSizeRest + sizeDifRest));
			} else {
				g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, normSizeRest));

			}
			Fonts.drawCenteredString("Settings", 655, 596, 600, 184, g);

		}
	}
	
	public void drawLoadingScreen(Graphics g) {
		g.drawImage(openingScreen, 0, 0, null);
		g.drawImage(loadingAnim.getImage(), 450, 250, null);
		loadingAnim.next();
		if(loadingAnim.getImage() == null)
			loadingAnim.next();
	}

	public void mouseReleased(int x, int y) {

		if (!frame.isLoading()) {
			if (playContains(x, y)) {
				File akt_Overworld = new File(System.getenv("APPDATA") + "\\tetro-maze\\saves\\overworldSave.txt");
				if (akt_Overworld.exists()) {
					frame.loadLevel(akt_Overworld.getPath());
				} else {
					frame.loadLevel("/res/levels/overworld" + frame.getLevelSolved() + ".txt");
				}

			} else if (tutorialContains(x, y)) {
				frame.startTutorial();
			} else if (optionsContains(x, y)) {
				frame.startOption();
			}
		}
	}
	
	public void mousePressed(int x, int y) {

		if (!frame.isLoading()) {
			if (playContains(x, y)) {
				frame.playSound("ButtonKlick", -5f);
			} else if (tutorialContains(x, y)) {
				frame.playSound("ButtonKlick", -5f);
			} else if (optionsContains(x, y)) {
				frame.playSound("ButtonKlick", -5f);
			}
		}
	}
	
	

	public void mouseMoved(int x, int y) {
		if (playContains(x, y)) {
			if (highlighted != 0) {
				frame.playSound("menuHover", -6f);
				highlight(0);
			}
		} else if (tutorialContains(x, y)) {
			if (highlighted != 1) {
				frame.playSound("menuHover", -6f);
				highlight(1);
			}
		} else if (optionsContains(x, y)) {
			if (highlighted != 2) {
				frame.playSound("menuHover", -6f);
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
		frame.checkIfLoading();
		if (frame.isLoading()) {
			loadingx = (loadingx + 3290) % 2300 - 1000;
			frame.increaseLoadingTicks();
		}

	}

	@Override
	public void render(float interpolation, int fps, int ups) {
		repaint();
	}

	@Override
	public void secondPassed() {

	}

	private boolean playContains(int x, int y) {
		return x > 35 && y > 332 && x < 1252 && y < 523;
	}

	private boolean tutorialContains(int x, int y) {
		return x > 35 && y > 571 && x < 644 && y < 764;
	}

	private boolean optionsContains(int x, int y) {
		return x > 655 && y > 571 && x < 1254 && y < 764;
	}

}
