package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import data.Level;
import data.TetroType;
import input.GuiMouseHandler;
import input.KeyHandler;
import logics.World;

/**
 * @author Marius Created on 13.09.2018
 */

public class OverworldPanel extends Panel {

	private static final long serialVersionUID = 1L;
	private GuiMouseHandler guiMouseHandler;
	private BufferedImage backOverworld;

	public OverworldPanel(Level level, KeyHandler keyHandler, GameFrame frame, ArrayList<TetroType> tetroTypes) {
		super(level, keyHandler, frame, tetroTypes);
		backOverworld = frame.getImage("/res/imgs/backOverworld.png");
		world = new World(gamePanel, level, keyHandler, frame);

		
		guiMouseHandler = new GuiMouseHandler(frame, world);
		addMouseListener(guiMouseHandler);
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
				g.setFont(new Font("Timesnewroman", 1, 34));
				g.drawString("A new", 1070, 150);
				g.drawString("beginning", 1050, 190);
				g.setFont(new Font("Timesnewroman", 1, 44));
				break;

			case 2:
				g.setFont(new Font("Timesnewroman", 1, 34));
				g.drawString("Doors and", 1048, 150);
				g.drawString("Switches", 1060, 190);
				g.setFont(new Font("Timesnewroman", 1, 44));
				break;

			case 4:
				g.setFont(new Font("Timesnewroman", 1, 34));
				g.drawString("Crossroad", 1045, 150);
				g.setFont(new Font("Timesnewroman", 1, 44));
				break;

			case 5:
				g.setFont(new Font("Timesnewroman", 1, 34));
				g.drawString("Pass the", 1062, 150);
				g.drawString("destination!", 1040, 190);
				g.setFont(new Font("Timesnewroman", 1, 44));
				break;

			case 6:
				g.setFont(new Font("Timesnewroman", 1, 34));
				g.drawString("Keep track!", 1040, 150);
				g.setFont(new Font("Timesnewroman", 1, 44));
				break;

			case 7:
				g.setFont(new Font("Timesnewroman", 1, 34));
				g.drawString("A lack of", 1062, 150);
				g.drawString("Tetros.", 1075, 190);
				g.setFont(new Font("Timesnewroman", 1, 44));
				break;

			default:
				g.setFont(new Font("Timesnewroman", 1, 44));
				g.drawString("Level " + (Character.getNumericValue(frame.getNextLevel()) - 9), 1055, 150);
				break;
			}

		} else {
			g.setColor(Color.GRAY);
			g.setFont(new Font("Timesnewroman", 1, 34));
			g.drawString("Kein Level", 1048, 150);
			g.setFont(new Font("Timesnewroman", 1, 44));
			g.drawString("Play", 1085, 360);
		}
		g.drawString("Play", 1085, 360);
		g.setColor(Color.BLACK);
		g.drawString("Load", 1075, 470);
		g.drawString("Menu", 1070, 580);

	}

}
