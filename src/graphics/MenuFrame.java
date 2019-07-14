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
import input.MenuKeyHandler;
import loading.ImageLoader;
import loading.SavingLoadingHandler;
import loading.SettingsLoader;
import logics.GameLoop;
import sound.SoundPlayer;

public class MenuFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private static final int MIN_LOADING_TICKS = 20;

	private SavingLoadingHandler savingLoadingHandler;
	private SoundPlayer soundPlayer;
	private MenuPanel mPanel;
	private OptionPanel oPanel;
	private TutorialPanel tPanel;
	private MenuKeyHandler keyHandler;
	private GameLoop gameLoop;
	private int levelSolved;

	private String loadingLevelUrl;

	private ImageLoader imageLoader;

	private int loadingTicks = MIN_LOADING_TICKS;

	public static void main(String[] args) {
		SettingsLoader loader = new SettingsLoader(System.getenv("APPDATA") + "\\tetris-n-run\\settings.txt");
		File savesFile = new File(System.getenv("APPDATA") + "\\tetris-n-run\\saves");

		if (!savesFile.exists()) {
			savesFile.mkdirs();
		}

		File settingsFile = new File(System.getenv("APPDATA") + "\\tetris-n-run\\settings.txt");

		if (!settingsFile.exists()) {
			new MenuFrame(0);
		} else {
			loader.loadAll();

			new MenuFrame(loader.getKeyCodes(), loader.getLevelSolved());
		}
	}

	public MenuFrame(int levelSolved) {
		this.levelSolved = levelSolved;
		savingLoadingHandler = new SavingLoadingHandler();
		imageLoader = new ImageLoader();
		savingLoadingHandler.start(imageLoader);

		mPanel = new MenuPanel(this);
		tPanel = new TutorialPanel(this);
		oPanel = new OptionPanel(this);
		keyHandler = new MenuKeyHandler(oPanel);
		soundPlayer = new SoundPlayer();

		gameLoop = new GameLoop(mPanel);

		setLayout(new CardLayout());
		add(mPanel);
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/res/gegner.png")));
		addKeyListener(keyHandler);
		setResizable(false);
		setDefaultCloseOperation(0);
		pack();
		setDefaultCloseOperation(3);
		setLocationRelativeTo(null);
		setVisible(true);
		gameLoop.start();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
			}
		});

	}

	public MenuFrame(ArrayList<Integer> keyCodes, int levelSolved) {
		this(levelSolved);
		oPanel.setKeyCodes(keyCodes);
	}

	public void startMenu() {
		add(mPanel);
		remove(tPanel);
		remove(oPanel);

	}

	public void startTutorial() {
		tPanel.initImages();
		add(tPanel);
		remove(mPanel);
		remove(oPanel);

	}

	public void playSound(String sound, float volume) {
		soundPlayer.playSound(sound, volume);

	}

	public ArrayList<Integer> getKeyCodes() {

		return oPanel.getKeyCodes();
	}

	public void startOption() {
		oPanel.initImages();
		add(oPanel);
		remove(mPanel);
		remove(tPanel);

	}

	public int getLevelSolved() {
		return levelSolved;
	}

	public SavingLoadingHandler getSavingLoadingHandler() {
		return savingLoadingHandler;
	}

	public void loadLevel(String url) {
		loadingLevelUrl = url;
		savingLoadingHandler.loadLevel(url);
		loadingTicks = 0;
		
	}

	public void checkIfLoading() {
		if (loadingLevelUrl != null) {
			if (savingLoadingHandler.isLoaded(loadingLevelUrl) && loadingTicks >= MIN_LOADING_TICKS) {
				// Load is ready
				Level loadedLevel = savingLoadingHandler.getLoadedLevel(loadingLevelUrl);

				new GameFrame(getKeyCodes(), getLevelSolved(), this, loadedLevel);
				setVisible(false);

				loadingLevelUrl = null;

			}
		}
	}

	public boolean isLoading() {
		return loadingLevelUrl != null || loadingTicks < MIN_LOADING_TICKS;
	}

	public ImageLoader getImageLoader() {
		return imageLoader;
	}

	public BufferedImage getImage(String path) {
		return imageLoader.getImage(path);
	}

	public void increaseLoadingTicks() {
		loadingTicks++;
	}

	public HashMap<String, Animation> getAnimations(String url) {
		return savingLoadingHandler.getAnimations(url);
	}

}
