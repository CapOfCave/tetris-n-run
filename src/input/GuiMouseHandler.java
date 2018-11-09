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

		if (x >= 180 && y >= 671 && x < 879 && y < 854) {
			overworld.getPlayer().inventoryClick(e.getX(), e.getY());
		}

		if (x >= 1008 && y >= 225 && x < 1255 && y < 325) {
			// playbutton
			frame.startLevel();
		}
	}

}
