package logics.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;

import data.Animation;
import graphics.Frame;
import logics.worlds.World;

public class MovingBlock extends Entity {

	private static final long serialVersionUID = 1L;
	protected double lastX, lastY;
	protected boolean sticky = false;
	protected Point offset = new Point(0, 0);
	protected int direction;

	public MovingBlock(World world, double x, double y, HashMap<String, Animation> anims) {
		super(world, x, y, anims);
		lastX = x;
		lastY = y;
	}

	@Override
	public void draw(Graphics g, float interpolation) {
		int interpolX = (int) ((x - lastX) * interpolation + lastX);
		int interpolY = (int) ((y - lastY) * interpolation + lastY);

		g.drawImage(akt_animation.getImage(), interpolX - world.cameraX() + akt_animation.getOffsetX(),
				interpolY - world.cameraY() + akt_animation.getOffsetY(), null);

	}

	@Override
	public void drawDebug(Graphics g, float interpolation) {
		int interpolX = (int) ((x - lastX) * interpolation + lastX);
		int interpolY = (int) ((y - lastY) * interpolation + lastY);
		if (sticky) {
			g.setColor(Color.GREEN);
		} else {
			g.setColor(Color.RED);
		}

		g.drawRect(interpolX - world.cameraX(), interpolY - world.cameraY(), Frame.BLOCKSIZE, Frame.BLOCKSIZE);
	}

	@Override
	public void tick() {
		akt_animation.next();
	}

	public void interact() {
		world.getPlayer().setMovingBlock(this);
		offset = world.getPlayer().getMovingBlockOffset();
		sticky = true;
		direction = world.getPlayer().getDirection();
	}

	public void setPosition(double x, double y) {
		lastX = this.x;
		lastY = this.y;

		this.x = x + offset.x;
		this.y = y + offset.y;

	}

	public void unBind() {
		sticky = false;

	}

	public int getDirection() {
		return direction;
	}

}
