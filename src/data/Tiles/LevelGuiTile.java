package data.Tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import data.Animation;
import graphics.GameFrameHandler;
import graphics.OverworldPanel;
import logics.World;

public class LevelGuiTile extends Tile {

	boolean playerOn = false;
	HashMap<String, Animation> anims;
	Animation akt_anim;
	private BufferedImage imgD;
	private OverworldPanel panel;

	public LevelGuiTile(char key, int posX, int posY) {
		super(key, posX, posY, true, true, false);
		needsBackGround = true;

	}

	@Override
	public void setWorld(World world) {
		super.setWorld(world);
		
		img = world.getImage("/res/levelBackground/blockH" + key + ".png");
		imgD = world.getImage("/res/levelBackground/blockD" + key + ".png");
		anims = world.loadAnimations("/res/anims/lvlanim.txt");
		akt_anim = anims.get("close" + key);
	}
	
	@Override
	public void setFrame(GameFrameHandler gameFrameHandler) {
		super.setFrame(gameFrameHandler);
		panel = gameFrameHandler.getOPanel();
	}

	public void eventWhenEntering() {

		panel.setNextLevel(key);
		playerOn = true;
		if (akt_anim == anims.get("closing" + key)) {
			int index = akt_anim.getFrameAmount() - akt_anim.getAktIndex();
			akt_anim.reset();
			akt_anim = anims.get("opening" + key);
			akt_anim.setFrame(index);
		} else {
			akt_anim.reset();
			akt_anim = anims.get("opening" + key);
		}
	}

	public void eventWhenLeaving() {

		panel.setNextLevel(' ');
		playerOn = false;
		// akt_anim.reset();
		// akt_anim = anims.get("closinga");
		if (akt_anim == anims.get("opening" + key)) {
			int index = akt_anim.getFrameAmount() - akt_anim.getAktIndex();
			akt_anim.reset();
			akt_anim = anims.get("closing" + key);
			akt_anim.setFrame(index);
		} else {
			akt_anim.reset();
			akt_anim = anims.get("closing" + key);
		}
	}

	@Override
	public void draw(Graphics g, float interpolation) {
		if (key != 'h')
			g.drawImage(akt_anim.getImage(), (int) (posX * GameFrameHandler.BLOCKSIZE - world.cameraX()),
					(int) (posY * GameFrameHandler.BLOCKSIZE - world.cameraY() - 79), null);
		else
			g.drawImage(akt_anim.getImage(), (int) (posX * GameFrameHandler.BLOCKSIZE - world.cameraX()) - 45,
					(int) (posY * GameFrameHandler.BLOCKSIZE - world.cameraY() - 180), null);

	}

	@Override
	public void drawBackground(Graphics g, float interpolation) {
		if (playerOn) {
			g.drawImage(img, (int) (posX * GameFrameHandler.BLOCKSIZE - world.cameraX()),
					(int) (posY * GameFrameHandler.BLOCKSIZE - world.cameraY()), null);

		} else {
			g.drawImage(imgD, (int) (posX * GameFrameHandler.BLOCKSIZE - world.cameraX()),
					(int) (posY * GameFrameHandler.BLOCKSIZE - world.cameraY()), null);
		}

	}

	@Override
	public void tick() {
		akt_anim.next();
		if (akt_anim == anims.get("opening" + key) || akt_anim == anims.get("closing" + key)) {
			if (akt_anim.isFinished()) {
				if (playerOn) {
					akt_anim.reset();
					akt_anim = anims.get("open" + key);
				} else {
					akt_anim.reset();
					akt_anim = anims.get("close" + key);
				}
			}
		}
	}

	@Override
	public void interact() {
		panel.startLevel();
	}

	@Override
	public double getHeight() {
		return (posY - 1) * GameFrameHandler.BLOCKSIZE;
	}
}
