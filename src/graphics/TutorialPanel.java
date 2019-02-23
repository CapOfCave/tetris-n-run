package graphics;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


import input.TutorialMouseHandler;
import loading.ImageLoader;



public class TutorialPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private MenuFrame frame;
	private TutorialMouseHandler mouseHandler;
	private final int width = 1300, height = 900;
	private BufferedImage tutorial;

	public TutorialPanel(MenuFrame frame) {
		this.frame = frame;
		mouseHandler = new TutorialMouseHandler(frame, this);
		setPreferredSize(new Dimension(width, height));
		addMouseListener(mouseHandler);

		tutorial = ImageLoader.loadImage("/res/Tutorial.png");
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		g.drawImage(tutorial, 0, 0, null);
		g.setColor(Color.BLACK);
		g.setFont(new Font("TimesNewRoman", 1, 55));
		g.drawString("Menu", 1035, 823);
		

	}


	public void mousePressed(int x, int y) {
		frame.playSound("ButtonKlick", -5f);
		frame.startMenu();
		
	}
	
}
