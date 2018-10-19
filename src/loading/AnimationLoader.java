package loading;

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

	public static HashMap<String, Animation> loadAnimations(String url) {
		Scanner sc = null;
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

		HashMap<String, Pics> pics = new HashMap<>();
		HashMap<String, Animation> anims = new HashMap<>();

		while (sc.hasNext()) {
			String nextLine = sc.nextLine();
			if (nextLine.startsWith("pics")) {
				String name = nextLine.substring(nextLine.indexOf(" "), nextLine.indexOf("=")).trim();
				pics.put(name, new Pics(nextLine.substring(nextLine.indexOf("\"") + 1, nextLine.length() - 1),
						Integer.parseInt(nextLine.substring(4, nextLine.indexOf(" ")))));
			} else if (nextLine.startsWith("anim")) {
				int animTicks;
				if(nextLine.indexOf(" ") == 4) {
					animTicks = 3;
				} else {
					animTicks = Integer.parseInt(nextLine.substring(4, nextLine.indexOf(" ")));
				}
				String name = nextLine.substring(nextLine.indexOf(" "), nextLine.indexOf("=")).trim();
				String[] values = nextLine.substring(nextLine.indexOf("=") + 1).trim().split("\\+");
				Animation anim = new Animation(animTicks);
				for (String strFrame : values) {
					strFrame = strFrame.trim();
//					System.out.println((strFrame.substring(0, strFrame.indexOf("."))) + ";;;;;"
//							+ (Integer.parseInt(strFrame.substring(strFrame.indexOf(".") + 1))));
					anim.addFrame(pics.get(strFrame.substring(0, strFrame.indexOf(".")))
							.get(Integer.parseInt(strFrame.substring(strFrame.indexOf(".") + 1))));
				}
				anims.put(name, anim);
			}
		}
		return anims;
	}

}
