package logics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import data.Level;
import data.RawTetro;
import data.Tetro;
import data.TetroType;
import data.Tiles.DoorTile;
import data.Tiles.Tile;
import graphics.Frame;
import input.KeyHandler;
import loading.EntityLoader;
import loading.LevelSaver;
import logics.entities.Enemy;
import logics.entities.Entity;
import logics.entities.Player;
import logics.entities.items.Item;

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
	protected KeyHandler keyHandler;

	// Halten die Weltinformationen
	protected Tile[][] tileWorld;
	protected ArrayList<Item> itemWorld;
	protected ArrayList<Tetro> tetros;
	protected boolean[][] tetroWorldHitbox;
	protected ArrayList<TetroType> tetroTypes;
	protected ArrayList<Enemy> enemies;
	protected ArrayList<Entity> entities;
	protected ArrayList<DoorTile> doors;

	public World(Rectangle graphicClip, int blockSize, Level level, KeyHandler keyHandler, Frame frame) {

		// Initialisierungen
		this.graphicClip = graphicClip;
		this.blockSize = blockSize;
		this.tetroTypes = level.getTetroTypes();
		this.keyHandler = keyHandler;
		this.frame = frame;

		tetroFileURL = level.getTetrofileUrl();
		tetros = new ArrayList<>();
		entities = new ArrayList<>();
		enemies = new ArrayList<>();
		doors = level.getDoors();
		tileWorld = level.getArrWorld();
		for (Tile[] tt : tileWorld) {
			for (Tile t : tt) {
				t.setWorld(this);
			}
		}
		itemWorld = level.getItemWorld();
		for (Item i : itemWorld) {
			i.setWorld(this);
			i.init();
		}

		tetroWorldHitbox = new boolean[tileWorld.length][tileWorld[0].length];
		for (int i = 0; i < tetroWorldHitbox.length; i++) {
			for (int j = 0; j < tetroWorldHitbox[i].length; j++) {
				tetroWorldHitbox[i][j] = false;
			}
		}

		camera = new Camera(level.getPlayerX() * blockSize, level.getPlayerY() * blockSize,
				tileWorld.length * blockSize - (int) graphicClip.getHeight(),
				tileWorld[0].length * blockSize - (int) graphicClip.getWidth(),
				(int) (graphicClip.getWidth() / 2 - blockSize / 2),
				(int) (graphicClip.getHeight() / 2 - blockSize / 2.));

		player = new Player(this, level.getPlayerX(), level.getPlayerY(),
				EntityLoader.loadAnimations("/res/anims/character.txt"));
		addSpawner(10, 10, 2, 2, 2, 2, 3, true, 5);
		addSpawner(3, 3, 1, 1, 1, 1, 2, true, 3);
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

				if (tileWorld[j][i].getKey() == '0')
					g.drawImage(tileWorld[j][i].getImg(), i * blockSize - camera.getX(), j * blockSize - camera.getY(),
							blockSize, blockSize, null);

			}
		}

		// Lines - vertikal
		for (int i = 0; i <= tileWorld[0].length * blockSize; i += blockSize) {
			g.drawLine((i - camera.getX()), 0, (i - camera.getX()),
					Math.min(graphicClip.height, tileWorld.length * blockSize - camera.getY()));
		}
		// Lines - horizontal
		for (int i = 0; i <= tileWorld.length * blockSize; i += blockSize) {
			g.drawLine(0, (i - camera.getY()), Math.min(graphicClip.width, tileWorld[0].length * blockSize),
					(i - camera.getY()));
		}

		// Tetros
		for (Tetro t : tetros) {
			t.draw(g, debugMode);
		}

		// blocker blocks
		for (int j = 0; j < tileWorld.length; j++) {
			for (int i = 0; i < tileWorld[j].length; i++) {

				if (tileWorld[j][i].getKey() != '0')
					g.drawImage(tileWorld[j][i].getImg(), i * blockSize - camera.getX(), j * blockSize - camera.getY(),
							blockSize, blockSize, null);

			}
		}
		for (Enemy enemy : enemies) {
			enemy.draw(g, interpolation, debugMode);
		}
		for (Entity entity : entities) {
			entity.draw(g, interpolation, debugMode);
		}
		for (Item i : itemWorld) {
			i.draw(g, interpolation, debugMode);
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

	}

	public void tick() {
		// Player movement
		player.tick();

		// camera adjustment
		camera.tick(player.getX(), player.getY());

		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).tick();

		}

		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).tick();
		}

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

		// player.setWeapon(new Weapon(20,
		// ImageLoader.loadImage("/res/sword-in-hand.png"),
		// ImageLoader.loadImage("/res/sword-hit.png"), new Point(0, 0),
		// new Point(30, 5), blockSize, 0, 30, 45));
	}

	private void addTetroToHitbox(Tetro tetro, int x, int y, int rotation) {
		boolean[][] hitbox = tetro.getType().getHitbox();

		for (int j = 0; j < hitbox.length; j++) {
			for (int i = 0; i < hitbox[j].length; i++) {
				if (hitbox[j][i]) {
					switch (rotation % 4) {
					case 0:
						if (j + y >= 0 && i + x >= 0 && j + y <= tileWorld.length && i + x <= tileWorld[0].length)
							tetroWorldHitbox[j + y][i + x] = true;
						break;
					case 1:
						if (-i + y + 3 >= 0 && j + x >= 0 && -i + y + 3 <= tileWorld.length
								&& j + x <= tileWorld[0].length)
							tetroWorldHitbox[-i + y + 3][j + x] = true;
						break;
					case 2:
						if (-j + y + 1 >= 0 && -i + x + 3 >= 0 && -j + y + 1 <= tileWorld.length
								&& -i + x + 3 <= tileWorld[0].length)
							tetroWorldHitbox[-j + y + 1][-i + x + 3] = true;
						break;
					case 3:
						if (i + y >= 0 && -j + x + 1 >= 0 && i + y <= tileWorld.length
								&& -j + x + 1 <= tileWorld[0].length)
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

	public void drawInventory(Graphics2D g) {
		player.drawInventory(g);
	}

	public void save(String path) {
		ArrayList<RawTetro> rawTetros = new ArrayList<>();
		for (Tetro t : tetros) {
			rawTetros.add(createRawTetro(t));
		}

		Level temporaryLevel = new Level(tetroTypes, rawTetros, tileWorld, itemWorld, doors, blockSize, tetroFileURL,
				player.getTileX(), player.getTileY());
		LevelSaver saver = new LevelSaver();
		saver.saveLevel(temporaryLevel, path);

	}

	public void backToTheOverworld(boolean died) {
		frame.changeToOverworld(died);
	}

	private RawTetro createRawTetro(Tetro tetro) {
		return new RawTetro(tetroTypes.indexOf(tetro.getType()), tetro.getX(), tetro.getY(), tetro.getRotation());
	}

	public Player getPlayer() {
		return player;
	}

	public void inventoryClick(int x, int y) {
		player.inventoryClick(x, y);

	}

	public KeyHandler getKeyHandler() {
		return keyHandler;
	}

	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}

	public boolean isTetroAt(int tileY, int tileX) {
		return tetroWorldHitbox[tileY][tileX];
	}

	public int blockSize() {
		return blockSize;
	}

	public int getMaxX() {
		return tileWorld[0].length * blockSize;
	}

	public int getMaxY() {
		return tileWorld.length * blockSize;
	}

	public int cameraX() {
		return camera.getX();
	}

	public int cameraY() {
		return camera.getY();
	}

	public Tile getTileAt(int tileY, int tileX) {
		return tileWorld[tileY][tileX];
	}

	public void addEnemy(int x, int y, int health, EnemySpawner enemySpawner) {
		enemies.add(
				new Enemy(this, enemySpawner, health, x, y, EntityLoader.loadAnimations("/res/anims/enemyAnims.txt")));

	}

	public void addSpawner(int x, int y, int spawnOffsetLeft, int spawnOffsetTop, int spawnOffsetRight,
			int spawnOffsetBottom, int maxEnemies, boolean enemyOnlyOnTetros, double spawnRate) {
		entities.add(new EnemySpawner(this, x, y, spawnOffsetLeft, spawnOffsetTop, spawnOffsetRight, spawnOffsetBottom,
				maxEnemies, enemyOnlyOnTetros, spawnRate));
	}

	public int getVirtualMaxY() {
		return tileWorld[0].length;
	}

	public int getVirtualMaxX() {
		return tileWorld.length;
	}

	public void removeEnemy(Enemy enemy) {
		enemies.remove(enemy);

	}

	public int getGameBoundsX() {
		return graphicClip.x;
	}

	public int getGameBoundsY() {
		return graphicClip.y;
	}

	public int getTetroTypeCount() {
		return tetroTypes.size();
	}

	public TetroType getTetroType(int i) {
		return tetroTypes.get(i);
	}

	public ArrayList<Item> getItemsAt(int tileY, int tileX) {
		ArrayList<Item> outp = new ArrayList<>();
		for (Item i : itemWorld) {
			if (i.getY() == tileY && i.getX() == tileX) {
				outp.add(i);
			}
		}
		return outp;
	}

	public void removeItem(Item i) {
		itemWorld.remove(i);
	}

	public void switchDoors(int color) {
		for (DoorTile dT : doors) {
			if (dT.getColor() == color) {
				dT.changeState();
			}
		}
	}

}
