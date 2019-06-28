package loading;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class SettingsLoader {

	String url;
	ArrayList<Integer> keyCodes = null;
	int levelSolved = -1;

	public SettingsLoader(String url) {
		this.url = url;
	}

	public void loadAll() {
		ArrayList<Integer> keyCodes = new ArrayList<>();

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
		while (sc.hasNext()) {

			String nextLine = sc.nextLine();

			if (nextLine.startsWith("k")) {
				String codeInString = nextLine.substring(2);
				for (String code : codeInString.split(",")) {
					if (code != "")
						keyCodes.add(Integer.parseInt(code));
				}

			} else if (nextLine.startsWith("l")) {
				try {
				levelSolved = Integer.parseInt(nextLine.substring(2));
				} catch (NumberFormatException e) {
					e.printStackTrace();
					levelSolved = 0;
					System.err.println("Reset LevelSolved to 0");
				}
			}
		}
		if (keyCodes.size() > 0) {
			this.keyCodes = keyCodes;
		}

	}

	public int getLevelSolved() {
		if (levelSolved == -1) {
			loadAll();
		}
		return levelSolved;
	}

	public ArrayList<Integer> getKeyCodes() {
		if (keyCodes == null) {
			loadAll();
		}
		return keyCodes;
	}

}
