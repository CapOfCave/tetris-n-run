package logics.entities;

import java.awt.Graphics;
import java.util.HashMap;

import data.Animation;
import graphics.Frame;
import logics.worlds.World;

public class CubeSpawner extends Entity {

	private double cubeX;
	private double cubeY;
	private HashMap<String, Animation> cubeAnims;

	private MovingBlock child;

	public CubeSpawner(World world, double x, double y, HashMap<String, Animation> anims, double cx, double cy,
			HashMap<String, Animation> cubeAnims) {
		super(world, x, y, anims);
		this.cubeX = cx;
		this.cubeY = cy;
		this.cubeAnims = cubeAnims;
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void draw(Graphics g, float interpolation) {

		g.drawImage(akt_animation.getImage(), (int) x - world.cameraX() + akt_animation.getOffsetX(),
				(int) y - world.cameraY() + akt_animation.getOffsetY(), Frame.BLOCKSIZE, Frame.BLOCKSIZE, null);

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

}
