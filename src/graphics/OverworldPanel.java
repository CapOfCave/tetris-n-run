package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import data.Level;
import data.RawPlayer;
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
//	private BufferedImage playButtonAkt = ImageLoader.loadImage("/res/play.png");
//	private BufferedImage playButtonDeakt = ImageLoader.loadImage("/res/playNot.png");

	public OverworldPanel(Level level, KeyHandler keyHandler, Frame frame, RawPlayer rawPlayer) {
		super(level, keyHandler , frame);

		 world = new Overworld(gamePanel, level, keyHandler, frame, rawPlayer);

		guiMouseHandler = new GuiMouseHandler(frame, (Overworld)world);
		addMouseListener(guiMouseHandler);
	}

	@Override
	protected void paintComponent(Graphics g) {
	super.paintComponent(g);
		
		
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		
		Graphics2D inventoryGraphics = (Graphics2D) g.create(inventoryPanel.x, inventoryPanel.y, inventoryPanel.width,
				inventoryPanel.height);


		Graphics2D gameGraphics = (Graphics2D) g.create(gamePanel.x, gamePanel.y, gamePanel.width, gamePanel.height);
		world.draw(gameGraphics, interpolation, debugMode);
		Graphics2D previewGraphics = (Graphics2D) g.create( 54, 680, 1000, 1000);
		world.drawPlayerPreview(previewGraphics);
		world.drawInventory(inventoryGraphics);
		if (debugMode) {
			drawDebug(gameGraphics);
		}
		g.drawImage(ImageLoader.loadImage("/res/backOverworld.png"), 0, 0, 1300, 900, null);
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("Timesnewroman", 1, 44));
		
		
		if(Character.isLowerCase(frame.getNextLevel())) {
			g.setColor(Color.BLACK);
			g.drawString("Level " + (Character.getNumericValue(frame.getNextLevel()) - 9), 1055, 150);
			g.drawString("Play", 1080, 290);
			
		} else {
			g.setColor(Color.GRAY);
			g.drawString("Kein Level", 1020, 150);
			g.drawString("Play", 1080, 290);
		}
		g.setColor(Color.BLACK);
		
		g.drawString("Load", 1080, 400);
		String[] text = frame.getText();
		g.setFont(new Font("Timesnewroman", 0, 18));
		if(text.length > 0){
		for(int i = 0; i < text.length; i++) {
			g.drawString(text[i], 920, 710 + (i * 21));
		}
		}

	}

	public void save() {
		world.save(System.getenv("APPDATA") + "\\tetris-n-run\\levelSaves", "overworldSave.txt");
		
	}
	
	

	

}
