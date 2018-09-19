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

	public MovingEntity(BufferedImage img, int blockSize, Camera camera, boolean[][] tetroWorldHitbox, Tile[][] tileWorld) {
		super(img, blockSize);
		this.camera = camera;
		this.tetroWorldHitbox = tetroWorldHitbox;
		this.tileWorld = tileWorld;

		maxX = (tileWorld[0].length * blockSize) - blockSize;
		maxY = (tileWorld.length * blockSize) - blockSize;
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
		// Beginn der Bewegung

		// Rechts-Links-Movement
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

		// Vertikal
		// nach oben-movement (TL-TR)
		if (vSpeed < 0)
			if (!isRelAccessible(-blockSize / 2 + vSpeed, -blockSize / 2) || !isRelAccessible(-blockSize / 2 + vSpeed, blockSize / 2 - 1)) {
				vSpeed = 0;
				move_contact_solid(0);
			}
		// nach unten-movement (BL-BR)
		if (vSpeed > 0)
			if (!isRelAccessible(blockSize / 2 - 1 + vSpeed, -blockSize / 2) || !isRelAccessible(blockSize / 2 - 1 + vSpeed, blockSize / 2 - 1)) {
				vSpeed = 0;
				move_contact_solid(2);
			}
		// Horizontal
		// nach links-movement (TL-BL)
		if (hSpeed < 0)
			if (!isRelAccessible(-blockSize / 2, -blockSize / 2 + hSpeed) || !isRelAccessible(blockSize / 2 - 1, -blockSize / 2 + hSpeed)) {
				hSpeed = 0;
				move_contact_solid(3);
			}
		// nach rechts-movement (TR-BR)
		if (hSpeed > 0)
			if (!isRelAccessible(-blockSize / 2, blockSize / 2 - 1 + hSpeed) || !isRelAccessible(blockSize / 2 - 1, blockSize / 2 - 1 + hSpeed)) {
				hSpeed = 0;
				move_contact_solid(1);
			}

		double gesSpeed = Math.sqrt(hSpeed * hSpeed + vSpeed * vSpeed);
		if (gesSpeed > maxSpeed) {
			double factor = maxSpeed / gesSpeed;
			hSpeed = hSpeed * factor;
			vSpeed = vSpeed * factor;
		}

		if (Math.abs(hSpeed) > Math.abs(vSpeed)) {
			rotation = -90 * ((int) Math.copySign(1, hSpeed) - 2);
		} else if (Math.abs(hSpeed) < Math.abs(vSpeed)) {
			rotation = (int) (90 * (Math.copySign(1, vSpeed) + 1));
		}
		updateRotation();
		x += hSpeed;
		y += vSpeed;

	}

	private void updateRotation() {
		if (wantsToGoUp && !wantsToGoLeft && !wantsToGoDown && !wantsToGoRight) {
			rotation = 0; //oben
		} else if (!wantsToGoUp && wantsToGoLeft && !wantsToGoDown && !wantsToGoRight) {
			rotation = 270; //links
		} else if (!wantsToGoUp && !wantsToGoLeft && wantsToGoDown && !wantsToGoRight) {
			rotation = 180; //unten
		} else if (!wantsToGoUp && !wantsToGoLeft && !wantsToGoDown && wantsToGoRight) {
			rotation = 90; //rechts
		} if (wantsToGoUp && wantsToGoLeft && !wantsToGoDown && !wantsToGoRight) {
			rotation = 315; //oben links
		} else if (!wantsToGoUp && wantsToGoLeft && wantsToGoDown && !wantsToGoRight) {
			rotation = 225; //unten links
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

		if ((x + dx) >= maxX || (y + dy) >= maxY || (x + dx) <= 1 || (y + dy) <= 1) {

			return false;
		}

		return tetroWorldHitbox[getTileY(dy)][getTileX(dx)];
	}
}
