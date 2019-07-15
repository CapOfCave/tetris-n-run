package input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import graphics.MenuFrameHandler;
import graphics.TutorialPanel;

public class TutorialMouseHandler implements MouseListener {
	
	MenuFrameHandler frame;
	TutorialPanel panel;
	
	public TutorialMouseHandler(MenuFrameHandler frame, TutorialPanel panel) {
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
