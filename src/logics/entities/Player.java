package logics.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import data.RawPlayer;
import data.Tiles.Tile;
import graphics.Frame;
import logics.Inventory;
import logics.entities.items.Item;
import logics.entities.items.Weapon;
import logics.worlds.World;

/**
 * @author Lars Created on 05.08.2018
 */
public class Player extends LivingEntity {

	private static final long serialVersionUID = 1L;
	private Inventory inventory;
	private Weapon activeWeapon;
	private Tile akt_Tile;
	private boolean actionPressed;
	private MovingBlock movingBlockInHand = null;
	private Point movingBlockOffset;

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

		acc = rawPlayer.getAcc();
		health = rawPlayer.getHealth();
		brake = rawPlayer.getAcc();
		maxSpeed = rawPlayer.getMaxSpeed();
		inventory = rawPlayer.getInventory();

		inventory.setWorld(world);
	}

	@Override
	public void draw(Graphics g, float interpolation) {
		int interpolX = (int) ((x - lastX) * interpolation + lastX);
		int interpolY = (int) ((y - lastY) * interpolation + lastY);

		g.drawImage(akt_animation.getImage(), interpolX - world.cameraX() + akt_animation.getOffsetX(),
				interpolY - world.cameraY() + akt_animation.getOffsetY(), 55, 55, null);
		if (activeWeapon != null) {
			activeWeapon.draw(g, interpolX - world.cameraX(), interpolY - world.cameraY(), animation_key,
					akt_animation.getAktIndex());
		}

	}

	public void drawPreview(Graphics g) {

		g.drawImage(akt_animation.getImage(), 0, 0, 110, 110, null);

	}

	@Override
	public void drawDebug(Graphics g, float interpolation) {
		int interpolX = (int) ((x - lastX) * interpolation + lastX);
		int interpolY = (int) ((y - lastY) * interpolation + lastY);
		// Player hitbox
		g.setFont(new Font("helvetica", Font.PLAIN, 11));

		g.setColor(Color.RED);
		g.drawString(Integer.toString(health), interpolX + 30 - world.cameraX(), interpolY - world.cameraY() - 20);
		g.setColor(Color.GREEN);
		g.drawString(Integer.toString(hitTicks), interpolX + 40 - world.cameraX(), interpolY - world.cameraY() - 20);
		g.setColor(Color.BLACK);
		g.setFont(new Font("helvetica", Font.PLAIN, 11));
		g.drawString("rx=" + x + " | ry=" + y, 20, 40);
		g.drawString("vx=" + x / Frame.BLOCKSIZE + " | vy=" + y / Frame.BLOCKSIZE, 20, 55);

	}

	@Override
	public void tick() {
		super.tick();

		checkInput();
		checkActionPressEvent();
		move();
		checkTile();
		if (movingBlockInHand != null)
			movingBlockInHand.setPosition(x, y);

		if (activeWeapon != null)
			activeWeapon.tick();
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

	@Override
	public void applyDamage(Weapon weapon) {
		super.applyDamage(weapon);
	}

	private void checkInput() {

		if (!world.getKeyHandler().getCtrl()) {
			wantsToGoUp = world.getKeyHandler().getW();
			wantsToGoLeft = world.getKeyHandler().getA();
			wantsToGoDown = world.getKeyHandler().getS();
			wantsToGoRight = world.getKeyHandler().getD();
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
						wantsToGoUp = world.getKeyHandler().getW();
						break;
					case 1:
						wantsToGoRight = world.getKeyHandler().getD();
						break;
					case 2:
						wantsToGoDown = world.getKeyHandler().getS();
						break;
					case 3:
						wantsToGoLeft = world.getKeyHandler().getA();
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
		world.getKeyHandler().setActionpressed(false);
	}

	private void releaseMovingBlock() {
		movingBlockInHand.unBind();
		movingBlockInHand = null;
	}

	public void addToInventory(Item item, int position) {
		inventory.addItem(position, item);
	}

	public void hit() {

		if (activeWeapon != null && attackReady()) {
			activeWeapon.hit();
			hitTicks += activeWeapon.getCooldownTicks();
			for (Enemy enemy : world.getEnemies()) {
				if (activeWeapon.isInRange(x - world.cameraX(), y - world.cameraY(), rotation,
						new Rectangle((int) (enemy.getX() - world.cameraX()), (int) (enemy.getY() - world.cameraY()),
								Frame.BLOCKSIZE, Frame.BLOCKSIZE))) {
					enemy.applyDamage(activeWeapon);
				}
			}
		}

	}

	@Override
	protected double getExtremePosition(int direction) {
		if (movingBlockInHand == null) {
			return super.getExtremePosition(direction);
		} else {
			switch (direction % 4) {
			case 0:
				return movingBlockInHand.getY() - y - Frame.BLOCKSIZE / 2 + vSpeed;
			case 1:
				return movingBlockInHand.getX() - x + Frame.BLOCKSIZE / 2 + 1 + hSpeed; // +1: Verhindert den
																						// rechts-links-bug
			case 2:
				return movingBlockInHand.getY() - y + Frame.BLOCKSIZE / 2 + 1 + vSpeed;
			case 3:
				return movingBlockInHand.getX() - x - Frame.BLOCKSIZE / 2 + hSpeed;
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

		ArrayList<Item> itemsOnTile = world.getItemsAt(getTileY(), getTileX());

		for (Item i : itemsOnTile) {
			i.collectingEvent();
			world.removeItem(i);
		}

	}

	public void setWeapon(Weapon weapon) {
		this.activeWeapon = weapon;
	}

	public void drawInventory(Graphics2D g) {
		inventory.draw(g);

	}

	public void inventoryClick(int x, int y) {
		inventory.click(x, y);

	}

	@Override
	protected void kill() {
		world.backToTheOverworld(true);
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public void resetActionPressed() {

		world.getKeyHandler().setActionpressed(false);
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

}