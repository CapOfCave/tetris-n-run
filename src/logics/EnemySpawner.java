package logics;

import java.awt.Color;
import java.awt.Graphics;

import logics.entities.Entity;
import logics.worlds.GameWorld;
import logics.worlds.World;

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

	public EnemySpawner(World world, int x, int y, int spawnOffsetLeft, int spawnOffsetTop, int spawnOffsetRight,
			int spawnOffsetBottom, int maxEnemy, boolean enemyOnlyOnTetros, double d) {
		super(world, x, y, null);

		this.maxEnemy = maxEnemy;
		this.enemyOnlyOnTetros = enemyOnlyOnTetros;
		this.spawnChance = d;
		this.spawnOffsetLeft = spawnOffsetLeft;
		this.spawnOffsetTop = spawnOffsetTop;
		this.spawnOffsetRight = spawnOffsetRight;
		this.spawnOffsetBottom = spawnOffsetBottom;
	}

	public EnemySpawner(GameWorld world, int maxEnemy, boolean enemyOnlyOnTetros, int spawnChance) {
		this(world, -10, -10, -1, -1, -1, -1, maxEnemy, enemyOnlyOnTetros, spawnChance);
		global = true;
	}

	@Override
	public void draw(Graphics g, float interpolation, boolean debugMode) {

		if (debugMode) {
			g.setColor(Color.DARK_GRAY);
			g.fillRect((int) (x * world.blockSize() - world.cameraX()), (int) (y * world.blockSize() - world.cameraY()),
					world.blockSize(), world.blockSize());
			for (int i = (int) (x - spawnOffsetLeft); i < (int) (x - spawnOffsetLeft + spawnOffsetRight
					+ spawnOffsetLeft + 1); i++) {
				for (int j = (int) (y - spawnOffsetTop); j < (int) (y - spawnOffsetTop + spawnOffsetBottom
						+ spawnOffsetTop + 1); j++) {
					g.drawLine(i * world.blockSize() - world.cameraX(), j * world.blockSize() - world.cameraY(),
							(i + 1) * world.blockSize() - world.cameraX(),
							(j + 1) * world.blockSize() - world.cameraY());
				}
			}
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
			if ((world.isTetroAt(yPos, xPos) && world.getTileAt(yPos, xPos).isWalkableWithTetro())
					|| world.getTileAt(yPos, xPos).isWalkable()) {
				world.addEnemy(xPos * world.blockSize(), yPos * world.blockSize(), 25, this);
				enemyCount++;
			}
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
			System.err.println("Fehler @EnemySpawner#enemyKilled");
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

	public int getLoff() {
		return spawnOffsetLeft;
	}

	public int getToff() {
		return spawnOffsetTop;
	}

	public int getRoff() {
		return spawnOffsetRight;
	}

	public int getBoff() {
		return spawnOffsetBottom;
	}

	public int getMax() {
		return maxEnemy;
	}

	public boolean getTetroonly() {
		return enemyOnlyOnTetros;
	}

	public double getRate() {
		return spawnChance;
	}
}
