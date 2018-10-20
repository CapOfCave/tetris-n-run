package loading;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import data.Level;
import data.RawTetro;
import data.Tiles.DoorTile;
import data.Tiles.Tile;
import logics.entities.items.Item;

/**
 * @author Lars Created on 13.08.2018
 */
public class LevelSaver {

	public void saveLevel(Level level, String path) {
		print(createOutput(level), path);
	}

	private void print(ArrayList<String> content, String path) {
		File file = new File(path);
		if (!file.exists()) {
			try {
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
		StringBuilder settings = new StringBuilder("i");
		String tetroFileUrl = level.getTetrofileUrl();
		if (tetroFileUrl != null && tetroFileUrl != "") {
			settings.append(";tetrofile=" + tetroFileUrl);
		}
		String strSettings = settings.toString();
		if (!strSettings.equals("s")) {
			outpLines.add(strSettings);
		}

		// player position
		outpLines.add("p;x=" + level.getPlayerX() + ";y=" + level.getPlayerY());

		// tetros
		ArrayList<RawTetro> rawTetros = level.getUnfinishedTetros();
		for (RawTetro rt : rawTetros) {
			outpLines.add("t;x=" + rt.getX() + ";y=" + rt.getY() + ";r=" + rt.getRotation() + ";t=" + rt.getType());
		}

		// items
		ArrayList<Item> items = level.getItemWorld();
		for (Item i : items) {
			outpLines.add("i;x=" + i.getX() + ";y=" + i.getY() + ";t=" + i.getPath());
		}

		// doord
		ArrayList<DoorTile> doors = level.getDoors();
		for (DoorTile dT : doors) {
			outpLines.add("d;x=" + dT.getPosX() + ";y=" + dT.getPosY() + ";r=" + dT.getRotation() + ";c=" + dT.getColor() + ";o=" + dT.isWalkable());
		}

		// world
		Tile[][] world = level.getArrWorld();
		for (Tile[] row : world) {
			StringBuilder worldLine = new StringBuilder("w;");
			for (Tile field : row) {
				worldLine.append(field.getKey());
			}
			outpLines.add(worldLine.toString());
		}
		return outpLines;
	}

}
