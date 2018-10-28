package input;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import graphics.Frame;
import logics.worlds.Overworld;
import logics.worlds.World;


public class GuiMouseHandler implements MouseListener {

	Frame frame;
	Overworld overworld;
	
	public GuiMouseHandler(Frame frame, Overworld world) {
		this.frame = frame;
		this.overworld = world;
	}

	

	@Override
	public void mouseClicked(MouseEvent e) {
		
		int x = e.getX();
		int y = e.getY();
		
		if (x >= 180 && y >= 671 && x < 879 && y < 854) {
			overworld.getPlayer().inventoryClick(e.getX(), e.getY());
		}
		
		if(x >= 970 && y >= 100 && x < 970 + 256 && y < 100 + 64) {
			//playbutton
			frame.startLevel();
		}
		
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
