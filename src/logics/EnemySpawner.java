package logics;

import java.awt.Color;
import java.awt.Graphics;

import logics.entities.Entity;

public class EnemySpawner extends Entity {

	private static final long serialVersionUID = 1L;
	private int maxEnemy;
	private boolean enemyOnlyOnTetros;
	private double spawnChance;
	private boolean global = false;
	private int enemyCount = 0;
	int spawnOffsetLeft;
	int spawnOffsetTop;
	int spawnOffsetRight;
	int spawnOffsetBottom;

	public EnemySpawner(World world, int x, int y, int spawnOffsetLeft, int spawnOffsetTop, int spawnOffsetRight, int spawnOffsetBottom, int maxEnemy,
			boolean enemyOnlyOnTetros, double d) {
		super(world, null, x, y);

		this.maxEnemy = maxEnemy;
		this.enemyOnlyOnTetros = enemyOnlyOnTetros;
		this.spawnChance = d;
		this.spawnOffsetLeft = spawnOffsetLeft;
		this.spawnOffsetTop = spawnOffsetTop;
		this.spawnOffsetRight = spawnOffsetRight;
		this.spawnOffsetBottom = spawnOffsetBottom;
	}

	public EnemySpawner(World world, int maxEnemy, boolean enemyOnlyOnTetros, int spawnChance) {
		this(world, -10, -10, -1, -1, -1, -1, maxEnemy, enemyOnlyOnTetros, spawnChance);
		global = true;
	}

	public void draw(Graphics g, float interpolation, boolean debugMode) {

		if (debugMode) {
			g.setColor(Color.DARK_GRAY);
			g.fillRect((int) (x * world.blockSize() - world.cameraX()), (int) (y * world.blockSize() - world.cameraY()), world.blockSize(),
					world.blockSize());
		}

	}

	public void tick() {
		double r = Math.random();
		if (r < spawnChance / 100. && enemyCount < maxEnemy) {
			spawn();
		}

	}

	public void spawn() {
		int xPos;
		int yPos;
		if (global) {
			xPos = random(world.getMaxX());
			yPos = random(world.getMaxY());
		} else {

			xPos = (int) (x - spawnOffsetLeft + random(spawnOffsetRight + spawnOffsetLeft + 1));
			yPos = (int) (y - spawnOffsetTop + random(spawnOffsetBottom + spawnOffsetTop + 1));
		}

		if (enemyOnlyOnTetros) {
			if (world.isTetroAt(yPos, xPos) || world.getTileAt(yPos, xPos).isWalkable())
				world.addEnemy(xPos * world.blockSize(), yPos * world.blockSize(), 25, this);
			enemyCount++;
		} else {
			if (world.getTileAt(yPos, xPos).isWalkable()) {
				world.addEnemy(xPos * world.blockSize(), yPos * world.blockSize(), 10, this);
				enemyCount++;
			}
		}

	}

	public int random(int max) {
		return (int) (Math.random() * max);
	}

	public void enemyKilled() {
		enemyCount--;
		if (enemyCount < 0) {
			System.out.println("Fehler @EnemySpawner#enemyKilled");
		}
	}

	public int getMinX() {
		if (global) {
			return 0;
		} else {

			return (int) (x - spawnOffsetLeft);
		}

	}

	public int getMinY() {
		if (global) {
			return 0;
		} else {
			return (int) (y - spawnOffsetTop);
		}
	}

	public int getMaxX() {
		if (global) {
			return world.getMaxX();
		} else {
			return (int) (x + spawnOffsetRight);
		}
	}

	public int getMaxY() {
		if (global) {
			return world.getMaxY();
		} else {
			return (int) (y + spawnOffsetBottom);
		}
	}
}
