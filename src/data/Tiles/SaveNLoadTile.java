package data.Tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

import data.Animation;
import graphics.GameFrame;
import loading.AnimationLoader;
import loading.ImageLoader;
import logics.worlds.World;

public class SaveNLoadTile extends Tile {

	BufferedImage image3dSaved;
	BufferedImage image3dUnSaved;

	Animation akt_animation;
	boolean fileExists = false;
	boolean addingTetros;
	File loadFile = null;
	static HashMap<String, Animation> all_animations = null;

	private static final String folderName = System.getenv("APPDATA") + "\\tetris-n-run\\levelSaves\\tmpSaves\\";

	public SaveNLoadTile(char key, int posX, int posY, GameFrame frame, int[] tetroAmount, boolean addingTetros) {
		super(key, posX, posY, false, true, true, frame);

		image3dSaved = ImageLoader.loadImage("/res/blocks/saveNLoadAc.png");
		image3dUnSaved = ImageLoader.loadImage("/res/blocks/saveNLoadDeac.png");

		this.tetroAmount = tetroAmount;
		this.addingTetros = addingTetros;

		if (all_animations == null) {
			all_animations = AnimationLoader.loadAnimations("/res/anims/savenload.txt");
		}
	}

	@Override
	public void tick() {
		akt_animation.next();
	}

	@Override
	public void setWorld(World world) {
		super.setWorld(world);
		checkIfExists();
		if (fileExists) {
			akt_animation = all_animations.get("ac");
		} else {
			akt_animation = all_animations.get("deac");
		}
	}

	@Override
	public void draw(Graphics g, float interpolation) {
//		if (fileExists) {
//			g.drawImage(image3dSaved, (int) (posX * GameFrame.BLOCKSIZE - world.cameraX()),
//					(int) (posY * GameFrame.BLOCKSIZE - world.cameraY()), null);
//		} else {
//			g.drawImage(image3dUnSaved, (int) (posX * GameFrame.BLOCKSIZE - world.cameraX()),
//					(int) (posY * GameFrame.BLOCKSIZE - world.cameraY()), null);
//		} TODO remove since not needed

		g.drawImage(akt_animation.getImage(), (int) (posX * GameFrame.BLOCKSIZE - world.cameraX()),
				(int) (posY * GameFrame.BLOCKSIZE - world.cameraY()), null);

	}

	@Override
	public void eventWhenEntering() {

		new File(folderName).mkdirs();
		int prefix = (new File(folderName).listFiles().length + 1);

		if (!fileExists) {
			frame.addLineToText("Spielstand wurde gespeichert.");
			world.playSound("save", 0);
			if (addingTetros)
				world.addTetroAmount(tetroAmount);
			else
				world.setTetroAmount(tetroAmount);
			world.save(folderName, prefix + "saveNLoadTile_" + posX + "_" + posY + ".txt");
			checkIfExists();
			akt_animation = all_animations.get("ac");
		}
	}

	@Override
	public void interact() {
		super.interact();
		new File(folderName).mkdirs();
		int prefix = (new File(folderName).listFiles().length + 1);

		if (!fileExists) {
			frame.addLineToText("Spielstand wurde gespeichert.");
			if (addingTetros)
				world.addTetroAmount(tetroAmount);

			else
				world.setTetroAmount(tetroAmount);
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
