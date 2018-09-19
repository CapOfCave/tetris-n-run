package logics.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import data.Tetro;
import data.Weapon;
import data.Tiles.Tile;
import loading.ImageLoader;
import logics.Camera;

public class Enemy extends MovingEntity {

	private Player player;

	private boolean isAlive = true;

	private boolean isAktive;

	public Enemy(int health, int blockSize, Camera camera, boolean[][] tetroWorldHitbox, Tile[][] tileWorld,
			Player player) {
		super(ImageLoader.loadImage("/res/character.png"), blockSize, camera, tetroWorldHitbox, tileWorld);

		this.player = player;
		this.health = health;

		acc = 0.8;
		brake = 4;
		maxSpeed = 9;
	}

	public Enemy(int enemyX, int enemyY, int health, int blockSize, Camera camera, ArrayList<Tetro> worldTetros, boolean[][] tetroWorldHitbox,
			Tile[][] tileWorld, Player player) {
		this(health, blockSize, camera, tetroWorldHitbox, tileWorld, player);
		x = enemyX;
		y = enemyY;
		lastX = x;
		lastY = y;
	}

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
		g2d.dispose();

		
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

		checkHealth();

		if (isAktive)
			aktionInPassiveMode();
		else
			aktionInPassiveMode();

		move();
	}

	public void aktionInAktivMode() {

	}

	public void aktionInPassiveMode() {
		if ((x - 5) <= player.getX() && (x + 5) >= player.getX() && (y - 5) <= player.getY() && (y + 5) >= player.getY()) {
			isAktive = true;
		} else {
			if (random(10) == 0) {
				wantsToGoDown = true;
				wantsToGoUp = false;

			} else if (random(10) == 0) {
				wantsToGoDown = false;
				wantsToGoUp = true;
			}

			if (random(10) == 0) {
				wantsToGoRight = true;
				wantsToGoLeft = false;

			} else if (random(10) == 0) {
				wantsToGoRight = false;
				wantsToGoLeft = true;
			}

		}

	}

	private void checkHealth() {
		if (health <= 0) {
			isAlive = false;
		}
	}

	public boolean isHittingPlayer(double x2, double y2) {
		return false;

	}

	public void applyDamage(Weapon weapon) {
		// TODO damage apply
		System.out.println("Ouch. That hurt");
		health -= weapon.getDamage();

	}


	public int random(int max) {
		return (int) (Math.random() * max);
	}

	public boolean isAlive() {
		return isAlive;
	}

}
