package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import input.OptionButton;
import input.OptionMouseHandler;
import loading.SettingSaver;
import tools.Fonts;

public class OptionPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private ArrayList<Integer> keyCodes;
	private ArrayList<OptionButton> optionButtons;

	private MenuFrameHandler menuFrame;
	private OptionMouseHandler mouseHandler;
	private BufferedImage option;

	private double fontMultiplier;

	private int highlight = -1;

	public OptionPanel(MenuFrameHandler menuFrame) {
		keyCodes = new ArrayList<Integer>();

		int panelWidth = menuFrame.getPanelWidth();
		int panelHeight = menuFrame.getPanelHeight();

		optionButtons = new ArrayList<OptionButton>();
		optionButtons.add(new OptionButton(320 * panelWidth / 1920, 250 * panelHeight / 1080, "Up:", 0, this));
		optionButtons.add(new OptionButton(320 * panelWidth / 1920, 300 * panelHeight / 1080, "Left:", 1, this));
		optionButtons.add(new OptionButton(320 * panelWidth / 1920, 350 * panelHeight / 1080, "Down:", 2, this));
		optionButtons.add(new OptionButton(320 * panelWidth / 1920, 400 * panelHeight / 1080, "Right:", 3, this));
		optionButtons.add(new OptionButton(320 * panelWidth / 1920, 450 * panelHeight / 1080, "Interaction:", 4, this));
		optionButtons.add(new OptionButton(320 * panelWidth / 1920, 500 * panelHeight / 1080, "Rotate:", 5, this));
		optionButtons.add(new OptionButton(320 * panelWidth / 1920, 550 * panelHeight / 1080, "Remove:", 6, this));
		optionButtons
				.add(new OptionButton(320 * panelWidth / 1920, 600 * panelHeight / 1080, "Move the camera:", 7, this));
		optionButtons.add(new OptionButton(320 * panelWidth / 1920, 650 * panelHeight / 1080, "Retry: ", 8, this));
		optionButtons.add(new OptionButton(320 * panelWidth / 1920, 700 * panelHeight / 1080, "Run: ", 9, this));
		optionButtons.add(new OptionButton(320 * panelWidth / 1920, 750 * panelHeight / 1080, "Delete last checkpoint: ", 10, this));
//		optionButtons.add(new OptionButton(320 * panelWidth / 1920, 800 * panelHeight / 1080,
//				"Delete last checkpoint: ", 11, this));

		keyCodes.add(0, 87);
		keyCodes.add(1, 65);
		keyCodes.add(2, 83);
		keyCodes.add(3, 68);
		keyCodes.add(4, 32);
		keyCodes.add(5, 82);
		keyCodes.add(6, 69);
		keyCodes.add(7, 17);
		keyCodes.add(8, 70);
		keyCodes.add(9, 16);
//		keyCodes.add(10, 84);
		keyCodes.add(10, 8);

		this.menuFrame = menuFrame;
		mouseHandler = new OptionMouseHandler(menuFrame, this);
		setPreferredSize(new Dimension(menuFrame.getScreenSize()));
		addMouseListener(mouseHandler);
		addMouseMotionListener(mouseHandler);
		setBackground(Color.BLACK);

		fontMultiplier = (menuFrame.getPanelWidth() / 1920.);

	}

	public void initImages() {
		option = menuFrame.getImage("/res/options.png");
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.translate(menuFrame.getPanelOffsetX(), menuFrame.getPanelOffsetY());
		g.drawImage(option, 0, 0, menuFrame.getPanelWidth(), menuFrame.getPanelHeight(), null);

		g.setColor(Color.BLACK);
		g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, (int) (80 * fontMultiplier)));
		String headline = "Controls";
		g.drawString(headline, menuFrame.getPanelWidth() / 2 - (g.getFontMetrics().stringWidth(headline) / 2),
				130 * menuFrame.getPanelHeight() / 1080);

		int size = (int) (55 * fontMultiplier);
		int sizeDif = (int) (10 * fontMultiplier);
		if (highlight == 0) {
			g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, sizeDif + size));
		} else {
			g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, size));
		}
		Fonts.drawCenteredString("Menu", getMenuBounds(), g);
		g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, (int) (24 * fontMultiplier)));

		for (int i = 0; i < optionButtons.size(); i++) {
			optionButtons.get(i).draw(g);
		}

	}

	private Rectangle getMenuBounds() {
		return new Rectangle(1331 * menuFrame.getPanelWidth() / 1920, 906 * menuFrame.getPanelHeight() / 1080,
				354 * menuFrame.getPanelWidth() / 1920, 120 * menuFrame.getPanelHeight() / 1080);

	}

	public void changeKeyCode(int keyCode) {
		for (int i = 0; i < optionButtons.size(); i++) {
			optionButtons.get(i).changeKeyCode(keyCode);
		}
		repaint();
	}

	public ArrayList<Integer> getKeyCodes() {
		return keyCodes;
	}

	public void setKeyCodes(ArrayList<Integer> keyCodes) throws IllegalArgumentException {
		if (this.keyCodes.size() != keyCodes.size()) {
			System.err.println("Keycode file Corrupted. Reloading defaults");
		} else {
			this.keyCodes = keyCodes;
		}

	}

	public void changeSingleKeyCode(int index, int newKeyCode) {
		keyCodes.set(index, newKeyCode);
	}

	public void mousePressed(int x, int y) {

		for (int i = 0; i < optionButtons.size(); i++) {
			optionButtons.get(i).deactivate();
		}
		for (int i = 0; i < optionButtons.size(); i++) {
			optionButtons.get(i).checkIfPressed(x, y);
		}

		if (getMenuBounds().contains(x, y)) {
			menuFrame.playSound("ButtonKlick", -5f);
		}
		repaint();

	}

	public void mouseReleased(int x, int y) {
		if (getMenuBounds().contains(x, y)) {

			SettingSaver.saveSettings(getKeyCodes(), menuFrame.getLevelSolved(),
					System.getenv("APPDATA") + "\\tetro-maze", "settings.txt");
			menuFrame.startMenu();
			repaint();
		}
	}

	public void mouseMoved(int x, int y) {
		if (getMenuBounds().contains(x, y)) {
			if (!isHighlighted(0)) {
				menuFrame.playSound("menuHover", -5f);
			}
			highlight(0);
		} else {
			highlight(-1);
		}
		repaint();
	}

	public void highlight(int i) {
		highlight = i;
	}

	public boolean isHighlighted(int i) {
		return i == highlight;
	}
}