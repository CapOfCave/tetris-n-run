package tools;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import data.RawTetro;
import data.Tiles.DekoTile;
import data.Tiles.DoorTile;
import data.Tiles.EmptyTile;
import data.Tiles.GoalTile;
import data.Tiles.InvalidSaveNLoadTile;
import data.Tiles.LevelGuiTile;
import data.Tiles.PressurePlateTile;
import data.Tiles.SaveNLoadTile;
import data.Tiles.Tile;
import data.Tiles.WallTile;
import graphics.GameFrameHandler;
import loading.LevelLoader;
import logics.entities.Entity;
import logics.entities.MovingBlockSpawner;
import logics.entities.Switch;

public class Coder {

	public static final int MODULO_SAVED = 100000;
	private ArrayList<Integer> primes;
	private final String url = "/res/primes.txt";
	private boolean showErrors;
	private static boolean completeSouts = false;

	public Coder(boolean b) {
		this.showErrors = b;
	}

	public Coder() {
		this(true);
	}

	public void loadPrimes() {
		primes = new ArrayList<>();

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
			primes.add(Integer.parseInt(sc.nextLine()));
		}
	}

	public Integer getPrime(int index) {
		if (primes == null) {
			if (showErrors) {
				System.err.println("Primes unnecessarily loaded");
			}
			loadPrimes();
		}
		return primes.get(index);
	}

	public int increaseCheckForSALT(int check, int currentLine, int posX, int posY, int[] tetroAmount) {
		int toAdd = 0;
		toAdd += getPrime(0) * getPrime(currentLine) * posX;
		toAdd %= Coder.MODULO_SAVED;

		toAdd += getPrime(1) * getPrime(currentLine) * posY;
		toAdd %= Coder.MODULO_SAVED;

		for (int i = 0; i < tetroAmount.length; i++) {
			toAdd += getPrime(i + 2) * getPrime(currentLine) * tetroAmount[i];
		}
		toAdd += check;
		toAdd %= Coder.MODULO_SAVED;

		if (completeSouts) {
			System.out.println(currentLine + ": " + toAdd);
		}
		return toAdd;
	}

	public int addWorldLineToChecker(int check, int currentLine, String row) {
		int toAdd = 0;
		for (int i = 0; i < row.length(); i++) {
			toAdd += getPrime(i) * getPrime(currentLine) * row.charAt(i);
		}
		toAdd += check;
		toAdd %= Coder.MODULO_SAVED;
		if (completeSouts) {
			System.out.println(currentLine + ": " + toAdd);
		}
		return toAdd;
	}

	public int increaseCheck(int check, int currentLine, int... args) {
		int toAdd = 0;
		for (int i = 0; i < args.length; i++) {
			toAdd += getPrime(i) * getPrime(currentLine) * args[i];
		}
		toAdd += check;
		toAdd %= Coder.MODULO_SAVED;
		if (completeSouts) {
			System.out.println(currentLine + ": " + toAdd);
		}
		return toAdd;
	}

	
	public static int getValidationNumber(String url) {
		Coder coder = new Coder(false);
		int check = 0;
		int currentLine = 1;
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
				check = coder.increaseCheck(check, currentLine++, playerX, playerY);
			} else if (nextLine.startsWith("o")) {
				String[] attribs = nextLine.split(";");
				for (int i = 1; i < attribs.length; i++) {
					toggleStates[i - 1] = Boolean.parseBoolean(attribs[i]);
				}
				check = coder.increaseCheck(check, currentLine++, toggleStates[0] ? 1 : 0, toggleStates[1] ? 1 : 0,
						toggleStates[2] ? 1 : 0, toggleStates[3] ? 1 : 0, toggleStates[4] ? 1 : 0,
						toggleStates[5] ? 1 : 0);
			} else if (nextLine.startsWith("m")) {
				String[] attribs = nextLine.split(";");
				int type = -1;
				int amount = 0;
				for (int i = 1; i < attribs.length; i++) {
					if (attribs[i].startsWith("type=")) {
						type = Integer.parseInt(attribs[i].substring(5));
					} else if (attribs[i].startsWith("amount=")) {
						amount = Integer.parseInt(attribs[i].substring(7));
					}
				}
				rawMaxTetroAmounts.put(type, amount);
				check = coder.increaseCheck(check, currentLine++, type, amount);
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
					check = coder.increaseCheck(check, currentLine++, x, y, rotation, type);
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
					rcx = cx * GameFrameHandler.BLOCKSIZE;
					rcy = cy * GameFrameHandler.BLOCKSIZE;
				}
				if (rx == -1 || ry == -1) {
					rx = x * GameFrameHandler.BLOCKSIZE;
					ry = y * GameFrameHandler.BLOCKSIZE;
				}
				if (type != null && rx >= 0 && ry >= 0) {
					if (type.equals("switch")) {
						entities.add(new Switch(null, rx, ry, color));
						check = coder.increaseCheck(check, currentLine, (int) rx / GameFrameHandler.BLOCKSIZE,
								(int) ry / GameFrameHandler.BLOCKSIZE, color);
					} else if (type.equals("moveblock")) {
					} else if (type.equals("moveblockspawner")) {

						entities.add(new MovingBlockSpawner(null, rx, ry, rcx, rcy, currentCubeX, currentCubeY));
						if (currentCubeX != -1000) {
							check = coder.increaseCheck(check, currentLine, (int) rx / GameFrameHandler.BLOCKSIZE,
									(int) ry / GameFrameHandler.BLOCKSIZE, (int) rcx / GameFrameHandler.BLOCKSIZE,
									(int) rcy / GameFrameHandler.BLOCKSIZE);
						} else {
							check = coder.increaseCheck(check, currentLine, (int) rx / GameFrameHandler.BLOCKSIZE,
									(int) ry / GameFrameHandler.BLOCKSIZE, (int) rcx / GameFrameHandler.BLOCKSIZE,
									(int) rcy / GameFrameHandler.BLOCKSIZE, currentCubeX, currentCubeY);
						}
					} else {
					}
				} else {
				}
				currentLine++;
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
					check = coder.increaseCheck(check, currentLine++, x, y, rotation, color, open ? 1 : 0);
				} else {
				}
			} else if (nextLine.startsWith("w")) {
				String strTemp = nextLine.substring(nextLine.indexOf(";") + 1);
				check = coder.addWorldLineToChecker(check, currentLine++, strTemp);
				world.add(strTemp);
				worldlength = Math.max(worldlength, strTemp.length());
			} else if (nextLine.startsWith("###")) {
				if (arrWorld == null)
					arrWorld = new Tile[world.size()][worldlength];
				currentLine++;
			} else if (nextLine.startsWith("Tl")) {
				String strSplit[] = nextLine.split(";");

				int x = -1;
				int y = -1;
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

					}

				}
				// System.ot.println("" + x + y + amountList + addingTetros);
				arrWorld[y][x] = new SaveNLoadTile('2', x, y, amountList, addingTetros);
				check = coder.increaseCheckForSALT(check, currentLine++, x, y, amountList);
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
						arrWorld[j][i] = new SaveNLoadTile(tileChar, i, j, new int[] { 0, 0 }, false);
					} else if (tileChar == '3') {
						arrWorld[j][i] = new InvalidSaveNLoadTile(i, j);
					} else if (tileChar == 'D') {

						for (DoorTile dT : doors) {
							if (dT.getPosX() == i && dT.getPosY() == j) {
								arrWorld[j][i] = dT;
								doorsToAdd.remove(dT);
								break;
							}
						}
						if (arrWorld[j][i] == null) {
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
						arrWorld[j][i] = new EmptyTile(tileChar, i, j);
					}
				}
			}
		}

		int[] tetroAmounts = new int[7];
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

		return check;

	}
}