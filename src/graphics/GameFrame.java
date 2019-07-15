package graphics;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;

import data.Animation;
import data.ConsoleLine;
import data.Level;
import data.TetroType;
import data.Tiles.SaveNLoadTile;
import input.KeyHandler;
import loading.ImageLoader;
import loading.SavingLoadingHandler;
import loading.TetroLoader;
import logics.GameLoop;
import sound.SoundPlayer;

/**
 * @author Lars Created on 05.08.2018
 * 
 *         Der Rahmen, der alles hält.
 */
public class GameFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	public static final int PANEL_WIDTH = 1300, PANEL_HEIGHT = 900;
	public static final int BLOCKSIZE = 45;
	public static final int CONSOLETEXTMARGINY = 21;
	public static final String fontString = "Times New Roman";

	private MenuFrame menuFrame;
	private OverworldPanel oPanel;
	private GameWorldPanel lPanel;
	private GameLoop gameLoop;
	private char nextLevel; // feld auf dem man steht, sonst nichts
	private int lastLevelSolved = 0;
	private boolean inOverworld = true;
	private ConsoleLine[] text;
	private SoundPlayer soundPlayer;

	private KeyHandler keyHandler;
	private SavingLoadingHandler savingLoadingHandler;
	private ImageLoader imageLoader;

	private ArrayList<TetroType> tetroTypes;

	private String loadingLevelUrl = null;
	private boolean loadPossible;

	public GameFrame(ArrayList<Integer> keyCodes, int levelSolved, MenuFrame menuFrame, Level level) {
		this.menuFrame = menuFrame;
		imageLoader = menuFrame.getImageLoader();
		savingLoadingHandler = menuFrame.getSavingLoadingHandler();
		tetroTypes = new TetroLoader(imageLoader).loadTetros("/res/tetros.txt");

		keyHandler = new KeyHandler(keyCodes);
		oPanel = new OverworldPanel(level, keyHandler, this, tetroTypes);

		initConsole();

		soundPlayer = new SoundPlayer();
		this.lastLevelSolved = levelSolved;
		setLayout(new CardLayout());
		add(oPanel);
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/res/gegner.png")));
		gameLoop = new GameLoop(oPanel);
		addKeyListener(keyHandler);
		setResizable(false);
		setDefaultCloseOperation(0);
		setBackground(new Color(34, 34, 34));
		pack();
		setDefaultCloseOperation(3);
		setLocationRelativeTo(menuFrame);
		gameLoop.start();
		setVisible(true);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (inOverworld) {
					oPanel.save();
				}
			}
		});

	}
	
	private void initConsole() {
		text = new ConsoleLine[7];
		text[0] = null;
		text[1] = null;
		text[2] = null;
		text[3] = null;
		text[4] = null;
		text[5] = null;
		text[6] = null;

	}

	public void changeToOverworld(boolean died) {
		File overworldFile = new File(System.getenv("APPDATA") + "\\tetris-n-run\\saves\\overworldSave.txt");
		if (!died) {// Finished the level
			initiateDeleteAllSALSaves();
			if (((int) nextLevel - 96) > lastLevelSolved) {
				lastLevelSolved = ((int) nextLevel - 96);
				overworldFile.delete();
			}
		}

		if (!overworldFile.exists()) {
			loadLevel("/res/levels/overworld" + lastLevelSolved + ".txt");
		}
		checkIfLoadPossible();
		show(oPanel, true);
		inOverworld = true;

	}
	
	public void checkIfLoadPossible() {
		File file = new File(System.getenv("APPDATA") + "\\tetris-n-run\\saves\\tmpSaves");
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
		loadPossible =  file.exists() && path != null;
	}

	public void startLevel() { // auf dem man steht
		if (Character.isLowerCase(nextLevel)) {
			oPanel.save();
			initiateDeleteAllSALSaves();
			loadLevel("/res/levels/level" + nextLevel + ".txt");
		}
	}

	public void loadLevel(String url) {
		// clearText(); //TODO nach geschmack einfügen
		loadingLevelUrl = url; // Aufpassen auf 2 Sachen laden!
		savingLoadingHandler.loadLevel(url);
	}

	public void checkIfLoadingHasFinished() {
		if (loadingLevelUrl != null) {
			if (savingLoadingHandler.isLoaded(loadingLevelUrl)) {

				Level loadedLevel = savingLoadingHandler.getLoadedLevel(loadingLevelUrl);
				if (isOverworld(loadingLevelUrl)) {
					show(new OverworldPanel(loadedLevel, keyHandler, this, tetroTypes), true);
				} else {
					show(new GameWorldPanel(loadedLevel, keyHandler, this, tetroTypes), false);
				}
				loadingLevelUrl = null;

			}
		}
	}

	private boolean isOverworld(String loadingLevelUrl) {
		return loadingLevelUrl.contains("overworld");
	}

	private void initiateDeleteAllSALSaves() {
		savingLoadingHandler.deleteAllSaveNLoads();
		savingLoadingHandler.abortLoadingSAL();
	}

	// Level to level
	public void switchLevel(String path, SaveNLoadTile tile) {
		loadLevel(path); // TODO was soll das Tile ? Siehe commit vom ca. 26.06.2019
	}

	public void loadLegacyLevel() { // From saveNLoad
		File file = new File(System.getenv("APPDATA") + "\\tetris-n-run\\saves\\tmpSaves");
		int folder_length = file.listFiles().length;
		String path = null;
		for (File f : file.listFiles()) {
			if (f.getName().startsWith(folder_length + "saveNLoad")) {
				path = f.getAbsolutePath();
			}
		}
		if (file.exists() && path != null) {
			loadLevel(path);

		} else {
			addLineToText("Es kann kein Level geladen werden, da kein Zwischenstad existiert.");

		}
	}

	public void addLineToText(String line) {
		addLineToText(line, 1);
	}

	public void addLineToText(String line, float factor) {
		for (int i = text.length - 1; i > 0; i--) {
			text[i] = text[i - 1];
			if (text[i] != null)
				text[i].addOffset(CONSOLETEXTMARGINY);
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
		menuFrame.setLocationRelativeTo(this);
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

	public GameWorldPanel getLPanel() {
		return lPanel;
	}

	public void save(Level level, String url) {
		savingLoadingHandler.saveLevel(level, url);
	}

	private void show(Panel panel, boolean isOverworld) {
		add(panel);
		if (panel != oPanel && oPanel != null) {
			remove(oPanel);
		}
		if (panel != lPanel && lPanel != null) {
			remove(lPanel);
		}
		if (isOverworld) {
			oPanel = (OverworldPanel) panel;
		} else {
			lPanel = (GameWorldPanel) panel;
		}

		gameLoop.changePlayable(panel);
	}

	public boolean isLoading() {
		return loadingLevelUrl != null;
	}

	public BufferedImage getImage(String url) {
		return imageLoader.getImage(url);
	}

	public ArrayList<TetroType> getTetroTypes() {
		return tetroTypes;
	}

	public HashMap<String, Animation> getAnimations(String url) {
		return menuFrame.getAnimations(url);

	}

	public boolean isLoadPossible() {
		return loadPossible;
	}
	
}