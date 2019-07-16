package graphics;

import java.awt.CardLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;

import data.Animation;
import data.Level;
import data.TetroType;
import loading.ImageLoader;
import loading.SavingLoadingHandler;
import loading.SettingsLoader;
import logics.GameLoop;
import logics.Playable;
import sound.SoundPlayer;

public class Frame extends JFrame {
	private static final long serialVersionUID = 1L;

	private MenuFrameHandler menuFrameHandler;
	private GameFrameHandler gameFrameHandler;

	private SavingLoadingHandler savingLoadingHandler;
	private ImageLoader imageLoader;
	private GameLoop gameLoop;
	private SoundPlayer soundPlayer;

	public static void main(String[] args) {
		new Frame();

	}

	public Frame() {

		// Object init
		savingLoadingHandler = new SavingLoadingHandler();
		imageLoader = new ImageLoader();
		savingLoadingHandler.start(imageLoader);
		soundPlayer = new SoundPlayer();

		// Frame init //Help pls commit
		initJFrame();
		loadMenuFrameHandler();
		postInitJFrame();

		gameLoop = new GameLoop(menuFrameHandler.getmPanel());
		gameLoop.start();
	}

	public void changeToGameFrame() {

		Level loadedLevel = savingLoadingHandler.getLoadedLevel(menuFrameHandler.getLoadingLevelUrl());
		if (loadedLevel.getPlayerX() == -1000) {
			System.err.println("Level file corrupted. Loading standard Overworld and deleting everything. Sorry.");
			File akt_Overworld = new File(System.getenv("APPDATA") + "\\tetris-n-run\\saves\\overworldSave.txt");
			akt_Overworld.delete();
			menuFrameHandler.loadLevel("/res/levels/overworld0.txt");
			return;
		}
		gameFrameHandler = new GameFrameHandler(this, menuFrameHandler.getKeyCodes(), menuFrameHandler.getLevelSolved(),
				loadedLevel);
		gameLoop.changePlayable(gameFrameHandler.getOPanel());
		menuFrameHandler.cleanUp();
		menuFrameHandler.resetLoadingLevelUrl();
	}

	public void changeToMenuFrame() {
		gameFrameHandler.cleanUp();
		gameLoop.changePlayable(menuFrameHandler.getmPanel());
		add(menuFrameHandler.getmPanel());
		addKeyListener(menuFrameHandler.getKeyHandler());
	}

	private void initJFrame() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/res/icon.png")));
		setLayout(new CardLayout());
		setResizable(false);
		setDefaultCloseOperation(0);
		
		//TODO fullscreen
//		setExtendedState(MAXIMIZED_BOTH);
//		setUndecorated(true);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				gameLoop.stop();
				if (gameFrameHandler != null) {
					gameFrameHandler.windowClosing();
				}
				System.exit(0);
			}
		});
	}
	private void postInitJFrame() {
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	

	private void loadMenuFrameHandler() {
		SettingsLoader settingsLoader = new SettingsLoader(System.getenv("APPDATA") + "\\tetris-n-run\\settings.txt");
		File savesFile = new File(System.getenv("APPDATA") + "\\tetris-n-run\\saves");

		if (!savesFile.exists()) {
			savesFile.mkdirs();
		}

		File settingsFile = new File(System.getenv("APPDATA") + "\\tetris-n-run\\settings.txt");

		if (!settingsFile.exists()) {
			menuFrameHandler = new MenuFrameHandler(this, 0);
		} else {
			settingsLoader.loadAll();
			menuFrameHandler = new MenuFrameHandler(this, settingsLoader.getKeyCodes(),
					settingsLoader.getLevelSolved());
		}
	}

	public void changePlayable(Playable playable) {
		gameLoop.changePlayable(playable);
	}

	public void loadLevel(String url) {
		savingLoadingHandler.loadLevel(url);
	}

	public boolean isLoaded(String levelUrl) {
		return savingLoadingHandler.isLoaded(levelUrl);
	}

	public BufferedImage getImage(String path) {
		return imageLoader.getImage(path);
	}

	public HashMap<String, Animation> getAnimations(String url) {
		return savingLoadingHandler.getAnimations(url);
	}

	public ArrayList<TetroType> getTetros(String string) {
		return savingLoadingHandler.getTetros(string);
	}

	public Level getLoadedLevel(String levelUrl) {
		return savingLoadingHandler.getLoadedLevel(levelUrl);
	}

	public void initiateDeleteAllSALSaves() {
		savingLoadingHandler.deleteAllSaveNLoads();
		savingLoadingHandler.abortLoadingSAL();
	}

	public void saveLevel(Level level, String url) {
		savingLoadingHandler.saveLevel(level, url);
	}

	public void playSound(String sound, float volume) {
		soundPlayer.playSound(sound, volume);
	}

}
