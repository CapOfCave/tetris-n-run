package logics;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import data.Tetro;
import loading.ImageLoader;

/**
 * @author Lars Created on 05.08.2018
 */
public class Player {
	private int x, y;
	private int lastX, lastY;
	private BufferedImage img;
	private int blockSize;
	private Camera camera;

	private ArrayList<Point> keyFrames;

	private ArrayList<Tetro> tetrosToRemove;
	private ArrayList<Tetro> worldTetros;
	private ArrayList<Tetro>[][] worldTetroHitbox;

	public Player(int blockSize, Camera camera, ArrayList<Tetro> worldTetros, ArrayList<Tetro>[][] worldTetroHitbox) {
		this.camera = camera;
		this.worldTetros = worldTetros;
		this.worldTetroHitbox = worldTetroHitbox;
		this.blockSize = blockSize;
		img = ImageLoader.loadImage("/res/character.png");
		keyFrames = new ArrayList<>();
		
		tetrosToRemove = new ArrayList<>();
	}

	public Player(int blockSize, Camera camera, ArrayList<Tetro> worldTetros, ArrayList<Tetro>[][] worldTetroHitbox, int playerX, int playerY) {
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
		int tetro_value = tetro.getBlockAt(x, y);
		String movePattern = tetro.getType().getStrMovepattern();

		if (tetro_value == 2) {
			for (int i = 0; i < movePattern.length(); i++) {
				int digit = (Character.digit(movePattern.charAt(i), 4) + 4 - tetro.getRotation()) % 4;
				handleTetroTile(digit, i == movePattern.length() - 1, tetro);
			}
		} else if (tetro_value == 3) {
			for (int i = movePattern.length() - 1; i >= 0; i--) {
				int digit = (2 + Character.digit(movePattern.charAt(i), 4) + (4 - tetro.getRotation())) % 4;
				handleTetroTile(digit, i == 0, tetro);
			}
		}
	}

	//Wird noch entfernt und durch echtes Movement ersetzt
	private void handleTetroTile(int digit, boolean lastIndex, Tetro tetro) {
		int yMove = ((digit + 1) % 2) * (digit - 1);
		int xMove = (digit % 2) * (2 - digit);
		keyFrames.add(new Point(xMove, yMove));
		if (lastIndex) {
			tetrosToRemove.add(tetro);
		} else {
			tetrosToRemove.add(Tetro.NULL);
		}
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
