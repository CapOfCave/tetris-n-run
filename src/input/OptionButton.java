package input;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import graphics.OptionPanel;

public class OptionButton {

	private int x;
	private int y;
	private int xBeginOfButton;
	private String text;
	private boolean activ = false;
	private int indexInArray;
	private OptionPanel optionPanel;

	public OptionButton(int x, int y, int xBeginOfButton, String text, int indexInArray, OptionPanel optionPanel) {
		this.x = x;
		this.y = y;
		this.xBeginOfButton = xBeginOfButton;
		this.text = text;
		this.optionPanel = optionPanel;
		this.indexInArray = indexInArray;

	}

	public void draw(Graphics g) {
		g.setFont(new Font("GameFrame.fontString", 0, 30));
		g.drawString(text + ": ", x, y);
		if (activ) 
			g.setFont(new Font("GameFrame.fontString", 2, 35));
		
		g.drawString(KeyEvent.getKeyText(optionPanel.getKeyCodes().get(indexInArray)), xBeginOfButton, y);
		// g.setFont(new Font("GameFrame.fontString", 1, 24));
	}

	public boolean checkIfPressed(int x, int y) {

		if ((xBeginOfButton - 10) < x && (xBeginOfButton + 100) > x && this.y - 25 < y && this.y + 25 > y) {
			System.out.println("true");
			wasKlicked();
			return true;
		}
		System.out.println("false");
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
			deactivate();
			return true;

		}

		return false;
	}

}
