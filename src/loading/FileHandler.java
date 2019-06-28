package loading;

import java.io.File;

public final class FileHandler {
	public static void deleteAllSaveNLoadSaves() {
		File tmpSaveFolder = new File(System.getenv("APPDATA") + "\\tetris-n-run\\saves\\tmpSaves");
		if (tmpSaveFolder.exists())
			for (File f : tmpSaveFolder.listFiles()) {
				f.delete();
			}
	}
}
