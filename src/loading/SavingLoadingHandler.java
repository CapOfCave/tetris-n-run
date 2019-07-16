package loading;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;

import data.Animation;
import data.Level;
import data.TetroType;
import tools.Coder;

public class SavingLoadingHandler implements Runnable {

	private boolean running = false;
	private Thread th;
	private LevelSaver levelSaver;
	private LevelLoader levelLoader;
	private ImageLoader imageLoader;
	private AnimationLoader animationLoader;

	private ArrayList<Level> levelsToSave;
	private ArrayList<String> levelsToSaveUrls;

	private ArrayList<String> levelsToLoadUrls;
	private HashMap<String, Level> loadedLevels;
	private boolean deleteAllSaveNLoads = false;
	private boolean everythingLoaded = false;
	private ArrayList<String> imagesToLoad;
	private Coder coder;
	
		

	private void init(ImageLoader imageLoader) {
		this.imageLoader = imageLoader;
		animationLoader = new AnimationLoader(imageLoader);
		coder = new Coder();
		levelSaver = new LevelSaver(coder);
		levelLoader = new LevelLoader(coder);

		levelsToSave = new ArrayList<>();
		levelsToSaveUrls = new ArrayList<>();

		loadedLevels = new HashMap<>();
		levelsToLoadUrls = new ArrayList<>();
		
		imagesToLoad = new ArrayList<>();
	}

	@Override
	public void run() {
		while (running) {
			if (deleteAllSaveNLoads) {
				deleteAllSaveNLoads = false;
				FileHandler.deleteAllSaveNLoadSaves();
			} else if (imagesToLoad.size() > 0) {
				imageLoader.loadAndSave(imagesToLoad.remove(0));
			} else if (levelsToLoadUrls.size() > 0) {
				if (!everythingLoaded) {
					loadAll();
				}

				loadedLevels.put(levelsToLoadUrls.get(0), levelLoader.loadLevel(levelsToLoadUrls.get(0)));
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
		levelsToSaveUrls.add(url); //  Bad Style, no todo though
	}
	
	public void loadImage(String url) {
		imagesToLoad.add(url);
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
	
	public void loadAll() {
		everythingLoaded = true;
		coder.loadPrimes();
		Enumeration<URL> en;
		try {
			en = getClass().getClassLoader().getResources("res");
			if (en.hasMoreElements()) {
				try {
					// Thx to
					// https://stackoverflow.com/questions/5193786/how-to-use-classloader-getresources-correctly/5194002#5194002
					JarURLConnection metaInf = (JarURLConnection) en.nextElement().openConnection();
					Enumeration<JarEntry> enumer = metaInf.getJarFile().entries();
					while (enumer.hasMoreElements()) {
						String element = enumer.nextElement().getName();
						if (element.endsWith(".png")) { // additional constraints?
							imageLoader.loadAndSave("/" + element); // TODO welche?
						} else if (element.startsWith("res/anims") && element.endsWith(".txt")){
							animationLoader.loadAndSave("/" + element);
						}
					}

				} catch (ClassCastException e) {
					System.err.println("Dieses Feature funktioniert z.Z. nur beim Exportieren, nicht in Eclipse.");
					setEclipseVersion(true);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setEclipseVersion(boolean b) {
		imageLoader.setEclipseVersion(b);
		animationLoader.setEclipseVersion(b);
	}
	
	public HashMap<String, Animation> getAnimations(String path) {
		return animationLoader.getAnimations(path);
	}
	
	public BufferedImage getImage(String path) { //TODO unused
		return imageLoader.getImage(path);
	}

	public ArrayList<TetroType> getTetros(String string) {
		return new TetroLoader(imageLoader).loadTetros("/res/tetros.txt");
	}
	
}
