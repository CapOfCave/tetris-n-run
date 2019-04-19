package data.Tiles;

import java.awt.Graphics;

import data.Animation;
import graphics.GameFrame;
import loading.AnimationLoader;
import loading.ImageLoader;

public class LevelGuiTile extends Tile {

	boolean playerOn = false;
	Animation akt_anim;
	

	public LevelGuiTile(char key, int posX, int posY, GameFrame frame) {
		super(key, posX, posY, true, true, false, frame);
		needsBackGround = true;
		img = ImageLoader.loadImage("/res/blocks/block" + key + ".png");
		if (key == 'a') {
			akt_anim = AnimationLoader.loadAnimations("/res/anims/lvlanima.txt").get("anim");
		}
	}

	public void eventWhenEntering() {

		frame.setNextLevel(key);
		playerOn = true;
	}

	public void eventWhenLeaving() {

		frame.setNextLevel(' ');
		playerOn = false;
	}

	@Override
	public void drawBackground(Graphics g, float interpolation) {
		if (key == 'a') {
			if (playerOn) {
				g.drawImage(akt_anim.getImage(), (int) (posX * GameFrame.BLOCKSIZE - world.cameraX()),
						(int) (posY * GameFrame.BLOCKSIZE - world.cameraY()), null);

			} else {
				g.drawImage(img, (int) (posX * GameFrame.BLOCKSIZE - world.cameraX()),
						(int) (posY * GameFrame.BLOCKSIZE - world.cameraY()), null);
			}
		} else {
			super.drawBackground(g, interpolation);
		}

	}

	@Override
	public void tick() {
		if (key == 'a') {
			if (playerOn)
				akt_anim.next();
		}
	}

	@Override
	public void interact() {
		frame.startLevel();
	}
}
