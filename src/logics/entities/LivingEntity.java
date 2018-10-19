package logics.entities;

import java.util.HashMap;

import data.Animation;
import logics.World;
import logics.entities.items.Weapon;

/**
 * @author Lars Created on 18.09.2018
 */
public abstract class LivingEntity extends Entity {

	private static final long serialVersionUID = 1L;
	protected double lastX, lastY;
	protected double hSpeed;
	protected double vSpeed;
	protected int health;

	protected int hitTicks = 0;
	protected double acc;
	protected double brake;
	protected double maxSpeed;

	protected boolean wantsToGoUp = false;
	protected boolean wantsToGoDown = false;
	protected boolean wantsToGoLeft = false;
	protected boolean wantsToGoRight = false;

	protected double edgeTolerancePercentage = 25;

	public LivingEntity(World world, HashMap<String, Animation> anims) {
		super(world, anims);
		this.world = world;

	}

	public LivingEntity(World world, int x, int y, HashMap<String, Animation> anims) {
		super(world, x, y, anims);

	}

	@Override
	public void tick() {
		lastX = x;
		lastY = y;
		hitTicks = Math.max(0, --hitTicks);
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

	protected void checkHealth() {
		if (health <= 0) {
			kill();
		}
	}

	public void applyDamage(Weapon weapon, int direction) {
		health -= weapon.getDamage();

		// double h;
		// double v;
		// double ges = weapon.getKnockback();
		// if (0 <= direction && direction < 90) {
		// h = +ges * Math.sin(Math.toRadians(direction % 90));
		// v = -ges * Math.cos(Math.toRadians(direction % 90));
		// } else if (90 <= direction && direction < 180) {
		// h = +ges * Math.cos(Math.toRadians(direction % 90));
		// v = +ges * Math.sin(Math.toRadians(direction % 90));
		//
		// } else if (180 <= direction && direction < 270) {
		// h = -ges * Math.sin(Math.toRadians(direction % 90));
		// v = +ges * Math.cos(Math.toRadians(direction % 90));
		//
		// } else if (270 <= direction && direction < 360) {
		// h = -ges * Math.cos(Math.toRadians(direction % 90));
		// v = -ges * Math.sin(Math.toRadians(direction % 90));
		//
		// } else {
		// System.out.println("Fehler @LivingEntity#applyDamage");
		// System.exit(1);
		// h = -1;
		// v = -1;
		// }
		//
		// x += h;
		// y += v;

	}

	protected abstract void kill();

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
			if (!isRelAccessible(-world.blockSize() / 2 + vSpeed, -world.blockSize() / 2)) {
				if (isRelAccessible(-world.blockSize() / 2 + vSpeed,
						-world.blockSize() / 2 + edgeTolerancePercentage * world.blockSize() / 100) && !wantsToGoLeft) {
					// TODO edgecut
				} else {
					vSpeed = 0;
					move_contact_solid(0);
				}

			}

			// rechter edgecut
			if (!isRelAccessible(-world.blockSize() / 2 + vSpeed, world.blockSize() / 2 - 1)) {
				if (isRelAccessible(-world.blockSize() / 2 + vSpeed,
						world.blockSize() / 2 - 1 - edgeTolerancePercentage * world.blockSize() / 100)
						&& !wantsToGoRight) {

				} else {
					vSpeed = 0;
					move_contact_solid(0);
				}
			}
		}
		// nach unten-movement (BL-BR)
		if (vSpeed > 0) {
			if (!isRelAccessible(world.blockSize() / 2 - 1 + vSpeed, -world.blockSize() / 2)) {
				if (isRelAccessible(world.blockSize() / 2 - 1 + vSpeed,
						-world.blockSize() / 2 + edgeTolerancePercentage * world.blockSize() / 100) && !wantsToGoLeft) {

				} else {
					vSpeed = 0;
					move_contact_solid(2);
				}
			}
			if (!isRelAccessible(world.blockSize() / 2 - 1 + vSpeed, world.blockSize() / 2 - 1)) {
				if (isRelAccessible(world.blockSize() / 2 - 1 + vSpeed,
						world.blockSize() / 2 - 1 - edgeTolerancePercentage * world.blockSize() / 100)
						&& !wantsToGoRight) {

				} else {
					vSpeed = 0;
					move_contact_solid(2);
				}

			}

		}
		// Horizontal
		// nach links-movement (TL-BL)
		if (hSpeed < 0) {
			if (!isRelAccessible(-world.blockSize() / 2, -world.blockSize() / 2 + hSpeed)) {
				if (isRelAccessible(-world.blockSize() / 2 + edgeTolerancePercentage * world.blockSize() / 100,
						-world.blockSize() / 2 + hSpeed) && !wantsToGoUp) {

				} else {
					hSpeed = 0;
					move_contact_solid(3);
				}
			}
			if (!isRelAccessible(world.blockSize() / 2 - 1, -world.blockSize() / 2 + hSpeed)) {
				if (isRelAccessible(world.blockSize() / 2 - 1 - edgeTolerancePercentage * world.blockSize() / 100,
						-world.blockSize() / 2 + hSpeed) && !wantsToGoDown) {

				} else {
					hSpeed = 0;
					move_contact_solid(3);
				}

			}

		}

		// nach rechts-movement (TR-BR)
		if (hSpeed > 0) {
			if (!isRelAccessible(-world.blockSize() / 2, world.blockSize() / 2 - 1 + hSpeed)) {
				if (isRelAccessible(-world.blockSize() / 2 + edgeTolerancePercentage * world.blockSize() / 100,
						world.blockSize() / 2 - 1 + hSpeed) && !wantsToGoUp) {

				} else {
					hSpeed = 0;
					move_contact_solid(1);
				}
			}
			if (!isRelAccessible(world.blockSize() / 2 - 1, world.blockSize() / 2 - 1 + hSpeed)) {
				if (isRelAccessible(world.blockSize() / 2 - 1 - edgeTolerancePercentage * world.blockSize() / 100,
						world.blockSize() / 2 - 1 + hSpeed) && !wantsToGoDown) {

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

		if (hitTicks > 0) {
			animation_key = "hit" + rotation / 90;
		} else if (Math.abs(hSpeed) < 1 && Math.abs(vSpeed) < 1) {
			animation_key = "stand" + rotation / 90;
		} else {
			animation_key = "walk" + rotation / 90;
		}
		akt_animation = anims.get(animation_key);
		// if (wantsToGoUp && wantsToGoLeft && !wantsToGoDown && !wantsToGoRight) {
		// rotation = 315; // oben links
		// } else if (!wantsToGoUp && wantsToGoLeft && wantsToGoDown && !wantsToGoRight)
		// {
		// rotation = 225; // unten links
		// } else if (!wantsToGoUp && !wantsToGoLeft && wantsToGoDown && wantsToGoRight)
		// {
		// rotation = 135; // unten rechts
		// } else if (wantsToGoUp && !wantsToGoLeft && !wantsToGoDown && wantsToGoRight)
		// {
		// rotation = 45; // oben rechts
		// }
	}

	private void move_contact_solid(int i) {
		switch (i) {
		case 0:
			y = getTileY() * world.blockSize();
			break;
		case 1:
			x = getTileX() * world.blockSize();
			break;
		case 2:
			y = getTileY() * world.blockSize();
			break;
		case 3:
			x = getTileX() * world.blockSize();
			break;
		}

	}

	private int getTileX(double dx) {
		return (int) ((x + dx + world.blockSize() / 2) / world.blockSize());
	}

	private int getTileY(double dy) {
		return (int) ((y + dy + world.blockSize() / 2) / world.blockSize());
	}

	public int getTileX() {
		return (int) ((x + world.blockSize() / 2) / world.blockSize());
	}

	public int getTileY() {
		return (int) ((y + world.blockSize() / 2) / world.blockSize());
	}

	private boolean isRelAccessible(double dy, double dx) {

		if ((x + world.blockSize() / 2 + dx) >= world.getMaxX() || (y + world.blockSize() / 2 + dy) >= world.getMaxY()
				|| (x + world.blockSize() / 2 + dx) < 0 || (y + world.blockSize() / 2 + dy) < 0) {
			return false;
		}

		return (world.isTetroAt(getTileY(dy), getTileX(dx))
				&& world.getTileAt(getTileY(dy), getTileX(dx)).isWalkableWithTetro())
				|| world.getTileAt(getTileY(dy), getTileX(dx)).isWalkable();
	}
}
