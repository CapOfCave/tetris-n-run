package logics.worlds;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import data.Level;
import data.RawPlayer;
import data.RawSpawner;
import data.RawTetro;
import data.Tetro;
import data.TetroType;
import data.Tiles.DoorTile;
import data.Tiles.Tile;
import graphics.Frame;
import graphics.Renderer;
import input.KeyHandler;
import loading.ImageLoader;
import loading.LevelSaver;
import logics.Camera;
import logics.GameLoop;
import logics.entities.Enemy;
import logics.entities.EnemySpawner;
import logics.entities.Entity;
import logics.entities.MovingBlock;
import logics.entities.Player;
import logics.entities.items.Item;

public abstract class World {
	private static final double min_interaction_distance = 20;
	private static final double min_interaction_looking_distance = 50;

	// Variablen
	protected Rectangle graphicClip;

	// Wird zum Speichern übernommen
	protected String tetroFileURL;

	// Standard-Bilder
	protected BufferedImage blockImg;
	protected BufferedImage backgroundImg;
	protected BufferedImage nullTileImg;
	protected Renderer renderer;

	// Wichtigste Bezugsobjekte
	protected Player player;
	protected Camera camera;
	protected Frame frame;
	protected KeyHandler keyHandler;

	// Halten die Weltinformationen
	protected Tile[][] tileWorld;
	protected boolean[][] tetroWorldHitbox;

	protected ArrayList<DoorTile> doors;
	protected ArrayList<Tetro> tetros;
	protected ArrayList<TetroType> tetroTypes;
	protected ArrayList<Item> items;
	protected ArrayList<Enemy> enemies;
	protected ArrayList<EnemySpawner> spawner;
	protected ArrayList<Entity> otherEntities;
	protected ArrayList<Entity> allEntities;
	private ArrayList<Entity> toAdd;
	private ArrayList<Entity> toRemove;
	private int tetroAmount[];
	private boolean toggleStates[];

	public World(Rectangle graphicClip, Level level, KeyHandler keyHandler, Frame frame, RawPlayer rawPlayer) {

		// Initialisierungen

		this.graphicClip = graphicClip;
		this.tetroTypes = level.getTetroTypes();
		this.keyHandler = keyHandler;
		this.frame = frame;
		toggleStates = level.getToggleStates();

		toAdd = new ArrayList<>();
		toRemove = new ArrayList<>();
		renderer = new Renderer();

		nullTileImg = ImageLoader.loadImage("/res/blocks/block0.png");

		tetroAmount = level.getTetroAmounts();
		tetroFileURL = level.getTetrofileUrl();
		tetros = new ArrayList<>();
		allEntities = new ArrayList<>();
		enemies = new ArrayList<>();
		otherEntities = level.getEntities();
		for (Entity e : otherEntities) {
			e.setWorld(this);
		}
		allEntities.addAll(otherEntities);
		doors = level.getDoors();
		tileWorld = level.getArrWorld();
		for (Tile[] tt : tileWorld) {
			for (Tile t : tt) {
				if (t != null)
					t.setWorld(this);
			}
		}
		items = level.getItemWorld();
		for (Item i : items) {
			i.setWorld(this);
		}
		allEntities.addAll(items);

		tetroWorldHitbox = new boolean[tileWorld.length][tileWorld[0].length];
		for (int i = 0; i < tetroWorldHitbox.length; i++) {
			for (int j = 0; j < tetroWorldHitbox[i].length; j++) {
				tetroWorldHitbox[i][j] = false;
			}
		}

		camera = new Camera(this, level.getPlayerX() * Frame.BLOCKSIZE, level.getPlayerY() * Frame.BLOCKSIZE,
				tileWorld.length * Frame.BLOCKSIZE - (int) graphicClip.getHeight(),
				tileWorld[0].length * Frame.BLOCKSIZE - (int) graphicClip.getWidth(),
				(int) (graphicClip.getWidth() / 2 - Frame.BLOCKSIZE / 2),
				(int) (graphicClip.getHeight() / 2 - Frame.BLOCKSIZE / 2.));

		player = new Player(this, level.getPlayerX(), level.getPlayerY(), "/res/anims/character.txt", rawPlayer);

		// Erstellen der Tetros
		for (RawTetro ut : level.getUnfinishedTetros()) {
			Tetro ft = ut.createTetro(level.getTetroTypes(), camera);
			tetros.add(ft);
			addTetroToHitbox(ft, ft.getX(), ft.getY(), ft.getRotation());
		}
		spawner = new ArrayList<>();
		for (RawSpawner rS : level.getSpawner()) {
			addSpawner(rS);
		}
		level.getSpawner();

		// add everything to renderer
		for (int j = 0; j < tileWorld.length; j++) {
			for (int i = 0; i < tileWorld[j].length; i++) {
				if (tileWorld[j][i] != null)
					tileWorld[j][i].addTo(renderer);
			}
		}

		for (Entity entity : allEntities) {
			entity.addTo(renderer);
			if (entity instanceof MovingBlock) {
				Tile currentTile = getTileAt((int) ((entity.getY() + Frame.BLOCKSIZE / 2) / Frame.BLOCKSIZE),
						(int) ((entity.getX() + Frame.BLOCKSIZE / 2) / Frame.BLOCKSIZE));
				currentTile.eventWhenMoveBlockEntering();
				((MovingBlock) entity).setCurrentTile(currentTile);
			}
		}
		player.addTo(renderer);
		for (int i = 0; i < toggleStates.length; i++) {
			if (toggleStates[i]) {
				switchDoors(i);
			}
		}
	}

	public void draw(Graphics g, float interpolation, boolean debugMode) {

		GameLoop.acutualframes++;
		camera.prepareDraw(interpolation);
		// 2D-Rendering background
		// Tetros
		for (int j = 0; j < tileWorld.length; j++) {
			for (int i = 0; i < tileWorld[j].length; i++) {
				if (tileWorld[j][i] != null)
					tileWorld[j][i].drawBackground(g, interpolation);
				else
					drawTileIfNull(g, interpolation, i, j);
			}
		}
		for (Tetro t : tetros) {
			t.draw(g);
		}
		// 3D-Rendering
		renderer.draw(g, interpolation);

		if (debugMode) {
			drawDebug(g, interpolation);
		}
	}

	public void drawTileIfNull(Graphics g, float interpolation, int x, int y) {
		g.drawImage(nullTileImg, (int) (x * Frame.BLOCKSIZE - cameraX()), (int) (y * Frame.BLOCKSIZE - cameraY()),
				null);
	}

	public void playSound(String sound, float volume) {
		frame.playSound(sound, volume);
	}

	public void drawPlayer(Graphics g, float interpolation) {
		// Player kann an einer anderen Stelle im Programm aufgerufen werden
		player.draw(g, interpolation);

	}

	public void drawPlayerPreview(Graphics g) {
		player.drawPreview(g);
	}

	public void drawDebug(Graphics g, float interpolation) {
		// for (int j = 0; j < tileWorld.length; j++) {
		// for (int i = 0; i < tileWorld[j].length; i++) {
		// if (tetroWorldHitbox[j][i]) {
		// g.setColor(Color.RED);
		// g.drawRect(i * Frame.BLOCKSIZE - camera.getX(), j * Frame.BLOCKSIZE -
		// camera.getY(),
		// Frame.BLOCKSIZE, Frame.BLOCKSIZE);
		// }
		// }
		// }
		for (Entity e : allEntities) {
			e.drawDebug(g, interpolation);
		}
		player.drawDebug(g, interpolation);
	}

	public void tick() {
		GameLoop.acutualupdates++;
		// Player movement
		player.tick();

		// camera adjustment
		camera.tick(player.getX(), player.getY());

		for (int i = 0; i < allEntities.size(); i++) {
			allEntities.get(i).tick();
		}
		renderer.tick();

	}

	public void addTetro(TetroType tetroType, int x, int y, int rotation) {

		if (tetroAmount[this.tetroTypes.indexOf(tetroType)] > 0) {
			int placeX;
			int placeY;

			if (x + camera.getX() < 0) {
				placeX = (x + camera.getX() - Frame.BLOCKSIZE / 2) / Frame.BLOCKSIZE;
			} else {
				placeX = (x + camera.getX() + Frame.BLOCKSIZE / 2) / Frame.BLOCKSIZE;
			}
			if (y + camera.getY() < 0) {
				placeY = (y + camera.getY() - Frame.BLOCKSIZE / 2) / Frame.BLOCKSIZE;
			} else {
				placeY = (y + camera.getY() + Frame.BLOCKSIZE / 2) / Frame.BLOCKSIZE;
			}
			Tetro tetro = new Tetro(tetroType, placeX, placeY, rotation, camera);
			if (isAllowed(tetro)) {
				tetroAmount[this.tetroTypes.indexOf(tetroType)] -= 1;
				tetros.add(tetro);
				addTetroToHitbox(tetro, placeX, placeY, rotation);
			} else {
				System.err.println("nicht erlaubte Platzierung");
			}
		} else {
			frame.addLineToText("mehr da. ");
			frame.addLineToText("Es sind keine Tetros dieser Art");
		}

	}

	private boolean isAllowed(Tetro tetro) {
		int virX = tetro.getX();
		int virY = tetro.getY();

		for (int j = 0; j < tetro.getType().getHitbox().length; j++) {
			for (int i = 0; i < tetro.getType().getHitbox()[j].length; i++) {
				if (tetro.getType().getHitbox()[j][i]) {

					switch (tetro.getRotation()) {
					case 0:
						if (!isTetroPlacableAt(i + virX, j + virY)) {
							// System.err.println(
							// "Nicht erlaubte Platzierung eines Tetros bei " + (i + virX) + "|" + (j +
							// virY));
							return false;
						} else {
							break;
						}
					case 1:
						if (!isTetroPlacableAt(j + virX, -i + virY + 3)) {
							// System.err.println("Nicht erlaubte Platzierung eines Tetros bei " + (j +
							// virX) + "|"
							// + (-i + virY + 3));
							return false;
						} else {
							break;
						}
					case 2:
						if (!isTetroPlacableAt(-i + virX + 3, -j + virY + 1)) {
							// System.err.println("Nicht erlaubte Platzierung eines Tetros bei " + (-i +
							// virX + 3) + "|"
							// + (-j + virY + 1));
							return false;
						} else {
							break;
						}
					case 3:
						if (!isTetroPlacableAt(-j + virX + 1, i + virY)) {
							// System.err.println("Nicht erlaubte Platzierung eines Tetros bei " + (-j +
							// virX + 1) + "|"
							// + (i + virY));
							return false;
						} else {
							break;
						}
					}
				}
			}
		}
		return true;
	}

	private boolean isTetroPlacableAt(int i, int j) {
		if (tileWorld[j][i] != null)
			return tileWorld[j][i].isTetroPlacable() && !tetroWorldHitbox[j][i];
		else
			return !tetroWorldHitbox[j][i];
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

	public void addEnemy(int x, int y, int health, EnemySpawner enemySpawner) {
		Enemy e = new Enemy(this, enemySpawner, health, x, y, "/res/anims/enemyAnims.txt");
		enemies.add(e);
		allEntities.add(e);
		renderer.addDrawable(e);
	}

	public void addSpawner(int x, int y, int spawnOffsetLeft, int spawnOffsetTop, int spawnOffsetRight,
			int spawnOffsetBottom, int maxEnemies, double spawnRate, boolean start) {
		EnemySpawner e = new EnemySpawner(this, x, y, spawnOffsetLeft, spawnOffsetTop, spawnOffsetRight,
				spawnOffsetBottom, maxEnemies, spawnRate, start);
		spawner.add(e);
		allEntities.add(e);
		renderer.addDrawable(e);
	}

	private void addSpawner(RawSpawner rS) {
		addSpawner(rS.getX(), rS.getY(), rS.getLoff(), rS.getToff(), rS.getRoff(), rS.getBoff(), rS.getMax(),
				rS.getRate(), rS.getStart());
	}

	public void save(String path, String fileName) {
		ArrayList<RawTetro> rawTetros = new ArrayList<>();
		for (Tetro t : tetros) {
			rawTetros.add(createRawTetro(t));
		}

		Level temporaryLevel = new Level(tetroTypes, rawTetros, tileWorld, items, doors, createRawSpawner(),
				otherEntities, tetroAmount, toggleStates, tetroFileURL, player.getTileX(), player.getTileY());
		LevelSaver saver = new LevelSaver();
		saver.saveLevel(temporaryLevel, path, fileName);

	}

	private ArrayList<RawSpawner> createRawSpawner() {
		ArrayList<RawSpawner> outp = new ArrayList<>();
		for (EnemySpawner eS : spawner) {
			outp.add(new RawSpawner((int) eS.getX(), (int) eS.getY(), eS.getLoff(), eS.getToff(), eS.getRoff(),
					eS.getBoff(), eS.getMax(), eS.getRate(), eS.getStart()));
		}
		return outp;
	}

	private RawTetro createRawTetro(Tetro tetro) {
		return new RawTetro(tetroTypes.indexOf(tetro.getType()), tetro.getX(), tetro.getY(), tetro.getRotation());
	}

	public int cameraX() {
		return camera.getX();
	}

	public int cameraY() {
		return camera.getY();
	}

	public KeyHandler getKeyHandler() {
		return keyHandler;
	}

	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}

	public Tile getTileAt(int tileY, int tileX) {
		return tileWorld[tileY][tileX];
	}

	public ArrayList<Item> getItemsAt(int tileY, int tileX) {
		ArrayList<Item> outp = new ArrayList<>();
		for (Item i : items) {
			if (i.getY() == tileY && i.getX() == tileX) {
				outp.add(i);
			}
		}
		return outp;
	}

	public void removeItem(Item i) {
		items.remove(i);
		allEntities.remove(i);
		renderer.removeDrawable(i);
	}

	public void backToTheOverworld(boolean died) {
		frame.changeToOverworld(died, new RawPlayer(player.getAcc(), player.getBrake(), player.getMaxSpeed(),
				player.getHealth(), player.getInventory()));
	}

	public Player getPlayer() {
		return player;
	}

	public void removeEnemy(Enemy enemy) {
		enemies.remove(enemy);
		allEntities.remove(enemy);
		renderer.removeDrawable(enemy);
	}

	public int getMaxX() {
		return tileWorld[0].length * Frame.BLOCKSIZE;
	}

	public int getMaxY() {
		return tileWorld.length * Frame.BLOCKSIZE;
	}

	public boolean isTetroAt(int tileY, int tileX) {
		return tetroWorldHitbox[tileY][tileX];
	}

	public int getVirtualMaxY() {
		return tileWorld[0].length;
	}

	public int getVirtualMaxX() {
		return tileWorld.length;
	}

	public void toggle(int color) {
		toggleStates[color] = !toggleStates[color];
		switchDoors(color);
	}

	public void switchDoors(int color) {
		toggleStates[color] = !toggleStates[color];
		for (DoorTile dT : doors) {
			if (dT.getColor() == color) {
				dT.changeState();
			}
		}
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

	public void actionPressed(double x, double y, int rotation) {

		// Entities
		for (Entity e : allEntities) {
			if ((Math.abs(e.getX() - x) < min_interaction_distance && Math.abs(e.getY() - y) < min_interaction_distance)
					|| isInFront(e, x, y, rotation)) {
				e.interact();
			}
		}
		// Tiles
		if (tileWorld[((int) y + Frame.BLOCKSIZE / 2) / Frame.BLOCKSIZE][((int) x + Frame.BLOCKSIZE / 2)
				/ Frame.BLOCKSIZE] != null) {
			tileWorld[((int) y + Frame.BLOCKSIZE / 2) / Frame.BLOCKSIZE][((int) x + Frame.BLOCKSIZE / 2)
					/ Frame.BLOCKSIZE].interact();
		}
		for (Entity e : toAdd) {
			allEntities.add(e);
			renderer.addDrawable(e);
		}
		toAdd.clear();
		for (Entity e : toRemove) {
			allEntities.remove(e);
			renderer.removeDrawable(e);
		}
		toRemove.clear();

	}

	private boolean isInFront(Entity e, double x, double y, int rotation) {
		if ((e.getX() > x && e.getX() - x < min_interaction_looking_distance
				&& Math.abs(e.getY() - y) < min_interaction_distance && rotation / 90 == 1)
				|| (e.getX() < x && x - e.getX() < min_interaction_looking_distance
						&& Math.abs(e.getY() - y) < min_interaction_distance && rotation / 90 == 3)
				|| (e.getY() > y && e.getY() - y < min_interaction_looking_distance
						&& Math.abs(e.getX() - x) < min_interaction_distance && rotation / 90 == 2)
				|| (e.getY() < y && y - e.getY() < min_interaction_looking_distance
						&& Math.abs(e.getX() - x) < min_interaction_distance && rotation / 90 == 0)) {
			return true;
		}
		return false;
	}

	public void drawInventory(Graphics2D inventoryGraphics) {
		player.drawInventory(inventoryGraphics);
	}

	public void removeEntity(Entity entity) {
		toRemove.add(entity);

	}

	public void addEntity(Entity entity) {
		toAdd.add(entity);

	}

	public int[] getTetroAmount() {
		return tetroAmount;
	}

	public void setTetroAmount(int[] tetroAmount) {
		this.tetroAmount = tetroAmount;
	}

}
