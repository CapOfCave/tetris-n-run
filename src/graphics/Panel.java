package graphics;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JPanel;

import data.Level;
import data.TetroType;
import input.KeyHandler;
import logics.Playable;
import logics.worlds.World;

public abstract class Panel extends JPanel implements Playable {
	private static final long serialVersionUID = 1L;
	protected final int width = 1300, height = 850;
	protected final Rectangle gamePanel = new Rectangle(20, 20, 901, 601);
	protected boolean debugMode = false;
	protected KeyHandler keyHandler;
	protected ArrayList<TetroType> tetroTypes;
	protected ArrayList<Point> tetroDrawPositions;
	protected float interpolation;
	protected Frame frame;
	protected World world;
	
	public Panel(Level level, KeyHandler keyHandler, Frame frame) {
		this.keyHandler = keyHandler;
		this.frame = frame;
		setPreferredSize(new Dimension(width, height));
		tetroTypes = level.getTetroTypes();
		tetroDrawPositions = new ArrayList<>();
		for (int i = 0; i < tetroTypes.size(); i++) {
			tetroDrawPositions.add(new Point(972, i * 100 + 72));
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
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

}
