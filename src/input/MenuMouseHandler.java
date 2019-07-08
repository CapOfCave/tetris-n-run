package input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import graphics.MenuFrame;
import graphics.MenuPanel;


public class MenuMouseHandler implements MouseListener, MouseMotionListener {
	
	MenuFrame frame;
	MenuPanel panel;
	
	public MenuMouseHandler(MenuFrame frame, MenuPanel panel) {
		this.frame = frame;
		this.panel = panel;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		panel.mousePressed(e.getX(), e.getY());
		
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

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		panel.mouseMoved(e.getX(), e.getY());
		
	}

}
