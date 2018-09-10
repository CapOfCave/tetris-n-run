package input;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

import logics.InHandHandler;
import logics.World;

/**
 * @author Lars Created on 05.08.2018
 */
public class MouseHandler implements MouseInputListener {

	private int mouseX = -1;
	private int mouseY = -1;
	private InHandHandler inHandHandler;
	private World world;

	public MouseHandler(InHandHandler inHandHandler, World world) {
		this.inHandHandler = inHandHandler;
		this.world = world;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			// Linke Maustaste: Aufheben
			inHandHandler.setInHandPosition(e.getX(), e.getY());
			inHandHandler.setInHand(e.getX(), e.getY());
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			// Rechte Maustaste : drehen
			inHandHandler.rotateInHand(false);
		} else if (e.getButton() == MouseEvent.BUTTON2) {
			//TODO temporary save option
			world.save("C:\\Users\\AMD\\tetros\\level.txt");
		} 
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1)
			inHandHandler.placeInHand();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		inHandHandler.setInHandPosition(e.getX(), e.getY());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO mousemove-animations
	}

	/*
	 * __________________________________________________________________________________________
	 */
	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	public int getMouseX() {
		return mouseX;
	}

	public int getMouseY() {
		return mouseY;
	}

}
