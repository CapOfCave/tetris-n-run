package graphics;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Point;
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
	private int PANEL_WIDTH = 0;
	private int PANEL_HEIGHT = 0; // 1300 x 900
//	private Dimension screenSize = new Dimension(1920, 1080);
	private Dimension screenSize = new Dimension(1300, 900);
	private Point panelOffset;

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
			File akt_Overworld = new File(System.getenv("APPDATA") + "\\tetro-maze\\saves\\overworldSave.txt");
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
		
		calcPanelSize();
		

		setUndecorated(true);

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
	private void calcPanelSize() {
		setExtendedState(MAXIMIZED_BOTH);
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();//TODO später wieder einfügen
		PANEL_WIDTH = (int) Math.min(screenSize.getWidth(), 16. / 9 * screenSize.getHeight());
		PANEL_HEIGHT = (int) Math.min(screenSize.getHeight(), 9. / 16 * screenSize.getWidth());
		panelOffset = new Point((screenSize.width - PANEL_WIDTH) / 2, (screenSize.height - PANEL_HEIGHT) / 2);
	}

	private void postInitJFrame() {
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	

	private void loadMenuFrameHandler() {
		SettingsLoader settingsLoader = new SettingsLoader(System.getenv("APPDATA") + "\\tetro-maze\\settings.txt");
		File savesFile = new File(System.getenv("APPDATA") + "\\tetro-maze\\saves");

		if (!savesFile.exists()) {
			savesFile.mkdirs();
		}

		File settingsFile = new File(System.getenv("APPDATA") + "\\tetro-maze\\settings.txt");

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

	public int getPanelWidth() {
		return PANEL_WIDTH;
	}
	
	public int getPanelHeight() {
		return PANEL_HEIGHT;
	}
	
	public int getScreenWidth() {
		return (int) screenSize.getWidth();
	}
	
	public int getScreenHeight() {
		return (int) screenSize.getHeight();
	}
	
	public Dimension getScreenSize() {
		return screenSize;
	}

	public int getPanelOffsetX() {
		return panelOffset.x;
	}
	public int getPanelOffsetY() {
		return panelOffset.y;
	}

}
