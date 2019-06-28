package loading;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import data.Level;

public class SavingLoadingHandler implements Runnable {

	private boolean running = false;
	private Thread th;
	private LevelSaver levelSaver;
	private ImageLoader imageLoader;

	private ArrayList<Level> levelsToSave;
	private ArrayList<String> levelsToSaveUrls;

	private ArrayList<String> levelsToLoadUrls;
	private HashMap<String, Level> loadedLevels;
	private boolean deleteAllSaveNLoads = false;

	private void init(ImageLoader imageLoader) {
		levelSaver = new LevelSaver();
		this.imageLoader = imageLoader;

		levelsToSave = new ArrayList<>();
		levelsToSaveUrls = new ArrayList<>();

		loadedLevels = new HashMap<>();
		levelsToLoadUrls = new ArrayList<>();
	}

	@Override
	public void run() {
		while (running) {
			if (deleteAllSaveNLoads) {
				deleteAllSaveNLoads = false;
				FileHandler.deleteAllSaveNLoadSaves();
			} else if (levelsToLoadUrls.size() > 0) {
				if (!imageLoader.isEverythingLoaded()) {
					System.out.println("Now Loading: All Images");
					imageLoader.loadAll();
					System.out.println("Image Load completed");
				}

				loadedLevels.put(levelsToLoadUrls.get(0), LevelLoader.loadLevel(levelsToLoadUrls.get(0)));
				levelsToLoadUrls.remove(0);

			} else if (levelsToSave.size() > 0) {
				levelSaver.saveLevel(levelsToSave.get(0), levelsToSaveUrls.get(0));
				levelsToSave.remove(0);
				levelsToSaveUrls.remove(0);
			} else {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public void start(ImageLoader imageLoader) {
		if (!running) {
			running = true;
			init(imageLoader);
			th = new Thread(this);
			th.start();
		}
	}

	public void stop() {
		if (running) {
			running = false;
			try {
				th.join(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void loadLevel(String url) {
		levelsToLoadUrls.add(url);
	}

	public void saveLevel(Level level, String url) {
		levelsToSave.add(level);
		levelsToSaveUrls.add(url); // TODO Bad Style
	}

	public boolean isLoaded(String levelUrl) {
		return loadedLevels.get(levelUrl) != null;
	}

	public Level getLoadedLevel(String levelUrl) {
		return loadedLevels.get(levelUrl);
	}

	public void deleteAllSaveNLoads() {
		deleteAllSaveNLoads = true;

	}

	public void abortLoadingSAL() {
		File tmpSaveFolder = new File(System.getenv("APPDATA") + "\\tetris-n-run\\saves\\tmpSaves");
		if (tmpSaveFolder.exists())
			for (File f : tmpSaveFolder.listFiles()) {
				if (levelsToLoadUrls.remove(f.getPath())) {
					System.err.println("Aborted " + f.getPath());
				}
			}
	}
}
