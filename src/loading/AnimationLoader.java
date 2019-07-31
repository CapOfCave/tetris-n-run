package loading;

import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import data.Animation;
import data.Pics;

/**
 * @author Lars Created on 07.10.2018
 */
public class AnimationLoader {

	
	private ImageLoader imageLoader;
	private HashMap<String, HashMap<String, Animation>> animations;
	private boolean eclipseVersion = false;
	

	public AnimationLoader(ImageLoader imageLoader) {
		this.imageLoader = imageLoader;
		animations = new HashMap<>();
	}
	
	public void loadAndSave(String path) {
		animations.put(path, loadAnimations(path, true));
		
	}
	public HashMap<String, Animation> getAnimations(String path) {
		if (animations.get(path) == null) {
			loadAndSave(path);
			if (!eclipseVersion) {
			}
		}
		return animations.get(path);
	}


	
	private HashMap<String, Animation> loadAnimations(String url, boolean loadingThread) {
		
		Scanner sc = null;
		try {
		if (!LevelLoader.isAbsolute(url)) {
			sc = new Scanner(Toolkit.getDefaultToolkit().getClass().getResourceAsStream(url));
		} else {
			try {
				sc = new Scanner(new File(url));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		} catch(NullPointerException e) {
			System.err.println("Wrong url or File not found: \"" + url + "\"");
			e.printStackTrace();
			System.exit(2);
		}

		HashMap<String, Pics> pics = new HashMap<>();
		HashMap<String, Animation> anims = new HashMap<>();

		while (sc.hasNext()) {
			String nextLine = sc.nextLine();
			if (nextLine.startsWith("pics")) {
				String name = nextLine.substring(nextLine.indexOf(" "), nextLine.indexOf("=")).trim();
				try {
					pics.put(name, new Pics(nextLine.substring(nextLine.indexOf("\"") + 1, nextLine.length() - 1),
							Integer.parseInt(nextLine.substring(4, nextLine.indexOf(" "))), imageLoader, loadingThread));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					System.exit(3);
				}
			} else if (nextLine.startsWith("anim")) {
				int animTicks;
				if (nextLine.indexOf(" ") == 4) {
					animTicks = 3;
				} else {
					animTicks = Integer.parseInt(nextLine.substring(4, nextLine.indexOf(" ")));
				}
				String name = nextLine.substring(nextLine.indexOf(" "), nextLine.indexOf("=")).trim();
				String[] values = nextLine.substring(nextLine.indexOf("=") + 1).trim().split("\\+");
				Animation anim = new Animation(animTicks);
				for (String strFrame : values) {
					String[] valuesSplit = strFrame.split(";");
					int xOff;
					int yOff;
					if (valuesSplit.length < 3|| valuesSplit[1].equals("") || valuesSplit[2].equals("")) {
						xOff = 0;
						yOff = 0;
					} else {
						xOff = Integer.parseInt(valuesSplit[1].trim());
						yOff = Integer.parseInt(valuesSplit[2].trim());
					}
					valuesSplit[0] = valuesSplit[0].trim();
					anim.addFrame(pics.get(valuesSplit[0].substring(0, valuesSplit[0].indexOf(".")))
							.get(Integer.parseInt(valuesSplit[0].substring(valuesSplit[0].indexOf(".") + 1))), new Point(xOff, yOff));
				}
				anims.put(name, anim);
			}
		}
		return anims;
	}
	
	public void setEclipseVersion(boolean b) {
		this.eclipseVersion  = b;
	}

	
}
