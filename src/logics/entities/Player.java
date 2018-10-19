package logics.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

import data.Animation;
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

	public Player(World world, HashMap<String, Animation> anims) {
		super(world, anims);
		inventory = new Inventory();

		Weapon weapon = new Weapon(world, 20, "/res/anims/sword.txt", new Point(0, 0), new Point(30, 5), 8, 60, 60, 10);
		ItemSaver.writeItem("C:\\\\JavaEclipse\\\\weapon.txt", weapon);
		weapon = (Weapon) ItemLoader.readItem("C:\\\\JavaEclipse\\\\weapon.txt");

		weapon.setWorld(world);

		inventory.addItem(weapon);
		inventory.addItem(new Item(world, "/res/anims/item.txt"));
		inventory.addItem(new Item(world, "/res/anims/item.txt"));
		inventory.addItem(new Item(world, "/res/anims/item.txt"));
		inventory.addItem(new Item(world, "/res/anims/item.txt"));
		inventory.addItem(new Item(world, "/res/anims/item.txt"));

		acc = 0.8;
		brake = 4;
		maxSpeed = 9;
		health = 50;

		akt_animation = anims.get("walk1");
	}

	public Player(World world, int playerX, int playerY, HashMap<String, Animation> anims) {
		this(world, anims);
		x = playerX;
		y = playerY;
		lastX = x;
		lastY = y;
	}

	@Override
	public void draw(Graphics g, float interpolation, boolean debugMode) {
		int interpolX = (int) ((x - lastX) * interpolation + lastX);
		int interpolY = (int) ((y - lastY) * interpolation + lastY);

		g.drawImage(akt_animation.getImage(), interpolX - world.cameraX(), interpolY - world.cameraY(),
				world.blockSize(), world.blockSize(), null);

		if (activeWeapon != null)
			activeWeapon.draw(g, interpolX - world.cameraX(), interpolY - world.cameraY(), animation_key,
					akt_animation.getAnimFrame(), debugMode);

		if (debugMode) {
			drawDebug(g, interpolX, interpolY);

		}

	}

	private void drawDebug(Graphics g, int interpolX, int interpolY) {
		// Player hitbox
		g.setColor(Color.ORANGE);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 200, 33);
		g.setColor(Color.BLACK);
		g.drawString("Rot.:=" + rotation, 2, 15);

		g.setColor(Color.RED);
		g.drawString(Integer.toString(health), interpolX + 30, interpolY - 20);

	}

	@Override
	public void tick() {
		lastX = x;
		lastY = y;

		akt_animation.next();
		checkHealth();

		checkInput();
		move();
		checkTile();

		if (activeWeapon != null)
			activeWeapon.tick();
	}

	@Override
	public void applyDamage(Weapon weapon, int direction) {
		super.applyDamage(weapon, direction);
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
				if (activeWeapon.isInRange(x - world.cameraX(), y - world.cameraY(), rotation,
						new Rectangle((int) (enemy.getX() - world.cameraX()), (int) (enemy.getY() - world.cameraY()),
								world.blockSize(), world.blockSize()))) {
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
		world.backToTheOverworld();
	}

}