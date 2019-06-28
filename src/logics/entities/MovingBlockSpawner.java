package logics.entities;

import java.awt.Graphics;

import graphics.GameFrame;
import logics.World;

public class MovingBlockSpawner extends Entity {

	
	private static final String animPath = "/res/anims/movingblockspawner.txt";
	
	private double cubeX;
	private double cubeY;
	
	private MovingBlock child;

	public MovingBlockSpawner(World world, double x, double y, double cx, double cy) {
		super(world, x, y, animPath, null);
		this.cubeX = cx;
		this.cubeY = cy;
		type = "moveblockspawner";
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void draw(Graphics g, float interpolation) {

		g.drawImage(akt_animation.getImage(), (int) x - world.cameraX() + akt_animation.getOffsetX(),
				(int) y - world.cameraY() + akt_animation.getOffsetY(), GameFrame.BLOCKSIZE, GameFrame.BLOCKSIZE, null);

	}

	@Override
	public void tick() {
		akt_animation.next();
	}

	@Override
	public void interact() {
		if (child != null) {
			child.kill();
			world.cameraTrackingShot((int) cubeX, (int) cubeY); // TODO remove
		} else {
			world.cameraTrackingShot((int) cubeX, (int) cubeY);

		}

		child = null;
		child = new MovingBlock(world, cubeX, cubeY);
		world.addEntity(child);

	}

	public double getCX() {
		return cubeX;
	}

	public double getCY() {
		return cubeY;
	}

}
