package input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import graphics.CreditsPanel;
import graphics.MenuFrameHandler;

public class CreditsMouseHandler implements MouseListener, MouseMotionListener {

	MenuFrameHandler frame;
	CreditsPanel panel;

	public CreditsMouseHandler(MenuFrameHandler frame, CreditsPanel panel) {
		this.frame = frame;
		this.panel = panel;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		panel.mousePressed(e.getX() - frame.getPanelOffsetX(), e.getY() - frame.getPanelOffsetY());

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
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		System.out.println("move");
		int x = e.getX() - frame.getPanelOffsetX();
		int y = e.getY() - frame.getPanelOffsetY();

		if (panel.getMenuBounds().contains(x, y)) {
			
			if (!panel.isHighlighted(0)) {
				frame.playSound("menuHover", -6f);
				panel.highlight(0);
			} else {
				panel.highlight(-1);
			}

		}
		
		panel.repaint();

	}
}
