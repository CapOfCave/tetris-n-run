package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import input.TutorialMouseHandler;

public class TutorialPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private MenuFrame frame;
	private TutorialMouseHandler mouseHandler;
	private BufferedImage tutorial;

	public TutorialPanel(MenuFrame frame) {
		this.frame = frame;
		mouseHandler = new TutorialMouseHandler(frame, this);
		setPreferredSize(new Dimension(GameFrame.PANEL_WIDTH, GameFrame.PANEL_HEIGHT));
		addMouseListener(mouseHandler);

		tutorial = frame.getImage("/res/Tutorial.png");
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(tutorial, 0, 0, null);
		g.setColor(Color.BLACK);
		g.setFont(new Font(GameFrame.fontString, 1, 55));
		g.drawString("Menu", 1035, 823);

	}

	public void mousePressed(int x, int y) {
		if (x >= 950 && y >= 745 && x <= 1250 && y <= 856) {

			frame.playSound("ButtonKlick", -5f);
			frame.startMenu();
		}
	}

}
