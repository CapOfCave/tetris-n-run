package logics.entities;

import java.awt.Graphics;

import graphics.GameFrameHandler;
import logics.World;

public class MovingBlockSpawner extends Entity {

	private static final String animPath = "/res/anims/movingblockspawner.txt";

	private double cubeX;
	private double cubeY;

	private int currentCubeX;
	private int currentCubeY;

	private MovingBlock child;

	public MovingBlockSpawner(World world, double x, double y, double cx, double cy, int currentCubeX,
			int currentCubeY) {
		super(world, x, y, animPath, null);
		this.cubeX = cx;
		this.cubeY = cy;
		this.currentCubeX = currentCubeX;
		this.currentCubeY = currentCubeY;
		type = "moveblockspawner";
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void draw(Graphics g, float interpolation) {

		g.drawImage(akt_animation.getImage(), (int) x - world.cameraX() + akt_animation.getOffsetX(),
				(int) y - world.cameraY() + akt_animation.getOffsetY(), GameFrameHandler.BLOCKSIZE, GameFrameHandler.BLOCKSIZE, null);

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
		child = new MovingBlock(world, cubeX, cubeY);
		world.initiateAddEntity(child);

	}

	public double getCX() {
		return cubeX;
	}

	public double getCY() {
		return cubeY;
	}

	public int getCurrentCubeY() {
		if (child == null) {
			return -1000;
		}
		return (int) child.getY();
	}

	public int getCurrentCubeX() {
		if (child == null) {
			return -1000;
		}
		return (int) child.getX();
	}

	public void initMoveBlock() {
		if (currentCubeX != -1000 && currentCubeY != -1000) {
			child = new MovingBlock(world, currentCubeX, currentCubeY);
			world.addEntityDirectly(child);
		}

	}


}
