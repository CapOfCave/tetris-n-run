package input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import graphics.MenuFrame;
import graphics.MenuPanel;


public class MenuMouseHandler implements MouseListener {
	
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
