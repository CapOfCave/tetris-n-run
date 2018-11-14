package data.Tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import graphics.Frame;
import loading.ImageLoader;
import logics.worlds.World;

public class SaveNLoadTile extends Tile {

	BufferedImage image3dSaved;
	BufferedImage image3dUnSaved;

	boolean fileExists = false;
	File loadFile = null;
	private static final String folderName = System.getenv("APPDATA") + "\\tetris-n-run\\levelSaves\\tmpSaves\\";

	public SaveNLoadTile(char key, int posX, int posY, boolean walkable, boolean walkableWithTetro, Frame frame) {
		super(key, posX, posY, walkable, walkableWithTetro, frame);

		image3dSaved = ImageLoader.loadImage("/res/blocks/saveNLoad2.png");
		image3dUnSaved = ImageLoader.loadImage("/res/blocks/saveNLoad1.png");

	}

	@Override
	public void setWorld(World world) {
		super.setWorld(world);
		checkIfExists();
	}

	@Override
	public void draw(Graphics g, float interpolation) {
		if (fileExists) {
			g.drawImage(image3dSaved, (int) (posX * Frame.BLOCKSIZE - world.cameraX()),
					(int) (posY * Frame.BLOCKSIZE - world.cameraY()), null);
		} else {
			g.drawImage(image3dUnSaved, (int) (posX * Frame.BLOCKSIZE - world.cameraX()),
					(int) (posY * Frame.BLOCKSIZE - world.cameraY()), null);
		}
	}

	@Override
	public void eventWhenEntering() {

		new File(folderName).mkdirs();
		int prefix = (new File(folderName).listFiles().length + 1);

		if (!fileExists) {
			frame.addLineToText("Spielstand wurde gespeichert.");
			world.save(folderName, prefix + "saveNLoadTile_" + posX + "_" + posY + ".txt");
			checkIfExists();
		}
	}

	@Override
	public void interact() {
		super.interact();
		new File(folderName).mkdirs();
		int prefix = (new File(folderName).listFiles().length + 1);

		if (!fileExists) {
			frame.addLineToText("Spielstand wurde gespeichert.");
			world.save(folderName, prefix + "saveNLoadTile_" + posX + "_" + posY + ".txt");
			checkIfExists();
		} else {
			frame.addLineToText("Spielstand wurde geladen.");
			// remove later saves
			int index = Integer.parseInt(loadFile.getName().substring(0, loadFile.getName().indexOf("save")));
			for (File f : new File(folderName).listFiles()) {
				if (Integer.parseInt(f.getName().substring(0, f.getName().indexOf("save"))) > index) {
					f.delete();
				}
			}
			frame.swichLevel(loadFile.getAbsolutePath());

		}
	}

	private void checkIfExists() {

		if (new File(folderName).exists()) {
			for (File f : new File(folderName).listFiles()) {
				if (f.getName().endsWith("saveNLoadTile_" + posX + "_" + posY + ".txt")) {
					fileExists = true;
					loadFile = f;
				}
			}
		} else {
			fileExists = false;
		}
	}
	
	@Override
	public double getHeight() {
		return -1;
	}
}
