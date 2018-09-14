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
	
	private BufferedImage playButtonAkt = ImageLoader.loadImage("/res/play.png");
	private BufferedImage playButtonDeakt = ImageLoader.loadImage("/res/playNot.png");

	private ArrayList<TetroType> tetroTypes;
	private ArrayList<Point> tetroDrawPositions;
	private boolean debugMode = false;
	private float interpolation;
	private Frame frame;
	private KeyHandler keyHandler;
	
	public OverworldPanel(Level level, KeyHandler keyHandler, Frame frame) {
		this.keyHandler = keyHandler;
		setPreferredSize(new Dimension(width, height));
		blockSize = level.getBlockSize();
		this.frame = frame;
		overworld = new Overworld(gamePanel, blockSize, level, keyHandler);
		tetroTypes = level.getTetroTypes();
		tetroDrawPositions = new ArrayList<>();
		for (int i = 0; i < tetroTypes.size(); i++) {
			tetroDrawPositions.add(new Point(972, i * 100 + 72));
		}
		
		
		guiMouseHandler = new GuiMouseHandler(frame);
		addMouseListener(guiMouseHandler);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D gameGraphics = (Graphics2D)g.create(gamePanel.x, gamePanel.y, gamePanel.width, gamePanel.height);
		overworld.draw(gameGraphics, interpolation, debugMode);
		//guiMouseHandler.drawSideBar(g, debugMode);
		overworld.drawPlayer(gameGraphics, interpolation, debugMode);
		
		
		
		if(Character.isLowerCase(frame.getNextLevel())) {
			g.drawString("Spiele Level: " + frame.getNextLevel(), 970, 80);
			g.drawImage(playButtonAkt, 970, 100, null);
		}else {
			g.drawString("Kein Level ausgewählt", 970, 80);
			g.drawImage(playButtonDeakt, 970, 100, null);
		}
			
		
	}

	@Override
	public void render(float interpolation) {
		this.interpolation = interpolation;
		this.repaint();
	}

	@Override
	public void tick() {
		overworld.tick();
		if (keyHandler.isF3pressed()) {
			debugMode = !debugMode;
			keyHandler.setF3pressed(false);
		}
		
	}

}
