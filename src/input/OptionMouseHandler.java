package input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import graphics.MenuFrame;
import graphics.OptionPanel;

public class OptionMouseHandler implements MouseListener {

	MenuFrame frame;
	OptionPanel panel;

	public OptionMouseHandler(MenuFrame frame, OptionPanel panel) {
		this.frame = frame;
		this.panel = panel;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		panel.mousePressed(e.getX(),e.getY());

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
