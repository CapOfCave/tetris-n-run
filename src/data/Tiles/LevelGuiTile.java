package data.Tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import data.Animation;
import graphics.GameFrame;
import loading.AnimationLoader;
import loading.ImageLoader;

public class LevelGuiTile extends Tile {

	boolean playerOn = false;
	HashMap<String, Animation> anims;
	Animation akt_anim;
	private BufferedImage imgD;

	public LevelGuiTile(char key, int posX, int posY, GameFrame frame) {
		super(key, posX, posY, true, true, false, frame);
		needsBackGround = true;
		img = ImageLoader.loadImage("/res/levelBackground/blockH" + key + ".png");
		imgD = ImageLoader.loadImage("/res/levelBackground/blockD" + key + ".png");

		anims = AnimationLoader.loadAnimations("/res/anims/lvlanim.txt");
		akt_anim = anims.get("close" + key);
	}

	public void eventWhenEntering() {

		frame.setNextLevel(key);
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

		frame.setNextLevel(' ');
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
			g.drawImage(akt_anim.getImage(), (int) (posX * GameFrame.BLOCKSIZE - world.cameraX()),
					(int) (posY * GameFrame.BLOCKSIZE - world.cameraY() - 79), null);
		else
			g.drawImage(akt_anim.getImage(), (int) (posX * GameFrame.BLOCKSIZE - world.cameraX()) - 45,
					(int) (posY * GameFrame.BLOCKSIZE - world.cameraY() - 180), null);

	}

	@Override
	public void drawBackground(Graphics g, float interpolation) {
		if (playerOn) {
			g.drawImage(img, (int) (posX * GameFrame.BLOCKSIZE - world.cameraX()),
					(int) (posY * GameFrame.BLOCKSIZE - world.cameraY()), null);

		} else {
			g.drawImage(imgD, (int) (posX * GameFrame.BLOCKSIZE - world.cameraX()),
					(int) (posY * GameFrame.BLOCKSIZE - world.cameraY()), null);
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
		frame.startLevel();
	}

	@Override
	public double getHeight() {
		return (posY - 1) * GameFrame.BLOCKSIZE;
	}
}
