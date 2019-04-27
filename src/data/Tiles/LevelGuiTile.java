package data.Tiles;

import java.awt.Graphics;
import java.util.HashMap;

import data.Animation;
import graphics.GameFrame;
import loading.AnimationLoader;
import loading.ImageLoader;

public class LevelGuiTile extends Tile {

	boolean playerOn = false;
	HashMap<String, Animation> anims;
	Animation akt_anim;

	public LevelGuiTile(char key, int posX, int posY, GameFrame frame) {
		super(key, posX, posY, true, true, false, frame);
		needsBackGround = true;
		img = ImageLoader.loadImage("/res/blocks/block" + key + ".png");
		anims = AnimationLoader.loadAnimations("/res/anims/lvlanima.txt");
		akt_anim = anims.get("closea");
	}

	public void eventWhenEntering() {

		frame.setNextLevel(key);
		playerOn = true;
		if (akt_anim == anims.get("closinga")) {
			int index = akt_anim.getFrameAmount() - akt_anim.getAktIndex();
			System.out.println(index + ", " + akt_anim.getFrameAmount() + ", " + akt_anim.getAktIndex());
			akt_anim.reset();
			akt_anim = anims.get("openinga");
			akt_anim.setFrame(index);
		} else {
			akt_anim.reset();
			akt_anim = anims.get("openinga");
		}
	}

	public void eventWhenLeaving() {

		frame.setNextLevel(' ');
		playerOn = false;
//		akt_anim.reset();
//		akt_anim = anims.get("closinga");
		if (akt_anim == anims.get("openinga")) {
			int index = akt_anim.getFrameAmount() - akt_anim.getAktIndex();
			akt_anim.reset();
			akt_anim = anims.get("closinga");
			akt_anim.setFrame(index);
		} else {
			akt_anim.reset();
			akt_anim = anims.get("closinga");
		}
	}

	@Override
	public void drawBackground(Graphics g, float interpolation) {
//		if (playerOn) {
		g.drawImage(akt_anim.getImage(), (int) (posX * GameFrame.BLOCKSIZE - world.cameraX()),
				(int) (posY * GameFrame.BLOCKSIZE - world.cameraY() - 79), null);

//		} else {
//			g.drawImage(img, (int) (posX * GameFrame.BLOCKSIZE - world.cameraX()),
//					(int) (posY * GameFrame.BLOCKSIZE - world.cameraY() - 79), null);
//		}

	}

	@Override
	public void tick() {
		akt_anim.next();
		if (akt_anim == anims.get("openinga") || akt_anim == anims.get("closinga")) {
			if (akt_anim.isFinished()) {
				if (playerOn) {
					akt_anim.reset();
					akt_anim = anims.get("opena");
				} else {
					akt_anim.reset();
					akt_anim = anims.get("closea");
				}
			}
		}
	}

	@Override
	public void interact() {
		frame.startLevel();
	}
}
