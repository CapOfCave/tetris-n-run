package input;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

import data.TetroType;
import logics.InHandHandler;
import logics.worlds.World;

/**
 * @author Lars Created on 05.08.2018
 */
public class MouseHandler implements MouseInputListener {

	private int mouseX = -1;
	private int mouseY = -1;
	private InHandHandler inHandHandler;
	private World world;
	private TetroType currentlyHoveredTetro = null;

	public MouseHandler(InHandHandler inHandHandler, World world) {
		this.inHandHandler = inHandHandler;
		this.world = world;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (e.getX() > 1008 && e.getX() < 1255 && e.getY() > 516 && e.getY() < 616) {
				world.playSound("ButtonKlick", -5f);
				world.backToTheOverworld(true);
			}
			// Linke Maustaste: Aufheben

			inHandHandler.setInHandPosition(e.getX(), e.getY());
			inHandHandler.setInHand(e.getX(), e.getY());

		} else if (e.getButton() == MouseEvent.BUTTON3) {
			// Rechte Maustaste : drehen
			inHandHandler.rotateInHand(false);
		} else if (e.getButton() == MouseEvent.BUTTON2) {
			world.save("C:/JavaEclipse/", "level.txt");
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
		TetroType hovered = inHandHandler.getTetroTypeAt(e.getX(), e.getY());
		if(hovered != null && hovered != currentlyHoveredTetro) {
			world.playSound("menuHover", -6f);
		}
		currentlyHoveredTetro = hovered;
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

	public TetroType getCurrentlyHoveredTetro() {
		return currentlyHoveredTetro;
	}


}
