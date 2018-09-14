package input;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import graphics.Frame;
import logics.Overworld;


public class GuiMouseHandler implements MouseListener {

	Frame frame;
	
	public GuiMouseHandler(Frame frame) {
		this.frame = frame;
	}

	

	@Override
	public void mouseClicked(MouseEvent e) {
		
		int x = e.getX();
		int y = e.getY();
		
		if (x >= 20 && y >= 20 && x < 900 && y < 600) {
			// in game Rect.
			System.out.println("Rect.");
		}
		
		if(x >= 970 && y >= 100 && x < 970 + 256 && y < 100 + 64) {
			//playbutton
			System.out.println("play");
			frame.startLevel();
		}
		
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
