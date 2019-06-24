package logics.worlds;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import data.DrawAndSortable;
import data.Level;
import data.RawPlayer;
import data.RawTetro;
import data.Tetro;
import data.TetroType;
import data.WallImgFrame;
import data.Tiles.DoorTile;
import data.Tiles.PressurePlateTile;
import data.Tiles.SaveNLoadTile;
import data.Tiles.Tile;
import graphics.GameFrame;
import graphics.Panel;
import graphics.Renderer;
import input.KeyHandler;
import loading.ImageLoader;
import loading.LevelSaver;
import logics.ParticleHandler;
import logics.Camera;
import logics.GameLoop;
import logics.InHandHandler;
import logics.entities.Entity;
import logics.entities.MovingBlock;
import logics.entities.Player;
import logics.entities.Switch;

public class World {
	private static final double min_interaction_distance = 20;
	private static final double min_interaction_looking_distance = 50;
	private static final int render_image_offset = 115;

	// Variablen
	protected Rectangle graphicClip;
	private Rectangle renderRect;

	// Wird zum Speichern übernommen
	protected String tetroFileURL;

	// Standard-Bilder
	protected BufferedImage blockImg;
	protected BufferedImage backgroundImg;
	protected BufferedImage[] nullTileImgs = { ImageLoader.loadImage("/res/blocks/block0.png"),
			ImageLoader.loadImage("/res/blocks/block0i.png"), ImageLoader.loadImage("/res/blocks/block0j.png"),
			ImageLoader.loadImage("/res/blocks/ventilator.png"), ImageLoader.loadImage("/res/blocks/block0l.png"), };
	// protected double[] probs = { 0.53, 0.454, 0.01, 0.005, 0.001 };
	protected double[] probs = { 1, 0, 0, 0, 0 };

	protected Renderer renderer;

	// Wichtigste Bezugsobjekte
	protected Player player;
	protected SaveNLoadTile lastUsedSALTile;
	protected Camera camera;
	protected GameFrame frame;
	protected KeyHandler keyHandler;
	protected ParticleHandler particleHandler;

	// Halten die Weltinformationen
	protected Tile[][] tileWorld;
	protected boolean[][] tetroWorldHitbox;
	protected WallImgFrame[][] wallImgFrames;

	protected ArrayList<DoorTile> doors;
	protected ArrayList<Tetro> tetros;
	protected ArrayList<TetroType> tetroTypes;
	protected ArrayList<Entity> otherEntities;
	protected ArrayList<Entity> allEntities;
	private ArrayList<Entity> toAdd;
	private ArrayList<Entity> toRemove;
	private int tetroAmount[];
	private boolean toggleStates[];
	protected int[][] worldDeco;
	protected InHandHandler inHandHandler;
	private ArrayList<Tetro> newestTetros;
	private boolean noClip = false;
	private SaveNLoadTile lastCrossedSALTile = null;
	private Entity lastTouched = null;
	private boolean tookBackTetro = false;

	public World(Rectangle graphicClip, Level level, KeyHandler keyHandler, GameFrame frame, RawPlayer rawPlayer) {
		double probsTotal = 0;
		for (int i = 0; i < probs.length; i++) {
			probsTotal += probs[i];
		}

		if (probsTotal != 1)
			System.err.println("Total deko Probs: " + probsTotal);

		// Initialisierungen
		this.graphicClip = graphicClip;
		particleHandler = new ParticleHandler(this);

		renderRect = new Rectangle(-render_image_offset, -render_image_offset,
				graphicClip.width + 2 * render_image_offset, graphicClip.height + 2 * render_image_offset);
		this.tetroTypes = level.getTetroTypes();
		this.keyHandler = keyHandler;
		this.frame = frame;
		toggleStates = level.getToggleStates();

		toAdd = new ArrayList<>();
		toRemove = new ArrayList<>();
		renderer = new Renderer();

		tetroAmount = level.getTetroAmounts();
		tetroFileURL = level.getTetrofileUrl();
		tetros = new ArrayList<>();
		allEntities = new ArrayList<>();
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

		tetroWorldHitbox = new boolean[tileWorld.length][tileWorld[0].length];
		for (int i = 0; i < tetroWorldHitbox.length; i++) {
			for (int j = 0; j < tetroWorldHitbox[i].length; j++) {
				tetroWorldHitbox[i][j] = false;
			}
		}

		camera = new Camera(this, level.getPlayerX(), level.getPlayerY(),
				tileWorld.length * GameFrame.BLOCKSIZE - (int) graphicClip.getHeight(),
				tileWorld[0].length * GameFrame.BLOCKSIZE - (int) graphicClip.getWidth(),
				(int) (graphicClip.getWidth() / 2 - GameFrame.BLOCKSIZE / 2),
				(int) (graphicClip.getHeight() / 2 - GameFrame.BLOCKSIZE / 2.));

		player = new Player(this, level.getPlayerX(), level.getPlayerY(), "/res/anims/character.txt", rawPlayer);

		// Erstellen der Tetros
		newestTetros = new ArrayList<>();
		for (RawTetro ut : level.getUnfinishedTetros()) {
			Tetro ft = ut.createTetro(level.getTetroTypes(), camera);
			tetros.add(ft);
			addTetroToHitbox(ft, ft.getX(), ft.getY(), ft.getRotation());
		}

		for (int i = 0; i < toggleStates.length; i++) {
			if (toggleStates[i]) {
				switchDoors(i);
			}
		}

		// Wall Image Framing
		// True: empty, false: wall
		wallImgFrames = new WallImgFrame[tileWorld.length + 1][tileWorld[0].length + 1];
		int width = tileWorld[0].length;
		int height = tileWorld.length;
		for (int j = 0; j < wallImgFrames.length; j++) {
			for (int i = 0; i < wallImgFrames[j].length; i++) {
				boolean topleft = (j == 0) || (i == 0) || (tileWorld[j - 1][i - 1] == null)
						|| (tileWorld[j - 1][i - 1].getKey() != '1');
				boolean topright = (j == 0) || (i == width) || (tileWorld[j - 1][i] == null)
						|| (tileWorld[j - 1][i].getKey() != '1');
				boolean bottomright = (j == height) || (i == width) || (tileWorld[j][i] == null)
						|| (tileWorld[j][i].getKey() != '1');
				boolean bottomleft = (j == height) || (i == 0) || (tileWorld[j][i - 1] == null)
						|| (tileWorld[j][i - 1].getKey() != '1');

				int imageId = (topleft ? 8 : 0) + (topright ? 4 : 0) + (bottomright ? 2 : 0) + (bottomleft ? 1 : 0);

				wallImgFrames[j][i] = new WallImgFrame(this, imageId, i, j);
			}
		}
		// add everything to renderer
		for (Entity entity : allEntities) {
			if (entity instanceof MovingBlock) {
				Tile currentTile = getTileAt((int) ((entity.getY() + GameFrame.BLOCKSIZE / 2) / GameFrame.BLOCKSIZE),
						(int) ((entity.getX() + GameFrame.BLOCKSIZE / 2) / GameFrame.BLOCKSIZE));
				if (currentTile != null)
					currentTile.eventWhenMoveBlockEntering();
				((MovingBlock) entity).setCurrentTile(currentTile);
			}
		}

		// Deko erzeugen
		worldDeco = new int[tileWorld.length][tileWorld[0].length];
		for (int y = 0; y < worldDeco.length; y++) {
			for (int x = 0; x < worldDeco[y].length; x++) {
				double randdub = new Random((long) (x * 56789 + y * 12345)).nextDouble();
				int outp = -1;
				for (int i = 0; i < probs.length && outp == -1; i++) {
					if (randdub < probs[i]) {
						outp = i;
					} else {
						randdub -= probs[i];
					}
				}
				worldDeco[y][x] = outp;
			}
		}

		player.addTo(renderer);
	}

	public void draw(Graphics g, float interpolation, boolean debugMode) {
		GameLoop.actualframes++;
		camera.prepareDraw(interpolation);
		// 2D-Rendering background
		// Tiles
		for (int j = 0; j < tileWorld.length; j++) {
			for (int i = 0; i < tileWorld[j].length; i++) {
				if (renderRect.contains(i * GameFrame.BLOCKSIZE - camera.getX(),
						j * GameFrame.BLOCKSIZE - camera.getY())) {
					// draw
					if (tileWorld[j][i] != null) {
						tileWorld[j][i].drawBackground(g, interpolation);
					} else {
						drawTileIfNull(g, interpolation, i, j);
					}
				}
			}
		}
		if (inHandHandler != null) {
			inHandHandler.drawFloorTiles(g);

		}

		for (Tetro t : tetros) {
			t.draw(g);
		}

		// 3D-Rendering
		renderer.draw(g, interpolation);
		particleHandler.show((Graphics2D) g);
		if (debugMode) {
			drawDebug(g, interpolation);
		}

	}

	public void drawMap(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, 10000, 10000);
		int canvasX = 382;
		int canvasY = 184;

		int size;
		if (tileWorld.length > 45 || tileWorld[0].length > 95) {
			size = 3;
		} else if (tileWorld.length > 35 || tileWorld[0].length > 75) {
			size = 4;
		} else {
			size = 5;
		}
		int startX = canvasX / 2 - (tileWorld[0].length / 2 * size);
		int startY = canvasY / 2 - (tileWorld.length / 2 * size);
		// Tetros
		for (int y = 0; y < tetroWorldHitbox.length; y++) {
			for (int x = 0; x < tetroWorldHitbox[y].length; x++) {
				if (tetroWorldHitbox[y][x]) {
					g.setColor(Color.DARK_GRAY.brighter());
					g.fillRect((x + 1) * size + startX, (y + 1) * size + startY, size, size);
				}
			}
		}

		int drawX = 0;
		int drawY = 0;
		// Walls
		for (Tile[] row : tileWorld) {
			drawX = 0;
			drawY += size;
			for (Tile tile : row) {
				drawX += size;
				if (tile != null) {
					switch (tile.getKey()) {
					case '1':
						g.setColor(Color.BLACK);
						g.fillRect(drawX + startX, drawY + startY, size, size);
						break;
					case 'à':
					case 'è':
					case 'ì':
					case 'ò':
					case 'ù':
						PressurePlateTile tmpPPT = (PressurePlateTile) tile;
						g.setColor(tmpPPT.getColor());
						if (tmpPPT.isOccupiedByMoveblock()) {
							g.fillRect(drawX + startX, drawY + startY + size - 2, size, 2);
						} else {
							g.fillRect(drawX + startX, drawY + startY + size - 3, size, 3);
						}

						break;
					case '!':
						g.setColor(Color.WHITE);
						g.fillRect(drawX + startX, drawY + startY, size, size);
						break;
					case '2':
						g.setColor(Color.GRAY);
						g.fillRect(drawX + startX, drawY + startY, size, size);
						break;

					default:
						break;
					}

				}

			}
		}

		// Doors
		drawY = 0;
		for (Tile[] row : tileWorld) {
			drawX = 0;
			drawY += size;
			for (Tile tile : row) {
				drawX += size;
				if (tile == null || tile.getKey() != 'D') {
					continue;
				}
				DoorTile tmpDoorTile = (DoorTile) tile;
				g.setColor(tmpDoorTile.getColor());
				if (!tmpDoorTile.isOpen()) {
					g.fillRect(drawX + startX, drawY + startY, size, size);
				} else if (tmpDoorTile.getRotation() % 2 == 0) {
					g.fillRect(drawX + startX - 2, drawY + startY, 2, size);
					g.fillRect(drawX + startX + size, drawY + startY, 2, size);
				} else {
					g.fillRect(drawX + startX, drawY + startY - 2, size, 2);
					g.fillRect(drawX + startX, drawY + startY + size, size, 2);
				}
			}
		}
		// Schalter
		for (int i = 0; i < allEntities.size(); i++) {
			Entity tmpEntity = allEntities.get(i);
			if (tmpEntity.getType() == "switch") {
				Switch tmpSwitch = (Switch) tmpEntity;
				g.setColor(tmpSwitch.getColor());
				if (tmpSwitch.isToggled()) {
					for (int j = 0; j < size; j++)
						g.fillRect((int) tmpSwitch.getX() / GameFrame.BLOCKSIZE * size + startX + size + size - j - 1,
								(int) tmpSwitch.getY() / GameFrame.BLOCKSIZE * size + startY + size + j, 1,
								1 + size - j - 1);
				} else
					for (int j = 0; j < size; j++)
						g.fillRect((int) tmpSwitch.getX() / GameFrame.BLOCKSIZE * size + startX + size + j,
								(int) tmpSwitch.getY() / GameFrame.BLOCKSIZE * size + startY + size + j, 1,
								1 + size - j - 1);
			}
		}

		// Spieler
		g.setColor(Color.RED);
		g.fillRect(player.getTileX() * size + size + startX, player.getTileY() * size + size + startY, size, size);

		// Kamera
		g.setColor(new Color(255, 255, 255, 50));
		g.fillRect(cameraX() / 45 * size + size + startX, cameraY() / 45 * size + size + startY, size * 20, size * 14);
		g.setColor(new Color(255, 255, 255, 60));
		g.drawRect(cameraX() / 45 * size + size + startX, cameraY() / 45 * size + size + startY, size * 20, size * 14);

	}

	public void drawTileIfNull(Graphics g, float interpolation, int x, int y) {

		if (worldDeco[y][x] == -1) {
			System.err.println("Überprüfe deine Wahrscheinlichkeitsverteilung.");
		} else {
			g.drawImage(nullTileImgs[worldDeco[y][x]], (int) (x * GameFrame.BLOCKSIZE - cameraX()),
					(int) (y * GameFrame.BLOCKSIZE - cameraY()), null);
			// if (inHandHandler != null && inHandHandler.isHoldingTetro()) {
			// if (Tools.distance((x + 0.5) * GameFrame.BLOCKSIZE - camera.getX(),
			// (y + 0.5) * GameFrame.BLOCKSIZE - camera.getY(), inHandHandler.getCenterX(),
			// inHandHandler.getCenterY()) < (TetroType.hitboxExpansion + 2) *
			// GameFrame.BLOCKSIZE) { // ungefähre
			// // entfernung
			// // passend
			//
			// g.drawImage(tetroPreview, (int) (x * GameFrame.BLOCKSIZE - cameraX()),
			// (int) (y * GameFrame.BLOCKSIZE - cameraY()), null);
			// }
			// }
		}
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
		for (Entity e : allEntities) {
			e.drawDebug(g, interpolation);
		}
		player.drawDebug(g, interpolation);
		g.setColor(Color.PINK);
		g.drawOval((int) ((4 - 0.5) * GameFrame.BLOCKSIZE - camera.getX()),
				(int) ((4 - 0.5) * GameFrame.BLOCKSIZE - camera.getY()), 5, 5);

	}

	public void tick() {
		GameLoop.actualupdates++;

		particleHandler.tick();

		// Player movement
		player.tick();

		for (Tile[] tar : tileWorld) {
			for (Tile t : tar) {
				if (t != null) {
					t.tick();
				}
			}
		}

		// camera adjustment
		camera.tick();

		for (int i = 0; i < allEntities.size(); i++) {
			allEntities.get(i).tick();
		}

		prepareRender();

		renderer.tick();

		if (keyHandler.getKillPlayer()) {
			keyHandler.resetKillPlayer();
			if (lastUsedSALTile != null)
				lastUsedSALTile.interact();
		}

	}

	private void prepareRender() {
		// add everything to renderer
		for (int j = 0; j < wallImgFrames.length; j++) {
			for (int i = 0; i < wallImgFrames[j].length; i++) {
				if (wallImgFrames[j][i] != null) {
					addIfNessessary(wallImgFrames[j][i]);
				}
			}
		}

		for (int j = 0; j < tileWorld.length; j++) {
			for (int i = 0; i < tileWorld[j].length; i++) {
				if (tileWorld[j][i] != null) {
					addIfNessessary(tileWorld[j][i]);
				}

			}
		}

		for (Entity entity : allEntities) {
			addIfNessessary(entity);
		}

	}

	private void addIfNessessary(DrawAndSortable das) {
		if (renderRect.contains(das.getX() - camera.getX(), das.getY() - camera.getY())) {
			if (!renderer.isDAScontained(das)) {
				das.addTo(renderer);
			}
		} else if (renderer.isDAScontained(das)) {
			renderer.removeDrawable(das);
		}
	}

	public void addTetro(TetroType tetroType, int x, int y, int mouse_x, int mouse_y, int rotation) {
		if (!keyHandler.getKameraKey() || noClip) {

			if (tetroAmount[this.tetroTypes.indexOf(tetroType)] > 0 || noClip) {
				int placeX;
				int placeY;

				if (x + camera.getX() < 0) {
					placeX = (x + camera.getX() - GameFrame.BLOCKSIZE / 2) / GameFrame.BLOCKSIZE;
				} else {
					placeX = (x + camera.getX() + GameFrame.BLOCKSIZE / 2) / GameFrame.BLOCKSIZE;
				}
				if (y + camera.getY() < 0) {
					placeY = (y + camera.getY() - GameFrame.BLOCKSIZE / 2) / GameFrame.BLOCKSIZE;
				} else {
					placeY = (y + camera.getY() + GameFrame.BLOCKSIZE / 2) / GameFrame.BLOCKSIZE;
				}
				Tetro tetro = new Tetro(tetroType, placeX, placeY, rotation, camera);
				if (isAllowed(tetro) && Panel.gamePanel.contains(mouse_x, mouse_y)) {
					// success
					tetroAmount[this.tetroTypes.indexOf(tetroType)] -= 1;
					newestTetros.add(0, tetro);
					tookBackTetro = false;
					tetros.add(tetro);
					addTetroToHitbox(tetro, placeX, placeY, rotation);

					playSound("synth", -5f);

				} else {
					playSound("error", -3f);
					System.err.println("nicht erlaubte Platzierung");
				}
			} else {
				frame.addLineToText("Es sind keine Tetros dieser Art mehr da.");
			}
		} else {
			frame.addLineToText("Tetros gesetzt werden. ");
			frame.addLineToText("Es können im Kameramodus keine");
		}

	}

	public void removeLastTetro() {
		if (!tookBackTetro || noClip) {
			if (newestTetros.size() == 0) {
				frame.addLineToText("Du hast seit dem letzten Speichervorgang noch kein Tetro platziert.");
			} else {
				Tetro newestTetro = newestTetros.get(0);
				if (!isPlayeronTetro(newestTetro) || noClip) {
					// success
					removeTetroFromHitbox(newestTetro, newestTetro.getX(), newestTetro.getY(),
							newestTetro.getRotation());
					tetroAmount[this.tetroTypes.indexOf(newestTetro.getType())] += 1;
					tetros.remove(newestTetro);
					newestTetros.remove(0);
					tookBackTetro = true;
					playSound("glassbreak", 5);
					particleHandler.startBreakingAnimation(newestTetro.getX(), newestTetro.getY(), newestTetro);
				} else {
					frame.addLineToText("Du stehst auf diesem Block.");
				}
			}
		} else {
			frame.addLineToText("Du kannst nur den zuletzt gesetzten Block entfernen.");
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

	private void removeTetroFromHitbox(Tetro tetro, int x, int y, int rotation) {
		boolean[][] hitbox = tetro.getType().getHitbox();

		for (int j = 0; j < hitbox.length; j++) {
			for (int i = 0; i < hitbox[j].length; i++) {
				if (hitbox[j][i]) {
					switch (rotation % 4) {
					case 0:
						if (j + y >= 0 && i + x >= 0 && j + y <= tileWorld.length && i + x <= tileWorld[0].length)
							tetroWorldHitbox[j + y][i + x] = false;
						break;
					case 1:
						if (-i + y + 3 >= 0 && j + x >= 0 && -i + y + 3 <= tileWorld.length
								&& j + x <= tileWorld[0].length)
							tetroWorldHitbox[-i + y + 3][j + x] = false;
						break;
					case 2:
						if (-j + y + 1 >= 0 && -i + x + 3 >= 0 && -j + y + 1 <= tileWorld.length
								&& -i + x + 3 <= tileWorld[0].length)
							tetroWorldHitbox[-j + y + 1][-i + x + 3] = false;
						break;
					case 3:
						if (i + y >= 0 && -j + x + 1 >= 0 && i + y <= tileWorld.length
								&& -j + x + 1 <= tileWorld[0].length)
							tetroWorldHitbox[i + y][-j + x + 1] = false;
						break;
					}
				}
			}

		}
	}

	private boolean isPlayeronTetro(Tetro tetro) {
		boolean[][] hitbox = tetro.getType().getHitbox();
		int x = tetro.getX();
		int y = tetro.getY();
		int rotation = tetro.getRotation();
		int playerX = player.getTileX();
		int playerY = player.getTileY();

		for (int j = 0; j < hitbox.length; j++) {
			for (int i = 0; i < hitbox[j].length; i++) {
				if (hitbox[j][i]) {
					switch (rotation % 4) {
					case 0:
						if (j + y == playerY && i + x == playerX)
							return true;
						break;
					case 1:
						if (-i + y + 3 == playerY && j + x == playerX)
							return true;
						break;
					case 2:
						if (-j + y + 1 == playerY && -i + x + 3 == playerX)
							return true;
						break;
					case 3:
						if (i + y == playerY && -j + x + 1 == playerX)
							return true;
						break;
					}
				}
			}

		}
		return false;
	}

	public void save(String path, String fileName) {
		ArrayList<RawTetro> rawTetros = new ArrayList<>();
		for (Tetro t : tetros) {
			rawTetros.add(createRawTetro(t));
		}

		Level temporaryLevel = new Level(tetroTypes, rawTetros, tileWorld, doors, otherEntities, tetroAmount,
				toggleStates, tetroFileURL, player.getTileX(), player.getTileY());
		LevelSaver saver = new LevelSaver();
		saver.saveLevel(temporaryLevel, path, fileName);

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

	public Tile getTileAt(int tileY, int tileX) {
		return tileWorld[tileY][tileX];
	}

	public void backToTheOverworld(boolean died) {
		frame.changeToOverworld(died, new RawPlayer(player.getAcc(), player.getBrake(), player.getMaxSpeed()));
	}

	public Player getPlayer() {
		return player;
	}

	public int getMaxX() {
		return tileWorld[0].length * GameFrame.BLOCKSIZE;
	}

	public int getMaxY() {
		return tileWorld.length * GameFrame.BLOCKSIZE;
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
			if (dT.getColorAsInt() == color) {
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
		if (tileWorld[((int) y + GameFrame.BLOCKSIZE / 2) / GameFrame.BLOCKSIZE][((int) x + GameFrame.BLOCKSIZE / 2)
				/ GameFrame.BLOCKSIZE] != null) {
			tileWorld[((int) y + GameFrame.BLOCKSIZE / 2) / GameFrame.BLOCKSIZE][((int) x + GameFrame.BLOCKSIZE / 2)
					/ GameFrame.BLOCKSIZE].interact();
		}
		for (Entity e : toAdd) {
			allEntities.add(e);
			otherEntities.add(e);
			renderer.addDrawable(e);
		}
		toAdd.clear();
		for (Entity e : toRemove) {
			allEntities.remove(e);
			otherEntities.remove(e);
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

	public void addTetroAmount(int[] tetroAmount) {
		for (int i = 0; i < tetroAmount.length; i++) {

			if (i < this.tetroAmount.length)
				this.tetroAmount[i] += tetroAmount[i];
		}
	}

	public boolean isEntityAt(Entity collider, double y, double x) {
		for (Entity barrier : allEntities) {
			if (barrier == collider
					|| (collider instanceof Player && barrier == ((Player) collider).getMovingBlockInHand())
					|| Math.abs(barrier.getX() - x) + Math.abs(barrier.getY() - y) > 3 * GameFrame.BLOCKSIZE) {
				continue;
			} else {
				if (barrier.collidesWith(y, x)) {
					// System.ot.println(collider + " collides with " + barrier);
					return true;
				}
			}
		}
		return false;
	}

	public void addInHandHandler(InHandHandler inHandHandler) {
		this.inHandHandler = inHandHandler;

	}

	public void rotateTetro() {
		if (inHandHandler != null) {
			inHandHandler.rotateInHand(false);
			keyHandler.setRotateKey(false);
		}

	}

	public void cameraTrackingShot(int x, int y) {
		camera.trackingShot(x, y);
	}

	public void setLastUsedSALTile(SaveNLoadTile tile) {
		newestTetros = new ArrayList<>();
		lastUsedSALTile = tile;
	}

	public void setLastCrossedSALTile(SaveNLoadTile tile) {
		
		lastCrossedSALTile = tile;
	}

	public SaveNLoadTile getLastCrossedSALTile() {
		return lastCrossedSALTile;
	}

	public double minDistanceToEntity(int rotation, double x, double y, Entity collider, double minDist) {
		minDist = 100;
		for (Entity barrier : allEntities) {
			if (barrier == collider
					|| (collider instanceof Player && barrier == ((Player) collider).getMovingBlockInHand())
					|| Math.abs(barrier.getX() - x) + Math.abs(barrier.getY() - y) > 3 * GameFrame.BLOCKSIZE) {
				continue;
			} else {
				switch (rotation) {
				case 0:
					if (barrier.getX() < x - GameFrame.BLOCKSIZE / 2 || barrier.getX() >= x + GameFrame.BLOCKSIZE
							|| barrier.getY() > y) {
						continue;
					} else {
						if (y - barrier.getY() < minDist) {
							minDist = y - barrier.getY();
							lastTouched = barrier;
						}
					}
					break;
				case 1:
					if (barrier.getY() < y - GameFrame.BLOCKSIZE / 2 || barrier.getY() >= y + GameFrame.BLOCKSIZE
							|| barrier.getX() < x) {
						continue;
					} else {
						if (barrier.getX() - x < minDist) {
							minDist = barrier.getX() - x;
							lastTouched = barrier;
						}
					}
					break;
				case 2:
					if (barrier.getX() < x - GameFrame.BLOCKSIZE / 2 || barrier.getX() >= x + GameFrame.BLOCKSIZE
							|| barrier.getY() < y) {
						continue;
					} else {
						if (barrier.getY() - y < minDist) {
							minDist = barrier.getY() - y;
							lastTouched = barrier;
						}
					}
					break;
				case 3:
					if (barrier.getY() < y - GameFrame.BLOCKSIZE / 2 || barrier.getY() >= y + GameFrame.BLOCKSIZE
							|| barrier.getX() > x) {
						continue;
					} else {
						if (x - barrier.getX() < minDist) {
							minDist = x - barrier.getX();
							lastTouched = barrier;
						}
					}
					break;
				}
				// Only colliders left

			}
		}
		return minDist;
	}

	public void switchNoClip() {
		noClip = !noClip;
		player.switchNoClip();

	}

	public MovingBlock getLastTouchedMovingBlock() {
		if (lastTouched instanceof MovingBlock)
			return (MovingBlock) lastTouched;
		else
			return null;
	}

	public ParticleHandler getParticleHandler() {
		return particleHandler;
	}

}
