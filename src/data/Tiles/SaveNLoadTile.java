package data.Tiles;

import java.awt.Graphics;

import java.awt.image.BufferedImage;
import java.io.File;

import graphics.Frame;
import loading.ImageLoader;

public class SaveNLoadTile extends Tile {

	BufferedImage image3d;

	public SaveNLoadTile(char key, int posX, int posY, boolean walkable, boolean walkableWithTetro, Frame frame) {
		super(key, posX, posY, walkable, walkableWithTetro, frame);

		image3d = ImageLoader.loadImage("/res/blocks/block3.png");

	}
	@Override
	public void eventWhenEntering() {
		String folderName = System.getenv("APPDATA") + "\\tetris-n-run\\levelSaves\\tmpSaves\\";
		int prefix = (new File(folderName).listFiles().length + 1);
		boolean exists = false;
		for (File f : new File(folderName).listFiles()) {
			if (f.getName().endsWith("saveNLoadTile_" + posX + "_" + posY + ".txt")) {
				exists = true;
			}
		}

		if (!exists) {
			frame.addLineToText("Spielstand wurde gespeichert.");
			world.save(folderName, prefix + "saveNLoadTile_" + posX + "_" + posY + ".txt");
		}
	}

	@Override
	public void draw(Graphics g, float interpolation) {
		g.drawImage(image3d, (int) (posX * Frame.BLOCKSIZE - world.cameraX()),
				(int) (posY * Frame.BLOCKSIZE - world.cameraY()), null);
	}

	@Override
	public void interact() {
		super.interact();
		String folderName = System.getenv("APPDATA") + "\\tetris-n-run\\levelSaves\\tmpSaves\\";
		int prefix = (new File(folderName).listFiles().length + 1);
		boolean exists = false;
		File loadFile = null;
		for (File f : new File(folderName).listFiles()) {
			if (f.getName().endsWith("saveNLoadTile_" + posX + "_" + posY + ".txt")) {
				exists = true;
				loadFile = f;
			}
		}

		if (!exists) {
			frame.addLineToText("Spielstand wurde gespeichert.");
			world.save(folderName, prefix + "saveNLoadTile_" + posX + "_" + posY + ".txt");

		} else {
			frame.addLineToText("Spielstand wurde geladen.");
			frame.swichLevel(loadFile.getAbsolutePath());
			int index = Integer.parseInt(loadFile.getName().substring(0, loadFile.getName().indexOf("save")));
			for (File f : new File(folderName).listFiles()) {
				if (Integer.parseInt(f.getName().substring(0, f.getName().indexOf("save"))) > index) {
					f.delete();
				}
			}
		}
	}

}
