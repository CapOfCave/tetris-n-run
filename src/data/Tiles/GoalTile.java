package data.Tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import data.RawPlayer;
import graphics.GameFrame;
import loading.ImageLoader;

public class GoalTile extends Tile {

	private BufferedImage image3d;

	public GoalTile(int posX, int posY, GameFrame frame) {
		super('!', posX, posY, false, true, true, frame);
		needsBackGround = true;
		image3d = ImageLoader.loadImage("/res/goal.png");
	}

	@Override
	public void interact() {
		frame.changeToOverworld(false, new RawPlayer(world.getPlayer().getAcc(), world.getPlayer().getBrake(),
				world.getPlayer().getMaxSpeed()));
		world.getPlayer().resetActionPressed();
	}

	@Override
	public void draw(Graphics g, float interpolation) {
		g.drawImage(image3d, (int) (posX * GameFrame.BLOCKSIZE - world.cameraX()),
				(int) (posY * GameFrame.BLOCKSIZE - world.cameraY()), null);
	}
	
	@Override
	public double getHeight() {
		return -1;
	}

}