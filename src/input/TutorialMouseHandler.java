package input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import graphics.MenuFrame;
import graphics.TutorialPanel;

public class TutorialMouseHandler implements MouseListener {
	
	MenuFrame frame;
	TutorialPanel panel;
	
	public TutorialMouseHandler(MenuFrame frame, TutorialPanel panel) {
		this.frame = frame;
		this.panel = panel;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		panel.mousePressed(e.getX(), e.getY());
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}

}
