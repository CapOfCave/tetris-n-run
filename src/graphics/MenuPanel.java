package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import input.MenuMouseHandler;
import loading.ImageLoader;

public class MenuPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private MenuFrame frame;
	private MenuMouseHandler mouseHandler;
	private final int width = 1300, height = 900;
	private BufferedImage menu;

	public MenuPanel(MenuFrame frame) {
		this.frame = frame;
		mouseHandler = new MenuMouseHandler(frame, this);
		setPreferredSize(new Dimension(width, height));
		addMouseListener(mouseHandler);

		menu = ImageLoader.loadImage("/res/Menu.png");

		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		g.drawImage(menu, 0, 0, null);
		g.setColor(Color.BLACK);
		g.setFont(new Font("TimesNewRoman", 1, 130));
		g.drawString("Play", 490, 472);
		g.setFont(new Font("TimesNewRoman", 1, 100));
		g.drawString("Tutorial", 135, 705);
		g.drawString("Optionen", 745, 705);

	}

	public void mousePressed(int x, int y) {
		
		repaint();
		
		if (x > 35 && y > 332 && x < 1252 && y < 523) {
			new GameFrame();
			frame.dispose();
		}
		
		if (x > 35 && y > 571 && x < 644 && y < 764) {
			frame.startTutorial();
		}
	}
}
