package logics.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import loading.ImageLoader;
import loading.ItemLoader;
import loading.ItemSaver;
import logics.Inventory;
import logics.World;
import logics.entities.items.Item;
import logics.entities.items.Weapon;

/**
 * @author Lars Created on 05.08.2018
 */
public class Player extends LivingEntity {

	private static final long serialVersionUID = 1L;
	private Inventory inventory;
	private Weapon activeWeapon;

	public Player(World world) {
		super(world, ImageLoader.loadImage("/res/character1.png"));
		inventory = new Inventory();

		Weapon weapon = new Weapon(world, 20, "/res/sword-in-hand.png", "/res/sword-hit.png", new Point(0, 0), new Point(30, 5), 8, 60, 60, 10);
		ItemSaver.writeItem("C:\\\\JavaEclipse\\\\weapon.txt", weapon);

		weapon = (Weapon) ItemLoader.readItem("C:\\\\JavaEclipse\\\\weapon.txt");

		weapon.setWorld(world);

		inventory.addItem(weapon);
		inventory.addItem(new Item(world, "/res/blocks/blocka.png"));
		inventory.addItem(new Item(world, "/res/blocks/blockb.png"));
		inventory.addItem(new Item(world, "/res/blocks/blockc.png"));
		inventory.addItem(new Item(world, "/res/blocks/blockd.png"));
		inventory.addItem(new Item(world, "/res/blocks/blocke.png"));

		acc = 0.8;
		brake = 4;
		maxSpeed = 9;
		health = 50;

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
		// g2d.drawImage(img, (int) (interpolX) - world.cameraX(), (int) (interpolY) - world.cameraY(),
		// world.blockSize(),
		// world.blockSize(), null);
		g2d.drawImage(img, -world.blockSize() / 2, -world.blockSize() / 2, world.blockSize(), world.blockSize(), null);

		if (activeWeapon != null)
			activeWeapon.draw(g2d, -world.blockSize() / 2, -world.blockSize() / 2, debugMode);

		if (debugMode) {
			drawDebug(g, g2d, interpolX, interpolY);

		}

		g2d.dispose();

	}

	private void drawDebug(Graphics g, Graphics2D g2d, float interpolX, float interpolY) {
		// Player hitbox
		g.setColor(Color.ORANGE);
		g2d.fillOval(-world.blockSize() / 2, -world.blockSize() / 2, 5, 5);
		g2d.fillOval(-world.blockSize() / 2, world.blockSize() / 2, 5, 5);
		g2d.fillOval(world.blockSize() / 2, world.blockSize() / 2, 5, 5);
		g2d.fillOval(world.blockSize() / 2, -world.blockSize() / 2, 5, 5);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 200, 33);
		g.setColor(Color.BLACK);
		g.drawString(" x=" + x + " |  y=" + y, 2, 15);
		g.drawString("dx=" + hSpeed + " | dy=" + vSpeed, 2, 30);

		g2d.setColor(Color.RED);
		g2d.drawString(Integer.toString(health), 10, -20);

	}

	@Override
	public void tick() {
		lastX = x;
		lastY = y;

		checkHealth();

		checkInput();
		move();
		checkTile();

		if (activeWeapon != null)
			activeWeapon.tick();
	}

	@Override
	public void applyDamage(Weapon weapon, int direction) {
		super.applyDamage(weapon, rotation);
		System.out.println("ouch");
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
		if (activeWeapon != null && activeWeapon.attackReady()) {
			activeWeapon.hit();
			for (Enemy enemy : world.getEnemies()) {
				if (activeWeapon.isInRange(x - world.cameraX(), y - world.cameraY(), rotation, new Rectangle((int) (enemy.getX() - world.cameraX()),
						(int) (enemy.getY() - world.cameraY()), world.blockSize(), world.blockSize()))) {
					enemy.applyDamage(activeWeapon, rotation);
				}
			}
		}

	}

	private void checkTile() {

		world.getTileAt(getTileY(), getTileX()).eventWhenEntering();

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
		System.out.println("U ded mate");
	}

}