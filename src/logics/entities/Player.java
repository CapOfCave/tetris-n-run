package logics.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import data.Tetro;
import data.Weapon;
import data.Tiles.Tile;
import input.KeyHandler;
import loading.ImageLoader;
import logics.Camera;
import logics.Inventory;

/**
 * @author Lars Created on 05.08.2018
 */
public class Player extends MovingEntity {

	private KeyHandler keyHandler;

	private ArrayList<Enemy> enemies;
	private Inventory inventory;
	private Weapon activWeapon;

	public Player(int blockSize, Camera camera, boolean[][] tetroWorldHitbox, ArrayList<Enemy> enemies, KeyHandler keyHandler, Tile[][] tileWorld) {
		super(ImageLoader.loadImage("/res/character.png"), blockSize, camera, tetroWorldHitbox, tileWorld);
		this.keyHandler = keyHandler;
		this.enemies = enemies;
		inventory = new Inventory();

		acc = 0.8;
		brake = 4;
		maxSpeed = 9;

	}

	public Player(int blockSize, Camera camera, ArrayList<Tetro> worldTetros, boolean[][] tetroWorldHitbox, ArrayList<Enemy> enemies, int playerX,
			int playerY, KeyHandler keyHandler, Tile[][] tileWorld) {
		this(blockSize, camera, tetroWorldHitbox, enemies, keyHandler, tileWorld);
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
		g2d.translate(interpolX - camera.getX() + blockSize / 2, interpolY - camera.getY() + blockSize / 2);
		g2d.rotate(Math.toRadians(rotation - 90));
		// g2d.drawImage(img, (int) (interpolX) - camera.getX(), (int) (interpolY) - camera.getY(), blockSize,
		// blockSize, null);
		g2d.drawImage(img, -blockSize / 2, -blockSize / 2, blockSize, blockSize, null);

		if (activWeapon != null)
			
			activWeapon.draw(g, g2d, - blockSize / 2, - blockSize / 2, debugMode);

		g2d.dispose();

		if (debugMode) {
			drawDebug(g, interpolX, interpolY);

		}

	}

	private void drawDebug(Graphics g, float interpolX, float interpolY) {
		// Player hitbox
		g.setColor(Color.ORANGE);
		g.fillOval((int) (interpolX - camera.getX()), (int) (interpolY - camera.getY()), 5, 5);
		g.fillOval((int) (interpolX - camera.getX() + blockSize - 1), (int) (interpolY - camera.getY()), 5, 5);
		g.fillOval((int) (interpolX - camera.getX() + blockSize - 1), (int) (interpolY - camera.getY() + blockSize - 1), 5, 5);
		g.fillOval((int) (interpolX - camera.getX()), (int) (interpolY - camera.getY() + blockSize - 1), 5, 5);

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 200, 33);
		g.setColor(Color.BLACK);
		g.drawString(" x=" + x + " |  y=" + y, 2, 15);
		g.drawString("dx=" + hSpeed + " | dy=" + vSpeed, 2, 30);


		
		// if (weapon != null)
		// for (int dx = -300; dx <= 300; dx++) {
		// for (int dy = -100; dy <= 100; dy++) {
		// if (weapon.isInRange(interpolX - camera.getX(), interpolY - camera.getY(),
		// new Rectangle((int) (interpolX - camera.getX() + dx), (int) (interpolY - camera.getY() + dy), 0, 0))) {
		// g.drawOval((int) (interpolX - camera.getX() + dx), (int) (interpolY - camera.getY() + dy), 1, 1);
		// }
		// }
		// }

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
		wantsToGoUp = keyHandler.getW();
		wantsToGoLeft = keyHandler.getA();
		wantsToGoDown = keyHandler.getS();
		wantsToGoRight = keyHandler.getD();

	}

	public void hit() {
		if (activWeapon != null) {
			activWeapon.hit();
			for (Enemy enemy : enemies) {

			
				if (activWeapon.isInRange(x - camera.getX(), y - camera.getY(), rotation,

						new Rectangle((int) enemy.getX() - camera.getX(), (int) enemy.getY() - camera.getY(), blockSize, blockSize))) {
					enemy.applyDamage(activWeapon);
				}
			}
		}

	}

	private void checkTile() {

		tileWorld[getTileY()][getTileX()].eventWhenEntering();

	}

	public void setWeapon(Weapon weapon) {
		this.activWeapon = weapon;
	}

	public void drawInventory(Graphics2D g) {
		inventory.draw(g);
		
	}

}