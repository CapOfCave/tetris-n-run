package logics;

import java.awt.Graphics2D;
import java.util.ArrayList;


import data.Tetro;
import data.Tiles.Tile;


public class EnemySpawner {

	private ArrayList<Enemy> enemysInWorld;
	private int maxEnemy;
	private int maxX, maxY;
	private int blockSize;
	private Camera camera;
	private ArrayList<Tetro> worldTetros;
	private boolean[][] tetroWorldHitbox;
	private Tile[][] tileWorld;
	private Player player;
	
	public EnemySpawner(int maxEnemy, int maxX, int maxY, int blockSize, Camera camera, ArrayList<Tetro> worldTetros, boolean[][] tetroWorldHitbox, Tile[][] tileWorld){
		
		enemysInWorld = new ArrayList<>();
		this.maxEnemy = maxEnemy;
		
		this.maxX = maxX;
		this.maxY = maxY;
		this.blockSize = blockSize;
		this.camera = camera;
		this.worldTetros = worldTetros;
		this.tetroWorldHitbox = tetroWorldHitbox;
		this.tileWorld = tileWorld;
		
	}
	
	public boolean isEnemyHittingPlayer(double x, double y) {
		for(Enemy enemy: enemysInWorld) {
		 if(enemy.isHittingPlayer(x, y))
			 return true;
		}
		return false;
	}

	public void draw(Graphics2D g, float interpolation, boolean debugMode) {
		for(Enemy enemy: enemysInWorld)
			enemy.draw(g, interpolation, debugMode);
		
	}

	public void tick() {
		if(random(50) == 0 && (enemysInWorld.size() + 1) <= maxEnemy) {
			spawn();
		}
		
		for(Enemy enemy: enemysInWorld)
			enemy.tick();
		
	}
	
	public void spawn() {
		int xPos = random(maxX);
		int yPos = random(maxY);
		
		if(tileWorld[yPos / blockSize][xPos / blockSize].getKey() == '0' && player != null) {
			System.out.println("spawn");
			enemysInWorld.add(new Enemy(xPos, yPos, blockSize, camera, worldTetros, tetroWorldHitbox, tileWorld, player));
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
