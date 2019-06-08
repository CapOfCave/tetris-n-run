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
public class Player extends LivingEntity {

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

	public Player(World world, String animPath, RawPlayer rawPlayer) {
		super(world, animPath, null);

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
		g.setColor(Color.CYAN);
		g.drawOval((int) (x + relCheckX + GameFrame.BLOCKSIZE / 2 - world.cameraX()),
				(int) (y + relCheckY + GameFrame.BLOCKSIZE / 2 - world.cameraY()), 3, 3);
	}

	@Override
	public void tick() {
		super.tick();

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

	@Override
	protected double getExtremePosition(int direction) {
		if (movingBlockInHand == null) {
			return super.getExtremePosition(direction);
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

	@Override
	protected void kill() {
		world.backToTheOverworld(true);
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

	@Override
	protected void bump(double speedloss) {
		world.playSound("metal" + (int) (Math.random() * 4), -40f + 3.6f * (float) speedloss);
	}

	@Override
	public double getAcc() {
		if (movingBlockInHand == null) {
			return super.getAcc();
		} else {
			return moveblockacc;
		}
	}

	@Override
	public double getMaxSpeed() {
		if (movingBlockInHand != null) {
			return moveblockmaxSpeed;
		} else if (sprinting) {
			return sprintngmaxSpeed;
		} else {
			return super.getMaxSpeed();
		}
	}
}