package logics;

import logics.worlds.World;

/**
 * @author Lars Created on 05.08.2018
 */
public class Camera {
	private double x, y;
	private int lastX, lastY;
	private int drawX, drawY;
	private int maxY;
	private int maxX;
	private int trackingShotPhase = 0; // 0: Normal; 1: Hinweg; 2: Bleiben; 3: Rückweg
	private int trackX = 0;
	private int trackY = 0;

	private double stickyness = .3; // höher -> spieler näher an der Mitte
	private int offsetX;
	private int offsetY;
	private World world;

	private final double ACC1 = 4;
	private final double MAX1 = 35;
	private final double BRAKE1 = 5;

	private final int BLOCK_FOCUS_TICKS = 10;

	private final double ACC2 = 5;
	private final double MAX2 = 50;
	private final double BRAKE2 = 7;

	private double speed = 0;
	private double angle = Double.MAX_VALUE;

	private final double stopping_distance1 = MAX1 * MAX1 / 2. / BRAKE1;
	private final double stopping_distance2 = MAX2 * MAX2 / 2. / BRAKE2;
	private int ticks_passed;

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

	public void tick() {
		lastX = (int) x;
		lastY = (int) y;
		if (trackingShotPhase == 0) {
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
				y = clipBorderY((world.getPlayer().getY() - offsetY) * stickyness + lastY * (1 - stickyness));
				x = clipBorderX((world.getPlayer().getX() - offsetX) * stickyness + lastX * (1 - stickyness));
			}
		} else {
			switch (trackingShotPhase) {
			case 1:
				speed += ACC1;
				if (speed > MAX1) {
					speed = MAX1;
					trackingShotPhase = 2;
				}

			case 2: // Max. Speed erreicht, jetzt bremsen
				double dist = Math.sqrt((x - trackX) * (x - trackX) + (y - trackY) * (y - trackY));
				if (dist < stopping_distance1) {
					speed -= BRAKE1;
					if (speed < 0) {
						speed = 0;
						trackingShotPhase = 3;
						ticks_passed = 0;

					}
				}
				break;

			case 3:
				ticks_passed++;
				if (ticks_passed > BLOCK_FOCUS_TICKS) {
//					angle += Math.PI;
					trackingShotPhase = 4;
				}
				break;
			case 4:
				speed += ACC2;
				if (speed > MAX2) {
					speed = MAX2;
					trackingShotPhase = 5;
				}

			case 5:

				dist = Math.sqrt((x - clipBorderX(world.getPlayer().getX() - offsetX))
						* (x - clipBorderX(world.getPlayer().getX() - offsetX))
						+ (y - clipBorderY(world.getPlayer().getY() - offsetY))
								* (y - clipBorderY(world.getPlayer().getY() - offsetY)));

				angle = Math.acos((clipBorderX(world.getPlayer().getX() - offsetX) - x) / dist);
				System.out.println((clipBorderX(world.getPlayer().getX() - offsetX) - x) + " ... " + dist);
				if (clipBorderY(world.getPlayer().getY() - offsetY) - y < 0) {
					angle = Math.PI * 2 - angle;
				}
				if (dist < stopping_distance2) {
					speed -= BRAKE2;
					if (speed > dist) {
						speed = dist;
						trackingShotPhase = 6;
					} else if (speed <= 0) {
						speed = 0;
						trackingShotPhase = 6;

					}
				}
				break;
			case 6:
				speed = 0;
				trackingShotPhase = 0;
				break;
			}
			if (speed > 0) {
				y += speed * Math.sin(angle);
				System.out.println("X: " + x + "; angle=" + angle);
				x += speed * Math.cos(angle);
			}

//			y = clipBorderY(y + speed * Math.sin(angle));
//			x = clipBorderX(x + speed * Math.cos(angle));
		}

	}

	public void prepareDraw(float interpolation) {
		drawX = (int) ((x - lastX) * interpolation + lastX);
		drawY = (int) ((y - lastY) * interpolation + lastY);
	}

	private int clipBorderX(double x) {
		return (int) Math.max(0, Math.min(maxX, x));
//		return (int)x;
	}

	private int clipBorderY(double y) {
		return (int) Math.max(0, Math.min(maxY, y));
//		return (int) y;
	}

	public void trackingShot(int pX, int pY) {
		if (trackingShotPhase == 0) {
			trackingShotPhase = 1;

//			stickyness = .04;
			trackX = clipBorderX(pX - offsetX);
			trackY = clipBorderY(pY - offsetY);

			System.out.println("(" + x + "|" + y + ")   " + "(   " + trackX + "|" + trackY + ")");
			System.out.println((trackY - y) + " / " + (trackX - x));
			System.out.println(Math.toDegrees(Math.atan((double) (trackY - y) / (trackX - x))));

			angle = Math.acos((trackX - x) / Math.sqrt((trackY - y) * (trackY - y) + (trackX - x) * (trackX - x)));
			if ((trackY - y) < 0) {
				angle = Math.PI * 2 - angle;
			}
//			angle = Math.atan((double) (trackY - y) / (trackX - x));

			System.out.println(Math.toDegrees(angle));
		}

	}

}
