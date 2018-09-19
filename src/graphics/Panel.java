package graphics;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JPanel;

import data.Level;
import data.TetroType;
import input.KeyHandler;
import input.MouseHandler;
import logics.InHandHandler;
import logics.Playable;
import logics.World;

/**
 * @author Lars Created on 05.08.2018
 */
public class Panel extends JPanel implements Playable {
	private static final long serialVersionUID = 1L;
	private final int width = 1300, height = 640;
	private final Rectangle gamePanel = new Rectangle(20, 20, 901, 601);
	// private final Rectangle gamePanel = new Rectangle(50, 50, 901, 601);
	private int blockSize;

	private World world;
	private InHandHandler inHandHandler;
	private MouseHandler mouseHandler;
	private KeyHandler keyHandler;

	private ArrayList<TetroType> tetroTypes;
	private ArrayList<Point> tetroDrawPositions;
	private boolean debugMode = false;
	private float interpolation;

	public Panel(Level level, KeyHandler keyHandler) {
		this.keyHandler = keyHandler;
		setPreferredSize(new Dimension(width, height));
		blockSize = level.getBlockSize();
		world = new World(gamePanel, blockSize, level, keyHandler);
		tetroTypes = level.getTetroTypes();
		tetroDrawPositions = new ArrayList<>();
		for (int i = 0; i < tetroTypes.size(); i++) {
			tetroDrawPositions.add(new Point(972, i * 85 + 40));
		}

		inHandHandler = new InHandHandler(tetroTypes, tetroDrawPositions, blockSize, gamePanel, world);
		mouseHandler = new MouseHandler(inHandHandler, world);
		addMouseListener(mouseHandler);
		addMouseMotionListener(mouseHandler);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D gameGraphics = (Graphics2D) g.create(gamePanel.x, gamePanel.y, gamePanel.width, gamePanel.height);
		world.draw(gameGraphics, interpolation, debugMode);
		inHandHandler.drawPreview(g, debugMode);
		world.drawPlayer(gameGraphics, interpolation, debugMode);
		for (int i = 0; i < tetroTypes.size(); i++) {
			tetroTypes.get(i).draw(g, tetroDrawPositions.get(i).x, tetroDrawPositions.get(i).y, 0, debugMode);
		}

	}

	@Override
	public void render(float interpolation) {
		this.interpolation = interpolation;
		repaint();

	}

	@Override
	public void tick() {
		world.tick();

		if (keyHandler.isF3pressed()) {
			debugMode = !debugMode;
			keyHandler.setF3pressed(false);
		}
		
	}
}
