package graphics;

import java.awt.CardLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;

import data.ConsoleLine;
import data.RawPlayer;
import data.Tiles.SaveNLoadTile;
import input.KeyHandler;
import loading.LevelLoader;
import loading.RawPlayerLoader;
import loading.RawPlayerSaver;
import logics.GameLoop;
import sound.SoundPlayer;

/**
 * @author Lars Created on 05.08.2018
 * 
 *         Der Rahmen, der alles hält.
 */
public class GameFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private MenuFrame menuFrame;
	private OverworldPanel oPanel;
	private GameWorldPanel lPanel;
	private RawPlayer rawPlayer;
	private GameLoop gameLoop;
	private char nextLevel;
	private int levelSolvedAtThisDifficulty = 0;
	private boolean inOverworld = true;
	private ConsoleLine[] text;
	private SoundPlayer soundPlayer;
	public static final int BLOCKSIZE = 45;
	public static final int TEXTOFFSET = 21;

	private KeyHandler keyHandler;

	private final int panel_width;
	private final int panel_height;
	private int difficulty;

	public GameFrame(int width, int height, ArrayList<Integer> keyCodes, int levelSolved, MenuFrame menuFrame,
			int difficulty) {
		this.panel_width = width;
		this.panel_height = height;
		this.menuFrame = menuFrame;
		this.difficulty = difficulty;
		keyHandler = new KeyHandler(keyCodes);
		text = new ConsoleLine[7];
		text[0] = null;
		text[1] = null;
		text[2] = null;
		text[3] = null;
		text[4] = null;
		text[5] = null;
		text[6] = null;

		soundPlayer = new SoundPlayer();
		this.levelSolvedAtThisDifficulty = levelSolved;

		rawPlayer = RawPlayerLoader.readRawPlayer();
		rawPlayer.init();

		File akt_Overworld = new File(System.getenv("APPDATA") + "\\tetris-n-run\\levelSaves\\overworldSave.txt");
		if (akt_Overworld.exists()) {
			oPanel = new OverworldPanel(width, height,
					LevelLoader.loadLevel(akt_Overworld.getPath(), this, rawPlayer, difficulty), keyHandler, this,
					rawPlayer);
		} else {
			oPanel = new OverworldPanel(width, height,
					LevelLoader.loadLevel("/res/levels/overworld" + levelSolved + ".txt", this, rawPlayer, difficulty),
					keyHandler, this, rawPlayer);
		}
		setLayout(new CardLayout());
		add(oPanel);
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/res/gegner.png")));
		gameLoop = new GameLoop(oPanel);
		addKeyListener(keyHandler);
		setResizable(false);
		setDefaultCloseOperation(0);
		pack();
		setDefaultCloseOperation(3);
		setLocationRelativeTo(null);
		gameLoop.start();
		setVisible(true);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (inOverworld)
					oPanel.save();

			}
		});

	}

	public void changeToOverworld(boolean died, RawPlayer rawPlayer) {

		clearText();
		File overworldFile = new File(System.getenv("APPDATA") + "\\tetris-n-run\\levelSaves\\overworldSave.txt");
		if (!died) {

			RawPlayerSaver.writePlayer(System.getenv("APPDATA") + "\\tetris-n-run\\saves\\player.txt", rawPlayer);

			deleteAll();
		}

		if (((int) nextLevel - 96) > levelSolvedAtThisDifficulty && !died) {
			levelSolvedAtThisDifficulty = ((int) nextLevel - 96);
			overworldFile.delete();
		}

		oPanel = new OverworldPanel(panel_width, panel_height, LevelLoader
				.loadLevel("/res/levels/overworld" + levelSolvedAtThisDifficulty + ".txt", this, rawPlayer, difficulty),
				keyHandler, this, rawPlayer);

		if (overworldFile.exists()) {
			oPanel = new OverworldPanel(panel_width, panel_height,
					LevelLoader.loadLevel(overworldFile.getPath(), this, rawPlayer, difficulty), keyHandler, this,
					rawPlayer);
		} else {
			oPanel = new OverworldPanel(panel_width, panel_height,
					LevelLoader.loadLevel("/res/levels/overworld" + levelSolvedAtThisDifficulty + ".txt", this,
							rawPlayer, difficulty),
					keyHandler, this, rawPlayer);
		}

		add(oPanel);
		remove(lPanel);
		gameLoop.changePlayable(oPanel);
		inOverworld = true;

	}

	public void startLevel() {
		clearText();
		if (Character.isLowerCase(nextLevel)) {
			oPanel.save();
			deleteAll();
			lPanel = new GameWorldPanel(panel_width, panel_height,
					LevelLoader.loadLevel("/res/levels/level" + nextLevel + ".txt", this, rawPlayer, difficulty),
					keyHandler, this, rawPlayer);

			add(lPanel);
			remove(oPanel);
			gameLoop.changePlayable(lPanel);
		}
	}

	private void deleteAll() {
		File tmpSaveFolder = new File(System.getenv("APPDATA") + "\\tetris-n-run\\levelSaves\\tmpSaves");
		if (tmpSaveFolder.exists())
			for (File f : tmpSaveFolder.listFiles()) {
				f.delete();
			}
	}

	// Level to level
	public void swichLevel(String path, SaveNLoadTile tile) {
//		clearText();
		GameWorldPanel tempPanel = lPanel;

		lPanel = new GameWorldPanel(panel_width, panel_height, LevelLoader.loadLevel(path, this, rawPlayer, difficulty),
				keyHandler, this, rawPlayer);

		add(lPanel);
		remove(tempPanel);
		gameLoop.changePlayable(lPanel);
		lPanel.setLastUsedSALTile(tile);
		lPanel.updateTetros();
		

	}

	public void loadLevel() {
		File file = new File(System.getenv("APPDATA") + "\\tetris-n-run\\levelSaves\\tmpSaves");
		int folder_length = file.listFiles().length;
		String path = null;
		for (File f : file.listFiles()) {
			if (f.getName().startsWith(folder_length + "saveNLoad")) {
				path = f.getAbsolutePath();
			}
		}
		if (path == null) {
			System.err.println("Fehler. Pfad nicht gefunden.");
			return;
		}
		if (file.exists()) {
			lPanel = new GameWorldPanel(panel_width, panel_height,
					LevelLoader.loadLevel(path, this, rawPlayer, difficulty), keyHandler, this, rawPlayer);

			add(lPanel);
			remove(oPanel);
			gameLoop.changePlayable(lPanel);
		} else {
			addLineToText("Es kann kein Level geladen werden, da kein Zwischenstad existiert.");

		}
	}

	public void addLineToText(String line) {
		addLineToText(line, 1);
	}

	public void addLineToText(String line, int factor) {
		for (int i = text.length - 1; i > 0; i--) {
			text[i] = text[i - 1];
			if (text[i] != null)
				text[i].addOffset(TEXTOFFSET);
		}
		text[0] = new ConsoleLine(line, factor);
	}

	public void clearText() {
		for (int i = 0; i < text.length; i++) {
			text[i] = null;
		}

	}

	public void backToMenu() {
		oPanel.save();
		menuFrame.setVisible(true);
		this.dispose();

	}

	public void playSound(String sound, float volume) {
		soundPlayer.playSound(sound, volume);
	}

	public char getNextLevel() {
		return nextLevel;
	}

	public void setNextLevel(char nextLevel) {
		this.nextLevel = nextLevel;
	}

	public ConsoleLine[] getText() {
		return text;
	}

	public void setLevelSolved(int levelSolved) {

	}

	public void updateConsole() {
		for (ConsoleLine cl : text) {
			if (cl != null)
				cl.tick();
		}

	}

}
