package logics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import data.Tetro;
import data.Weapon;
import data.Tiles.Tile;
import input.KeyHandler;
import loading.ImageLoader;

/**
 * @author Lars Created on 05.08.2018
 */
public class Player {
	private double x, y;
	private double lastX, lastY;
	private BufferedImage img;
	private int blockSize;
	private Camera camera;
	private KeyHandler keyHandler;

	private ArrayList<Tetro> worldTetros;
	private boolean[][] worldTetroHitbox;
	private Weapon weapon;

	private double hSpeed;
	private double vSpeed;

	private double acc = 0.8;
	private double brake = 4;
	private double maxSpeed = 9;

	private int rotation = 0; // TODO rotaion

	protected Tile[][] tileWorld;

	public Player(int blockSize, Camera camera, ArrayList<Tetro> worldTetros, boolean[][] tetroWorldHitbox, KeyHandler keyHandler,
			Tile[][] tileWorld) {
		this.camera = camera;
		this.worldTetros = worldTetros;
		this.worldTetroHitbox = tetroWorldHitbox;
		this.blockSize = blockSize;
		this.keyHandler = keyHandler;
		this.tileWorld = tileWorld;
		img = ImageLoader.loadImage("/res/character.png");

	}

	public Player(int blockSize, Camera camera, ArrayList<Tetro> worldTetros, boolean[][] tetroWorldHitbox, int playerX, int playerY,
			KeyHandler keyHandler, Tile[][] tileWorld) {
		this(blockSize, camera, worldTetros, tetroWorldHitbox, keyHandler, tileWorld);
		x = playerX;
		y = playerY;
		lastX = x;
		lastY = y;
	}

	public BufferedImage getImage() {
		return img;
	}

	public void draw(Graphics g, float interpolation, boolean debugMode) {
		float interpolX = (int) ((x - lastX) * interpolation + lastX);
		float interpolY = (int) ((y - lastY) * interpolation + lastY);
		g.drawImage(img, (int) (interpolX) - camera.getX(), (int) (interpolY) - camera.getY(), blockSize, blockSize, null);

		if (weapon != null)
			weapon.draw(g, rotation, (int) (interpolX) - camera.getX(), (int) (interpolY) - camera.getY());

		if (debugMode) {
			drawDebug(g);

		}
	}

	private void drawDebug(Graphics g) {
		g.setColor(Color.ORANGE);
		g.fillOval((int) (x - camera.getX()), (int) (y - camera.getY()), 5, 5);
		g.fillOval((int) (x - camera.getX() + blockSize - 1), (int) (y - camera.getY()), 5, 5);
		g.fillOval((int) (x - camera.getX() + blockSize - 1), (int) (y - camera.getY() + blockSize - 1), 5, 5);
		g.fillOval((int) (x - camera.getX()), (int) (y - camera.getY() + blockSize - 1), 5, 5);

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 200, 33);
		g.setColor(Color.BLACK);
		g.drawString(" x=" + x + " |  y=" + y, 2, 15);
		g.drawString("dx=" + hSpeed + " | dy=" + vSpeed, 2, 30);

	}

	public void tick() {
		lastX = x;
		lastY = y;

		move();
	}

	public void hit() {
		if (weapon != null)
			weapon.hit();
	}

	private void move() {
		// Beginn der Bewegung

		// Rechts-Links-Movement
		double abs_hSpeed = Math.abs(hSpeed);
		if (keyHandler.getA() && !keyHandler.getD()) {
			hSpeed -= acc;
			if (hSpeed > 0) {
				hSpeed -= brake;
			}
		} else if (!keyHandler.getA() && keyHandler.getD()) {
			hSpeed += acc;
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
		if (keyHandler.getW() && !keyHandler.getS()) {
			vSpeed -= acc;
			if (vSpeed > 0) {
				vSpeed -= brake;
			}
		} else if (!keyHandler.getW() && keyHandler.getS()) {
			vSpeed += acc;
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

		// Vertikal
		// nach oben-movement (TL-TR)
		if (vSpeed < 0)
			if (!isRelAccessible(-blockSize / 2 + vSpeed, -blockSize / 2) || !isRelAccessible(-blockSize / 2 + vSpeed, blockSize / 2 - 1)) {
				vSpeed = 0;
				move_contact_solid(0);
			}
		// nach unten-movement (BL-BR)
		if (vSpeed > 0)
			if (!isRelAccessible(blockSize / 2 - 1 + vSpeed, -blockSize / 2) || !isRelAccessible(blockSize / 2 - 1 + vSpeed, blockSize / 2 - 1)) {
				vSpeed = 0;
				move_contact_solid(2);
			}
		// Horizontal
		// nach links-movement (TL-BL)
		if (hSpeed < 0)
			if (!isRelAccessible(-blockSize / 2, -blockSize / 2 + hSpeed) || !isRelAccessible(blockSize / 2 - 1, -blockSize / 2 + hSpeed)) {
				hSpeed = 0;
				move_contact_solid(3);
			}
		// nach rechts-movement (TR-BR)
		if (hSpeed > 0)
			if (!isRelAccessible(-blockSize / 2, blockSize / 2 - 1 + hSpeed) || !isRelAccessible(blockSize / 2 - 1, blockSize / 2 - 1 + hSpeed)) {
				hSpeed = 0;
				move_contact_solid(1);
			}

		double gesSpeed = Math.sqrt(hSpeed * hSpeed + vSpeed * vSpeed);
		if (gesSpeed > maxSpeed) {
			double factor = maxSpeed / gesSpeed;
			hSpeed = hSpeed * factor;
			vSpeed = vSpeed * factor;
		}

		x += hSpeed;
		y += vSpeed;
		checkTile();
	}

	private void move_contact_solid(int i) {
		switch (i) {
		case 0:
			y = getTileY() * blockSize;
			break;
		case 1:
			x = getTileX() * blockSize;
			break;
		case 2:
			y = getTileY() * blockSize;
			break;
		case 3:
			x = getTileX() * blockSize;
			break;
		}

	}

	private boolean isRelAccessible(double dy, double dx) {
		return worldTetroHitbox[getTileY(dy)][getTileX(dx)];
	}

	private Tile getRelTile(double dy, double dx) {
		return getTile(getTileY(dy), getTileX(dx));
	}

	private Tile getTile(int y, int x) {
		return tileWorld[y][x];
	}

	private void checkTile() {

		getRelTile(0, 0).eventWhenEntering();

	}

	public Point getXY() {
		return new Point(getTileX(), getTileY());
	}

	public double getY() {
		return y;
	}

	public double getX() {
		return x;
	}

	private int getTileX(double dx) {
		return (int) ((x + dx + blockSize / 2) / blockSize);
	}

	private int getTileY(double dy) {
		return (int) ((y + dy + blockSize / 2) / blockSize);
	}

	public int getTileX() {
		return (int) ((x + blockSize / 2) / blockSize);
	}

	public int getTileY() {
		return (int) ((y + blockSize / 2) / blockSize);
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

}