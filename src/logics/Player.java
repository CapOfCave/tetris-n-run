package logics;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import data.Tetro;
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
	private ArrayList<Tetro>[][] worldTetroHitbox;

	private double hSpeed;
	private double vSpeed;
	private double acc = 0.5;
	private double brake = 0.8;

	public Player(int blockSize, Camera camera, ArrayList<Tetro> worldTetros, ArrayList<Tetro>[][] worldTetroHitbox, KeyHandler keyHandler) {
		this.camera = camera;
		this.worldTetros = worldTetros;
		this.worldTetroHitbox = worldTetroHitbox;
		this.blockSize = blockSize;
		this.keyHandler = keyHandler;
		img = ImageLoader.loadImage("/res/character.png");

	}

	public Player(int blockSize, Camera camera, ArrayList<Tetro> worldTetros, ArrayList<Tetro>[][] worldTetroHitbox, int playerX, int playerY,
			KeyHandler keyHandler) {
		this(blockSize, camera, worldTetros, worldTetroHitbox, keyHandler);
		x = playerX;
		y = playerY;
		lastX = x;
		lastY = y;
	}

	public BufferedImage getImage() {
		return img;
	}

	public void draw(Graphics g, float interpolation) {
		float interpolX = (int) ((x - lastX) * interpolation + lastX);
		float interpolY = (int) ((y - lastY) * interpolation + lastY);
		g.drawImage(img, (int) (interpolX) - camera.getX(), (int) (interpolY) - camera.getY(), blockSize, blockSize, null);
		// g.drawImage(img, x * blockSize - camera.getX(), y * blockSize - camera.getY(), 40, 40, null);

	}

	public void tick() {
		lastX = x;
		lastY = y;

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
		
		x += hSpeed;
		y += vSpeed;
	}

	public void move() {

	}

	public Point getXY() {
		return new Point((int) x, (int) y);
	}

	public double getY() {
		return y;
	}

	public double getX() {
		return x;
	}

	public int getIntX() {
		return (int) x;
	}

	public int getIntY() {
		return (int) y;
	}
}
