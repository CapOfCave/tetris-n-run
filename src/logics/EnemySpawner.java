package logics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import data.Tetro;
import data.Tiles.Tile;
import logics.entities.Enemy;
import logics.entities.Entity;
import logics.entities.Player;

public class EnemySpawner extends Entity {

	private int maxEnemy;
	private int maxX, maxY;
	private boolean enemyOnlyOnTetros;
	private int spawnChance;
	private boolean global = false;

	private Player player;
	private Camera camera;

	private ArrayList<Tetro> worldTetros;
	private boolean[][] tetroWorldHitbox;
	private Tile[][] tileWorld;
	ArrayList<Enemy> enemies;
	private int diameter;

	public EnemySpawner(int x, int y, int maxEnemy, boolean enemyOnlyOnTetros, int spawnChance, int maxX, int maxY, int blockSize, Camera camera,
			ArrayList<Tetro> worldTetros, boolean[][] tetroWorldHitbox, Tile[][] tileWorld, ArrayList<Enemy> enemies, int diameter) {
		super(null, blockSize, x, y);

		this.enemies = enemies;
		this.maxEnemy = maxEnemy;
		this.maxX = maxX;
		this.maxY = maxY;
		this.camera = camera;
		this.worldTetros = worldTetros;
		this.tetroWorldHitbox = tetroWorldHitbox;
		this.tileWorld = tileWorld;
		this.enemyOnlyOnTetros = enemyOnlyOnTetros;
		this.spawnChance = spawnChance;
		this.diameter = diameter;

	}

	public EnemySpawner(int maxEnemy, boolean enemyOnlyOnTetros, int spawnChance, int maxX, int maxY, int blockSize, Camera camera,
			ArrayList<Tetro> worldTetros, boolean[][] tetroWorldHitbox, Tile[][] tileWorld, ArrayList<Enemy> enemies) {
		this(-10, -10, maxEnemy, enemyOnlyOnTetros, spawnChance, maxX, maxY, blockSize, camera, worldTetros, tetroWorldHitbox, tileWorld, enemies,
				-1);
		global = true;
	}

	public void draw(Graphics g, float interpolation, boolean debugMode) {

		if (debugMode) {
			g.setColor(Color.DARK_GRAY);
			g.fillRect((int) x / camera.getX(), (int) y / camera.getY(), blockSize, blockSize);
		}

	}

	public void tick() {
		if (Math.random() < spawnChance / 100 && (enemies.size()) < maxEnemy) {
			spawn();
		}

	}

	public void spawn() {
		int xPos;
		int yPos;
		if (global) {
			xPos = random(maxX);
			yPos = random(maxY);
		} else {

			xPos = (int) (x - diameter / 2. + random(diameter));
			yPos = (int) (y - diameter / 2. + random(diameter));
		}
		System.out.println("spawn " + xPos + "|" + yPos + "; " + player + "; " + tileWorld[yPos][xPos].getKey());
		if (tileWorld[yPos][xPos].getKey() == '0' && player != null) {
			System.out.println("spawn??aaaaaaaaaddd");
			if (enemyOnlyOnTetros) {
				if (tetroWorldHitbox[yPos][xPos])
					enemies.add(new Enemy(xPos* blockSize, yPos * blockSize, 10, blockSize, camera, worldTetros, tetroWorldHitbox, tileWorld, player));
			} else {
				enemies.add(new Enemy(xPos* blockSize, yPos* blockSize, 10, blockSize, camera, worldTetros, tetroWorldHitbox, tileWorld, player));
			}

		}

	}

	public int random(int max) {
		return (int) (Math.random() * max);
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}
