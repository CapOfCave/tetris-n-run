package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import data.Level;
import data.TetroType;
import input.OverworldMouseHandler;
import input.KeyHandler;
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

	public OverworldPanel(Level level, KeyHandler keyHandler, GameFrame frame, ArrayList<TetroType> tetroTypes) {
		super(level, keyHandler, frame, tetroTypes);
		backOverworld = frame.getImage("/res/imgs/backOverworld.png");
		buttonImg = frame.getImage("/res/imgs/overworldButton.png");
		buttonImgPressed = frame.getImage("/res/imgs/overworldButtonPressed.png");
		world = new World(gamePanel, level, keyHandler, frame);

		overworldMouseHandler = new OverworldMouseHandler(frame, this, world);
		addMouseListener(overworldMouseHandler);
		addMouseMotionListener(overworldMouseHandler);
		frame.checkIfLoadPossible();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D gameGraphics = (Graphics2D) g.create(gamePanel.x, gamePanel.y, gamePanel.width, gamePanel.height);
		if (frame.isLoading()) {
			drawLoadingScreen(gameGraphics);
		} else {
			drawOverworldScreen(gameGraphics);
		}

		Graphics2D previewGraphics = (Graphics2D) g.create(54, 680, 1000, 1000);
		world.drawPlayerPreview(previewGraphics);

		drawLevelCaption(g);
		drawGuiButtons(g);
		drawGuiButtonCaptions(g);

		g.drawImage(backOverworld, 0, 0, 1300, 900, null);

		drawConsole(g);

	}

	private void drawOverworldScreen(Graphics gameGraphics) {

		world.draw(gameGraphics, interpolation, debugMode);
		if (debugMode) {
			drawDebug(gameGraphics);
		}
	}

	private void drawLoadingScreen(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, gamePanel.width, gamePanel.height);
		g.setColor(Color.WHITE);
		g.drawRect((int) ((0.5 - loadingScreenProgression / 120.) * gamePanel.width),
				(int) ((0.5 - loadingScreenProgression / 120.) * gamePanel.height),
				(int) ((loadingScreenProgression / 60.) * gamePanel.width),
				(int) ((loadingScreenProgression / 60.) * gamePanel.height));

	}

	@Override
	public void secondPassed() {
	}

	public void save() {
		world.initiateSaving(System.getenv("APPDATA") + "\\tetris-n-run\\saves\\overworldSave.txt");
	}

	public void drawLevelCaption(Graphics g) {
		if (Character.isLowerCase(frame.getNextLevel())) {
			g.setColor(Color.BLACK);

			switch ((Character.getNumericValue(frame.getNextLevel()) - 9)) {
			case 1:
				g.setFont(new Font(GameFrame.fontString, 1, 34));
				g.drawString("A new", 1070, 150);
				g.drawString("beginning", 1050, 190);
				g.setFont(new Font(GameFrame.fontString, 1, 44));
				break;

			case 2:
				g.setFont(new Font(GameFrame.fontString, 1, 34));
				g.drawString("Doors and", 1048, 150);
				g.drawString("Switches", 1060, 190);
				g.setFont(new Font(GameFrame.fontString, 1, 44));
				break;

			case 4:
				g.setFont(new Font(GameFrame.fontString, 1, 34));
				g.drawString("Crossroad", 1045, 150);
				g.setFont(new Font(GameFrame.fontString, 1, 44));
				break;

			case 5:
				g.setFont(new Font(GameFrame.fontString, 1, 34));
				g.drawString("Pass the", 1062, 150);
				g.drawString("destination!", 1040, 190);
				g.setFont(new Font(GameFrame.fontString, 1, 44));
				break;

			case 6:
				g.setFont(new Font(GameFrame.fontString, 1, 34));
				g.drawString("Keep track!", 1040, 150);
				g.setFont(new Font(GameFrame.fontString, 1, 44));
				break;

			case 7:
				g.setFont(new Font(GameFrame.fontString, 1, 34));
				g.drawString("A lack of", 1062, 150);
				g.drawString("Tetros.", 1075, 190);
				g.setFont(new Font(GameFrame.fontString, 1, 44));
				break;

			default:
				g.setFont(new Font(GameFrame.fontString, 1, 44));
				g.drawString("Level " + (Character.getNumericValue(frame.getNextLevel()) - 9), 1055, 150);
				break;
			}

		} else {
			g.setColor(Color.GRAY);
			g.setFont(new Font(GameFrame.fontString, 1, 34));
			g.drawString("Kein Level", 1048, 150);

			// g.drawString("Play", 1085, 360);
		}

	}

	private void drawGuiButtons(Graphics g) {
		if (clicked == 0) {
			g.drawImage(buttonImgPressed, 1008, 296, 248, 100, null);
		} else {
			g.drawImage(buttonImg, 1008, 296, 248, 100, null);
		}
		if (clicked == 1) {
			g.drawImage(buttonImgPressed, 1008, 406, 248, 100, null);
		} else {
			g.drawImage(buttonImg, 1008, 406, 248, 100, null);
		}
		if (clicked == 2) {
			g.drawImage(buttonImgPressed, 1008, 516, 248, 100, null);
		} else {
			g.drawImage(buttonImg, 1008, 516, 248, 100, null);

		}

	}

	private void drawGuiButtonCaptions(Graphics g) {
		int size = 44;
		int sizeDif = 6;

		// Color stays gray if needed

		if (highlighted == 0 && Character.isLowerCase(frame.getNextLevel())) {
			g.setFont(new Font(GameFrame.fontString, 1, sizeDif + size));
		} else {
			g.setFont(new Font(GameFrame.fontString, 1, size));
		}
		Fonts.drawCenteredString("Start", 1008, 296, 248, 100, g);
		
		if (frame.isLoadPossible()) {
			g.setColor(Color.BLACK);
		} else {
			g.setColor(Color.GRAY);
		}
		
		if (highlighted == 1 && frame.isLoadPossible()) {
			g.setFont(new Font(GameFrame.fontString, 1, sizeDif + size));
		} else {
			g.setFont(new Font(GameFrame.fontString, 1, size));
		}
		Fonts.drawCenteredString("Load", 1008, 406, 248, 100, g);

		g.setColor(Color.BLACK);
		if (highlighted == 2) {
			g.setFont(new Font(GameFrame.fontString, 1, sizeDif + size));
		} else {
			g.setFont(new Font(GameFrame.fontString, 1, size));

		}

		Fonts.drawCenteredString("Menu", 1008, 516, 248, 100, g);
	}

	public boolean isHighlighted(int i) {
		return highlighted == i;
	}

	public void highlight(int i) {
		this.highlighted = i;
	}

	public void click(int i) {
		if (i == 0 && !Character.isLowerCase(frame.getNextLevel())) {
			frame.playSound("error", -5f);
			return;
		} else if (i == 1 && !frame.isLoadPossible()) {
			frame.playSound("error", -5f);
			return;
		}
		
		this.clicked = i;
		if (i == -1) {
			return;
		}
		frame.playSound("ButtonKlick", -5f);
	}

	public boolean isClicked(int i) {
		return clicked == i;
	}


}
