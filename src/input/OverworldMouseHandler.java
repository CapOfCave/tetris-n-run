package input;

import java.awt.PointerInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import graphics.GameFrameHandler;
import graphics.OverworldPanel;
import logics.World;

public class OverworldMouseHandler implements MouseListener, MouseMotionListener {

	OverworldPanel panel;
	GameFrameHandler gameFrame;
	World overworld;
	PointerInfo a;

	public OverworldMouseHandler(GameFrameHandler gameFrame, OverworldPanel panel, World world) {
		this.gameFrame = gameFrame;
		this.overworld = world;
		this.panel = panel;

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX() - gameFrame.getPanelOffsetX();
		int y = e.getY() - gameFrame.getPanelOffsetY();

		if (panel.getStartBounds().contains(x, y)) {
			panel.click(0);
		} else if (panel.getLoadBounds().contains(x, y)) {
			panel.click(1);
		} else if (panel.getMenuBounds().contains(x, y)) {
			panel.click(2);
		} else {
			panel.click(-1);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int x = e.getX() - gameFrame.getPanelOffsetX();
		int y = e.getY() - gameFrame.getPanelOffsetY();

		if (!gameFrame.isLoading()) {
			if (panel.getStartBounds().contains(x, y) && panel.isClicked(0)) {
				panel.startLevel();
			}

			if (panel.getLoadBounds().contains(x, y) && panel.isClicked(1)) {
				gameFrame.loadLegacyLevel();
				overworld.initiateSaving(System.getenv("APPDATA") + "\\tetro-maze\\saves\\overworldSave.txt");
			}

			if (panel.getMenuBounds().contains(x, y) && panel.isClicked(2)) {
				gameFrame.backToMenu();
				overworld.initiateSaving(System.getenv("APPDATA") + "\\tetro-maze\\saves\\overworldSave.txt");
			}
		}
		panel.click(-1);

	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int x = e.getX() - gameFrame.getPanelOffsetX();
		int y = e.getY() - gameFrame.getPanelOffsetY();

		if (panel.getStartBounds().contains(x, y)) {
			if (!panel.isHighlighted(0)) {
				gameFrame.playSound("menuHover", -5f);
				panel.highlight(0);
			}
		} else if (panel.getLoadBounds().contains(x, y)) {
			if (!panel.isHighlighted(1)) {
				gameFrame.playSound("menuHover", -5f);
				panel.highlight(1);
			}
		} else if (panel.getMenuBounds().contains(x, y)) {
			if (!panel.isHighlighted(2)) {
				gameFrame.playSound("menuHover", -5f);
				panel.highlight(2);
			}
		} else {
			panel.highlight(-1);
		}
	}
}