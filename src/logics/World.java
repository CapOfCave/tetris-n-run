package logics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import data.RawTetro;
import data.Tetro;
import data.TetroType;
import data.Weapon;
import data.Tiles.Tile;
import graphics.Frame;
import input.KeyHandler;
import loading.ImageLoader;
import loading.LevelSaver;

/**
 * @author Lars Created on 05.08.2018
 */
public class World {

	// Variablen
	protected int blockSize;
	protected Rectangle graphicClip;

	// Wird zum Speichern übernommen
	protected String tetroFileURL;

	// Standard-Bilder
	protected BufferedImage blockImg;
	protected BufferedImage backgroundImg;

	// Wichtigste Bezugsobjekte
	protected Player player;
	protected Camera camera;
	protected Frame frame;
	protected EnemySpawner enemySpawner;

	// Halten die Weltinformationen
	protected Tile[][] tileWorld;
	protected ArrayList<Tetro> tetros;
	protected boolean[][] tetroWorldHitbox;
	protected ArrayList<TetroType> tetroTypes;
	

	public World(Rectangle graphicClip, int blockSize, Level level, KeyHandler keyHandler) {

		// Initialisierungen
		this.graphicClip = graphicClip;
		this.blockSize = blockSize;
		this.tetroTypes = level.getTetroTypes();
		this.tetroFileURL = level.getTetrofileUrl();
		tetros = new ArrayList<>();
		tileWorld = level.getArrWorld(); // [columns][rows] //[y][x]
		
		tetroWorldHitbox = new boolean[tileWorld.length][tileWorld[0].length];
		for (int i = 0; i < tetroWorldHitbox.length; i++) {
			for (int j = 0; j < tetroWorldHitbox[i].length; j++) {
				tetroWorldHitbox[i][j] = false;
				if (tileWorld[i][j].isWalkable()) {
					tetroWorldHitbox[i][j] = true;
				}
			}
		}

		camera = new Camera(level.getPlayerX() * blockSize, level.getPlayerY() * blockSize,
				tileWorld.length * blockSize - (int) graphicClip.getHeight(), tileWorld[0].length * blockSize - (int) graphicClip.getWidth(),
				(int) (graphicClip.getWidth() / 2 - blockSize / 2), (int) (graphicClip.getHeight() / 2 - blockSize / 2.));
		enemySpawner = new EnemySpawner(2, tileWorld[0].length * blockSize, tileWorld.length * blockSize, blockSize, camera, tetros, tetroWorldHitbox, tileWorld);
		player = new Player(blockSize, camera, tetros, tetroWorldHitbox, enemySpawner.getEnemyList(),  level.getPlayerX(), level.getPlayerY(), keyHandler, tileWorld);
		enemySpawner.setPlayer(player);


		// Erstellen der Tetros
		for (RawTetro ut : level.getUnfinishedTetros()) {
			Tetro ft = ut.createTetro(level.getTetroTypes(), blockSize, camera);
			tetros.add(ft);
			addTetroToHitbox(ft, ft.getX(), ft.getY(), ft.getRotation());
		}

	}

	public void draw(Graphics2D g, float interpolation, boolean debugMode) {

		camera.prepareDraw(interpolation);

		// blocker blocks
		for (int j = 0; j < tileWorld.length; j++) {
			for (int i = 0; i < tileWorld[j].length; i++) {

				g.drawImage(tileWorld[j][i].getImg(), i * blockSize - camera.getX(), j * blockSize - camera.getY(), blockSize, blockSize, null);

			}
		}

		// Lines - vertikal
		for (int i = 0; i <= tileWorld[0].length * blockSize; i += blockSize) {
			g.drawLine((i - camera.getX()), 0, (i - camera.getX()), Math.min(graphicClip.height, tileWorld.length * blockSize - camera.getY()));
		}
		// Lines - horizontal
		for (int i = 0; i <= tileWorld.length * blockSize; i += blockSize) {
			g.drawLine(0, (i - camera.getY()), Math.min(graphicClip.width, tileWorld[0].length * blockSize), (i - camera.getY()));
		}

		// Tetros
		for (Tetro t : tetros) {
			t.draw(g, debugMode);
		}

		if (debugMode)
			for (int j = 0; j < tileWorld.length; j++) {
				for (int i = 0; i < tileWorld[j].length; i++) {
					if (tetroWorldHitbox[j][i]) {
						g.setColor(Color.RED);
						g.drawRect(i * blockSize - camera.getX(), j * blockSize - camera.getY(), blockSize, blockSize);
					}
				}
			}

		enemySpawner.draw( g, interpolation, debugMode);
	}

	public void tick() {
		// Player movement
		player.tick();

		// camera adjustment
		camera.tick(player.getX(), player.getY());
		
		enemySpawner.tick();
	}

	public void addTetro(TetroType tetroType, int x, int y, int rotation) {

		int placeX;
		int placeY;

		if (x + camera.getX() < 0) {
			placeX = (x + camera.getX() - blockSize / 2) / blockSize;
		} else {
			placeX = (x + camera.getX() + blockSize / 2) / blockSize;
		}
		if (y + camera.getY() < 0) {
			placeY = (y + camera.getY() - blockSize / 2) / blockSize;
		} else {
			placeY = (y + camera.getY() + blockSize / 2) / blockSize;
		}
		Tetro tetro = new Tetro(tetroType, placeX, placeY, rotation, blockSize, camera);
		tetros.add(tetro);
		addTetroToHitbox(tetro, placeX, placeY, rotation);

		player.setWeapon(new Weapon(ImageLoader.loadImage("/res/sword-in-hand.png"), ImageLoader.loadImage("/res/sword-hit.png"), new Point(0, 0),
				new Point(30, 5), blockSize, 0, 30, 45));
	}

	private void addTetroToHitbox(Tetro tetro, int x, int y, int rotation) {
		boolean[][] hitbox = tetro.getType().getHitbox();

		for (int j = 0; j < hitbox.length; j++) {
			for (int i = 0; i < hitbox[j].length; i++) {
				if (hitbox[j][i]) {
					switch (rotation % 4) {
					case 0:
						tetroWorldHitbox[j + y][i + x] = true;
						break;
					case 1:
						tetroWorldHitbox[-i + y + 3][j + x] = true;
						break;
					case 2:
						tetroWorldHitbox[-j + y + 1][-i + x + 3] = true;
						break;
					case 3:
						tetroWorldHitbox[i + y][-j + x + 1] = true;
						break;
					}
				}
			}

		}
	}

	public void drawPlayer(Graphics2D g, float interpolation, boolean debugMode) {
		// Player kann an einer anderen Stelle im Programm aufgerufen werden
		player.draw(g, interpolation, debugMode);

	}

	public void save(String path) {
		ArrayList<RawTetro> rawTetros = new ArrayList<>();
		for (Tetro t : tetros) {
			rawTetros.add(createRawTetro(t));
		}

		Level temporaryLevel = new Level(tetroTypes, rawTetros, tileWorld, blockSize, tetroFileURL, player.getTileX(), player.getTileY());
		LevelSaver saver = new LevelSaver();
		saver.saveLevel(temporaryLevel, path);

	}

	private RawTetro createRawTetro(Tetro tetro) {
		return new RawTetro(tetroTypes.indexOf(tetro.getType()), tetro.getX(), tetro.getY(), tetro.getRotation());
	}

	public Player getPlayer() {
		return player;
	}

}
