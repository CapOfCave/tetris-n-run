package input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import graphics.GameFrame;
import logics.worlds.Overworld;

public class GuiMouseHandler implements MouseListener {

	GameFrame frame;
	Overworld overworld;

	public GuiMouseHandler(GameFrame frame, Overworld world) {
		this.frame = frame;
		this.overworld = world;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		
		
		if (x >= 180 && y >= 671 && x < 879 && y < 854) {
			overworld.getPlayer().inventoryClick(e.getX(), e.getY());
		}

		if (x >= 1008 && y >= 286 && x < 1255 && y < 386) {
			// play button
			frame.playSound("ButtonKlick", -5f);
			frame.startLevel();
			//overworld.save(System.getenv("APPDATA") + "\\tetris-n-run\\levelSaves" , "\\overworldSave.txt");
		}
		
		if (x >= 1008 && y >= 386 && x < 1255 && y < 486) {
			// load button
			frame.playSound("ButtonKlick", -5f);
			frame.loadLevel();
			overworld.save(System.getenv("APPDATA") + "\\tetris-n-run\\levelSaves" , "\\overworldSave.txt");
		}
		
		if (x >= 1008 && y >= 486 && x < 1255 && y < 586) {
			// menu button
			frame.playSound("ButtonKlick", -5f);
			frame.backToMenu();
			overworld.save(System.getenv("APPDATA") + "\\tetris-n-run\\levelSaves" , "\\overworldSave.txt");
		}
	}

}
