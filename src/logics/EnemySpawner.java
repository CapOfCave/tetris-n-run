package logics;

import java.awt.Color;
import java.awt.Graphics;

import logics.entities.Entity;

public class EnemySpawner extends Entity {

	private int maxEnemy;
	private boolean enemyOnlyOnTetros;
	private double spawnChance;
	private boolean global = false;
	private int diameter;
	private int enemyCount = 0;

	public EnemySpawner(World world, int x, int y, int diameter, int maxEnemy, boolean enemyOnlyOnTetros, double d) {
		super(world, null, x, y);

		this.maxEnemy = maxEnemy;
		this.enemyOnlyOnTetros = enemyOnlyOnTetros;
		this.spawnChance = d;
		this.diameter = diameter;

	}

	public EnemySpawner(World world, int maxEnemy, boolean enemyOnlyOnTetros, int spawnChance) {
		this(world, -10, -10, -1, maxEnemy, enemyOnlyOnTetros, spawnChance);
		global = true;
	}

	public void draw(Graphics g, float interpolation, boolean debugMode) {

		if (debugMode) {
			g.setColor(Color.DARK_GRAY);
			g.fillRect((int) (x / world.cameraX()), (int) (y / world.cameraY()), world.blockSize(), world.blockSize());
		}

	}

	public void tick() {
		if (Math.random() < spawnChance / 100. && enemyCount < maxEnemy) {
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

			xPos = (int) (x - diameter / 2. + random(diameter));
			yPos = (int) (y - diameter / 2. + random(diameter));
		}
		if (world.getTileAt(yPos, xPos).isWalkable()) {
			System.out.println("Spawn2");
			if (enemyOnlyOnTetros) {
				if (world.isTetroAt(yPos, xPos))
					world.addEnemy(xPos * world.blockSize(), yPos * world.blockSize(), 10, this);
			} else {
				world.addEnemy(xPos * world.blockSize(), yPos * world.blockSize(), 10, this);
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
}
