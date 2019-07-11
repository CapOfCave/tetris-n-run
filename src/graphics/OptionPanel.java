package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import input.OptionButton;
import input.OptionMouseHandler;
import loading.SettingSaver;

public class OptionPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private ArrayList<Integer> keyCodes;
	private ArrayList<OptionButton> optionButtons;

	private MenuFrame frame;
	private OptionMouseHandler mouseHandler;
	private BufferedImage option;

	public OptionPanel(MenuFrame frame) {
		keyCodes = new ArrayList<Integer>();
		optionButtons = new ArrayList<OptionButton>();
		optionButtons.add(new OptionButton(100, 250, 500 ,"Up:", 0, this));
		optionButtons.add(new OptionButton(100, 300, 500, "Left:", 1, this));
		optionButtons.add(new OptionButton(100, 350, 500, "Down:", 2, this));
		optionButtons.add(new OptionButton(100, 400, 500, "Right:", 3, this));
		optionButtons.add(new OptionButton(100, 450, 500, "Interaction:", 4, this));
		optionButtons.add(new OptionButton(100, 500, 500, "Rotate:", 5, this));
		optionButtons.add(new OptionButton(100, 550, 500, "Remove:", 6, this));
		optionButtons.add(new OptionButton(100, 600, 500, "Move the camera:", 7, this));
		optionButtons.add(new OptionButton(100, 650, 500, "Seppuku: ", 8, this));
		
		keyCodes.add(0, 87);
		keyCodes.add(1, 65);
		keyCodes.add(2, 83);
		keyCodes.add(3, 68);
		keyCodes.add(4, 32);
		keyCodes.add(5, 82);
		keyCodes.add(6, 69);
		keyCodes.add(7, 17);
		keyCodes.add(8, 70);

		this.frame = frame;
		mouseHandler = new OptionMouseHandler(frame, this);
		setPreferredSize(new Dimension(GameFrame.PANEL_WIDTH, GameFrame.PANEL_HEIGHT));
		addMouseListener(mouseHandler);

		option = frame.getImage("/res/Optionen.png");

	}

	public OptionPanel(MenuFrame menuFrame, ArrayList<Integer> keyCodes) {
		this(menuFrame);
		this.keyCodes = keyCodes;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(option, 0, 0, null);
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("GameFrame.fontString", 1, 80));
		String headline = "Settings";
		g.drawString(headline, 650 - (g.getFontMetrics().stringWidth(headline) / 2), 130);
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("GameFrame.fontString", 1, 55));
		g.drawString("Menu", 1035, 823);
		g.setFont(new Font("GameFrame.fontString", 1, 24));
		
		for(int i = 0; i < optionButtons.size(); i++) {
			optionButtons.get(i).draw(g);
		}
		
	}

	public void mousePressed(int x, int y) {
		
		repaint();
		
		for(int i = 0; i < optionButtons.size(); i++) {
			optionButtons.get(i).deactivate();;
		}
		for(int i = 0; i < optionButtons.size(); i++) {
			optionButtons.get(i).checkIfPressed(x, y);
		}
		
		if (x >= 950 && y >= 745 && x <= 1250 && y <= 856) {
			SettingSaver.saveSettings(getKeyCodes(), frame.getLevelSolved(), System.getenv("APPDATA") + "\\tetris-n-run", "settings.txt");
			frame.startMenu();
			frame.playSound("ButtonKlick", -5f);
		}
		repaint();

	}

	public void changeKeyCode(int keyCode) {
		for(int i = 0; i < optionButtons.size(); i++) {
			optionButtons.get(i).changeKeyCode(keyCode);
		}
		repaint();
	}

	public ArrayList<Integer> getKeyCodes() {
		return keyCodes;
	}

	public void setKeyCodes(ArrayList<Integer> keyCodes) {
		this.keyCodes = keyCodes;
	}
	
	public void changeSingleKeyCode(int index, int newKeyCode) {
		keyCodes.set(index, newKeyCode);
	}

}