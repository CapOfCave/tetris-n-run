package logics.entities;

import java.awt.Rectangle;

import graphics.GameFrame;
import logics.worlds.World;

/**
 * @author Lars Created on 18.09.2018
 */
public abstract class LivingEntity extends Entity {

	private static final long serialVersionUID = 1L;
	protected double lastX, lastY;
	protected double hSpeed;
	protected double vSpeed;
	protected boolean noClip = false;

	private double acc;
	protected double brake;
	private double maxSpeed;

	protected boolean wantsToGoUp = false;
	protected boolean wantsToGoDown = false;
	protected boolean wantsToGoLeft = false;
	protected boolean wantsToGoRight = false;

	protected double edgeTolerancePercentage = 25;

	private double speedloss = 0;
	double relCheckX, relCheckY;

	public LivingEntity(World world, String animPath, Rectangle relCollisionsRect) {
		super(world, animPath, relCollisionsRect);
		this.world = world;

	}

	public LivingEntity(World world, int x, int y, String animPath, Rectangle relCollisionsRect) {
		super(world, x, y, animPath, relCollisionsRect);

	}

	@Override
	public void tick() {
		lastX = x;
		lastY = y;
		akt_animation.next();
	}

	protected void move() {
		accelerate();

		if (!noClip) {
			speedloss = 0; // for sounds
			checkCollisions();
			if (speedloss > getAcc()) {
				bump(speedloss);
			}
		}
		checkMaxSpeed();

		updateRotation();
		if (!noClip) {
			x += hSpeed;
			y += vSpeed;
		} else {
			x += hSpeed * 2.5;
			y += vSpeed * 2.5;
		}

	}

	protected void bump(double speedloss) {

	}

	private void accelerate() {
		double abs_hSpeed = Math.abs(hSpeed);
		if (wantsToGoLeft && !wantsToGoRight) {
			hSpeed -= getAcc();
			if (hSpeed > 0) {
				hSpeed -= brake;
			}
		} else if (!wantsToGoLeft && wantsToGoRight) {
			hSpeed += getAcc();
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
			vSpeed -= getAcc();
			if (vSpeed > 0) {
				vSpeed -= brake;
			}
		} else if (!wantsToGoUp && wantsToGoDown) {
			vSpeed += getAcc();
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

	protected abstract void kill();

	private void checkMaxSpeed() {
		double gesSpeed = Math.sqrt(hSpeed * hSpeed + vSpeed * vSpeed);
		if (gesSpeed > getMaxSpeed()) {
			double factor = getMaxSpeed() / gesSpeed;
			hSpeed = hSpeed * factor;
			vSpeed = vSpeed * factor;
		}

	}

	private void checkCollisions() {
		// Vertikal
		// nach oben-movement (TL-TR)
		if (vSpeed < 0) {
			// linker edgecut
			if (!isRelAccessible(getExtremePosition(0), -GameFrame.BLOCKSIZE / 2)) {
				if (isRelAccessible(getExtremePosition(0),
						-GameFrame.BLOCKSIZE / 2 + edgeTolerancePercentage * GameFrame.BLOCKSIZE / 100)
						&& !wantsToGoLeft) {
					move_contact_solid(3, true);
				} else {
					speedloss += Math.abs(vSpeed);
					vSpeed = 0;
					move_contact_solid(0);
				}

			}

			// rechter edgecut
			if (!isRelAccessible(getExtremePosition(0), GameFrame.BLOCKSIZE / 2 - 1)) {
				if (isRelAccessible(getExtremePosition(0),
						GameFrame.BLOCKSIZE / 2 - 1 - edgeTolerancePercentage * GameFrame.BLOCKSIZE / 100)
						&& !wantsToGoRight) {
					move_contact_solid(1, true);
				} else {
					speedloss += Math.abs(vSpeed);
					vSpeed = 0;
					move_contact_solid(0);
				}
			}
		}
		// nach unten-movement (BL-BR)
		if (vSpeed > 0) {
			if (!isRelAccessible(getExtremePosition(2), -GameFrame.BLOCKSIZE / 2)) {
				if (isRelAccessible(getExtremePosition(2),
						-GameFrame.BLOCKSIZE / 2 + edgeTolerancePercentage * GameFrame.BLOCKSIZE / 100)
						&& !wantsToGoLeft) {
					move_contact_solid(3, true);
				} else {
					speedloss += Math.abs(vSpeed);
					vSpeed = 0;
					move_contact_solid(2);
				}
			}
			if (!isRelAccessible(getExtremePosition(2), GameFrame.BLOCKSIZE / 2 - 1)) {
				if (isRelAccessible(getExtremePosition(2),
						GameFrame.BLOCKSIZE / 2 - edgeTolerancePercentage * GameFrame.BLOCKSIZE / 100)
						&& !wantsToGoRight) {
					move_contact_solid(1, true);

				} else {
					speedloss += Math.abs(vSpeed);
					vSpeed = 0;
					move_contact_solid(2);
				}

			}

		}
		// Horizontal
		// nach links-movement (TL-BL)
		if (hSpeed < 0) {
			if (!isRelAccessible(-GameFrame.BLOCKSIZE / 2, getExtremePosition(3))) {
				if (isRelAccessible(-GameFrame.BLOCKSIZE / 2 + edgeTolerancePercentage * GameFrame.BLOCKSIZE / 100,
						getExtremePosition(3)) && !wantsToGoUp) {
					move_contact_solid(0, true);
				} else {
					speedloss += Math.abs(hSpeed);
					hSpeed = 0;
					move_contact_solid(3);
				}
			}
			if (!isRelAccessible(GameFrame.BLOCKSIZE / 2 - 1, getExtremePosition(3))) {
				if (isRelAccessible(GameFrame.BLOCKSIZE / 2 - 1 - edgeTolerancePercentage * GameFrame.BLOCKSIZE / 100,
						getExtremePosition(3)) && !wantsToGoDown) {
					move_contact_solid(2, true);
				} else {
					speedloss += Math.abs(hSpeed);
					hSpeed = 0;
					move_contact_solid(3);
				}

			}

		}

		// nach rechts-movement (TR-BR)
		if (hSpeed > 0) {
			if (!isRelAccessible(-GameFrame.BLOCKSIZE / 2, getExtremePosition(1))) {
				if (isRelAccessible(-GameFrame.BLOCKSIZE / 2 + edgeTolerancePercentage * GameFrame.BLOCKSIZE / 100,
						getExtremePosition(1)) && !wantsToGoUp) {
					move_contact_solid(0, true);
				} else {
					speedloss += Math.abs(hSpeed);
					hSpeed = 0;
					move_contact_solid(1);
				}
			}
			if (!isRelAccessible(GameFrame.BLOCKSIZE / 2 - 1, getExtremePosition(1))) {
				if (isRelAccessible(GameFrame.BLOCKSIZE / 2 - 1 - edgeTolerancePercentage * GameFrame.BLOCKSIZE / 100,
						getExtremePosition(1)) && !wantsToGoDown) {
					move_contact_solid(2, true);
				} else {
					speedloss += Math.abs(hSpeed);
					hSpeed = 0;
					move_contact_solid(1);
				}
			}
		}

	}

	protected double getExtremePosition(int direction) {
		switch (direction % 4) {
		case 0:
			return -GameFrame.BLOCKSIZE / 2 + vSpeed;
		case 1:
			return GameFrame.BLOCKSIZE / 2 + 1 + hSpeed; // +1: Verhindert den rechts-links-bug
		case 2:
			return GameFrame.BLOCKSIZE / 2 + 1 + vSpeed;
		case 3:
			return -GameFrame.BLOCKSIZE / 2 + hSpeed;
		default:
			System.err.println("Fehler @LivingEntity#getExtremePosition bei " + this);
			return 0;
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

		if (Math.abs(hSpeed) < 1 && Math.abs(vSpeed) < 1) {
			animation_key = "stand" + rotation / 90;
		} else {
			animation_key = "walk" + rotation / 90;
		}
		if (anims.get(animation_key) != akt_animation) {
			akt_animation = anims.get(animation_key);
			akt_animation.reset();
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

	@SuppressWarnings("unused")
	private void move_contact_solid_alt(int i) {
		switch (i) {
		case 0:
			double alt1 = y;
			y = getTileY() * GameFrame.BLOCKSIZE;
			break;
		case 1:
			double alt2 = x;
			x = getTileX() * GameFrame.BLOCKSIZE;
			break;
		case 2:
			double alt3 = y;
			y = getTileY() * GameFrame.BLOCKSIZE;
			break;
		case 3:
			double alt4 = x;
			x = getTileX() * GameFrame.BLOCKSIZE;
			break;
		}

	}

	private void move_contact_solid(int rotation) {
		move_contact_solid(rotation, false);
	}

	private void move_contact_solid(int rotation, boolean inv) {
		// Nächster Punkt des Spielers
		relCheckX = (rotation % 2) * (rotation - 2) * (-GameFrame.BLOCKSIZE / 2);
		relCheckY = ((rotation + 1) % 2) * (rotation - 1) * (GameFrame.BLOCKSIZE / 2);

		double minDist = 0;
		switch (rotation) {
		case 0:
			if (!isRelAccessible(true, relCheckY, -GameFrame.BLOCKSIZE / 2)
					|| !isRelAccessible(true, relCheckY, GameFrame.BLOCKSIZE / 2 - 1)) {
				minDist = y - getTileY() * GameFrame.BLOCKSIZE;
			} 

			break;
		case 1:

			if (!isRelAccessible(true, -GameFrame.BLOCKSIZE / 2, relCheckX)
					|| !isRelAccessible(true, GameFrame.BLOCKSIZE / 2 - 1, relCheckX)) {
				minDist = getTileX() * GameFrame.BLOCKSIZE - x;

			}
			break;
		case 2:
			if (!isRelAccessible(true, relCheckY, -GameFrame.BLOCKSIZE / 2)
					|| !isRelAccessible(true, relCheckY, GameFrame.BLOCKSIZE / 2 - 1)) {
				minDist = getTileY() * GameFrame.BLOCKSIZE - y;
			} 
			break;
		case 3:
			if (!isRelAccessible(true, -GameFrame.BLOCKSIZE / 2, relCheckX)
					|| !isRelAccessible(true, GameFrame.BLOCKSIZE / 2 - 1, relCheckX)) {
				minDist = x - getTileX() * GameFrame.BLOCKSIZE;

			} 
			break;
		}
//		if (inv) {
//			minDist = -minDist;
//		}
		minDist = world.minDistanceToEntity(rotation, x + relCheckX + GameFrame.BLOCKSIZE / 2,
				y + relCheckY + GameFrame.BLOCKSIZE / 2, this, minDist);
		switch (rotation) {
		case 0:
			y -= minDist;
			break;
		case 1:
			x += minDist;
			break;
		case 2:
			y += minDist;
			break;
		case 3:
			x -= minDist;
			break;

		}
	}

	private int getTileX(double dx) {
		return (int) ((x + dx + GameFrame.BLOCKSIZE / 2) / GameFrame.BLOCKSIZE);
	}

	private int getTileY(double dy) {
		return (int) ((y + dy + GameFrame.BLOCKSIZE / 2) / GameFrame.BLOCKSIZE);
	}

	public int getTileX() {
		return (int) ((x + GameFrame.BLOCKSIZE / 2) / GameFrame.BLOCKSIZE);
	}

	public int getTileY() {
		return (int) ((y + GameFrame.BLOCKSIZE / 2) / GameFrame.BLOCKSIZE);
	}

	private boolean isRelAccessible(double dy, double dx) {
		return isRelAccessible(false, dy, dx);
	}

	private boolean isRelAccessible(boolean wallsOnly, double dy, double dx) {

		// world bounds
		if ((x + GameFrame.BLOCKSIZE / 2 + dx) >= world.getMaxX()
				|| (y + GameFrame.BLOCKSIZE / 2 + dy) >= world.getMaxY() || (x + GameFrame.BLOCKSIZE / 2 + dx) < 0
				|| (y + GameFrame.BLOCKSIZE / 2 + dy) < 0) {
			return false;
		}
		if (!wallsOnly && world.isEntityAt(this, y + GameFrame.BLOCKSIZE / 2 + dy, x + GameFrame.BLOCKSIZE / 2 + dx)) {
			return false;
		}
		// Empty Tile
		if (world.getTileAt(getTileY(dy), getTileX(dx)) == null)
			return (world.isTetroAt(getTileY(dy), getTileX(dx)));

		// tetro or walkable tile
		return (world.isTetroAt(getTileY(dy), getTileX(dx))
				&& world.getTileAt(getTileY(dy), getTileX(dx)).isWalkableWithTetro())
				|| world.getTileAt(getTileY(dy), getTileX(dx)).isWalkable();
	}

	protected double getAcc() {
		return acc;
	}

	protected void setAcc(double acc) {
		this.acc = acc;
	}

	public double getBrake() {
		return brake;
	}

	public void setBrake(double brake) {
		this.brake = brake;
	}

	public double getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public boolean getNoClip() {
		return noClip;
	}
}
