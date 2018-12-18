package loading;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import data.Level;
import data.RawSpawner;
import data.RawTetro;
import data.Tiles.DoorTile;
import data.Tiles.Tile;
import logics.entities.MovingBlockSpawner;
import logics.entities.Entity;
import logics.entities.Switch;
import logics.entities.items.Item;

/**
 * @author Lars Created on 13.08.2018
 */
public class LevelSaver {
//TODO correct save/Load von Cubespawnern
	public void saveLevel(Level level, String path, String fileName) {
		print(createOutput(level), path, fileName);
	}

	private void print(ArrayList<String> content, String path, String fileName) {
		File file = new File(path + "\\" + fileName);
		if (!file.exists()) {
			try {
				File temp = new File(path);
				temp.mkdirs();

				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for (String str : content) {
				bw.write(str);
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private ArrayList<String> createOutput(Level level) {
		ArrayList<String> outpLines = new ArrayList<>();

		// settings
		StringBuilder settings = new StringBuilder("b");
		String tetroFileUrl = level.getTetrofileUrl();
		if (tetroFileUrl != null && tetroFileUrl != "") {
			settings.append(";tetrofile=" + tetroFileUrl);
		}
		String strSettings = settings.toString();
		if (!strSettings.equals("i")) {
			outpLines.add(strSettings);
		}

		// player position
		outpLines.add("p;x=" + level.getPlayerX() + ";y=" + level.getPlayerY());

		// Toggle states
		boolean[] toggleStates = level.getToggleStates();
		outpLines.add("o;" + toggleStates[0] + ";" + toggleStates[1] + ";" + toggleStates[2] + ";" + toggleStates[3]
				+ ";" + toggleStates[4]);
		// } else if (nextLine.startsWith("m")) {
		// String strSplit[] = nextLine.split(";");
		// int type = -1;
		// int amount = 0;
		// for (String str : strSplit) {
		// if (str == strSplit[0])
		// continue;
		// if (str.startsWith("amount=") || str.startsWith("a=")) {
		// amount = Integer.parseInt(str.substring(str.indexOf("=") + 1));
		// } else if (str.startsWith("type=") || str.startsWith("t=")) {
		// type = Integer.parseInt(str.substring(str.indexOf("=") + 1));
		// }
		// }
		// if (type >= 0) {
		// rawMaxTetroAmounts.put(type, amount);
		// }
		int[] tetroAmounts = level.getTetroAmounts();
		for (int i = 0; i < tetroAmounts.length; i++) {
			if (tetroAmounts[i] != 0) {
				outpLines.add("m;type=" + i + ";amount=" + tetroAmounts[i]);
			}
		}

		// tetros
		ArrayList<RawTetro> rawTetros = level.getUnfinishedTetros();
		for (RawTetro rt : rawTetros) {
			outpLines.add("t;x=" + rt.getX() + ";y=" + rt.getY() + ";r=" + rt.getRotation() + ";t=" + rt.getType());
		}

		// items
		ArrayList<Item> items = level.getItemWorld();
		for (Item i : items) {
			outpLines.add("i;x=" + (int)i.getX() + ";y=" + (int)i.getY() + ";t=" + i.getPath());
		}

		// doors
		ArrayList<DoorTile> doors = level.getDoors();
		for (DoorTile dT : doors) {
			outpLines.add("d;x=" + dT.getPosX() + ";y=" + dT.getPosY() + ";r=" + dT.getRotation() + ";c="
					+ dT.getColor() + ";o=" + dT.isToggled());
		}

		// Spawners
		ArrayList<RawSpawner> spawners = level.getSpawner();
		for (RawSpawner spawner : spawners) {
			outpLines.add("s;x=" + spawner.getX() + ";y=" + spawner.getY() + ";loff=" + spawner.getLoff() + ";boff="
					+ spawner.getBoff() + ";toff=" + spawner.getToff() + ";roff=" + spawner.getRoff() + ";max="
					+ spawner.getMax() + ";rate=" + spawner.getRate() + ";start=" + spawner.getStart());
		}

		// Other entities
		ArrayList<Entity> entities = level.getEntities();
		for (Entity entity : entities) {
			String outp = "e;rx=" + (int) entity.getX() + ";ry=" + (int) entity.getY() + ";type=" + entity.getType()
					+ ";url=" + entity.getAnimPath();
			switch (entity.getType()) {
			case "moveblock":
				outpLines.add(outp);
				break;
			case "moveblockspawner":
				MovingBlockSpawner cubeSpawner = (MovingBlockSpawner) entity;
				outpLines.add(outp + ";cx=" + (int) cubeSpawner.getCX() + ";cy=" + (int) cubeSpawner.getCY() + ";curl="
						+ cubeSpawner.getCurl());
				break;
			case "switch":
				Switch entitySwitch = (Switch) entity;
				outpLines.add(outp + ";color=" + entitySwitch.getColor());
				break;
			default:
				System.err.println("Requested Entity (\"" + entity.getType() + "\") undefind in saving process");
			}

		}

		// world
		Tile[][] world = level.getArrWorld();
		for (Tile[] row : world) {
			StringBuilder worldLine = new StringBuilder("w;");
			for (Tile field : row) {
				if(field != null)
				worldLine.append(field.getKey());
				else
					worldLine.append('0');
			}
			outpLines.add(worldLine.toString());
		}

		// ###Line
		outpLines.add("###");

		// Tiles in line
		for (Tile[] row : world) {

			for (Tile field : row) {
				if (field != null && field.getKey() == '2') {
					StringBuilder worldLine = new StringBuilder("Tl;");
					worldLine.append("x=" + field.getPosX() + ";");
					worldLine.append("y=" + field.getPosY() + ";");
					worldLine.append("amount=");
					for (int amount : field.getTetroAmount())
						worldLine.append(amount + ",");
					outpLines.add(worldLine.toString());
				}
			}

		}

		return outpLines;
	}

}
