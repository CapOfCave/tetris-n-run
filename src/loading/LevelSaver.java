package loading;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import data.Level;
import data.RawTetro;
import data.Tiles.Tile;

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
			for(String str: content) {
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
		StringBuilder settings = new StringBuilder("s");
		String tetroFileUrl = level.getTetrofileUrl();
		if (tetroFileUrl != null && tetroFileUrl != "") {
			settings.append(";tetrofile=" + tetroFileUrl);
		}
		int blockSize = level.getBlockSize();
		if (blockSize > 0) {
			settings.append(";blockSize=" + blockSize);
		}
		String strSettings = settings.toString();
		if (!strSettings.equals("s")) {
			outpLines.add(strSettings);
		}
		
		//player position
		outpLines.add("p;x=" + level.getPlayerX() + ";y=" + level.getPlayerY());
		
		
		// tetros
		ArrayList<RawTetro> rawTetros = level.getUnfinishedTetros();
		for (RawTetro rt : rawTetros) {
			outpLines.add("t;x=" + rt.getX() + ";y=" + rt.getY() + ";r=" + rt.getRotation() + ";t=" + rt.getType());
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
