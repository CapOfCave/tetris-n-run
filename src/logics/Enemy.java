package logics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import data.Tetro;
import data.Weapon;
import data.Tiles.Tile;
import input.KeyHandler;
import loading.ImageLoader;

public class Enemy {

	private double x, y;
	private double lastX, lastY;
	private BufferedImage img;
	private Camera camera;
	private int blockSize;
	
	private ArrayList<Tetro> worldTetros;
	private boolean[][] worldTetroHitbox;

	private double hSpeed;
	private double vSpeed;

	private double acc = 0.8;
	private double brake = 4;
	private double maxSpeed = 9;

	protected Tile[][] tileWorld;
	
	public Enemy(int blockSize,Camera camera, ArrayList<Tetro> worldTetros, boolean[][] tetroWorldHitbox, Tile[][] tileWorld) {
		
		this.worldTetros = worldTetros;
		this.camera = camera;
		this.worldTetroHitbox = tetroWorldHitbox;
		this.blockSize = blockSize;
		this.tileWorld = tileWorld;
		img = ImageLoader.loadImage("/res/character.png");
		
	}
	
	public Enemy(int enemyX, int enemyY,int blockSize,Camera camera, ArrayList<Tetro> worldTetros, boolean[][] tetroWorldHitbox, 
			 Tile[][] tileWorld) {
		this(blockSize,camera, worldTetros, tetroWorldHitbox, tileWorld);
		x = enemyX;
		y = enemyY;
		lastX = x;
		lastY = y;
	}
	
	public void draw(Graphics g, float interpolation, boolean debugMode) {
		float interpolX = (int) ((x - lastX) * interpolation + lastX);
		float interpolY = (int) ((y - lastY) * interpolation + lastY);
		g.drawImage(img, (int) (interpolX) - camera.getX(), (int) (interpolY) - camera.getY(), blockSize, blockSize, null);

		

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
	
	
	
	
	public BufferedImage getImage() {
		return img;
	}

	public boolean isHittingPlayer(double x2, double y2) {
		return false;
		
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void applyDamage(Weapon weapon) {
		//TODO damage apply
		System.out.println("Ouch. That hurt");
	}
	
}
