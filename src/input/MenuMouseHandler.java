package input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import graphics.MenuPanel;


public class MenuMouseHandler implements MouseListener, MouseMotionListener {
	
	MenuPanel panel;
	
	public MenuMouseHandler(MenuPanel panel) {
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
		panel.mousePressed(e.getX() - panel.getPanelOffsetX(), e.getY()- panel.getPanelOffsetY());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		panel.mouseReleased(e.getX() - panel.getPanelOffsetX(), e.getY()- panel.getPanelOffsetY());
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		panel.mouseMoved(e.getX() - panel.getPanelOffsetX(), e.getY()- panel.getPanelOffsetY());
		
	}

}
