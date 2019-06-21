package loading;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class SettingsLoader {

	String url;
	ArrayList<Integer> keyCodes = null;
	ArrayList<Integer> levelSolved = null;
	int difficulty = -1;

	public SettingsLoader(String url) {
		this.url = url;
	}

	public void loadAll() {
		ArrayList<Integer> keyCodes = new ArrayList<>();
		ArrayList<Integer> levelSolved = new ArrayList<>();

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
				String levelInString = nextLine.substring(2);
				for (String code : levelInString.split(",")) {
					if (code != "")
						levelSolved.add(Integer.parseInt(code));
				}
			} else if (nextLine.startsWith("d")) {
				this.difficulty = Integer.parseInt(nextLine.substring(2));
			}

		}
		System.out.println(difficulty);
		if (keyCodes.size() > 0) {
			this.keyCodes = keyCodes;
		}
		if (levelSolved.size() > 0) {
			this.levelSolved = levelSolved;
		}

	}

	public ArrayList<Integer> getLevelSolved() {
		if (levelSolved == null) {
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

	public int getDifficulty() {
		if (difficulty == -1) {
			loadAll();
		}
		return difficulty;
	}

}
