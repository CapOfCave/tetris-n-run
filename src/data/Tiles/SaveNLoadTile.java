package data.Tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

import data.Animation;
import graphics.GameFrame;
import logics.World;

public class SaveNLoadTile extends Tile {

	BufferedImage image3dSaved;
	BufferedImage image3dUnSaved;

	Animation akt_animation;
	boolean fileExists = false;
	boolean isCreating = false;
	boolean addingTetros;
	File loadFile = null;
	static HashMap<String, Animation> all_animations = null;
	private String tip;
	private String tip2;
	private String tip3;
	private String tip4;

	private static final String folderName = System.getenv("APPDATA") + "\\tetris-n-run\\saves\\tmpSaves\\";

	public SaveNLoadTile(char key, int posX, int posY, int[] tetroAmount, boolean addingTetros, String tip, String tip2,
			String tip3, String tip4) {
		super(key, posX, posY, false, true, true);
		needsBackGround = true;

		this.tetroAmount = tetroAmount;
		this.addingTetros = addingTetros;
		this.tip = tip;
		this.tip2 = tip2;
		this.tip3 = tip3;
		this.tip4 = tip4;

	}

	@Override
	public void tick() {
		akt_animation.next();
	}

	@Override
	public void setWorld(World world) {
		super.setWorld(world);
		if (all_animations == null) {
			all_animations = world.loadAnimations("/res/anims/savenload.txt");
		}
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

		g.drawImage(akt_animation.getImage(),
				(int) (posX * GameFrame.BLOCKSIZE - world.cameraX() + akt_animation.getOffsetX()),
				(int) (posY * GameFrame.BLOCKSIZE - world.cameraY() + akt_animation.getOffsetY()), null);
		// TODO Tile-Render-Reihenfolge
	}

	@Override
	public void eventWhenEntering() {
		world.setLastCrossedSALTile(this);
		new File(folderName).mkdirs();
		int prefix = (new File(folderName).listFiles().length + 1);

		if (!fileExists && !isCreating) {
			world.initiateSaving(folderName + prefix + "saveNLoadTile_" + posX + "_" + posY + ".txt");
			isCreating = true;
			frame.addLineToText("Spielstand wurde gespeichert.");
			world.playSound("save", 0);
			if (addingTetros)
				world.addTetroAmount(tetroAmount);
			else
				world.setTetroAmount(tetroAmount);

			checkIfExists();
			akt_animation = all_animations.get("ac");
			world.setLastUsedSALTile(this);
		}
	}

	@Override
	public void interact() {
		super.interact();
		new File(folderName).mkdirs();
		if (fileExists) {
			frame.addLineToText("Spielstand wurde geladen.");
			// remove later saves

			int index = Integer.parseInt(loadFile.getName().substring(0, loadFile.getName().indexOf("save")));
			for (File f : new File(folderName).listFiles()) {
				if (Integer.parseInt(f.getName().substring(0, f.getName().indexOf("save"))) > index) {
					f.delete();
				}
			}
			frame.switchLevel(loadFile.getAbsolutePath(), this);

		} else if (isCreating) {
			checkIfExists();
			if (fileExists) {
				interact();
			} else {
				frame.addLineToText("Wenns n bug war sry"); // TODO remove :D
				frame.addLineToText("Bitte warte einige Sekunden, bevor du den Spielstand laden kannst.");
			}
		}
	}

	public void refreshTetros() {
		if (addingTetros)
			world.addTetroAmount(tetroAmount);

		else
			world.setTetroAmount(tetroAmount);
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

	public String getTip() {
		return tip;
	}

	public String getTip2() {
		return tip2;
	}

	public String getTip3() {
		return tip3;
	}

	public String getTip4() {
		return tip4;
	}
}
