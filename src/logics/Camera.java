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
	private boolean inTrackingShot = false;
	private int trackX = 0;
	private int trackY = 0;

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
		if (!inTrackingShot) {
			lastX = x;
			lastY = y;

			if (world.getKeyHandler().getKameraKey()) {
				if (world.getKeyHandler().getUpKey()) {
					y = clipBorderY(y - 15);
				}
				if (world.getKeyHandler().getLeftKey()) {
					x = clipBorderX(x - 15);
				}
				if (world.getKeyHandler().getDownKey()) {
					y = clipBorderY(y + 15);
				}
				if (world.getKeyHandler().getRightKey()) {
					x = clipBorderX(x + 15);
				}

			} else {
				y = clipBorderY((pY - offsetY) * stickyness + lastY * (1 - stickyness));
				x = clipBorderX((pX - offsetX) * stickyness + lastX * (1 - stickyness));
			}
		} else {
			System.out.println(trackX - x - offsetX + " " + (trackY - y - offsetY));
			if (trackY - y - offsetY < 100 && trackX - x - offsetX < 100) {

				inTrackingShot = false;
				stickyness = .3;
			} else {
				lastX = x;
				lastY = y;

				y = clipBorderY((trackY - offsetY) * stickyness + lastY * (1 - stickyness));
				x = clipBorderX((trackX - offsetX) * stickyness + lastX * (1 - stickyness));
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

	public void trackingShot(int x, int y) {
		if (!inTrackingShot) {
			inTrackingShot = true;
			stickyness = .04;
			System.out.println(x);
			trackX = x;
			trackY = y;

		}

	}

}
