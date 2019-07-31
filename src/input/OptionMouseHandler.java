package input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import graphics.MenuFrameHandler;
import graphics.OptionPanel;

public class OptionMouseHandler implements MouseListener, MouseMotionListener {

	MenuFrameHandler frame;
	OptionPanel panel;

	public OptionMouseHandler(MenuFrameHandler frame, OptionPanel panel) {
		this.frame = frame;
		this.panel = panel;
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
		panel.mousePressed(e.getX() - frame.getPanelOffsetX(), e.getY()- frame.getPanelOffsetY());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		panel.mouseReleased(e.getX() - frame.getPanelOffsetX(), e.getY()- frame.getPanelOffsetY());
	}


	@Override
	public void mouseDragged(MouseEvent e) {
		
	}


	@Override
	public void mouseMoved(MouseEvent e) {
		panel.mouseMoved(e.getX() - frame.getPanelOffsetX(), e.getY() - frame.getPanelOffsetY());
		
	}

	
}
