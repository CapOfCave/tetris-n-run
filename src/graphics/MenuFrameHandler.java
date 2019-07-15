package graphics;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import data.Animation;
import input.MenuKeyHandler;

public class MenuFrameHandler {
	
	private static final int MIN_LOADING_TICKS = 20;
	
	private Frame frame;
	
	private MenuPanel mPanel;
	private OptionPanel oPanel;
	private TutorialPanel tPanel;
	
	private MenuKeyHandler keyHandler;

	private int levelSolved;
	private String loadingLevelUrl;
	private int loadingTicks = MIN_LOADING_TICKS;

	public MenuFrameHandler(Frame frame, int levelSolved) {
		this.levelSolved = levelSolved;
		this.frame = frame;

		mPanel = new MenuPanel(this);
		tPanel = new TutorialPanel(this);
		oPanel = new OptionPanel(this);
		keyHandler = new MenuKeyHandler(oPanel);

		frame.add(mPanel);
		frame.addKeyListener(keyHandler);
		
	}

	public MenuFrameHandler(Frame frame, ArrayList<Integer> keyCodes, int levelSolved) {
		this(frame, levelSolved);
		oPanel.setKeyCodes(keyCodes);
	}

	public void startMenu() {
		frame.add(mPanel);
		frame.remove(tPanel);
		frame.remove(oPanel);

	}

	public void startTutorial() {
		tPanel.initImages();
		frame.add(tPanel);
		frame.remove(mPanel);
		frame.remove(oPanel);

	}

	public void playSound(String sound, float volume) {
		frame.playSound(sound, volume);
	}

	public ArrayList<Integer> getKeyCodes() {
		return oPanel.getKeyCodes();
	}

	public void startOption() {
		oPanel.initImages();
		frame.add(oPanel);
		frame.remove(mPanel);
		frame.remove(tPanel);

	}

	public int getLevelSolved() {
		return levelSolved;
	}

	public void loadLevel(String url) {
		loadingLevelUrl = url;
		frame.loadLevel(url);
		loadingTicks = 0;
		
	}

	public void checkIfLoading() {
		if (loadingLevelUrl != null) {
			if (frame.isLoaded(loadingLevelUrl) && loadingTicks >= MIN_LOADING_TICKS) {
				// Load is ready
				frame.changeToGameFrame();
				loadingLevelUrl = null;

			}
		}
	}

	public boolean isLoading() {
		return loadingLevelUrl != null || loadingTicks < MIN_LOADING_TICKS;
	}


	public BufferedImage getImage(String path) {
		return frame.getImage(path);
	}

	public void increaseLoadingTicks() {
		loadingTicks++;
	}

	public HashMap<String, Animation> getAnimations(String url) {
		return frame.getAnimations(url);
	}

	public MenuPanel getmPanel() {
		return mPanel;
	}

	public void cleanUp() {
		frame.remove(mPanel);
		frame.removeKeyListener(keyHandler);
	}

	public String getLoadingLevelUrl() {
		return loadingLevelUrl;
	}

}
