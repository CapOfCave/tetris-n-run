package logics.entities;

import java.awt.image.BufferedImage;

import data.Tiles.Tile;
import logics.Camera;


/**
 * @author Lars Created on 18.09.2018
 */
public abstract class MovingEntity extends Entity {

	protected double maxX, maxY;
	protected boolean[][] tetroWorldHitbox;
	protected Tile[][] tileWorld;
	

	protected double lastX, lastY;
	protected double hSpeed;
	protected double vSpeed;
	protected int health;

	protected double acc;
	protected double brake;
	protected double maxSpeed;

	protected Camera camera;
	protected boolean wantsToGoUp = false;
	protected boolean wantsToGoDown = false;
	protected boolean wantsToGoLeft = false;
	protected boolean wantsToGoRight = false;

	protected double edgeTolerancePercentage = 25;

	public MovingEntity(BufferedImage img, int blockSize, Camera camera, boolean[][] tetroWorldHitbox, Tile[][] tileWorld) {
		super(img, blockSize);
		this.camera = camera;
		this.tetroWorldHitbox = tetroWorldHitbox;
		this.tileWorld = tileWorld;

		maxX = (tileWorld[0].length * blockSize);
		maxY = (tileWorld.length * blockSize);
	}

	public MovingEntity(BufferedImage img, int blockSize, Camera camera, boolean[][] tetroWorldHitbox, Tile[][] tileWorld, int x, int y) {
		super(img, blockSize, x, y);
		this.camera = camera;
		this.tetroWorldHitbox = tetroWorldHitbox;
		this.tileWorld = tileWorld;

		maxX = (tileWorld[0].length * blockSize) - blockSize;
		maxY = (tileWorld.length * blockSize) - blockSize;
	}

	@Override
	public void tick() {
		lastX = x;
		lastY = y;
	}

	protected void move() {
		accelerate();

		checkCollisions();

		checkMaxSpeed();

		updateRotation();

		x += hSpeed;
		y += vSpeed;

	}

	private void accelerate() {
		double abs_hSpeed = Math.abs(hSpeed);
		if (wantsToGoLeft && !wantsToGoRight) {
			hSpeed -= acc;
			if (hSpeed > 0) {
				hSpeed -= brake;
			}
		} else if (!wantsToGoLeft && wantsToGoRight) {
			hSpeed += acc;
			if (hSpeed < 0) {
				hSpeed += brake;
			}
		} else if (abs_hSpeed > 0.001) {
			abs_hSpeed -= brake;
			if (abs_hSpeed < 0) {
				abs_hSpeed = 0;
			}
			hSpeed = hSpeed / Math.abs(hSpeed) * abs_hSpeed;
		} else {
			hSpeed = 0;
		}

		// Unten-Oben-Movement
		double abs_vSpeed = Math.abs(vSpeed);
		if (wantsToGoUp && !wantsToGoDown) {
			vSpeed -= acc;
			if (vSpeed > 0) {
				vSpeed -= brake;
			}
		} else if (!wantsToGoUp && wantsToGoDown) {
			vSpeed += acc;
			if (vSpeed < 0) {
				vSpeed += brake;
			}
		} else if (abs_vSpeed > 0.001) {
			abs_vSpeed -= brake;
			if (abs_vSpeed < 0) {
				abs_vSpeed = 0;
			}
			vSpeed = vSpeed / Math.abs(vSpeed) * abs_vSpeed;
		} else {
			vSpeed = 0;
		}

	}

	private void checkMaxSpeed() {
		double gesSpeed = Math.sqrt(hSpeed * hSpeed + vSpeed * vSpeed);
		if (gesSpeed > maxSpeed) {
			double factor = maxSpeed / gesSpeed;
			hSpeed = hSpeed * factor;
			vSpeed = vSpeed * factor;
		}

	}

	private void checkCollisions() {
		// Vertikal
		// nach oben-movement (TL-TR)
		if (vSpeed < 0) {
			// linker edgecut
			if (!isRelAccessible(-blockSize / 2 + vSpeed, -blockSize / 2)) {
				if (isRelAccessible(-blockSize / 2 + vSpeed, -blockSize / 2 + edgeTolerancePercentage * blockSize / 100) && !wantsToGoLeft) {
					// TODO edgecut
				} else {
					vSpeed = 0;
					move_contact_solid(0);
				}

			}

			// rechter edgecut
			if (!isRelAccessible(-blockSize / 2 + vSpeed, blockSize / 2 - 1)) {
				if (isRelAccessible(-blockSize / 2 + vSpeed, blockSize / 2 - 1 - edgeTolerancePercentage * blockSize / 100) && !wantsToGoRight) {

				} else {
					vSpeed = 0;
					move_contact_solid(0);
				}
			}
		}
		// nach unten-movement (BL-BR)
		if (vSpeed > 0) {
			if (!isRelAccessible(blockSize / 2 - 1 + vSpeed, -blockSize / 2)) {
				if (isRelAccessible(blockSize / 2 - 1 + vSpeed, -blockSize / 2 + edgeTolerancePercentage * blockSize / 100) && !wantsToGoLeft) {

				} else {
					vSpeed = 0;
					move_contact_solid(2);
				}
			}
			if (!isRelAccessible(blockSize / 2 - 1 + vSpeed, blockSize / 2 - 1)) {
				if (isRelAccessible(blockSize / 2 - 1 + vSpeed, blockSize / 2 - 1 - edgeTolerancePercentage * blockSize / 100) && !wantsToGoRight) {

				} else {
					vSpeed = 0;
					move_contact_solid(2);
				}

			}

		}
		// Horizontal
		// nach links-movement (TL-BL)
		if (hSpeed < 0) {
			if (!isRelAccessible(-blockSize / 2, -blockSize / 2 + hSpeed)) {
				if (isRelAccessible(-blockSize / 2 + edgeTolerancePercentage * blockSize / 100, -blockSize / 2 + hSpeed) && !wantsToGoUp) {

				} else {
					hSpeed = 0;
					move_contact_solid(3);
				}
			}
			if (!isRelAccessible(blockSize / 2 - 1, -blockSize / 2 + hSpeed)) {
				if (isRelAccessible(blockSize / 2 - 1 - edgeTolerancePercentage * blockSize / 100, -blockSize / 2 + hSpeed) && !wantsToGoDown) {

				} else {
					hSpeed = 0;
					move_contact_solid(3);
				}

			}

		}

		// nach rechts-movement (TR-BR)
		if (hSpeed > 0) {
			if (!isRelAccessible(-blockSize / 2, blockSize / 2 - 1 + hSpeed)) {
				if (isRelAccessible(-blockSize / 2 + edgeTolerancePercentage * blockSize / 100, blockSize / 2 - 1 + hSpeed) && !wantsToGoUp) {

				} else {
					hSpeed = 0;
					move_contact_solid(1);
				}
			}
			if (!isRelAccessible(blockSize / 2 - 1, blockSize / 2 - 1 + hSpeed)) {
				if (isRelAccessible(blockSize / 2 - 1 - edgeTolerancePercentage * blockSize / 100, blockSize / 2 - 1 + hSpeed) && !wantsToGoDown) {

				} else {
					hSpeed = 0;
					move_contact_solid(1);
				}
			}
		}

	}

	private void updateRotation() {

		// Wenn Gegensätze gedrückt werden
		if (Math.abs(hSpeed) > Math.abs(vSpeed)) {
			rotation = -90 * ((int) Math.copySign(1, hSpeed) - 2);
		} else if (Math.abs(hSpeed) < Math.abs(vSpeed)) {
			rotation = (int) (90 * (Math.copySign(1, vSpeed) + 1));
		}

		if (wantsToGoUp && !wantsToGoLeft && !wantsToGoDown && !wantsToGoRight) {
			rotation = 0; // oben
		} else if (!wantsToGoUp && wantsToGoLeft && !wantsToGoDown && !wantsToGoRight) {
			rotation = 270; // links
		} else if (!wantsToGoUp && !wantsToGoLeft && wantsToGoDown && !wantsToGoRight) {
			rotation = 180; // unten
		} else if (!wantsToGoUp && !wantsToGoLeft && !wantsToGoDown && wantsToGoRight) {
			rotation = 90; // rechts
		}
		if (wantsToGoUp && wantsToGoLeft && !wantsToGoDown && !wantsToGoRight) {
			rotation = 315; // oben links
		} else if (!wantsToGoUp && wantsToGoLeft && wantsToGoDown && !wantsToGoRight) {
			rotation = 225; // unten links
		} else if (!wantsToGoUp && !wantsToGoLeft && wantsToGoDown && wantsToGoRight) {
			rotation = 135; // unten rechts
		} else if (wantsToGoUp && !wantsToGoLeft && !wantsToGoDown && wantsToGoRight) {
			rotation = 45; // oben rechts
		}
	}

	private void move_contact_solid(int i) {
		switch (i) {
		case 0:
			y = getTileY() * blockSize;
			break;
		case 1:
			x = getTileX() * blockSize;
			break;
		case 2:
			y = getTileY() * blockSize;
			break;
		case 3:
			x = getTileX() * blockSize;
			break;
		}

	}

	private int getTileX(double dx) {
		return (int) ((x + dx + blockSize / 2) / blockSize);
	}

	private int getTileY(double dy) {
		return (int) ((y + dy + blockSize / 2) / blockSize);
	}

	public int getTileX() {
		return (int) ((x + blockSize / 2) / blockSize);
	}

	public int getTileY() {
		return (int) ((y + blockSize / 2) / blockSize);
	}

	private boolean isRelAccessible(double dy, double dx) {

		if ((x + blockSize / 2 + dx) >= maxX || (y + blockSize / 2 + dy) >= maxY || (x + blockSize / 2 + dx) < 0 || (y + blockSize / 2 + dy) < 0) {
			return false;
		}

		return tetroWorldHitbox[getTileY(dy)][getTileX(dx)];
	}
}
