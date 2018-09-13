package graphics;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import data.TetroType;
import input.GuiMouseHandler;
import input.KeyHandler;
import loading.ImageLoader;
import logics.Level;
import logics.Overworld;
import logics.Playable;
import logics.World;

/**
 * @author Marius Created on 13.09.2018
 */

public class OverworldPanel extends JPanel implements Playable {
	private static final long serialVersionUID = 1L;
	private final int width = 1300, height = 640;
	private final Rectangle gamePanel = new Rectangle(20, 20, 901, 601);
//	private final Rectangle gamePanel = new Rectangle(50, 50, 901, 601);
	private int blockSize;

	private Overworld overworld;
	
	private GuiMouseHandler guiMouseHandler;
	
	private BufferedImage playButton = ImageLoader.loadImage("/res/play.png");

	private ArrayList<TetroType> tetroTypes;
	private ArrayList<Point> tetroDrawPositions;
	private boolean debugMode = false;
	private float interpolation;
	
	public OverworldPanel(Level level, KeyHandler keyHandler) {
		setPreferredSize(new Dimension(width, height));
		blockSize = level.getBlockSize();
		overworld = new Overworld(gamePanel, blockSize, level, keyHandler);
		tetroTypes = level.getTetroTypes();
		tetroDrawPositions = new ArrayList<>();
		for (int i = 0; i < tetroTypes.size(); i++) {
			tetroDrawPositions.add(new Point(972, i * 100 + 72));
		}
		
		
		guiMouseHandler = new GuiMouseHandler(overworld);
		addMouseListener(guiMouseHandler);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		
		System.out.println("hibrgrasguzhvbes");
		Graphics2D gameGraphics = (Graphics2D)g.create(gamePanel.x, gamePanel.y, gamePanel.width, gamePanel.height);
		overworld.draw(gameGraphics, interpolation, debugMode);
		guiMouseHandler.drawSideBar(g, debugMode);
		overworld.drawPlayer(gameGraphics, interpolation, debugMode);
		
		g.drawImage(playButton, 100, 100, null);
		
		
	}

	@Override
	public void render(float interpolation) {
		this.interpolation = interpolation;
		repaint();
		
	}

	@Override
	public void tick() {
		overworld.tick();
		
	}

}
