package input;

import java.awt.PointerInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import graphics.GameFrame;
import logics.worlds.World;

public class GuiMouseHandler implements MouseListener {

	GameFrame frame;
	World overworld;
	PointerInfo a;

	public GuiMouseHandler(GameFrame frame, World world) {
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
		int x = e.getX();
		int y = e.getY();
		
		if (x >= 1008 && y >= 286 && x < 1255 && y < 386) {
			// play button
			frame.playSound("ButtonKlick", -5f);
		}
		
		if (x >= 1008 && y >= 386 && x < 1255 && y < 486) {
			// load button
			frame.playSound("ButtonKlick", -5f);
		}
		
		if (x >= 1008 && y >= 486 && x < 1255 && y < 586) {
			// menu button
			frame.playSound("ButtonKlick", -5f);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		
		
		if (x >= 1008 && y >= 286 && x < 1255 && y < 386) {
			// play button
			frame.startLevel();
			//overworld.save(System.getenv("APPDATA") + "\\tetris-n-run\\levelSaves" , "\\overworldSave.txt");
		}
		
		if (x >= 1008 && y >= 386 && x < 1255 && y < 486) {
			// load button
			frame.loadLevel();
			overworld.save(System.getenv("APPDATA") + "\\tetris-n-run\\saves" , "\\overworldSave.txt");
		}
		
		if (x >= 1008 && y >= 486 && x < 1255 && y < 586) {
			// menu button
			frame.backToMenu();
			overworld.save(System.getenv("APPDATA") + "\\tetris-n-run\\saves" , "\\overworldSave.txt");
		}
	}

}
