package loading;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import data.Level;
import data.RawTetro;
import data.Tiles.DekoTile;
import data.Tiles.DoorTile;
import data.Tiles.EmptyTile;
import data.Tiles.GoalTile;
import data.Tiles.LevelGuiTile;
import data.Tiles.PressurePlateTile;
import data.Tiles.SaveNLoadTile;
import data.Tiles.Tile;
import data.Tiles.WallTile;
import graphics.GameFrame;
import logics.entities.Entity;
import logics.entities.MovingBlock;
import logics.entities.MovingBlockSpawner;
import logics.entities.Switch;

/**
 * @author Lars Created on 13.08.2018
 */
public class LevelLoader {

	private static final int tetrotype_amount = 7;

	public static Level loadLevel(String url) {
		boolean error = false;
		ArrayList<RawTetro> rawTetros = new ArrayList<>();
		ArrayList<String> world = new ArrayList<>();
		ArrayList<DoorTile> doors = new ArrayList<>();
		ArrayList<DoorTile> doorsToAdd = new ArrayList<>();
		ArrayList<Entity> entities = new ArrayList<>();
		Tile[][] arrWorld = null;

		HashMap<Integer, Integer> rawMaxTetroAmounts = new HashMap<>();
		boolean[] toggleStates = new boolean[6];
		for (int i = 0; i < toggleStates.length; i++) {
			toggleStates[i] = false;
		}

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

			// Lines for Tetros, enteties, etc.
			String nextLine = sc.nextLine();
			if (nextLine.startsWith("p")) {
				String[] attribs = nextLine.split(";");
				for (String attr : attribs) {
					if (attr.startsWith("x=")) {
						playerX = Integer.parseInt(attr.substring(attr.indexOf("=") + 1));
					}
					if (attr.startsWith("y=")) {
						playerY = Integer.parseInt(attr.substring(attr.indexOf("=") + 1));
					}
				}
			} else if (nextLine.startsWith("o")) {
				String[] attribs = nextLine.split(";");
				for (int i = 1; i < attribs.length; i++) {
					toggleStates[i - 1] = Boolean.parseBoolean(attribs[i]);
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

			} else if (nextLine.startsWith("e")) {
				String strSplit[] = nextLine.split(";");
				String type = null;
				double x = -1;
				double y = -1;
				double rx = -1;
				double ry = -1;
				double cx = -1;
				double cy = -1;
				double rcx = -1;
				double rcy = -1;
				int color = 0;
				int currentCubeX = -1000;
				int currentCubeY = -1000;

				for (String str : strSplit) {
					if (str == strSplit[0])
						continue;
					if (str.startsWith("x=")) {
						x = Integer.parseInt(str.substring(2));
					} else if (str.startsWith("y=")) {
						y = Integer.parseInt(str.substring(2));
					} else if (str.startsWith("rx=")) {
						rx = Integer.parseInt(str.substring(3));
					} else if (str.startsWith("ry=")) {
						ry = Integer.parseInt(str.substring(3));
					} else if (str.startsWith("type=")) {
						type = str.substring(str.indexOf("=") + 1);
					} else if (str.startsWith("cx=")) {
						cx = Integer.parseInt(str.substring(3));
					} else if (str.startsWith("cy=")) {
						cy = Integer.parseInt(str.substring(3));
					} else if (str.startsWith("rcx=")) {
						rcx = Integer.parseInt(str.substring(4));
					} else if (str.startsWith("rcy=")) {
						rcy = Integer.parseInt(str.substring(4));
					} else if (str.startsWith("color=") || str.startsWith("c=")) {
						color = Integer.parseInt(str.substring(str.indexOf("=") + 1));
					} else if (str.startsWith("currentCubeX=")) {
						currentCubeX = Integer.parseInt(str.substring(13));
					} else if (str.startsWith("currentCubeY=")) {
						currentCubeY = Integer.parseInt(str.substring(13));
					}

				}

				if (rcx == -1 || rcy == -1) {
					rcx = cx * GameFrame.BLOCKSIZE;
					rcy = cy * GameFrame.BLOCKSIZE;
				}
				if (rx == -1 || ry == -1) {
					rx = x * GameFrame.BLOCKSIZE;
					ry = y * GameFrame.BLOCKSIZE;
				}
				if (type != null && rx >= 0 && ry >= 0) {
					if (type.equals("switch")) {
						entities.add(new Switch(null, rx, ry, color));
					} else if (type.equals("moveblock")) {
						entities.add(new MovingBlock(null, rx, ry));
					} else if (type.equals("moveblockspawner")) {
						entities.add(new MovingBlockSpawner(null, rx, ry, rcx, rcy, currentCubeX, currentCubeY));
					} else {
						System.err.println("Unbekannte Entity bei [virtual](" + x + "|" + y + "): \"" + type + "\"");
					}
				} else {
					System.err.println("Entityerstellung fehlerhaft bei [virtual](" + x + "|" + y + ").");
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
					doors.add(new DoorTile(color, x, y, rotation, open));
					doorsToAdd.add(doors.get(doors.size() - 1));
				} else {
					System.err.println("Fehler im Level \"" + url + "\": Tür kann nicht erstellt werden wegen "
							+ (x >= 0) + (y >= 0) + (rotation >= 0) + (color >= 0));
					System.exit(3);
				}

			} else if (nextLine.startsWith("w")) {
				String strTemp = nextLine.substring(nextLine.indexOf(";") + 1);
				world.add(strTemp);
				worldlength = Math.max(worldlength, strTemp.length());
			} else if (nextLine.startsWith("###")) {
				if (arrWorld == null)
					arrWorld = new Tile[world.size()][worldlength];
			} else if (nextLine.startsWith("Tl")) {
				String strSplit[] = nextLine.split(";");

				int x = -1;
				int y = -1;
				String tip = null;
				String tip2 = null;
				String tip3 = null;
				String tip4 = null;
				boolean addingTetros = false;
				int amountList[] = { 0, 0, 0, 0, 0, 0, 0 };
				for (String str : strSplit) {

					if (str.startsWith("x=")) {
						x = Integer.parseInt(str.substring(2));
					} else if (str.startsWith("y=")) {
						y = Integer.parseInt(str.substring(2));
					} else if (str.startsWith("amount=")) {

						String[] amounts = str.substring(str.indexOf("=") + 1).split(",");

						for (int i = 0; i < amounts.length; i++) {
							if (i < amountList.length)
								amountList[i] = Integer.parseInt(amounts[i]);
						}
					} else if (str.startsWith("a=")) {
						if (str.substring(2).equals("true"))
							addingTetros = true;

					} else if (str.startsWith("tip=")) {
						tip = str.substring(4);
					} else if (str.startsWith("tip2=")) {
						tip2 = str.substring(5);
					} else if (str.startsWith("tip3=")) {
						tip3 = str.substring(5);
					} else if (str.startsWith("tip4=")) {
						tip4 = str.substring(5);
					}

				}
				// System.ot.println("" + x + y + amountList + addingTetros);
				arrWorld[y][x] = new SaveNLoadTile('2', x, y, amountList, addingTetros, tip, tip2, tip3, tip4);
			} else if (nextLine.startsWith("Td")) {
				String strSplit[] = nextLine.split(";");
				int x = -1;
				int y = -1;
				int xo = -1;
				int yo = -1;
				int pidq = 0;
				String name = "block0";

				for (String str : strSplit) {

					if (str.startsWith("x=")) {
						x = Integer.parseInt(str.substring(2));
					} else if (str.startsWith("y=")) {
						y = Integer.parseInt(str.substring(2));
					} else if (str.startsWith("xo=")) {
						xo = Integer.parseInt(str.substring(3));
					} else if (str.startsWith("yo=")) {
						yo = Integer.parseInt(str.substring(3));
					} else if (str.startsWith("name=")) {
						name = str.substring(5);
					} else if (str.startsWith("p=")) {
						pidq = Integer.parseInt(str.substring(2));
					}

				}
				arrWorld[y][x] = new DekoTile('X', x, y, xo, yo, name, pidq);
			}

		}
		sc.close();

		if (arrWorld == null)
			arrWorld = new Tile[world.size()][worldlength];
		for (int j = 0; j < world.size(); j++) {
			String worldString = world.get(j);
			for (int i = 0; i < worldString.length(); i++) {

				if (arrWorld[j][i] == null) {
					char tileChar = worldString.charAt(i);

					if (tileChar == '1') {
						arrWorld[j][i] = new WallTile(tileChar, i, j);
					} else if (tileChar == '0') {
						arrWorld[j][i] = null;
					} else if (tileChar == '2') {
						arrWorld[j][i] = new SaveNLoadTile(tileChar, i, j, new int[] { 0, 0 }, false, null, null, null,
								null);
						System.err.println("Tl;x=" + i + ";y=" + j + ";amount=0,0,0,0,0,0,0;");
					} else if (tileChar == 'D') {

						for (DoorTile dT : doors) {
							if (dT.getPosX() == i && dT.getPosY() == j) {
								arrWorld[j][i] = dT;
								doorsToAdd.remove(dT);
								break;
							}
						}
						if (arrWorld[j][i] == null) {
							// System.err.println(
							// "Fehler im Level \"" + url + "\": Tür nicht bestimmt" + "(" + i + "/" + j +
							// ")");
							System.err.println("d;x=" + i + ";y=" + j + ";r=;c=;o=false");
							arrWorld[j][i] = new EmptyTile(tileChar, i, j);

						}

					} else if (tileChar == 'à' || tileChar == 'è' || tileChar == 'ì' || tileChar == 'ò'
							|| tileChar == 'ù' || tileChar == 'À') {
						arrWorld[j][i] = new PressurePlateTile(tileChar, i, j);
					} else if (tileChar == '!') {
						arrWorld[j][i] = new GoalTile(i, j);
					} else if (Character.isLowerCase(tileChar)) {
						arrWorld[j][i] = new LevelGuiTile(tileChar, i, j);
					} else {
						System.err.println("Unbekanntes Tile bei (" + i + "|" + j + ")");
						arrWorld[j][i] = new EmptyTile(tileChar, i, j);
					}
				}
			}
		}

		for (DoorTile dT : doorsToAdd) {
			System.err.println("Unused door: x=" + dT.getPosX() + ";y=" + dT.getPosY());
			error = true;
		}

		int[] tetroAmounts = new int[tetrotype_amount];
		for (int i = 0; i < tetroAmounts.length; i++) {
			if (rawMaxTetroAmounts.get(i) != null) {
				tetroAmounts[i] = rawMaxTetroAmounts.get(i);
			} else {
				tetroAmounts[i] = 0;
			}
		}

		if (error) {
			System.exit(1);
		}

		return new Level(rawTetros, arrWorld, doors, entities, tetroAmounts, toggleStates,
				playerX * GameFrame.BLOCKSIZE, playerY * GameFrame.BLOCKSIZE);
	}

	// private static void initWallTiles(Tile[][] arrWorld) {
	// for (int j = 0; j < arrWorld.length; j++) {
	// for (int i = 0; i < arrWorld[j].length; i++) {
	// if (arrWorld[j][i] != null && arrWorld[j][i].getKey() == '1') {
	// boolean t, r, b, l;
	// t = (j == 0 || arrWorld[j - 1][i] == null || arrWorld[j - 1][i].getKey() !=
	// '1');
	// r = (i == arrWorld[j].length - 1 || arrWorld[j][i + 1] == null
	// || arrWorld[j][i + 1].getKey() != '1');
	// b = (j == arrWorld.length - 1 || arrWorld[j + 1][i] == null || arrWorld[j +
	// 1][i].getKey() != '1');
	// l = (i == 0 || arrWorld[j][i - 1] == null || arrWorld[j][i - 1].getKey() !=
	// '1');
	//
	// }
	//
	// }
	//
	// }
	//
	//
	// }

	public static boolean isAbsolute(String url) {
		return !url.startsWith("/") && !url.startsWith("\\") && !url.startsWith(File.separator);
	}

}
