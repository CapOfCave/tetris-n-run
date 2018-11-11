package graphics;

import java.awt.CardLayout;
import java.io.File;

import javax.swing.JFrame;

import data.RawPlayer;
import input.KeyHandler;
import loading.LevelLoader;
import loading.RawPlayerLoader;
import loading.RawPlayerSaver;
import logics.GameLoop;

/**
 * @author Lars Created on 05.08.2018
 * 
 *         Der Rahmen, der alles hält.
 */
public class Frame extends JFrame {

	private static final long serialVersionUID = 1L;
	private OverworldPanel oPanel;
	private GameWorldPanel lPanel;
	private RawPlayer rawPlayer;
	private GameLoop gameLoop;
	private char nextLevel;
	private int levelSolved = 3;
	private boolean inOverworld = true;
	private String[] text;
	public static final int BLOCKSIZE = 45;

	private KeyHandler keyHandler;

	public static void main(String[] args) {

		new Frame();
	}

	public Frame() {
		keyHandler = new KeyHandler();
		text = new String[7];
		text[0] = "Satz1";
		text[1] = "Satz2";
		text[2] = "Satz3";
		text[3] = "Satz4";
		text[4] = "Satz5";
		text[5] = "Satz6";
		text[6] = "Satz7";

		rawPlayer = RawPlayerLoader.readRawPlayer("C:\\JavaEclipse\\Player.txt");
		rawPlayer.init();

		oPanel = new OverworldPanel(
				LevelLoader.loadLevel("/res/levels/overworld" + levelSolved + ".txt", this, rawPlayer), keyHandler,
				this, rawPlayer);
		setLayout(new CardLayout());
		add(oPanel);
		gameLoop = new GameLoop(oPanel);
		addKeyListener(keyHandler);
		setResizable(false);
		pack();
		setDefaultCloseOperation(3);
		setLocationRelativeTo(null);
		gameLoop.start();
		setVisible(true);

	}

	public void changeToOverworld(boolean died, RawPlayer rawPlayer) {
		clearText();
		if (!died) {
			RawPlayerSaver.writePlayer("C:\\JavaEclipse\\Player.txt", rawPlayer);
			File file = new File(System.getenv("APPDATA") + "\\tetris-n-run\\levelSaves\\saveNLodeTile.txt");
			file.delete();
		}

		if (((int) nextLevel - 96) > levelSolved && !died)
			levelSolved = ((int) nextLevel - 96);

		oPanel = new OverworldPanel(
				LevelLoader.loadLevel("/res/levels/overworld" + levelSolved + ".txt", this, rawPlayer), keyHandler,
				this, rawPlayer);

		add(oPanel);
		remove(lPanel);
		gameLoop.changePlayable(oPanel);
		inOverworld = true;

	}

	public void startLevel() {
		clearText();
		if (Character.isLowerCase(nextLevel)) {
			File file = new File(System.getenv("APPDATA") + "\\tetris-n-run\\levelSaves\\saveNLodeTile.txt");
			file.delete();

			lPanel = new GameWorldPanel(
					LevelLoader.loadLevel("/res/levels/level" + nextLevel + ".txt", this, rawPlayer), keyHandler, this,
					rawPlayer);

			add(lPanel);
			remove(oPanel);
			gameLoop.changePlayable(lPanel);
		}
	}

	public void swithLevel(String path) {
		clearText();
		GameWorldPanel tempPanel = lPanel;

		lPanel = new GameWorldPanel(LevelLoader.loadLevel(path, this, rawPlayer), keyHandler, this, rawPlayer);

		add(lPanel);
		remove(tempPanel);
		gameLoop.changePlayable(lPanel);

	}

	public void loadLevel() {
		File file = new File(System.getenv("APPDATA") + "\\tetris-n-run\\levelSaves\\saveNLodeTile.txt");
		System.out.println("load");
		if (file.exists()) {
			lPanel = new GameWorldPanel(
					LevelLoader.loadLevel(System.getenv("APPDATA") + "\\tetris-n-run\\levelSaves\\saveNLodeTile.txt",
							this, rawPlayer),
					keyHandler, this, rawPlayer);

			add(lPanel);
			remove(oPanel);
			gameLoop.changePlayable(lPanel);
		} else {
			addLineToText("da kein Zwischenstad existiert.");
			addLineToText("Es kann kein Level geladen werden, ");

		}
	}

	public void addLineToText(String line) {
		for (int i = text.length - 1; i > 0; i--) {
			text[i] = text[i - 1];
		}
		text[0] = line;
	}

	public void clearText() {
		for (int i = 0; i < text.length; i++) {
			text[i] = "";
		}

	}

	public char getNextLevel() {
		return nextLevel;
	}

	public void setNextLevel(char nextLevel) {
		this.nextLevel = nextLevel;
	}

	public String[] getText() {
		return text;
	}

	public void setText(String[] text) {
		this.text = text;
	}

}
