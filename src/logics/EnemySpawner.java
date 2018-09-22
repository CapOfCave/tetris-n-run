package logics;

import java.awt.Graphics2D;
import java.util.ArrayList;

import data.Tetro;
import data.Tiles.Tile;
import logics.entities.Enemy;
import logics.entities.Player;

public class EnemySpawner {

	private int maxEnemy;
	private int maxX, maxY;
	private int blockSize;
	private boolean enemyOnlyOnTetros;
	private int spawnChance;

	private Player player;
	private Camera camera;

	private ArrayList<Tetro> worldTetros;
	private boolean[][] tetroWorldHitbox;
	private Tile[][] tileWorld;
	private ArrayList<Enemy> enemysInWorld;

	public EnemySpawner(int maxEnemy, boolean enemyOnlyOnTetros, int spawnChance, int maxX, int maxY, int blockSize, Camera camera,
			ArrayList<Tetro> worldTetros, boolean[][] tetroWorldHitbox, Tile[][] tileWorld) {

		enemysInWorld = new ArrayList<>();
		this.maxEnemy = maxEnemy;
		this.maxX = maxX;
		this.maxY = maxY;
		this.blockSize = blockSize;
		this.camera = camera;
		this.worldTetros = worldTetros;
		this.tetroWorldHitbox = tetroWorldHitbox;
		this.tileWorld = tileWorld;
		this.enemyOnlyOnTetros = enemyOnlyOnTetros;
		this.spawnChance = spawnChance;

	}

	public boolean isEnemyHittingPlayer(double x, double y) {
		for (Enemy enemy : enemysInWorld) {
			if (enemy.isHittingPlayer(x, y))
				return true;
		}
		return false;
	}

	public void draw(Graphics2D g, float interpolation, boolean debugMode) {
		for (Enemy enemy : enemysInWorld)

			enemy.draw(g, interpolation, debugMode);

	}

	public void tick() {
		if (random(spawnChance) == 0 && (enemysInWorld.size() + 1) <= maxEnemy) {
			spawn();
		}

		for (int i = 0; i < enemysInWorld.size(); i++) {
			enemysInWorld.get(i).tick();
			if (!enemysInWorld.get(i).isAlive())
				enemysInWorld.remove(i);

		}

	}

	public void spawn() {
		int xPos = random(maxX);
		int yPos = random(maxY);

		if (tileWorld[yPos / blockSize][xPos / blockSize].getKey() == '0' && player != null) {
			if (enemyOnlyOnTetros) {
				if (tetroWorldHitbox[yPos / blockSize][xPos / blockSize])
					enemysInWorld.add(new Enemy(xPos, yPos, 10, blockSize, camera, worldTetros, tetroWorldHitbox, tileWorld, player));
			} else {
				enemysInWorld.add(new Enemy(xPos, yPos, 10, blockSize, camera, worldTetros, tetroWorldHitbox, tileWorld, player));
			}

		}

	}

	public int random(int max) {
		return (int) (Math.random() * max);
	}

	public ArrayList<Enemy> getEnemyList() {
		return enemysInWorld;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}
