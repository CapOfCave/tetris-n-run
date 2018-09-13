package logics;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import data.Tetro;
import loading.ImageLoader;

public class OverworldPlayer {
	private int x, y;
	private int lastX, lastY;
	private BufferedImage img;
	private int blockSize;
	private Camera camera;

	private ArrayList<Point> keyFrames;

	private ArrayList<Tetro> tetrosToRemove;
	private ArrayList<Tetro> worldTetros;
	private ArrayList<Tetro>[][] worldTetroHitbox;

	public OverworldPlayer(int blockSize, Camera camera, ArrayList<Tetro> worldTetros, ArrayList<Tetro>[][] worldTetroHitbox) {
		this.camera = camera;
		this.worldTetros = worldTetros;
		this.worldTetroHitbox = worldTetroHitbox;
		this.blockSize = blockSize;
		img = ImageLoader.loadImage("/res/character.png");
		keyFrames = new ArrayList<>();
		
		tetrosToRemove = new ArrayList<>();
	}

	public OverworldPlayer(int blockSize, Camera camera, ArrayList<Tetro> worldTetros, ArrayList<Tetro>[][] worldTetroHitbox, int playerX, int playerY) {
		this(blockSize, camera, worldTetros, worldTetroHitbox);
		x = playerX;
		y = playerY;
		lastX = x;
		lastY = y;
	}

	public BufferedImage getImage() {
		return img;
	}

	public void draw(Graphics g, float interpolation) {
		float interpolX = (x - lastX) * interpolation + lastX;
		float interpolY = (y - lastY) * interpolation + lastY;
		g.drawImage(img, (int)(interpolX * blockSize) - camera.getX(), (int)(interpolY * blockSize) - camera.getY(), blockSize, blockSize, null);
		// g.drawImage(img, x * blockSize - camera.getX(), y * blockSize - camera.getY(), 40, 40, null);

	}

	public void tick() {
		lastX = x;
		lastY = y;
		
		//Beginn der Bewegung über eine KeyFrame-abfolge
		if (keyFrames.size() > 0) {
			Point p = keyFrames.get(0);
			//Eigentliche Bewegung
			this.x += p.x;
			this.y += p.y;
			keyFrames.remove(p);
			
			//Durchlaufende Tetros entfernen - entfernen
			Tetro temp = tetrosToRemove.get(0);
			tetrosToRemove.remove(0);
			if (temp != Tetro.NULL) {
				worldTetros.remove(temp);
				if (worldTetroHitbox[y][x].size() > 0) {
					Tetro nextMove = worldTetroHitbox[y][x].get(0);
					worldTetroHitbox[nextMove.getStartPoint1().y][nextMove.getStartPoint1().x].remove(nextMove);
					worldTetroHitbox[nextMove.getStartPoint2().y][nextMove.getStartPoint2().x].remove(nextMove);
					move(nextMove);

				}
			}

		}
	}



	//Wird noch entfernt und durch echtes Movement ersetzt
	public void move(Tetro tetro) {
		
		
		
	}

	

	public Point getXY() {
		return new Point(x, y);
	}

	public int getRealY() {
		return y * blockSize;
	}

	public int getRealX() {
		return x * blockSize;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
