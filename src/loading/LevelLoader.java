package loading;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import data.RawTetro;
import data.TetroType;
import logics.Level;

/**
 * @author Lars Created on 13.08.2018
 */
public class LevelLoader {

	public static Level loadLevel(String url) {
		String tetrofileUrl = null;
		int blockSize = -1;
		ArrayList<TetroType> tetroTypes;
		ArrayList<RawTetro> rawTetros = new ArrayList<>();
		ArrayList<String> world = new ArrayList<>();
		int worldlength = 0;
		int playerX = 0;
		int playerY = 0;
		
		Scanner sc = null;
		if (!isAbsolute(url)) {
			sc = new Scanner(Toolkit.getDefaultToolkit().getClass().getResourceAsStream(url));
		} else {
			try {
				sc =  new Scanner(new File(url));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		while (sc.hasNext()) {
			String nextLine = sc.nextLine();
			if (nextLine.startsWith("s")) {
				String[] strSettings = nextLine.split(";");
				for (String str : strSettings) {
					if (str == strSettings[0])
						continue;
					if (str.startsWith("tetrofile=")) {
						tetrofileUrl = str.substring(10);
					} else if (str.startsWith("blockSize=") || str.startsWith("size=") || str.startsWith("blocksize=")) {
						blockSize = Integer.parseInt(str.substring(10));
					}

				}
			} else if (nextLine.startsWith("p")) {
				String[] attribs = nextLine.split(";");
				for(String attr:attribs) {
					if (attr.startsWith("x=")) {
						playerX = Integer.parseInt(attr.substring(attr.indexOf("=") + 1));
					}
					if (attr.startsWith("y=")) {
						playerY = Integer.parseInt(attr.substring(attr.indexOf("=") + 1));
					}
				}
				
			} else if (nextLine.startsWith("t")) {
				int x = -100;
				int y = -100;
				int rotation = -1;
				int type = -1;
				String[] strTetros = nextLine.split(";");
				for (String str : strTetros) {
					if (str == strTetros[0])
						continue;
					if (str.startsWith("x=")) {
						x = Integer.parseInt(str.substring(2));
					} else if (str.startsWith("y=")) {
						y = Integer.parseInt(str.substring(2));
					} else if (str.startsWith("rotation=") || str.startsWith("r=")) {
						rotation = Integer.parseInt(str.substring(str.indexOf("=") + 1));
					} else if (str.startsWith("tetroType=") || str.startsWith("tetro=") || str.startsWith("type") || str.startsWith("t")) {
						type = Integer.parseInt(str.substring(str.indexOf("=") + 1));
					}
				}
				if (x != -100 && y != -100 && rotation != -1 && type != -1) {
					rawTetros.add(new RawTetro(type, x, y, rotation));
				}
			} else if (nextLine.startsWith("w")) {
				String strTemp = nextLine.substring(nextLine.indexOf(";") + 1);
				world.add(strTemp);
				worldlength = Math.max(worldlength, strTemp.length());
			}
		}
		sc.close();
		int[][] arrWorld = new int[world.size()][worldlength];
		for (int j = 0; j < world.size(); j++) {
			String worldString = world.get(j);
			for (int i = 0; i < worldString.length(); i++) {
				arrWorld[j][i] = Character.digit(worldString.charAt(i), 4);
			}
		}

		if (blockSize > 0 && tetrofileUrl != null) {
			tetroTypes = TetroLoader.loadTetros(tetrofileUrl, blockSize);
			return new Level(tetroTypes, rawTetros, arrWorld, blockSize, tetrofileUrl, playerX, playerY);
		} else {
			System.out.println("Levelerstellung nicht erfolgreich. Keine Tetros");
			System.exit(1);
			return null;
		}
	}

	private static boolean isAbsolute(String url) {
		return !url.startsWith("/") && !url.startsWith("\\") && !url.startsWith(File.separator);
	}

}
