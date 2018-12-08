package input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import graphics.Frame;
import logics.worlds.Overworld;

public class GuiMouseHandler implements MouseListener {

	Frame frame;
	Overworld overworld;

	public GuiMouseHandler(Frame frame, Overworld world) {
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
		
		frame.addLineToText("Test");
		

		if (x >= 180 && y >= 671 && x < 879 && y < 854) {
			overworld.getPlayer().inventoryClick(e.getX(), e.getY());
		}

		if (x >= 1008 && y >= 225 && x < 1255 && y < 325) {
			// play button
			frame.playSound("klick", -5f);
			frame.startLevel();
			overworld.save(System.getenv("APPDATA") + "\\tetris-n-run\\levelSaves" , "\\overworldSave.txt");
		}
		if (x >= 1008 && y >= 325 && x < 1255 && y < 425) {
			// load button
			frame.playSound("klick", -20f);
			frame.loadLevel();
			overworld.save(System.getenv("APPDATA") + "\\tetris-n-run\\levelSaves" , "\\overworldSave.txt");
		}
	}

}
