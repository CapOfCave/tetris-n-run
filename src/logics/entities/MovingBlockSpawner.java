package logics.entities;

import java.awt.Graphics;

import graphics.GameFrame;
import logics.worlds.World;

public class MovingBlockSpawner extends Entity {

	private double cubeX;
	private double cubeY;
	private String cubeAnims;

	private MovingBlock child;

	public MovingBlockSpawner(World world, double x, double y, String animPath, double cx, double cy,
			String cubeAnims) {
		super(world, x, y, animPath, null);
		this.cubeX = cx;
		this.cubeY = cy;
		this.cubeAnims = cubeAnims;
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
		if (child != null)
			child.kill();
		child = null;
		child = new MovingBlock(world, cubeX, cubeY, cubeAnims);
		world.addEntity(child);
		
	}

	public double getCX() {
		return cubeX;
	}

	public double getCY() {
		return cubeY;
	}

	public String getCurl() {
		return cubeAnims;
	}

}
