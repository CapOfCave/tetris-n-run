package graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import data.Animation;
import data.ConsoleLine;
import data.Level;
import data.TetroType;
import data.Tiles.SaveNLoadTile;
import input.KeyHandler;

/**
 * @author Lars Created on 05.08.2018
 * 
 *         Der Rahmen, der alles hält.
 */
public class GameFrameHandler {

	public static final int PANEL_WIDTH = 1300, PANEL_HEIGHT = 900; // 1300 x 900
	public static final int BLOCKSIZE = 45;
	public static final int CONSOLETEXTMARGINY = 21;
	public static final String FONTSTRING = "Times New Roman";

	private Frame frame;

	private OverworldPanel oPanel;
	private GameWorldPanel lPanel;

	
	private boolean inOverworld = true;
	private ConsoleLine[] text;

	private KeyHandler keyHandler;

	private ArrayList<TetroType> tetroTypes;

	private String loadingLevelUrl = null;

	public GameFrameHandler(Frame frame, ArrayList<Integer> keyCodes, int levelSolved,
			Level level) {
		this.frame = frame;
		tetroTypes = frame.getTetros("/res/tetros.txt");

		keyHandler = new KeyHandler(keyCodes);

		oPanel = new OverworldPanel(level, keyHandler, this, tetroTypes, levelSolved);
		level.init(this);

		initConsole();

		frame.add(oPanel);
		frame.addKeyListener(keyHandler);

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
			if (oPanel.updateLastLevelSolved()) {
				overworldFile.delete();
			}
			
		}

		if (!overworldFile.exists()) {
			loadLevel("/res/levels/overworld" + oPanel.getLastLevelSolved() + ".txt");
		}
		oPanel.checkIfLoadPossible();
		show(oPanel, true);
		inOverworld = true;

	}

	public void checkIfLoadingHasFinished() {
		if (loadingLevelUrl != null) {
			if (frame.isLoaded(loadingLevelUrl)) {
				Level loadedLevel = frame.getLoadedLevel(loadingLevelUrl);
				if (loadedLevel.getPlayerX() == -1000) {
					loadingLevelUrl = null;
					System.err.println("Loading failed. Level file corrupted");
					return;
				}
				if (isOverworld(loadingLevelUrl)) {
					show(new OverworldPanel(loadedLevel, keyHandler, this, tetroTypes, oPanel.getLastLevelSolved()), true);
				} else {
					show(new GameWorldPanel(loadedLevel, keyHandler, this, tetroTypes), false);
				}
				loadedLevel.init(this);
				loadingLevelUrl = null;

			}
		}
	}

	private boolean isOverworld(String loadingLevelUrl) {
		return loadingLevelUrl.contains("overworld");
	}

	protected void initiateDeleteAllSALSaves() {
		frame.initiateDeleteAllSALSaves();

	}

	public void loadLevel(String url) {
		// clearText(); //TODO nach geschmack einfügen
		loadingLevelUrl = url; // Aufpassen auf 2 Sachen laden!
		frame.loadLevel(url);
	}

	// Level to level
	public void switchLevel(String path, SaveNLoadTile tile) {
		loadLevel(path); // TODO was soll das Tile ? Siehe commit vom ca. 26.06.2019
	}

	public void loadLegacyLevel() { // After pressing Load in overworld
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
		frame.changeToMenuFrame();

	}

	public void playSound(String sound, float volume) {
		frame.playSound(sound, volume);
	}

	public ConsoleLine[] getText() {
		return text;
	}

//	public void setLevelSolved(int levelSolved) {
//TODO unused
//	}

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
		frame.saveLevel(level, url);
	}

	private void show(Panel panel, boolean isOverworld) {
		frame.add(panel);
		if (panel != oPanel && oPanel != null) {
			frame.remove(oPanel);
		}
		if (panel != lPanel && lPanel != null) {
			frame.remove(lPanel);
		}
		if (isOverworld) {
			oPanel = (OverworldPanel) panel;
		} else {
			lPanel = (GameWorldPanel) panel;
		}

		frame.changePlayable(panel);
	}

	public boolean isLoading() {
		return loadingLevelUrl != null;
	}

	
	
	public BufferedImage getImage(String url) {
		return frame.getImage(url);
	}

	public ArrayList<TetroType> getTetroTypes() {
		return tetroTypes;
	}

	public HashMap<String, Animation> getAnimations(String url) {
		return frame.getAnimations(url);

	}

	public void windowClosing() {
		if (inOverworld) {
			oPanel.save();
		}
	}

	public void cleanUp() {
		frame.remove(oPanel);
		frame.removeKeyListener(keyHandler);

	}

	public OverworldPanel getOPanel() {
		return oPanel;
	}

	public void deleteLastSALT() {
		
		
	}

}