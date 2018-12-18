package logics;

import logics.worlds.World;

/**
 * @author Lars Created on 05.08.2018
 */
public class Camera {
	private int x, y;
	private int lastX, lastY;
	private int drawX, drawY;
	private int maxY;
	private int maxX;

	private double stickyness = .3; // höher -> spieler näher an der Mitte
	private int offsetX;
	private int offsetY;
	private World world;

	public Camera(World world, int x, int y, int maxY, int maxX, int offsetX, int offsetY) {
		this.world = world;
		this.maxX = maxX;
		this.maxY = maxY;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.x = clipBorderX(x - offsetX);
		this.y = clipBorderY(y - offsetY);
		this.lastX = clipBorderX(x - offsetX);
		this.lastY = clipBorderY(y - offsetY);

	}

	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}

	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}

	public int getX() {
		return drawX;
	}

	public int getY() {
		return drawY;
	}

	public void tick(double pX, double pY) {
		lastX = x;
		lastY = y;

		if (world.getKeyHandler().getCtrl()) {
			if (world.getKeyHandler().getW()) {
				y = clipBorderY(y - 15);
			}
			if (world.getKeyHandler().getA()) {
				x = clipBorderX(x - 15);
			}
			if (world.getKeyHandler().getS()) {
				y = clipBorderY(y + 15);
			}
			if (world.getKeyHandler().getD()) {
				x = clipBorderX(x + 15);
			}
			
		} else {
			if (!world.getKeyHandler().getShift()) {
				y = clipBorderY((pY - offsetY) * stickyness + lastY * (1 - stickyness));
				x = clipBorderX((pX - offsetX) * stickyness + lastX * (1 - stickyness));
			}
		}
	}

	public void prepareDraw(float interpolation) {
		drawX = (int) ((x - lastX) * interpolation + lastX);
		drawY = (int) ((y - lastY) * interpolation + lastY);
	}

	private int clipBorderX(double x) {
		return (int) Math.max(0, Math.min(maxX, x));
	}

	private int clipBorderY(double y) {
		return (int) Math.max(0, Math.min(maxY, y));
	}

}
