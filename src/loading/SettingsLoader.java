package loading;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class SettingsLoader {

	public static ArrayList<Integer> loadKeyCodes(String url) {
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

			// Lines for Tetros, enteties, etc.
			String nextLine = sc.nextLine();

			if (nextLine.startsWith("k")) {
				String codeInString = nextLine.substring(2);
				for (String code : codeInString.split(",")) {
					if (code != "")
						keyCodes.add(Integer.parseInt(code));
				}

			}

		}
		if (keyCodes.size() > 0)
			return keyCodes;
		else
			return null;
	}
}
