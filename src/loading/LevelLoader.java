package loading;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import data.Level;
import data.RawSpawner;
import data.RawTetro;
import data.TetroType;
import data.Tiles.DoorTile;
import data.Tiles.EmptyTile;
import data.Tiles.GoalTile;
import data.Tiles.LevelGuiTile;
import data.Tiles.Switch;
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
		ArrayList<TetroType> tetroTypes;
		ArrayList<RawTetro> rawTetros = new ArrayList<>();
		ArrayList<String> world = new ArrayList<>();
		ArrayList<Item> rawItems = new ArrayList<>();
		ArrayList<DoorTile> doors = new ArrayList<>();
		ArrayList<RawSpawner> spawner = new ArrayList<>();

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
			if (nextLine.startsWith("i")) {
				String[] strSettings = nextLine.split(";");
				for (String str : strSettings) {
					if (str == strSettings[0])
						continue;
					if (str.startsWith("tetrofile=")) {
						tetrofileUrl = str.substring(10);
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
					} else if (str.startsWith("tetroType=") || str.startsWith("tetro=") || str.startsWith("type=")
							|| str.startsWith("t=")) {
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
					item.setTypeUrl(typeUrl);
					rawItems.add(item);
				}

			} else if (nextLine.startsWith("d")) {
				int x = -100;
				int y = -100;
				int rotation = -1;
				int color = -1;
				boolean open = false;
				String strSplit[] = nextLine.split(";");
				for (String str : strSplit) {
					if (str == strSplit[0])
						continue;
					if (str.startsWith("x=")) {
						x = Integer.parseInt(str.substring(2));
					} else if (str.startsWith("y=")) {
						y = Integer.parseInt(str.substring(2));
					} else if (str.startsWith("r=") || str.startsWith("rotation=")) {
						rotation = Integer.parseInt(str.substring(str.indexOf("=") + 1));
					} else if (str.startsWith("c=") || str.startsWith("color=")) {
						color = Integer.parseInt(str.substring(str.indexOf("=") + 1));
					} else if (str.startsWith("o=") || str.startsWith("open=")) {
						open = Boolean.parseBoolean(str.substring(str.indexOf("=") + 1));
					}

				}
				if (x >= 0 && y >= 0 && rotation >= 0 && color >= 0) {
					doors.add(new DoorTile(color, x, y, rotation, open, frame));
				} else {
					System.err.println("Fehler im Level \"" + url + "\": Tür kann nicht erstellt werden wegen "
							+ (x >= 0) + (y >= 0) + (rotation >= 0) + (color >= 0));
					System.exit(3);
				}
			} else if (nextLine.startsWith("s")) {
				int x = -1;
				int y = -1;
				int loff = 0;
				int toff = 0;
				int roff = 0;
				int boff = 0;
				int max = 1;
				boolean tetroonly = true;
				double rate = -1;
				boolean start = false;
				String strSplit[] = nextLine.split(";");
				for (String str : strSplit) {
					if (str.startsWith("x=")) {
						x = Integer.parseInt(str.substring(2));
					} else if (str.startsWith("y=")) {
						y = Integer.parseInt(str.substring(2));
					} else if (str.startsWith("loff=")) {
						loff = Integer.parseInt(str.substring(5));
					} else if (str.startsWith("toff=")) {
						toff = Integer.parseInt(str.substring(5));
					} else if (str.startsWith("roff=")) {
						roff = Integer.parseInt(str.substring(5));
					} else if (str.startsWith("boff=")) {
						boff = Integer.parseInt(str.substring(5));
					} else if (str.startsWith("max=")) {
						max = Integer.parseInt(str.substring(4));
					} else if (str.startsWith("tetroonly=")) {
						tetroonly = Boolean.parseBoolean(str.substring(10));
					} else if (str.startsWith("rate=")) {
						rate = Double.parseDouble(str.substring(5).replace(",", "."));
					} else if (str.startsWith("start=")) {
						start = Boolean.parseBoolean(str.substring(6));
					}
				}
				if (x >= 0 && y >= 0 && rate >= 0) {
					spawner.add(new RawSpawner(x, y, loff, toff, roff, boff, max, tetroonly, rate, start));
				} else {
					System.err.println("Fehler im Level \"" + url + "\": Spawner nicht bestimmt");
				}
			} else if (nextLine.startsWith("w")) {
				String strTemp = nextLine.substring(nextLine.indexOf(";") + 1);
				world.add(strTemp);
				worldlength = Math.max(worldlength, strTemp.length());
			}

		}
		sc.close();

		Tile[][] arrWorld = new Tile[world.size()][worldlength];
		for (int j = 0; j < world.size(); j++) {
			String worldString = world.get(j);
			for (int i = 0; i < worldString.length(); i++) {

				char tileChar = worldString.charAt(i);

				if (tileChar == '1') {
					arrWorld[j][i] = new WallTile(tileChar, i, j, frame);
				} else if (tileChar == '0') {
					arrWorld[j][i] = new EmptyTile(tileChar, i, j, frame);
				} else if (tileChar == 'D') {
					for (DoorTile dT : doors) {
						if (dT.getPosX() == i && dT.getPosY() == j) {
							arrWorld[j][i] = dT;
						}
					}
					if (arrWorld[j][i] == null) {
						System.err.println("Fehler im Level \"" + url + "\": Tür nicht bestimmt");
						arrWorld[j][i] = new EmptyTile(tileChar, i, j, frame);

					}

				} else if (tileChar == 'à' || tileChar == 'è' || tileChar == 'ì' || tileChar == 'ò') {
					arrWorld[j][i] = new Switch(tileChar, i, j, frame);
				} else if (tileChar == '!') {
					arrWorld[j][i] = new GoalTile(i, j, frame);
				} else if (Character.isLowerCase(tileChar)) {
					arrWorld[j][i] = new LevelGuiTile(tileChar, i, j, frame);
				} else {
					System.err.println("Unbekanntes Tile bei (" + i + "|" + j + ")");
					arrWorld[j][i] = new EmptyTile(tileChar, i, j, frame);
				}

			}
		}

		if (tetrofileUrl != null) {
			tetroTypes = TetroLoader.loadTetros(tetrofileUrl);

			return new Level(tetroTypes, rawTetros, arrWorld, rawItems, doors, spawner, tetrofileUrl,
					playerX * Frame.BLOCKSIZE, playerY * Frame.BLOCKSIZE);
		} else {
			System.err.println("Levelerstellung nicht erfolgreich");
			System.exit(1);
			return null;
		}
	}

	public static boolean isAbsolute(String url) {
		return !url.startsWith("/") && !url.startsWith("\\") && !url.startsWith(File.separator);
	}

}
