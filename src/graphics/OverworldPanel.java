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

	public OverworldPanel(int width, int height, Level level, KeyHandler keyHandler, GameFrame frame, RawPlayer rawPlayer) {
		super(width, height, level, keyHandler , frame);

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
			gameGraphics.setColor(Color.WHITE);
			gameGraphics.fillRect(0, 0, 170, 55);
			drawDebug(gameGraphics);
		}
		g.drawImage(ImageLoader.loadImage("/res/backOverworld.png"), 0, 0, 1300, 900, null);
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("Timesnewroman", 1, 44));
		
		
		if(Character.isLowerCase(frame.getNextLevel())) {
			g.setColor(Color.BLACK);
			
			switch ((Character.getNumericValue(frame.getNextLevel()) - 9)) {
			case 1:
				g.setFont(new Font("Timesnewroman", 1, 34));
				g.drawString("Aller Anfang", 1035, 150);
				g.drawString("ist schwer", 1050, 190);
				g.setFont(new Font("Timesnewroman", 1, 44));
				break;
				
			case 2:
				g.setFont(new Font("Timesnewroman", 1, 34));
				g.drawString("Türen und", 1053, 150);
				g.drawString("Schalter", 1067, 190);
				g.setFont(new Font("Timesnewroman", 1, 44));
				break;
				
			case 4:
				g.setFont(new Font("Timesnewroman", 1, 34));
				g.drawString("Kreuzweg", 1051, 150);
				g.setFont(new Font("Timesnewroman", 1, 44));
				break;
				
			case 5:
				g.setFont(new Font("Timesnewroman", 1, 34));
				g.drawString("Am Ziel", 1070, 150);
				g.drawString("vorbei", 1080, 190);
				g.setFont(new Font("Timesnewroman", 1, 44));
				break;
				
			case 6:
				g.setFont(new Font("Timesnewroman", 1, 34));
				g.drawString("Behalte den", 1040, 150);
				g.drawString("Durchblick!", 1043, 190);
				g.setFont(new Font("Timesnewroman", 1, 44));
				break;
				
			case 7:
				g.setFont(new Font("Timesnewroman", 1, 34));
				g.drawString("Mangel an", 1047, 150);
				g.drawString("Tetros.", 1075, 190);
				g.setFont(new Font("Timesnewroman", 1, 44));
				break;

			default:
				g.setFont(new Font("Timesnewroman", 1, 44));
				g.drawString("Level " + (Character.getNumericValue(frame.getNextLevel()) - 9), 1055, 150);
				break;
			}
			
			
			g.drawString("Play", 1085, 360);
			
		} else {
			g.setColor(Color.GRAY);
			g.setFont(new Font("Timesnewroman", 1, 34));
			g.drawString("Kein Level", 1048, 150);
			g.setFont(new Font("Timesnewroman", 1, 44));
			g.drawString("Play", 1085, 360);
		}
		g.setColor(Color.BLACK);
		
		Graphics2D chatCraphics = (Graphics2D) g.create( 890, 671, 365, 184);
		
		g.drawString("Load", 1075, 470);
		g.drawString("Menu", 1070, 580);
		String[] text = frame.getText();
		chatCraphics.setFont(new Font("Timesnewroman", 0, 18));
		if(text.length > 0){
		for(int i = 0; i < text.length; i++) {
			chatCraphics.drawString(text[i], 30, 40 + (i * 21));
			
		}
		}

	}

	public void save() {
		world.save(System.getenv("APPDATA") + "\\tetris-n-run\\levelSaves", "overworldSave.txt");
		
	}

	
	
	

	

}
