package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import data.Level;
import input.GuiMouseHandler;
import input.KeyHandler;
import loading.ImageLoader;
import logics.worlds.Overworld;

/**
 * @author Marius Created on 13.09.2018
 */

public class OverworldPanel extends Panel {

	private static final long serialVersionUID = 1L;
	private GuiMouseHandler guiMouseHandler;
	private BufferedImage playButtonAkt = ImageLoader.loadImage("/res/play.png");
	private BufferedImage playButtonDeakt = ImageLoader.loadImage("/res/playNot.png");

	public OverworldPanel(Level level, KeyHandler keyHandler, Frame frame) {
		super(level, keyHandler, frame);

		world = new Overworld(gamePanel, level, keyHandler, frame);

		guiMouseHandler = new GuiMouseHandler(frame);
		addMouseListener(guiMouseHandler);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.WHITE);
		g.drawRect(0, 0, width, height);
		
		Graphics2D gameGraphics = (Graphics2D) g.create(gamePanel.x, gamePanel.y, gamePanel.width, gamePanel.height);
		
		
		world.draw(gameGraphics, interpolation, debugMode);
		// guiMouseHandler.drawSideBar(g, debugMode);
		world.drawPlayer(gameGraphics, interpolation, debugMode);
		
		g.drawImage(ImageLoader.loadImage("/res/backgroundOverworld.png"), 0, 0, null);
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("Calibri", 1, 60));
		
		
		if(Character.isLowerCase(frame.getNextLevel())) {
			g.setColor(Color.BLACK);
			g.drawString("Level " + frame.getNextLevel(), 1020, 150);
			g.drawImage(playButtonAkt, 970, 100, null);
			
		} else {
			g.drawString("Kein Level ausgewählt", 970, 80);
			g.drawImage(playButtonDeakt, 970, 100, null);
			
		}
//		gameGraphics.drawLine(0, 0, gamePanel.width - 1, 0);
//		gameGraphics.drawLine(0, 0, 0, gamePanel.height - 1);
//		gameGraphics.drawLine(gamePanel.width - 1, 0, gamePanel.width - 1, gamePanel.height - 1);
//		gameGraphics.drawLine(0, gamePanel.height - 1, gamePanel.width - 1, gamePanel.height - 1);

	}

}
