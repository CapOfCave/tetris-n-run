package logics.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import loading.ImageLoader;
import logics.Inventory;
import logics.World;
import logics.entities.items.Item;
import logics.entities.items.Weapon;

/**
 * @author Lars Created on 05.08.2018
 */
public class Player extends MovingEntity {

	private Inventory inventory;
	private Weapon activWeapon;

	public Player(World world) {
		super(world, ImageLoader.loadImage("/res/character.png"));
		inventory = new Inventory();

		inventory.addItem(new Item(world, ImageLoader.loadImage("/res/blocka.png")));
		inventory.addItem(new Item(world, ImageLoader.loadImage("/res/blockb.png")));
		inventory.addItem(new Item(world, ImageLoader.loadImage("/res/blockc.png")));
		inventory.addItem(new Item(world, ImageLoader.loadImage("/res/blockd.png")));
		inventory.addItem(new Item(world, ImageLoader.loadImage("/res/blocke.png")));
		inventory.addItem(new Weapon(world, 20, ImageLoader.loadImage("/res/sword-in-hand.png"), ImageLoader.loadImage("/res/sword-hit.png"),
				new Point(0, 0), new Point(30, 5), 0, 30, 45));
		acc = 0.8;
		brake = 4;
		maxSpeed = 9;

	}

	public Player(World world, int playerX, int playerY) {
		this(world);
		x = playerX;
		y = playerY;
		lastX = x;
		lastY = y;
	}

	@Override
	public void draw(Graphics g, float interpolation, boolean debugMode) {
		float interpolX = (int) ((x - lastX) * interpolation + lastX);
		float interpolY = (int) ((y - lastY) * interpolation + lastY);

		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setColor(Color.blue);
		g2d.translate(interpolX - world.cameraX() + world.blockSize() / 2, interpolY - world.cameraY() + world.blockSize() / 2);
		g2d.rotate(Math.toRadians(rotation - 90));
		// g2d.drawImage(img, (int) (interpolX) - world.cameraX(), (int) (interpolY) - world.cameraY(), world.blockSize(),
		// world.blockSize(), null);
		g2d.drawImage(img, -world.blockSize() / 2, -world.blockSize() / 2, world.blockSize(), world.blockSize(), null);

		if (activWeapon != null)
			activWeapon.draw(g, g2d, -world.blockSize() / 2, -world.blockSize() / 2, debugMode);

		g2d.dispose();

		if (debugMode) {
			drawDebug(g, interpolX, interpolY);

		}

	}

	private void drawDebug(Graphics g, float interpolX, float interpolY) {
		// Player hitbox
		g.setColor(Color.ORANGE);
		g.fillOval((int) (interpolX - world.cameraX()), (int) (interpolY - world.cameraY()), 5, 5);
		g.fillOval((int) (interpolX - world.cameraX() + world.blockSize() - 1), (int) (interpolY - world.cameraY()), 5, 5);
		g.fillOval((int) (interpolX - world.cameraX() + world.blockSize() - 1), (int) (interpolY - world.cameraY() + world.blockSize() - 1), 5, 5);
		g.fillOval((int) (interpolX - world.cameraX()), (int) (interpolY - world.cameraY() + world.blockSize() - 1), 5, 5);

		g.setColor(Color.GREEN);
		g.fillOval((int) (interpolX + edgeTolerancePercentage * world.blockSize() / 100 - world.cameraX()), (int) (interpolY + vSpeed - world.cameraY()), 5, 5);
		g.fillOval((int) (interpolX - world.cameraX() + world.blockSize() - 1), (int) (interpolY - world.cameraY()), 5, 5);
		g.fillOval((int) (interpolX - world.cameraX() + world.blockSize() - 1), (int) (interpolY - world.cameraY() + world.blockSize() - 1), 5, 5);
		g.fillOval((int) (interpolX - world.cameraX()), (int) (interpolY - world.cameraY() + world.blockSize() - 1), 5, 5);

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 200, 33);
		g.setColor(Color.BLACK);
		g.drawString(" x=" + x + " |  y=" + y, 2, 15);
		g.drawString("dx=" + hSpeed + " | dy=" + vSpeed, 2, 30);

	}

	@Override
	public void tick() {
		lastX = x;
		lastY = y;

		checkInput();
		move();
		checkTile();
	}

	private void checkInput() {
		wantsToGoUp = world.getKeyHandler().getW();
		wantsToGoLeft = world.getKeyHandler().getA();
		wantsToGoDown = world.getKeyHandler().getS();
		wantsToGoRight = world.getKeyHandler().getD();

	}

	public void addToInventory(Item item, int position) {
		inventory.addItem(position, item);
	}

	public void hit() {
		if (activWeapon != null) {
			activWeapon.hit();
			for (Enemy enemy : world.getEnemies()) {
				if (activWeapon.isInRange(x - world.cameraX(), y - world.cameraY(), rotation,
						new Rectangle((int) (enemy.getX() - world.cameraX()), (int) (enemy.getY() - world.cameraY()), world.blockSize(), world.blockSize()))) {
					enemy.applyDamage(activWeapon);
				}
			}
		}

	}

	private void checkTile() {

		world.getTileAt(getTileY(),getTileX()).eventWhenEntering();
		// if (itemWorld != null) // TODO: l�schen
		// if (itemWorld[getTileY()][getTileX()] != null)
		// itemWorld[getTileY()][getTileX()].collectingEvent();

	}

	public void setWeapon(Weapon weapon) {
		this.activWeapon = weapon;
	}

	public void drawInventory(Graphics2D g) {
		inventory.draw(g);

	}

	public void inventoryClick(int x, int y) {
		inventory.click(x, y);

	}

}