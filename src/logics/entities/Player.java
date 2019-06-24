package logics.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import data.RawPlayer;
import data.Tiles.Tile;
import graphics.GameFrame;
import logics.worlds.World;

/**
 * @author Lars Created on 05.08.2018
 */
public class Player extends Entity {

	private static final long serialVersionUID = 1L;
	private Tile akt_Tile;
	private boolean actionPressed;
	private MovingBlock movingBlockInHand = null;
	private boolean sprinting = false;
	private Point movingBlockOffset;
	private int ticksSinceFootstepNoice = 0;

	private final double moveblockacc = 0.2;
	private final double moveblockmaxSpeed = 4.0;
	private final double sprintngmaxSpeed = 14.4;

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
//	double relCheckX, relCheckY;

	public Player(World world, String animPath, RawPlayer rawPlayer) {
		super(world, animPath, null);
		this.world = world;
		akt_animation = anims.get("walk1");
		type = "player";

	}

	public Player(World world, int playerX, int playerY, String animPath, RawPlayer rawPlayer) {
		this(world, animPath, rawPlayer);
		x = playerX;
		y = playerY;
		lastX = x;
		lastY = y;

		setAcc(rawPlayer.getAcc());
		brake = rawPlayer.getAcc();
		setMaxSpeed(rawPlayer.getMaxSpeed());
	}

	@Override
	public void draw(Graphics g, float interpolation) {
		int interpolX = (int) ((x - lastX) * interpolation + lastX);
		int interpolY = (int) ((y - lastY) * interpolation + lastY);

		g.drawImage(akt_animation.getImage(), interpolX - world.cameraX() + akt_animation.getOffsetX(),
				interpolY - world.cameraY() + akt_animation.getOffsetY(), null);

	}

	public void drawPreview(Graphics g) {

		g.drawImage(akt_animation.getImage(), 0, 0, 110, 110, null);

	}

	@Override
	public void drawDebug(Graphics g, float interpolation) {
		// Player hitbox
		g.setFont(new Font("helvetica", Font.PLAIN, 11));
		g.setColor(Color.BLACK);
		g.setFont(new Font("helvetica", Font.PLAIN, 11));
		g.drawString("rx=" + x + " | ry=" + y, 20, 40);
		g.drawString("vx=" + x / GameFrame.BLOCKSIZE + " | vy=" + y / GameFrame.BLOCKSIZE, 20, 55);
	}

	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	@Override
	public void tick() {
		lastX = x;
		lastY = y;
		akt_animation.next();
		//TODO mit movingblock verbuggt
		checkInput();
		checkActionPressEvent();
		move();
		// footstep sound
		if (hSpeed != 0 || vSpeed != 0) {
			if (ticksSinceFootstepNoice > 12) {
				world.playSound("step", 0);
				
				ticksSinceFootstepNoice = 0;
				
			} else {
				ticksSinceFootstepNoice++;
			}

		} else {
			ticksSinceFootstepNoice = 0;
		}
		checkTile();
		if (movingBlockInHand != null)
			movingBlockInHand.setPosition(x, y);
		brake = 4;
	}

	private void checkActionPressEvent() {
		if (actionPressed) {
			if (movingBlockInHand != null) {
				releaseMovingBlock();
			} else {
				world.actionPressed(x, y, rotation);
			}
		}
	}

	private void checkInput() {

		if (world.getKeyHandler().getShift()) {
			sprinting = true;
		} else {
			sprinting = false;
		}

		if (!world.getKeyHandler().getKameraKey()) {
			wantsToGoUp = world.getKeyHandler().getUpKey();
			wantsToGoLeft = world.getKeyHandler().getLeftKey();
			wantsToGoDown = world.getKeyHandler().getDownKey();
			wantsToGoRight = world.getKeyHandler().getRightKey();
			if (movingBlockInHand != null) {
				int direction = movingBlockInHand.getDirection();
				if ((wantsToGoUp && direction != 0) || (wantsToGoRight && direction != 1)
						|| (wantsToGoDown && direction != 2) || (wantsToGoLeft && direction != 3)) {
					releaseMovingBlock();
				}

				if (movingBlockInHand != null) {
					wantsToGoDown = false;
					wantsToGoLeft = false;
					wantsToGoRight = false;
					wantsToGoUp = false;
					switch (direction) {
					case 0:
						wantsToGoUp = world.getKeyHandler().getUpKey();
						break;
					case 1:
						wantsToGoRight = world.getKeyHandler().getRightKey();
						break;
					case 2:
						wantsToGoDown = world.getKeyHandler().getDownKey();
						break;
					case 3:
						wantsToGoLeft = world.getKeyHandler().getLeftKey();
						break;
					}
				}
			}
			actionPressed = world.getKeyHandler().isActionPressed();
		} else {
			wantsToGoUp = false;
			wantsToGoDown = false;
			wantsToGoLeft = false;
			wantsToGoRight = false;

		}

		if (world.getKeyHandler().getRemoveKey()) {
			world.removeLastTetro();
			world.getKeyHandler().setRemoveKey(false);
		}

		if (world.getKeyHandler().getRotateKey()) {
			world.rotateTetro();
		}
		world.getKeyHandler().resetActionpressed();
	}

	private void releaseMovingBlock() {
		movingBlockInHand.unBind();
		movingBlockInHand = null;
	}

	protected double getExtremePosition(int direction) {
		if (movingBlockInHand == null) {
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
		} else {
			switch (direction % 4) {
			case 0:
				return movingBlockInHand.getY() - y - GameFrame.BLOCKSIZE / 2 + vSpeed;
			case 1:
				return movingBlockInHand.getX() - x + GameFrame.BLOCKSIZE / 2 + 1 + hSpeed; // +1: Verhindert den
			// rechts-links-bug
			case 2:
				return movingBlockInHand.getY() - y + GameFrame.BLOCKSIZE / 2 + 1 + vSpeed;
			case 3:
				return movingBlockInHand.getX() - x - GameFrame.BLOCKSIZE / 2 + hSpeed;
			default:
				System.err.println("Fehler @LivingEntity#getExtremePosition bei " + this);
				return 0;
			}
		}
	}

	protected void setAcc(double acc) {
		this.acc = acc;
	}

	private void checkTile() {

		if (akt_Tile != world.getTileAt(getTileY(), getTileX())) {
			Tile last_Tile = akt_Tile;

			akt_Tile = world.getTileAt(getTileY(), getTileX());
			if (akt_Tile != null)
				akt_Tile.eventWhenEntering();
			if (last_Tile != null) {
				last_Tile.eventWhenLeaving();
			}
		}

	}

	public void resetActionPressed() {

		world.getKeyHandler().resetActionpressed();
		actionPressed = false;
	}

	public void setMovingBlock(MovingBlock movingBlock) {
		this.movingBlockInHand = movingBlock;
		this.movingBlockOffset = new Point((int) (movingBlock.getX() - x), (int) (movingBlock.getY() - y));
	}

	public MovingBlock getMovingBlockInHand() {
		return movingBlockInHand;
	}

	public Point getMovingBlockOffset() {
		return movingBlockOffset;
	}

	public int getDirection() {
		return rotation / 90;
	}

	public void switchNoClip() {
		noClip = !noClip;
	}

	protected void bump(double speedloss) {
		world.playSound("metal" + (int) (Math.random() * 4), -40f + 3.6f * (float) speedloss);
	}

	private void move_contact_solid(int rotation, boolean inv) {
		// Nächster Punkt des Spielers
		double relCheckX = (rotation % 2) * (rotation - 2) * (-GameFrame.BLOCKSIZE / 2);
		double relCheckY = ((rotation + 1) % 2) * (rotation - 1) * (GameFrame.BLOCKSIZE / 2);

		double minDist = Math.abs(hSpeed * (rotation % 2) + vSpeed * (rotation + 1) % 2); // TODO läuft nicht
		switch (rotation) {
		case 0:
			if (!isRelAccessible(true, getExtremePosition(0), -GameFrame.BLOCKSIZE / 2)
					|| !isRelAccessible(true, getExtremePosition(0), GameFrame.BLOCKSIZE / 2)) {
				minDist = y - getTileY() * GameFrame.BLOCKSIZE;
			}

			break;
		case 1:
			if (!isRelAccessible(true, -GameFrame.BLOCKSIZE / 2, getExtremePosition(1))
					|| !isRelAccessible(true, GameFrame.BLOCKSIZE / 2, getExtremePosition(1))) {
				minDist = getTileX() * GameFrame.BLOCKSIZE - x;
			}
			break;
		case 2:
			if (!isRelAccessible(true, getExtremePosition(2), -GameFrame.BLOCKSIZE / 2)
					|| !isRelAccessible(true, getExtremePosition(2), GameFrame.BLOCKSIZE / 2)) {
				minDist = getTileY() * GameFrame.BLOCKSIZE - y;
			}
			break;
		case 3:
			if (!isRelAccessible(true, -GameFrame.BLOCKSIZE / 2, getExtremePosition(3))
					|| !isRelAccessible(true, GameFrame.BLOCKSIZE / 2, getExtremePosition(3))) {
				minDist = x - getTileX() * GameFrame.BLOCKSIZE;

			}
			break;
		}

		double minDistEntity = world.minDistanceToEntity(rotation, x + relCheckX + GameFrame.BLOCKSIZE / 2,
				y + relCheckY + GameFrame.BLOCKSIZE / 2, this, minDist);
//		if (minDistEntity < 100)
//			System.ouut.println(minDist + " " + minDistEntity + "; " + rotation);
		if (minDistEntity < minDist) {
			minDist = minDistEntity;
//			if (world.getLastTouchedMovingBlock() != null) {
//				world.getLastTouchedMovingBlock().interact();
//			}

		}

		if (minDist == Math.abs(hSpeed * (rotation % 2) + vSpeed * (rotation + 1) % 2)) {
			minDist = 0;
		}
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

	public double getAcc() {
		if (movingBlockInHand == null) {
			return acc;
		} else {
			return moveblockacc;
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

	public double getMaxSpeed() {
		if (movingBlockInHand != null) {
			return moveblockmaxSpeed;
		} else if (sprinting) {
			return sprintngmaxSpeed;
		} else {
			return maxSpeed;
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

	public double getBrake() {
		return brake;
	}

	public boolean getNoClip() {
		return noClip;
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

	private void checkMaxSpeed() {
		double gesSpeed = Math.sqrt(hSpeed * hSpeed + vSpeed * vSpeed);
		if (gesSpeed > getMaxSpeed()) {
			double factor = getMaxSpeed() / gesSpeed;
			hSpeed = hSpeed * factor;
			vSpeed = vSpeed * factor;
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
					move_contact_solid(0);
				} else {
					speedloss += Math.abs(vSpeed);

					move_contact_solid(0);
					vSpeed = 0;
				}

			}

			// rechter edgecut
			if (!isRelAccessible(getExtremePosition(0), GameFrame.BLOCKSIZE / 2 - 1)) {
				if (isRelAccessible(getExtremePosition(0),
						GameFrame.BLOCKSIZE / 2 - 1 - edgeTolerancePercentage * GameFrame.BLOCKSIZE / 100)
						&& !wantsToGoRight) {
					move_contact_solid(1, true);
					move_contact_solid(0);
				} else {
					speedloss += Math.abs(vSpeed);

					move_contact_solid(0);
					vSpeed = 0;
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
					move_contact_solid(2);
				} else {
					speedloss += Math.abs(vSpeed);

					move_contact_solid(2);
					vSpeed = 0;
				}
			}
			if (!isRelAccessible(getExtremePosition(2), GameFrame.BLOCKSIZE / 2 - 1)) {
				if (isRelAccessible(getExtremePosition(2),
						GameFrame.BLOCKSIZE / 2 - edgeTolerancePercentage * GameFrame.BLOCKSIZE / 100)
						&& !wantsToGoRight) {
					move_contact_solid(1, true);
					move_contact_solid(2);

				} else {
					speedloss += Math.abs(vSpeed);

					move_contact_solid(2);
					vSpeed = 0;
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
					move_contact_solid(3);
				} else {
					speedloss += Math.abs(hSpeed);

					move_contact_solid(3);
					hSpeed = 0;
				}
			}
			if (!isRelAccessible(GameFrame.BLOCKSIZE / 2 - 1, getExtremePosition(3))) {
				if (isRelAccessible(GameFrame.BLOCKSIZE / 2 - 1 - edgeTolerancePercentage * GameFrame.BLOCKSIZE / 100,
						getExtremePosition(3)) && !wantsToGoDown) {
					move_contact_solid(2, true);
					move_contact_solid(3);
				} else {
					speedloss += Math.abs(hSpeed);

					move_contact_solid(3);
					hSpeed = 0;
				}

			}

		}

		// nach rechts-movement (TR-BR)
		if (hSpeed > 0) {
			if (!isRelAccessible(-GameFrame.BLOCKSIZE / 2, getExtremePosition(1))) {
				if (isRelAccessible(-GameFrame.BLOCKSIZE / 2 + edgeTolerancePercentage * GameFrame.BLOCKSIZE / 100,
						getExtremePosition(1)) && !wantsToGoUp) {
					move_contact_solid(0, true);
					move_contact_solid(1);
				} else {
					speedloss += Math.abs(hSpeed);

					move_contact_solid(1);
					hSpeed = 0;
				}
			}
			if (!isRelAccessible(GameFrame.BLOCKSIZE / 2 - 1, getExtremePosition(1))) {
				if (isRelAccessible(GameFrame.BLOCKSIZE / 2 - 1 - edgeTolerancePercentage * GameFrame.BLOCKSIZE / 100,
						getExtremePosition(1)) && !wantsToGoDown) {
					move_contact_solid(2, true);
					move_contact_solid(1);
				} else {
					speedloss += Math.abs(hSpeed);

					move_contact_solid(1);
					hSpeed = 0;
				}
			}
		}

	}

	private boolean isRelAccessible(double dy, double dx) {
		return isRelAccessible(false, dy, dx);
	}

}