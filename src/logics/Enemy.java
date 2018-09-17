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
	private double maxX, maxY;
	private BufferedImage img;
	private Camera camera;
	private int blockSize;
	
	private ArrayList<Tetro> worldTetros;
	private boolean[][] worldTetroHitbox;
	private Player player;

	private double hSpeed;
	private double vSpeed;

	private double acc = 0.8;
	private double brake = 4;
	private double maxSpeed = 9;
	
	private boolean isAktive;
	private boolean wantsToGoUp = false, wantsToGoDown = false, wantsToGoLeft = false, wantsToGoRight = false;

	protected Tile[][] tileWorld;
	
	public Enemy(int blockSize,Camera camera, ArrayList<Tetro> worldTetros, boolean[][] tetroWorldHitbox, Tile[][] tileWorld, Player player) {
		
		this.worldTetros = worldTetros;
		this.camera = camera;
		this.worldTetroHitbox = tetroWorldHitbox;
		this.blockSize = blockSize;
		this.tileWorld = tileWorld;
		this.player = player;
		
		maxX = (tileWorld[0].length * blockSize)  - blockSize;
		maxY = (tileWorld.length * blockSize) - blockSize;
		
		img = ImageLoader.loadImage("/res/character.png");
		
	}
	
	public Enemy(int enemyX, int enemyY,int blockSize,Camera camera, ArrayList<Tetro> worldTetros, boolean[][] tetroWorldHitbox, 
			 Tile[][] tileWorld, Player player) {
		this(blockSize,camera, worldTetros, tetroWorldHitbox, tileWorld, player);
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
	
	public void tick() {
		lastX = x;
		lastY = y;
		
		if(isAktive)
			aktionInPassiveMode();
		else
			aktionInPassiveMode();
		
		move();
	}
	
	public void aktionInAktivMode() {
		
		
	}
	
	public void aktionInPassiveMode() {
		if((x - 5) <= player.getX() && (x + 5) >= player.getX() 
				&& (y - 5) <= player.getY() && (y + 5) >= player.getY()) {
			isAktive = true;
		}else {
			if(random(10) == 0) {
				wantsToGoDown = true;
				wantsToGoUp = false;
				
			}else if(random(10) == 0) {
				wantsToGoDown = false;
				wantsToGoUp = true;
			}
			
			if(random(10) == 0) {
				wantsToGoRight = true;
				wantsToGoLeft = false;
				
			} else if(random(10) == 0) {
				wantsToGoRight = false;
				wantsToGoLeft = true;
			}
			
		}
		
	}
	
	private void move() {
		// Beginn der Bewegung

		// Rechts-Links-Movement
		double abs_hSpeed = Math.abs(hSpeed);
		if (wantsToGoLeft && !wantsToGoRight) {
			hSpeed -= acc;
			if (hSpeed > 0) {
				hSpeed -= brake;
			}
		} else if (!wantsToGoLeft && wantsToGoRight) {
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
		if (wantsToGoUp && !wantsToGoDown) {
			vSpeed -= acc;
			if (vSpeed > 0) {
				vSpeed -= brake;
			}
		} else if (!wantsToGoUp && wantsToGoDown) {
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

		// Vertikal
		// nach oben-movement (TL-TR)
		if (vSpeed < 0)
			if (!isRelAccessible(-blockSize / 2 + vSpeed, -blockSize / 2) || !isRelAccessible(-blockSize / 2 + vSpeed, blockSize / 2 - 1)) {
				vSpeed = 0;
				move_contact_solid(0);
			}
		// nach unten-movement (BL-BR)
		if (vSpeed > 0)
			if (!isRelAccessible(blockSize / 2 - 1 + vSpeed, -blockSize / 2) || !isRelAccessible(blockSize / 2 - 1 + vSpeed, blockSize / 2 - 1)) {
				vSpeed = 0;
				move_contact_solid(2);
			}
		// Horizontal
		// nach links-movement (TL-BL)
		if (hSpeed < 0)
			if (!isRelAccessible(-blockSize / 2, -blockSize / 2 + hSpeed) || !isRelAccessible(blockSize / 2 - 1, -blockSize / 2 + hSpeed)) {
				hSpeed = 0;
				move_contact_solid(3);
			}
		// nach rechts-movement (TR-BR)
		if (hSpeed > 0)
			if (!isRelAccessible(-blockSize / 2, blockSize / 2 - 1 + hSpeed) || !isRelAccessible(blockSize / 2 - 1, blockSize / 2 - 1 + hSpeed)) {
				hSpeed = 0;
				move_contact_solid(1);
			}

		double gesSpeed = Math.sqrt(hSpeed * hSpeed + vSpeed * vSpeed);
		if (gesSpeed > maxSpeed) {
			double factor = maxSpeed / gesSpeed;
			hSpeed = hSpeed * factor;
			vSpeed = vSpeed * factor;
		}

		x += hSpeed;
		y += vSpeed;
		
	}
	
	private void move_contact_solid(int i) {
		switch (i) {
		case 0:
			y = getTileY() * blockSize;
			break;
		case 1:
			x = getTileX() * blockSize;
			break;
		case 2:
			y = getTileY() * blockSize;
			break;
		case 3:
			x = getTileX() * blockSize;
			break;
		}

	}

	private boolean isRelAccessible(double dy, double dx) {
		
		
		if((x + dx) >= maxX && (y + dy) >= maxY)
			return false;
		
		 return worldTetroHitbox[getTileY(dy)][getTileX(dx)];
	}
	
	public BufferedImage getImage() {
		return img;
	}

	public boolean isHittingPlayer(double x2, double y2) {
		return false;
		
	}
	
	private int getTileX(double dx) {
		return (int) ((x + dx + blockSize / 2) / blockSize);
	}

	private int getTileY(double dy) {
		return (int) ((y + dy + blockSize / 2) / blockSize);
	}

	public int getTileX() {
		return (int) ((x + blockSize / 2) / blockSize);
	}

	public int getTileY() {
		return (int) ((y + blockSize / 2) / blockSize);
	}

	public int random(int max) {
		return (int) (Math.random() * max);
	}
	
	
}
