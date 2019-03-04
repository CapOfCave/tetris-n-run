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
import input.TutorialMouseHandler;
import loading.ImageLoader;

public class OptionPanel extends JPanel{
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
		keyCodes.add(6, 17);
		keyCodes.add(7, 69);
		
		this.frame = frame;
		mouseHandler = new OptionMouseHandler(frame, this);
		setPreferredSize(new Dimension(width, height));
		addMouseListener(mouseHandler);

		option = ImageLoader.loadImage("/res/Optionen.png");

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
		g.drawString(KeyEvent.getKeyText(frame.getKeyCodes().get(0)), 192, 204);
		g.drawString(KeyEvent.getKeyText(frame.getKeyCodes().get(1)), 175, 243);
		g.drawString(KeyEvent.getKeyText(frame.getKeyCodes().get(2)), 192, 282);
		g.drawString(KeyEvent.getKeyText(frame.getKeyCodes().get(3)), 195, 321);
		g.drawString(KeyEvent.getKeyText(frame.getKeyCodes().get(4)), 268, 399);
		g.drawString(KeyEvent.getKeyText(frame.getKeyCodes().get(5)), 219, 517);
		g.drawString(KeyEvent.getKeyText(frame.getKeyCodes().get(6)), 242, 556);
		g.drawString(KeyEvent.getKeyText(frame.getKeyCodes().get(7)), 332, 634);

	}


	public void mousePressed(int x, int y) {
		System.out.println(x + " " + y);
		frame.playSound("ButtonKlick", -5f);
		if(x >= 190 && y >= 185 && x <= 290 && y <= 229)
			keyCodeForChange = 0;
		if(x >= 190 && y >= 185 && x <= 290 && y <= 229)
			keyCodeForChange = 0;
		if(x >= 190 && y >= 185 && x <= 290 && y <= 229)
			keyCodeForChange = 0;
		if(x >= 190 && y >= 185 && x <= 290 && y <= 229)
			keyCodeForChange = 0;
		if(x >= 190 && y >= 185 && x <= 290 && y <= 229)
			keyCodeForChange = 0;
		if(x >= 190 && y >= 185 && x <= 290 && y <= 229)
			keyCodeForChange = 0;
		//frame.startMenu();
		repaint();
		System.out.println(keyCodeForChange);
		
	}
	

	public void changeKeyCode(int keyCode) {
		if(keyCodeForChange >= 0) {
			for(int i = 0; i <=7; i++) {
				if(frame.getKeyCodes().get(i) == keyCode) {
					keyCodeForChange = -1;
					return;
				}
			}
			frame.getKeyCodes().set(keyCodeForChange, keyCode);
			repaint();
		}
		keyCodeForChange = -1;
		
	}
	
	public ArrayList<Integer> getKeyCodes(){
		return keyCodes;
	}
	

}