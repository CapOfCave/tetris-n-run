package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import input.OptionMouseHandler;
import loading.ImageLoader;

public class OptionPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private ArrayList<Integer> keyCodes;
	private int keyCodeForChange = -1;

	private MenuFrame frame;
	private OptionMouseHandler mouseHandler;
	private final int width = 1300, height = 900;
	private BufferedImage option;

	public OptionPanel(MenuFrame frame) {
		keyCodes = new ArrayList<Integer>();
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
		setPreferredSize(new Dimension(width, height));
		addMouseListener(mouseHandler);

		option = ImageLoader.loadImage("/res/Optionen.png");

	}

	public OptionPanel(MenuFrame menuFrame, ArrayList<Integer> keyCodes) {
		this(menuFrame);
		this.keyCodes = keyCodes;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		g.drawImage(option, 0, 0, null);
		g.setColor(Color.BLACK);
		g.setFont(new Font("TimesNewRoman", 1, 55));
		g.drawString("Menu", 1035, 823);
		g.setFont(new Font("TimesNewRoman", 1, 24));
		
		if(keyCodeForChange == 0) 
			g.setFont(new Font("TimesNewRoman", 2, 26));
		g.drawString(KeyEvent.getKeyText(frame.getKeyCodes().get(0)), 152, 204);
		g.setFont(new Font("TimesNewRoman", 1, 24));
		
		if(keyCodeForChange == 1) 
			g.setFont(new Font("TimesNewRoman", 2, 26));
		g.drawString(KeyEvent.getKeyText(frame.getKeyCodes().get(1)), 160, 243);
		g.setFont(new Font("TimesNewRoman", 1, 24));
		
		if(keyCodeForChange == 2) 
			g.setFont(new Font("TimesNewRoman", 2, 26));
		g.drawString(KeyEvent.getKeyText(frame.getKeyCodes().get(2)), 188, 282);
		g.setFont(new Font("TimesNewRoman", 1, 24));
		
		if(keyCodeForChange == 3) 
			g.setFont(new Font("TimesNewRoman", 2, 26));
		g.drawString(KeyEvent.getKeyText(frame.getKeyCodes().get(3)), 175, 321);
		g.setFont(new Font("TimesNewRoman", 1, 24));
		
		if(keyCodeForChange == 4) 
			g.setFont(new Font("TimesNewRoman", 2, 26));
		g.drawString(KeyEvent.getKeyText(frame.getKeyCodes().get(4)), 250, 399);
		g.setFont(new Font("TimesNewRoman", 1, 24));
		
		if(keyCodeForChange == 5) 
			g.setFont(new Font("TimesNewRoman", 2, 26));
		g.drawString(KeyEvent.getKeyText(frame.getKeyCodes().get(5)), 195, 517);
		g.setFont(new Font("TimesNewRoman", 1, 24));
		
		if(keyCodeForChange == 6) 
			g.setFont(new Font("TimesNewRoman", 2, 26));
		g.drawString(KeyEvent.getKeyText(frame.getKeyCodes().get(6)), 213, 556);
		g.setFont(new Font("TimesNewRoman", 1, 24));
		
		if(keyCodeForChange == 7) 
			g.setFont(new Font("TimesNewRoman", 2, 26));
		g.drawString(KeyEvent.getKeyText(frame.getKeyCodes().get(7)), 335, 634);
		g.setFont(new Font("TimesNewRoman", 1, 24));
		
		if(keyCodeForChange == 8) 
			g.setFont(new Font("TimesNewRoman", 2, 26));
		g.drawString(KeyEvent.getKeyText(frame.getKeyCodes().get(8)), 458, 712);
		g.setFont(new Font("TimesNewRoman", 1, 24));

	}

	public void mousePressed(int x, int y) {
		
		repaint();
		if (x >= 190 && y >= 185 && x <= 290 && y <= 219)
			keyCodeForChange = 0;
		if (x >= 180 && y >= 220 && x <= 280 && y <= 250)
			keyCodeForChange = 1;
		if (x >= 190 && y >= 255 && x <= 290 && y <= 290)
			keyCodeForChange = 2;
		if (x >= 190 && y >= 295 && x <= 290 && y <= 330)
			keyCodeForChange = 3;
		if (x >= 265 && y >= 375 && x <= 365 && y <= 405)
			keyCodeForChange = 4;
		if (x >= 215 && y >= 490 && x <= 315 && y <= 520)
			keyCodeForChange = 5;
		if (x >= 240 && y >= 530 && x <= 340 && y <= 560)
			keyCodeForChange = 6;
		if (x >= 325 && y >= 610 && x <= 425 && y <= 640)
			keyCodeForChange = 7;
		if (x >= 427 && y >= 690 && x <= 527 && y <= 720)
			keyCodeForChange = 8;
		if (x >= 950 && y >= 745 && x <= 1250 && y <= 856) {
			frame.startMenu();
			frame.playSound("ButtonKlick", -5f);

		}
		repaint();

	}

	public void changeKeyCode(int keyCode) {
		if (keyCodeForChange >= 0) {
			for (int i = 0; i < keyCodes.size(); i++) {
				if (keyCodes.get(i) == keyCode) {
					keyCodeForChange = -1;
					frame.playSound("error", -5f);
					repaint();
					return;
				}
			}
			frame.playSound("ButtonKlick", -5f);
			frame.getKeyCodes().set(keyCodeForChange, keyCode);
			repaint();
		}
		keyCodeForChange = -1;
		repaint();
	}

	public ArrayList<Integer> getKeyCodes() {
		return keyCodes;
	}

	public void setKeyCodes(ArrayList<Integer> keyCodes) {
		this.keyCodes = keyCodes;
	}

}