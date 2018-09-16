package logics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import data.Tetro;
import data.Tiles.Tile;
import input.KeyHandler;
import loading.ImageLoader;

public class Enemy {

	private double x, y;
	private double lastX, lastY;
	private BufferedImage img;
	private int blockSize;
	
	private ArrayList<Tetro> worldTetros;
	private boolean[][] worldTetroHitbox;

	private double hSpeed;
	private double vSpeed;

	private double acc = 0.8;
	private double brake = 4;
	private double maxSpeed = 9;

	protected Tile[][] tileWorld;
	
	public Enemy(int blockSize, ArrayList<Tetro> worldTetros, boolean[][] tetroWorldHitbox, KeyHandler keyHandler,
			Tile[][] tileWorld) {
		
		this.worldTetros = worldTetros;
		this.worldTetroHitbox = tetroWorldHitbox;
		this.blockSize = blockSize;
		this.tileWorld = tileWorld;
		img = ImageLoader.loadImage("/res/character.png");
		
	}
	
	public Enemy(int blockSize, ArrayList<Tetro> worldTetros, boolean[][] tetroWorldHitbox, int playerX, int playerY,
			KeyHandler keyHandler, Tile[][] tileWorld) {
		this(blockSize, worldTetros, tetroWorldHitbox, keyHandler, tileWorld);
		x = playerX;
		y = playerY;
		lastX = x;
		lastY = y;
	}
	
	public void draw(Graphics g, float interpolation, boolean debugMode) {
		float interpolX = (int) ((x - lastX) * interpolation + lastX);
		float interpolY = (int) ((y - lastY) * interpolation + lastY);
		g.drawImage(img, (int) (interpolX) , (int) (interpolY) , blockSize, blockSize, null);
		// g.drawImage(img, x * blockSize - camera.getX(), y * blockSize - camera.getY(), 40, 40, null);
		if (debugMode) {
			g.setColor(Color.ORANGE);
			g.fillOval((int) x , (int) y , 5, 5);
			g.fillOval((int) (x  + blockSize - 1), (int) y , 5, 5);
			g.fillOval((int) x  + blockSize - 1, (int) (y  + blockSize - 1), 5, 5);
			g.fillOval((int) x , (int) (y  + blockSize - 1), 5, 5);

			g.setColor(Color.WHITE);
			g.fillRect(0, 0, 200, 33);
			g.setColor(Color.BLACK);
			g.drawString(" x=" + x + " |  y=" + y, 2, 15);
			g.drawString("dx=" + hSpeed + " | dy=" + vSpeed, 2, 30);
			
		}
	}
	
	
	
	
	public BufferedImage getImage() {
		return img;
	}
	
}
