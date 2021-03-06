package logics.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import data.Animation;
import data.Tiles.SaveNLoadTile;
import data.Tiles.Tile;
import graphics.GameFrameHandler;
import logics.World;

/**
 * @author Lars Created on 05.08.2018
 */
public class Player extends Entity {

	private static final long serialVersionUID = 1L;
	private static final String animPath = "/res/anims/character.txt";
	private Tile akt_Tile;
	private boolean actionPressed;
	private MovingBlock movingBlockInHand = null;
	private boolean sprinting = false;
	private Point movingBlockOffset;
	private int ticksSinceFootstepNoice = 0;

	private final double acc = 0.8;
	private final double brake = 4.0;
	private final double maxSpeed = 9.0;

	private final double moveblockacc = 0.2;
	private final double moveblockmaxSpeed = 4.0;
	private final double sprintngmaxSpeed = 14.4;

	protected double lastX, lastY;
	private double hSpeed;
	private double vSpeed;

	protected boolean wantsToGoUp = false;
	protected boolean wantsToGoDown = false;
	protected boolean wantsToGoLeft = false;
	protected boolean wantsToGoRight = false;
	private Animation tpAnim;

	protected double edgeTolerancePercentage = 25;

	private double speedloss = 0;

	private boolean controllable = true;
	// double relCheckX, relCheckY;

	public Player(World world) {
		super(world, animPath, null);
		setWorld(world);
		akt_animation = anims.get("walk1");
		tpAnim = anims.get("tp");
		type = "player";

	}

	public Player(World world, int playerX, int playerY) {
		this(world);
		x = playerX;
		y = playerY;
		lastX = x;
		lastY = y;

		Tile satTile = world.getTileAt(getTileY(), getTileX());
		if (satTile instanceof SaveNLoadTile) {
			((SaveNLoadTile) satTile).refreshTetros();
			world.setLastUsedSALTile((SaveNLoadTile) satTile);
		}
	}

	@Override
	public void draw(Graphics g, float interpolation) {
		int interpolX = (int) ((x - lastX) * interpolation + lastX);
		int interpolY = (int) ((y - lastY) * interpolation + lastY);

		if (controllable)
			g.drawImage(akt_animation.getImage(), interpolX - world.cameraX() + akt_animation.getOffsetX(),
					interpolY - world.cameraY() + akt_animation.getOffsetY(), null);
		else {
			g.drawImage(tpAnim.getImage(), interpolX - world.cameraX() + akt_animation.getOffsetX(),
					interpolY - world.cameraY() + akt_animation.getOffsetY(), null);
			tpAnim.next();
		}
	}

	public void drawPreview(Graphics g, Rectangle previewRect) {

		g.drawImage(akt_animation.getImage(),
				previewRect.x + previewRect.width / 2 - akt_animation.getImage().getWidth(),
				previewRect.y + previewRect.height / 2 - akt_animation.getImage().getHeight(),
				akt_animation.getImage().getWidth() * 2, akt_animation.getImage().getHeight() * 2, null);

	}

	@Override
	public void drawDebug(Graphics g, float interpolation) {
		g.setFont(new Font("helvetica", Font.PLAIN, 11));
		g.setColor(Color.BLACK);
		g.setFont(new Font("helvetica", Font.PLAIN, 11));
		g.drawString("rx=" + (int) (x * 100) / 100. + " | ry=" + (int) (y * 100) / 100., 20, 40);
		g.drawString("vx=" + (int) (x / GameFrameHandler.BLOCKSIZE * 100) / 100. + " | vy="
				+ (int) (y / GameFrameHandler.BLOCKSIZE * 100) / 100., 20, 55);

		g.setColor(Color.GREEN);
	}

	@Override
	public void tick() {
		lastX = x;
		lastY = y;
		akt_animation.next();
		if (controllable) {
			checkInput();
			checkActionPressEvent();
			move();

			if (world.getKeyHandler().getKillPlayer()) {
				world.getKeyHandler().resetKillPlayer();
				world.interactWithLastUsedSALTile();
				return;
			}
		}
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

		if (isControllable()) {

			if (world.getKeyHandler().getShift()) {
				sprinting = true;
			} else {
				sprinting = false;
			}

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
		switch (direction % 4) {
		case 0:
			return -GameFrameHandler.BLOCKSIZE / 2 + vSpeed;
		case 1:
			return GameFrameHandler.BLOCKSIZE / 2 + 1 + hSpeed; // +1: Verhindert den rechts-links-bug
		case 2:
			return GameFrameHandler.BLOCKSIZE / 2 + 1 + vSpeed;
		case 3:
			return -GameFrameHandler.BLOCKSIZE / 2 + hSpeed;
		default:
			System.err.println("Fehler @LivingEntity#getExtremePosition bei " + this);
			return 0;
		}
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

		// set position values
		x = movingBlock.getX() + (getDirection() % 2) * (getDirection() - 2) * GameFrameHandler.BLOCKSIZE;
		y = movingBlock.getY() - ((getDirection() + 1) % 2) * (getDirection() - 1) * GameFrameHandler.BLOCKSIZE;

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

	protected void bump(double speedloss) {
		world.playSound("metal" + (int) (Math.random() * 4), Math.min(-40f + 3.6f * (float) speedloss, -3f));
	}

	private void move_contact_solid(int rotation, boolean inv) {
		// N�chster Punkt des Spielers
		double relCheckX = (rotation % 2) * (rotation - 2) * (-GameFrameHandler.BLOCKSIZE / 2);
		double relCheckY = ((rotation + 1) % 2) * (rotation - 1) * (GameFrameHandler.BLOCKSIZE / 2);

		double minDist = Math.abs(hSpeed * (rotation % 2) + vSpeed * (rotation + 1) % 2);
		switch (rotation) {
		case 0:
			if (!isRelAccessible(true, getExtremePosition(0), -GameFrameHandler.BLOCKSIZE / 2)
					|| !isRelAccessible(true, getExtremePosition(0), GameFrameHandler.BLOCKSIZE / 2)) {
				if (movingBlockInHand == null) {
					minDist = y - getTileY() * GameFrameHandler.BLOCKSIZE;
				} else {
					minDist = movingBlockInHand.getY() - movingBlockInHand.getTileY() * GameFrameHandler.BLOCKSIZE;
				}

			}

			break;
		case 1:
			if (!isRelAccessible(true, -GameFrameHandler.BLOCKSIZE / 2, getExtremePosition(1))
					|| !isRelAccessible(true, GameFrameHandler.BLOCKSIZE / 2, getExtremePosition(1))) {
				if (movingBlockInHand == null) {
					minDist = getTileX() * GameFrameHandler.BLOCKSIZE - x;
				} else {
					minDist = movingBlockInHand.getTileX() * GameFrameHandler.BLOCKSIZE - movingBlockInHand.getX();
				}
			}
			break;
		case 2:
			if (!isRelAccessible(true, getExtremePosition(2), -GameFrameHandler.BLOCKSIZE / 2)
					|| !isRelAccessible(true, getExtremePosition(2), GameFrameHandler.BLOCKSIZE / 2)) {
				if (movingBlockInHand == null) {
					minDist = getTileY() * GameFrameHandler.BLOCKSIZE - y;
				} else {
					minDist = movingBlockInHand.getTileY() * GameFrameHandler.BLOCKSIZE - movingBlockInHand.getY();
				}
			}
			break;
		case 3:
			if (!isRelAccessible(true, -GameFrameHandler.BLOCKSIZE / 2, getExtremePosition(3))
					|| !isRelAccessible(true, GameFrameHandler.BLOCKSIZE / 2, getExtremePosition(3))) {
				if (movingBlockInHand == null) {
					minDist = x - getTileX() * GameFrameHandler.BLOCKSIZE;
				} else {
					minDist = movingBlockInHand.getX() - movingBlockInHand.getTileX() * GameFrameHandler.BLOCKSIZE;
				}

			}
			break;
		}

		double minDistEntity = world.minDistanceToEntity(rotation, x + relCheckX + GameFrameHandler.BLOCKSIZE / 2,
				y + relCheckY + GameFrameHandler.BLOCKSIZE / 2, this, minDist);
		// if (minDistEntity < 100)
		// System.ouut.println(minDist + " " + minDistEntity + "; " + rotation);
		if (minDistEntity < minDist) {
			minDist = minDistEntity;
			// if (world.getLastTouchedMovingBlock() != null) {
			// world.getLastTouchedMovingBlock().interact();
			// }

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

		// dx, dy: �u�erer Hitbox - rand
		double x = this.x;
		double y = this.y;
		if (movingBlockInHand != null) {
			x = movingBlockInHand.getX();
			y = movingBlockInHand.getY();
		}

		// + BlockSize / 2, da dy, dx vom Zentrum aus gemessen werden
		// world bounds
		if ((x + GameFrameHandler.BLOCKSIZE / 2 + dx) >= world.getMaxX()
				|| (y + GameFrameHandler.BLOCKSIZE / 2 + dy) >= world.getMaxY()
				|| (x + GameFrameHandler.BLOCKSIZE / 2 + dx) < 0 || (y + GameFrameHandler.BLOCKSIZE / 2 + dy) < 0) {
			return false;
		}
		if (!wallsOnly && world.isEntityAt(this, y + GameFrameHandler.BLOCKSIZE / 2 + dy,
				x + GameFrameHandler.BLOCKSIZE / 2 + dx)) {
			return false;
		}
		// Empty Tile
		if (movingBlockInHand != null) {
			// Empty Tile?
			if (world.getTileAt(movingBlockInHand.getTileY(dy), movingBlockInHand.getTileX(dx)) == null) {
				// Gucke ob Tetro drauf
				return (world.isTetroAt(movingBlockInHand.getTileY(dy), movingBlockInHand.getTileX(dx)));
			}

			return (world.isTetroAt(movingBlockInHand.getTileY(dy), movingBlockInHand.getTileX(dx)) && world
					.getTileAt(movingBlockInHand.getTileY(dy), movingBlockInHand.getTileX(dx)).isWalkableWithTetro())
					|| world.getTileAt(movingBlockInHand.getTileY(dy), movingBlockInHand.getTileX(dx)).isWalkable();

		} else {
			if (world.getTileAt(getTileY(dy), getTileX(dx)) == null) { // Empty Tile?
				return (world.isTetroAt(getTileY(dy), getTileX(dx))); // Gucke ob Tetro drauf
			}
			// tetro or walkable tile?
			return (world.isTetroAt(getTileY(dy), getTileX(dx))
					&& world.getTileAt(getTileY(dy), getTileX(dx)).isWalkableWithTetro())
					|| world.getTileAt(getTileY(dy), getTileX(dx)).isWalkable();
		}

	}

	public double getAcc() {
		if (movingBlockInHand == null) {
			return acc;
		} else {
			return moveblockacc;
		}
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
			y = getTileY() * GameFrameHandler.BLOCKSIZE;
			break;
		case 1:
			double alt2 = x;
			x = getTileX() * GameFrameHandler.BLOCKSIZE;
			break;
		case 2:
			double alt3 = y;
			y = getTileY() * GameFrameHandler.BLOCKSIZE;
			break;
		case 3:
			double alt4 = x;
			x = getTileX() * GameFrameHandler.BLOCKSIZE;
			break;
		}

	}

	private void move_contact_solid(int rotation) {
		move_contact_solid(rotation, false);
	}

	protected void move() {
		accelerate();

		if (!world.noClip()) {
			speedloss = 0; // for sounds
			checkCollisions();
			if (speedloss > getAcc()) {
				bump(speedloss);
			}
		}
		checkMaxSpeed();

		updateRotation();
		if (!world.noClip()) {
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

		// Wenn Gegens�tze (oder nichts) gedr�ckt werden
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
			if (!isRelAccessible(getExtremePosition(0), -GameFrameHandler.BLOCKSIZE / 2)) {
				if (isRelAccessible(getExtremePosition(0),
						-GameFrameHandler.BLOCKSIZE / 2 + edgeTolerancePercentage * GameFrameHandler.BLOCKSIZE / 100)
						&& !wantsToGoLeft) {
					move_contact_solid(3, true);
					// move_contact_solid(0);
				} else {
					speedloss += Math.abs(vSpeed);

					move_contact_solid(0);
					vSpeed = 0;
				}

			}

			// rechter edgecut
			if (!isRelAccessible(getExtremePosition(0), GameFrameHandler.BLOCKSIZE / 2 - 1)) {
				if (isRelAccessible(getExtremePosition(0),
						GameFrameHandler.BLOCKSIZE / 2 - 1 - edgeTolerancePercentage * GameFrameHandler.BLOCKSIZE / 100)
						&& !wantsToGoRight) {
					move_contact_solid(1, true);
					// move_contact_solid(0);
				} else {
					speedloss += Math.abs(vSpeed);

					move_contact_solid(0);
					vSpeed = 0;
				}
			}
		}
		// nach unten-movement (BL-BR)
		if (vSpeed > 0) {
			if (!isRelAccessible(getExtremePosition(2), -GameFrameHandler.BLOCKSIZE / 2)) {
				if (isRelAccessible(getExtremePosition(2),
						-GameFrameHandler.BLOCKSIZE / 2 + edgeTolerancePercentage * GameFrameHandler.BLOCKSIZE / 100)
						&& !wantsToGoLeft) {
					move_contact_solid(3, true);
					// move_contact_solid(2);
				} else {
					speedloss += Math.abs(vSpeed);

					move_contact_solid(2);
					vSpeed = 0;
				}
			}
			if (!isRelAccessible(getExtremePosition(2), GameFrameHandler.BLOCKSIZE / 2 - 1)) {
				if (isRelAccessible(getExtremePosition(2),
						GameFrameHandler.BLOCKSIZE / 2 - edgeTolerancePercentage * GameFrameHandler.BLOCKSIZE / 100)
						&& !wantsToGoRight) {
					move_contact_solid(1, true);
					// move_contact_solid(2);

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
			if (!isRelAccessible(-GameFrameHandler.BLOCKSIZE / 2, getExtremePosition(3))) {
				if (isRelAccessible(
						-GameFrameHandler.BLOCKSIZE / 2 + edgeTolerancePercentage * GameFrameHandler.BLOCKSIZE / 100,
						getExtremePosition(3)) && !wantsToGoUp) {
					move_contact_solid(0, true);
					// move_contact_solid(3);
				} else {
					speedloss += Math.abs(hSpeed);

					move_contact_solid(3);
					hSpeed = 0;
				}
			}
			if (!isRelAccessible(GameFrameHandler.BLOCKSIZE / 2 - 1, getExtremePosition(3))) {
				if (isRelAccessible(
						GameFrameHandler.BLOCKSIZE / 2 - 1 - edgeTolerancePercentage * GameFrameHandler.BLOCKSIZE / 100,
						getExtremePosition(3)) && !wantsToGoDown) {
					move_contact_solid(2, true);
					// move_contact_solid(3);
				} else {
					speedloss += Math.abs(hSpeed);

					move_contact_solid(3);
					hSpeed = 0;
				}

			}

		}

		// nach rechts-movement (TR-BR)
		if (hSpeed > 0) {
			if (!isRelAccessible(-GameFrameHandler.BLOCKSIZE / 2, getExtremePosition(1))) {
				if (isRelAccessible(
						-GameFrameHandler.BLOCKSIZE / 2 + edgeTolerancePercentage * GameFrameHandler.BLOCKSIZE / 100,
						getExtremePosition(1)) && !wantsToGoUp) {
					move_contact_solid(0, true);
					// move_contact_solid(1);
				} else {
					speedloss += Math.abs(hSpeed);

					move_contact_solid(1);
					hSpeed = 0;
				}
			}
			if (!isRelAccessible(GameFrameHandler.BLOCKSIZE / 2 - 1, getExtremePosition(1))) {
				if (isRelAccessible(
						GameFrameHandler.BLOCKSIZE / 2 - 1 - edgeTolerancePercentage * GameFrameHandler.BLOCKSIZE / 100,
						getExtremePosition(1)) && !wantsToGoDown) {
					move_contact_solid(2, true);
					// move_contact_solid(1);
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

	private boolean isControllable() {
		return controllable && !world.getKeyHandler().getKameraKey();
	}

	public void freeze() {
		controllable = false;

	}

	public void increaseX(double d) {
		this.x += d;
	}

	public void increaseY(double dy) {
		this.y += dy;
	}

	public void resetSpeed() {
		animation_key = "stand2";
		if (anims.get(animation_key) != akt_animation) {
			akt_animation = anims.get(animation_key);
			akt_animation.reset();
		}
		hSpeed = 0;
		vSpeed = 0;
	}

}