package logics;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import data.RawTetro;
import data.Tetro;
import data.TetroType;
import loading.ImageLoader;
import loading.LevelSaver;

/**
 * @author Lars Created on 05.08.2018
 */
public class GameWorld {

	private int blockSize;
	private Rectangle graphicClip;
	private String tetroFileURL;

	private BufferedImage blockImg;
	private BufferedImage backgroundImg;

	// private BufferedImage blockImgs;

	private Player player;
	private Camera camera;
	private int[][] world;

	private ArrayList<Tetro> tetros;
	private ArrayList<Tetro>[][] tetroWorldHitbox;
	private ArrayList<TetroType> tetroTypes;

//	@SuppressWarnings("unchecked")
//	public GameWorld(Rectangle graphicClip, int blockSize, ArrayList<TetroType> tetroTypes, String tetroFileURL) {
//		this.graphicClip = graphicClip;
//		this.blockSize = blockSize;
//		this.tetroTypes = tetroTypes;
//		this.tetroFileURL = tetroFileURL;
//		tetros = new ArrayList<>();
//		world = new int[30][40]; // [columns][rows] //[y][x]
//		tetroWorldHitbox = new ArrayList[world.length][world[0].length];
//		for (int j = 0; j < tetroWorldHitbox.length; j++) {
//			for (int i = 0; i < tetroWorldHitbox[j].length; i++) {
//				tetroWorldHitbox[j][i] = new ArrayList<>();
//			}
//		}
//		camera = new Camera(0, 0, world.length * blockSize - (int) graphicClip.getHeight(), world[0].length * blockSize - (int) graphicClip.getWidth(),
//				(int) (graphicClip.getWidth() / 2 - blockSize / 2.), (int) (graphicClip.getHeight() / 2 - blockSize / 2.));
//		player = new Player(blockSize, camera, tetros, tetroWorldHitbox);
//		blockImg = ImageLoader.loadImage("/res/block.png");
//		backgroundImg = ImageLoader.loadImage("/res/background.png");
//		// blockImgs = ImageLoader.loadImage("/res/blocks.png");
//	}

	@SuppressWarnings("unchecked")
	public GameWorld(Rectangle graphicClip, int blockSize, Level level) {
		
		this.graphicClip = graphicClip;
		this.blockSize = blockSize;
		this.tetroTypes = level.getTetroTypes();
		this.tetroFileURL = level.getTetrofileUrl();
		ArrayList<RawTetro> rawTetros = level.getUnfinishedTetros();
		tetros = new ArrayList<>();

		world = level.getArrWorld(); // [columns][rows] //[y][x]
		tetroWorldHitbox = new ArrayList[world.length][world[0].length];
		for (int j = 0; j < tetroWorldHitbox.length; j++) {
			for (int i = 0; i < tetroWorldHitbox[j].length; i++) {
				tetroWorldHitbox[j][i] = new ArrayList<>();
			}
		}
		
		camera = new Camera(level.getPlayerX() * blockSize, level.getPlayerY() * blockSize, world.length * blockSize - (int) graphicClip.getHeight(), world[0].length * blockSize - (int) graphicClip.getWidth(),
				(int) (graphicClip.getWidth() / 2 - blockSize / 2), (int) (graphicClip.getHeight() / 2 - blockSize / 2.));
		for (RawTetro ut : rawTetros) {
			Tetro ft = ut.createTetro(level.getTetroTypes(), blockSize, camera);
			tetros.add(ft);
			Point p1 = ft.getStartPoint1();
			tetroWorldHitbox[p1.y][p1.x].add(ft);
			Point p2 = ft.getStartPoint2();
			tetroWorldHitbox[p2.y][p2.x].add(ft);
		}
		player = new Player(blockSize, camera, tetros, tetroWorldHitbox, level.getPlayerX(), level.getPlayerY());
		
		blockImg = ImageLoader.loadImage("/res/block.png");
		backgroundImg = ImageLoader.loadImage("/res/background.png");
	}

	public void draw(Graphics2D g, float interpolation, boolean debugMode) {

		camera.prepareDraw(interpolation);

		// blocker blocks
		for (int j = 0; j < world.length; j++) {
			for (int i = 0; i < world[j].length; i++) {

				if (world[j][i] == 1) {
					g.drawImage(blockImg, i * blockSize - camera.getX(), j * blockSize - camera.getY(), blockSize, blockSize, null);
				} else if (world[j][i] == 0) {
					g.drawImage(backgroundImg, i * blockSize - camera.getX(), j * blockSize - camera.getY(), blockSize, blockSize, null);

				}
			}
		}

		// Lines - o-u
		for (int i = 0; i <= world[0].length * blockSize; i += blockSize) {
			g.drawLine((i - camera.getX()), 0, (i - camera.getX()), Math.min(graphicClip.height, world.length * blockSize - camera.getY()));
		}
		// l-r
		for (int i = 0; i <= world.length * blockSize; i += blockSize) {
			g.drawLine(0, (i - camera.getY()), Math.min(graphicClip.width, world[0].length * blockSize), (i - camera.getY()));
		}

		// Tetros
		for (Tetro t : tetros) {
			t.draw(g, debugMode);
		}

	}

	public void tick() {
		// Player movement
		player.tick();

		// camera adjustment
		camera.tick(player.getRealX(),player.getRealY());
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

		Point p1 = tetro.getStartPoint1();
		Point p2 = tetro.getStartPoint2();
		if (player.getXY().equals(p1) || player.getXY().equals(p2)) {
			player.move(tetro);
		} else {
			try {
				tetroWorldHitbox[p1.y][p1.x].add(tetro);
				tetroWorldHitbox[p2.y][p2.x].add(tetro);
			} catch (ArrayIndexOutOfBoundsException e) {
				// TODO Remove catch-block when collisions are working
			}

		}

	}

	public void drawPlayer(Graphics2D g, float interpolation, boolean debugMode) {
		// Player
		player.draw(g, interpolation);

	}

	public void save(String path) {
		ArrayList<RawTetro> rawTetros = new ArrayList<>();
		for (Tetro t : tetros) {
			rawTetros.add(createRawTetro(t));
		}

		System.out.println(player.getY());
		Level temporaryLevel = new Level(tetroTypes, rawTetros, world, blockSize, tetroFileURL, player.getX(), player.getY());
		LevelSaver saver = new LevelSaver();
		saver.saveLevel(temporaryLevel, path);

	}

	private RawTetro createRawTetro(Tetro tetro) {
		return new RawTetro(tetroTypes.indexOf(tetro.getType()), tetro.getX(), tetro.getY(), tetro.getRotation());
	}
}
