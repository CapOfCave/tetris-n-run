package loading;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import data.Level;
import data.RawTetro;
import data.TetroType;
import data.Tiles.LevelGuiTile;
import data.Tiles.Tile;
import data.Tiles.WallTile;
import graphics.Frame;
import logics.entities.items.Item;

/**
 * @author Lars Created on 13.08.2018
 */
public class LevelLoader {

	public static Level loadLevel(String url, Frame frame) {
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
				sc = new Scanner(new File(url));
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
				for (String attr : attribs) {
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
					} else if (str.startsWith("tetroType=") || str.startsWith("tetro=") || str.startsWith("type=") || str.startsWith("t=")) {
						type = Integer.parseInt(str.substring(str.indexOf("=") + 1));
					}
				}
				if (x != -100 && y != -100 && rotation != -1 && type != -1) {
					rawTetros.add(new RawTetro(type, x, y, rotation));
				}
			} else if (nextLine.startsWith("i")) {
				String strSplit[] = nextLine.split(";");
				int x = -100;
				int y = -100;
				String typeUrl = null;
				for (String str : strSplit) {
					if (str == strSplit[0])
						continue;
					if (str.startsWith("x=")) {
						x = Integer.parseInt(str.substring(2));
					} else if (str.startsWith("y=")) {
						y = Integer.parseInt(str.substring(2));
					} else if (str.startsWith("type=") || str.startsWith("t=")) {
						typeUrl = str.substring(str.indexOf("=") + 1);
					}
				}
				if (x != -100 && y != -100 && typeUrl != null) {
					Item item = ItemLoader.readItem(typeUrl);
					item.setX(x);
					item.setY(y);
				}

			} else if (nextLine.startsWith("w")) {
				String strTemp = nextLine.substring(nextLine.indexOf(";") + 1);
				world.add(strTemp);
				worldlength = Math.max(worldlength, strTemp.length());
			}

			// TODO: ItemWorld erstellen (Item[][])
		}
		sc.close();
		Tile[][] arrWorld = new Tile[world.size()][worldlength];
		for (int j = 0; j < world.size(); j++) {
			String worldString = world.get(j);
			for (int i = 0; i < worldString.length(); i++) {

				char tileChar = worldString.charAt(i);

				if (Character.isLowerCase(tileChar)) {
					arrWorld[j][i] = new LevelGuiTile(worldString.charAt(i), i, j, frame);
				} else if (tileChar == '1') {
					arrWorld[j][i] = new WallTile(worldString.charAt(i), i, j, frame);
				} else if (tileChar == '0') {
					arrWorld[j][i] = new Tile(worldString.charAt(i), i, j, false, frame);
				} else {
					System.out.println("Unbekanntes Tile bei (" + i + "|" + j + ")");
					arrWorld[j][i] = new Tile(worldString.charAt(i), i, j, false, frame);
				}

			}
		}

		if (blockSize > 0 && tetrofileUrl != null) {
			tetroTypes = TetroLoader.loadTetros(tetrofileUrl, blockSize);

			// TODO: null ersetzen
			return new Level(tetroTypes, rawTetros, arrWorld, null, blockSize, tetrofileUrl, playerX * blockSize, playerY * blockSize);
		} else {
			System.out.println("Levelerstellung nicht erfolgreich. Keine Tetros");
			System.exit(1);
			return null;
		}
	}

	public static boolean isAbsolute(String url) {
		return !url.startsWith("/") && !url.startsWith("\\") && !url.startsWith(File.separator);
	}

}
