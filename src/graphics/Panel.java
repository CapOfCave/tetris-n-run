package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
	protected final int width = 1300, height = 900;
	public static final Rectangle gamePanel = new Rectangle(35, 35, 921, 621);
	protected final Rectangle inventoryPanel = new Rectangle(180, 671, 879, 854);
	protected boolean debugMode = false;

	protected KeyHandler keyHandler;
	protected ArrayList<TetroType> tetroTypes;
	protected ArrayList<Point> tetroDrawPositions;
	protected float interpolation;
	protected int ups;
	protected int fps;
	protected GameFrame frame;
	protected World world;
	

	public Panel(Level level, KeyHandler keyHandler, GameFrame frame) {
		this.keyHandler = keyHandler;
		this.frame = frame;
		setPreferredSize(new Dimension(width, height));
		tetroTypes = level.getTetroTypes();
		tetroDrawPositions = new ArrayList<>();

		tetroDrawPositions.add(new Point(1070, 72));
		tetroDrawPositions.add(new Point(1025, 175));
		tetroDrawPositions.add(new Point(1145, 175));
		tetroDrawPositions.add(new Point(1025, 290));
		tetroDrawPositions.add(new Point(1145, 290));
		tetroDrawPositions.add(new Point(1025, 406));
		tetroDrawPositions.add(new Point(1165, 406));

	}

	@Override
	public void render(float interpolation, int fps, int ups) {
		this.interpolation = interpolation;
		this.fps = fps;
		this.ups = ups;
		repaint();
	}

	@Override
	public void tick() {
		world.tick();

		if (keyHandler.isF3pressed()) {
			debugMode = !debugMode;
			keyHandler.setF3pressed(false);
			frame.addLineToText("debugMode " + (debugMode ? "enabled" : "disabled"));
		}
		if (keyHandler.isF4pressed()) {
			world.getPlayer().switchNoClip();
			keyHandler.setF4pressed(false);
			frame.addLineToText("noClip " + (world.getPlayer().getNoClip() ? "enabled" : "disabled"));
		}

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	protected void drawDebug(Graphics g) {
		world.drawDebug(g, interpolation);
		
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("", Font.PLAIN, 15));
		g.drawString("FPS: " + fps + " / Updates: " + ups, 15, 25);

		
	}

}
