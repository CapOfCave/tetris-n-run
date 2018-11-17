package input;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

import logics.InHandHandler;
import logics.worlds.GameWorld;

/**
 * @author Lars Created on 05.08.2018
 */
public class MouseHandler implements MouseInputListener {

	private int mouseX = -1;
	private int mouseY = -1;
	private InHandHandler inHandHandler;
	private GameWorld world;

	public MouseHandler(InHandHandler inHandHandler, GameWorld world) {
		this.inHandHandler = inHandHandler;
		this.world = world;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if(e.getX() > 1008 && e.getX() < 1255 && e.getY() > 516 && e.getY() < 616) {
				world.playSound("klick", -5f);
				world.backToTheOverworld(true);
			}
			// Linke Maustaste: Aufheben

			inHandHandler.setInHandPosition(e.getX(), e.getY());
			if (!inHandHandler.setInHand(e.getX(), e.getY())) {
				world.getPlayer().hit();
			}
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			// Rechte Maustaste : drehen
			inHandHandler.rotateInHand(false);
		} else if (e.getButton() == MouseEvent.BUTTON2) {
			// TODO temporary save option
			world.save("C:\\JavaEclipse", "level.txt");
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {

			try {
				inHandHandler.placeInHand();
			} catch (ArrayIndexOutOfBoundsException ex) {
			}
		}
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
		
		int x = e.getX();
		int y = e.getY();
		
		// klick in Inventory
		if(x >= 20 && x <= 1280 && y >= 641 && y <= 830) {
			
			world.inventoryClick(x, y);
		}
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
