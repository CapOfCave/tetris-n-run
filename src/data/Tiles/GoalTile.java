package data.Tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import data.RawPlayer;
import graphics.Frame;
import loading.ImageLoader;

public class GoalTile extends Tile {

	private BufferedImage image3d;

	public GoalTile(int posX, int posY, Frame frame) {
		super('!', posX, posY, false, true, frame);
		image3d = ImageLoader.loadImage("/res/goal.png");
	}

	@Override
	public void interact() {
		frame.changeToOverworld(false, new RawPlayer(world.getPlayer().getAcc(), world.getPlayer().getBrake(),
				world.getPlayer().getMaxSpeed(), world.getPlayer().getHealth(), world.getPlayer().getInventory()));
		world.getPlayer().resetActionPressed();
	}

	@Override
	public void draw(Graphics g, float interpolation) {
		g.drawImage(image3d, (int) (posX * Frame.BLOCKSIZE - world.cameraX()),
				(int) (posY * Frame.BLOCKSIZE - world.cameraY()), null);
	}

}