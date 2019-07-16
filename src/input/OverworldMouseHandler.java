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
	GameFrameHandler frame;
	World overworld;
	PointerInfo a;

	public OverworldMouseHandler(GameFrameHandler frame, OverworldPanel panel, World world) {
		this.frame = frame;
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
		int x = e.getX();
		int y = e.getY();

		if (startContains(x, y)) {
			panel.click(0);
		} else if (loadContains(x, y)) {
			panel.click(1);
		} else if (backToMenuContains(x, y)) {
			panel.click(2);
		} else {
			panel.click(-1);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		if (!frame.isLoading()) {
			if (startContains(x, y) && panel.isClicked(0)) {
				panel.startLevel();
			}

			if (loadContains(x, y)&& panel.isClicked(1)) {
				frame.loadLegacyLevel();
				overworld.initiateSaving(System.getenv("APPDATA") + "\\tetro-maze\\saves\\overworldSave.txt");
			}

			if (backToMenuContains(x, y)&& panel.isClicked(2)) {
				frame.backToMenu();
				overworld.initiateSaving(System.getenv("APPDATA") + "\\tetro-maze\\saves\\overworldSave.txt");
			}
		}
		panel.click(-1);

	}

	private boolean startContains(int x, int y) {
		return x >= 1008 && y >= 296 && x < 1255 && y < 396;
	}

	private boolean loadContains(int x, int y) {
		return x >= 1008 && y >= 406 && x < 1255 && y < 506;
	}

	private boolean backToMenuContains(int x, int y) {
		return x >= 1008 && y >= 516 && x < 1255 && y < 612;
	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		if (startContains(x, y)) {
			if (!panel.isHighlighted(0)) {
				frame.playSound("menuHover", -6f);
				panel.highlight(0);
			}
		} else if (loadContains(x, y)) {
			if (!panel.isHighlighted(1)) {
				frame.playSound("menuHover", -6f);
				panel.highlight(1);
			}
		} else if (backToMenuContains(x, y)) {
			if (!panel.isHighlighted(2)) {
				frame.playSound("menuHover", -6f);
				panel.highlight(2);
			}
		} else {
			panel.highlight(-1);
		}
	}
}