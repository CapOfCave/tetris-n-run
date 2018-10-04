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
	private int imgIndex = 0;
	private int zahl = 0;

	public Player(World world) {
		super(world, ImageLoader.loadImage("/res/character/characterLeft.png"));
		inventory = new Inventory();

		Weapon weapon = new Weapon(world, 20, "/res/sword-in-hand.png", "/res/sword-hit.png", new Point(0, 0), new Point(30, 5), 8, 60, 45);
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
		g2d.drawImage(img, -world.blockSize() / 2, -world.blockSize() / 2, world.blockSize(), world.blockSize(), imgIndex, 0, imgIndex + 32, 32, null);

		
		
		if (activeWeapon != null)
			activeWeapon.draw(g, g2d, -world.blockSize() / 2, -world.blockSize() / 2, debugMode);

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
		
		//if(zahl == 3) {
		//if(!((imgIndex + 32) >= img.getWidth()))
		//	imgIndex += 32;
		//else
		//	imgIndex = 0;
		
		//zahl = 0;
		//}
		zahl++;
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
		if (activeWeapon != null) {
			activeWeapon.hit();
			for (Enemy enemy : world.getEnemies()) {
				if (activeWeapon.isInRange(x - world.cameraX(), y - world.cameraY(), rotation, new Rectangle((int) (enemy.getX() - world.cameraX()),
						(int) (enemy.getY() - world.cameraY()), world.blockSize(), world.blockSize()))) {
					enemy.applyDamage(activeWeapon);
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
		// TODO Auto-generated method stub
		
	}

}