package logics.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import data.Tetro;
import data.Weapon;
import data.Tiles.Tile;
import input.KeyHandler;
import loading.ImageLoader;
import logics.Camera;

/**
 * @author Lars Created on 05.08.2018
 */
public class Player extends MovingEntity {

	private KeyHandler keyHandler;

	private ArrayList<Enemy> enemies;
	private Weapon weapon;

	private int rotation = 0; // TODO rotaion

	public Player(int blockSize, Camera camera, boolean[][] tetroWorldHitbox, ArrayList<Enemy> enemies, KeyHandler keyHandler, Tile[][] tileWorld) {
		super(ImageLoader.loadImage("/res/character.png"), blockSize, camera, tetroWorldHitbox, tileWorld);
		this.keyHandler = keyHandler;
		this.enemies = enemies;

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

		g.drawImage(img, (int) (interpolX) - camera.getX(), (int) (interpolY) - camera.getY(), blockSize, blockSize, null);

		if (weapon != null)
			weapon.draw(g, rotation, (int) (interpolX) - camera.getX(), (int) (interpolY) - camera.getY(), debugMode);

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
		if (weapon != null) {
			weapon.hit();
			for (Enemy enemy : enemies) {
				if (weapon.isInRange(x - camera.getX(), y - camera.getY(),
						new Rectangle((int) enemy.getX() - camera.getX(), (int) enemy.getY() - camera.getY(), blockSize, blockSize))) {
					enemy.applyDamage(weapon);
				}
			}
		}

	}

	private void checkTile() {

		tileWorld[getTileY()][getTileX()].eventWhenEntering();

	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

}