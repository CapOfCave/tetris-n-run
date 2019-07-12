package input;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import graphics.OptionPanel;

public class OptionButton {

	private int x;
	private int xOffset = -1;
	private int y;
	private int xBeginOfButtonRel;
	private String text;
	private boolean activ = false;
	private int indexInArray;
	private OptionPanel optionPanel;

	public OptionButton(int x, int y, int xBeginOfButtonRel, String text, int indexInArray, OptionPanel optionPanel) {
		this.x = x;
		this.y = y;
		this.xBeginOfButtonRel = xBeginOfButtonRel;
		this.text = text;
		this.optionPanel = optionPanel;
		this.indexInArray = indexInArray;

	}

	public void draw(Graphics g) {
		g.setFont(new Font("GameFrame.fontString", 0, 30));
		g.drawString(text, x, y);
		if (activ)
			g.setFont(new Font("GameFrame.fontString", 2, 35));

		if (xOffset < 0) {
			xOffset = g.getFontMetrics().stringWidth(getKeyText(optionPanel.getKeyCodes().get(indexInArray))) / 2;
		}
		g.drawString(getKeyText(optionPanel.getKeyCodes().get(indexInArray)), x + xBeginOfButtonRel - xOffset, y);
		// g.setFont(new Font("GameFrame.fontString", 1, 24));
	}

	public static String getKeyText(int keyCode) {
		switch (keyCode) {
		case 32:
			return "SPACE";
		case 17:
			return "CTRL";
		case 16:
			return "SHIFT";
		case 10:
			return "ENTER";
		case 37:
			return "LEFT";
		case 38:
			return "UP";
		case 39:
			return "RIGHT";
		case 40:
			return "DOWN";
		case 0:
			return "??";

		default:
			return KeyEvent.getKeyText(keyCode);
		}
	}

	public boolean checkIfPressed(int x, int y) {

		if ((this.x + xBeginOfButtonRel - 10 - xOffset) < x && (this.x + xBeginOfButtonRel + 100 - xOffset) > x
				&& this.y - 25 < y && this.y + 25 > y) {
			wasKlicked();
			return true;
		}
		return false;

	}

	public void wasKlicked() {
		activ = true;
	}

	public void deactivate() {
		activ = false;
	}

	public boolean changeKeyCode(int newKeyCode) {
		if (activ) {
			for (int i = 0; i < optionPanel.getKeyCodes().size(); i++) {
				if (optionPanel.getKeyCodes().get(i) == newKeyCode) {
					deactivate();
					return false;
				}
			}

			optionPanel.changeSingleKeyCode(indexInArray, newKeyCode);
			xOffset = -1;
			deactivate();
			return true;

		}

		return false;
	}

}
