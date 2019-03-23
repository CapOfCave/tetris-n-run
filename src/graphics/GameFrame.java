package graphics;

import java.awt.CardLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;

import data.RawPlayer;
import input.KeyHandler;
import loading.LevelLoader;
import loading.RawPlayerLoader;
import loading.RawPlayerSaver;
import loading.SettingSaver;
import loading.SettingsLoader;
import logics.GameLoop;
import sound.SoundPlayer;

/**
 * @author Lars Created on 05.08.2018
 * 
 *         Der Rahmen, der alles h�lt.
 */
public class GameFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private OverworldPanel oPanel;
	private ArrayList<Integer> keyCodes;
	private GameWorldPanel lPanel;
	private RawPlayer rawPlayer;
	private GameLoop gameLoop;
	private char nextLevel;
	private int levelSolved = 0;
	private boolean inOverworld = true;
	private String[] text;
	private SoundPlayer soundPlayer;
	public static final int BLOCKSIZE = 45;

	private KeyHandler keyHandler;

	private final int panel_width;
	private final int panel_height;

	public static void main(String[] args) {

		File savesFile = new File(System.getenv("APPDATA") + "\\tetris-n-run\\saves");

		if (!savesFile.exists()) {
			savesFile.mkdirs();

			// System.ot.println(savesFile);
		}
		
		File keyCodesFile = new File(System.getenv("APPDATA") + "\\tetris-n-run\\levelSaves\\settings.txt");
		
		if (!keyCodesFile.exists()) {
			new MenuFrame();
		}else {
		
		ArrayList<Integer> keyCodes = SettingsLoader.loadKeyCodes(System.getenv("APPDATA") + "\\tetris-n-run\\levelSaves\\settings.txt");

		if (keyCodes != null)
			new MenuFrame(keyCodes);
		else
			new MenuFrame();
		}
	}

	public GameFrame(int width, int height, ArrayList<Integer> keyCodes) {
		this.panel_width = width;
		this.panel_height = height;

		this.keyCodes = keyCodes;
		keyHandler = new KeyHandler(keyCodes);
		text = new String[7];
		text[0] = "";
		text[1] = "";
		text[2] = "";
		text[3] = "";
		text[4] = "";
		text[5] = "";
		text[6] = "";

		soundPlayer = new SoundPlayer();

		rawPlayer = RawPlayerLoader.readRawPlayer();
		rawPlayer.init();

		File file1 = new File(System.getenv("APPDATA") + "\\tetris-n-run\\levelSaves\\overworldSave.txt");
		if (file1.exists()) {
			oPanel = new OverworldPanel(width, height, LevelLoader.loadLevel(file1.getPath(), this, rawPlayer),
					keyHandler, this, rawPlayer);
		} else {
			oPanel = new OverworldPanel(width, height,
					LevelLoader.loadLevel("/res/levels/overworld" + levelSolved + ".txt", this, rawPlayer), keyHandler,
					this, rawPlayer);
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

		if (((int) nextLevel - 96) > levelSolved && !died) {
			levelSolved = ((int) nextLevel - 96);
			overworldFile.delete();
		}

		oPanel = new OverworldPanel(panel_width, panel_height,
				LevelLoader.loadLevel("/res/levels/overworld" + levelSolved + ".txt", this, rawPlayer), keyHandler,
				this, rawPlayer);

		if (overworldFile.exists()) {
			oPanel = new OverworldPanel(panel_width, panel_height,
					LevelLoader.loadLevel(overworldFile.getPath(), this, rawPlayer), keyHandler, this, rawPlayer);
		} else {
			oPanel = new OverworldPanel(panel_width, panel_height,
					LevelLoader.loadLevel("/res/levels/overworld" + levelSolved + ".txt", this, rawPlayer), keyHandler,
					this, rawPlayer);
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
					LevelLoader.loadLevel("/res/levels/level" + nextLevel + ".txt", this, rawPlayer), keyHandler, this,
					rawPlayer);

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
	public void swichLevel(String path) {
		clearText();
		GameWorldPanel tempPanel = lPanel;

		lPanel = new GameWorldPanel(panel_width, panel_height, LevelLoader.loadLevel(path, this, rawPlayer), keyHandler,
				this, rawPlayer);

		add(lPanel);
		remove(tempPanel);
		gameLoop.changePlayable(lPanel);

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
			lPanel = new GameWorldPanel(panel_width, panel_height, LevelLoader.loadLevel(path, this, rawPlayer),
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

	public void backToMenu() {
		oPanel.save();
		new MenuFrame(keyCodes);
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

	public String[] getText() {
		return text;
	}

	public void setText(String[] text) {
		this.text = text;
	}

}
