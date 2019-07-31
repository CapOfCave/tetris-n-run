package input;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

import data.TetroType;
import graphics.GameWorldPanel;
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
	GameWorldPanel gameWorlPanel;
	
	private TetroType currentlyHoveredTetro = null;

	public MouseHandler(InHandHandler inHandHandler, World world, GameWorldPanel gameWorlPanel) {
		this.inHandHandler = inHandHandler;
		this.world = world;
		this.gameWorlPanel = gameWorlPanel;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX() - world.getPanelOffsetX();
		int y = e.getY() - world.getPanelOffsetY();
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (gameWorlPanel.getBackBounds().contains(x,y)) {
				world.playSound("ButtonKlick", -5f);
				world.backToTheOverworld(true);
			}
			// Linke Maustaste: Aufheben

			inHandHandler.setInHandPosition(x, y);
			inHandHandler.setInHand(x, y);

		} else if (e.getButton() == MouseEvent.BUTTON3) {
			// Rechte Maustaste : drehen
			inHandHandler.rotateInHand(false);
		} else if (e.getButton() == MouseEvent.BUTTON2) {
			world.initiateSaving("C:/JavaEclipse/level.txt");
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
				inHandHandler.placeInHand();
			
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int x = e.getX() - world.getPanelOffsetX();
		int y = e.getY() - world.getPanelOffsetY();
		inHandHandler.setInHandPosition(x, y);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int x = e.getX() - world.getPanelOffsetX();
		int y = e.getY() - world.getPanelOffsetY();
		TetroType hovered = inHandHandler.getTetroTypeAt(x, y);
		if(hovered != null && hovered != currentlyHoveredTetro) {
			world.playSound("menuHover", -5f);
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
