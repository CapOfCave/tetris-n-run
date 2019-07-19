package input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import graphics.MenuFrameHandler;
import graphics.OptionPanel;

public class OptionMouseHandler implements MouseListener {

	MenuFrameHandler frame;
	OptionPanel panel;

	public OptionMouseHandler(MenuFrameHandler frame, OptionPanel panel) {
		this.frame = frame;
		this.panel = panel;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		panel.mousePressed(e.getX() - frame.getPanelOffsetX(), e.getY()- frame.getPanelOffsetY());

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

	}

}
