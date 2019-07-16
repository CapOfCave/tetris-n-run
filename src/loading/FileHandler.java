package loading;

import java.awt.Point;
import java.io.File;

public final class FileHandler {
	public static void deleteAllSaveNLoadSaves() {
		File tmpSaveFolder = new File(System.getenv("APPDATA") + "\\tetris-n-run\\saves\\tmpSaves");
		if (tmpSaveFolder.exists())
			for (File f : tmpSaveFolder.listFiles()) {
				f.delete();
			}
	}
	
	public static Point deleteNewestSaveNLoadSave() {
		File file = new File(System.getenv("APPDATA") + "\\tetris-n-run\\saves\\tmpSaves");
		int folder_length = file.listFiles().length;
		if (folder_length <= 1) {
			return null;
		}
		File newest = null;
		for (File f : file.listFiles()) {
			if (f.getName().startsWith(folder_length + "saveNLoad")) {
				newest = f;
			}
		}
		if (newest != null) {
			newest.delete();
			String[] names = newest.getName().split("_");
			return new Point(Integer.parseInt(names[1]), Integer.parseInt(names[2]));
		}
		return null;
	}
	
	public static Point getNewestSaveNLoadSave() {
		File file = new File(System.getenv("APPDATA") + "\\tetris-n-run\\saves\\tmpSaves");
		int folder_length = file.listFiles().length;
		File newest = null;
		for (File f : file.listFiles()) {
			if (f.getName().startsWith(folder_length + "saveNLoad")) {
				newest = f;
			}
		}
		if (newest != null) {
			String[] names = newest.getName().split("_");
			return new Point(Integer.parseInt(names[1]), Integer.parseInt(names[2]));
		}
		return null;
	}
	
}
