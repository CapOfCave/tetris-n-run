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
		
		g.setFont(new Font(GameFrame.fontString, 1, 28));
		g.drawString("Tutorial1:", 60, 80);
		g.drawString("Tutorial2:", 365, 80);
		g.drawString("Tutorial3:", 670, 80);
		g.drawString("Tutorial4:", 980, 80);
		g.drawString("Tutorial5:", 60, 490);
		g.drawString("Tutorial6:", 365, 490);
		g.drawString("Tutorial7:", 670, 490);
		
		g.setFont(new Font(GameFrame.fontString, 0, 16));
		g.drawString("Text1.0", 55, 270);
		g.drawString("Text1.1.", 55, 290);
		g.drawString("Text1.2.", 55, 310);
		g.drawString("Text1.3.", 55, 330);
		g.drawString("Text1.4.", 55, 350);
		g.drawString("Text1.5.", 55, 370);
		g.drawString("Text1.6.", 55, 390);
		g.drawString("Text1.7.", 55, 410);
		g.drawString("Text1.8.", 55, 430);
		
		g.drawString("Text2.0", 360, 270);
		g.drawString("Text2.1.", 360, 290);
		g.drawString("Text2.2.", 360, 310);
		g.drawString("Text2.3.", 360, 330);
		g.drawString("Text2.4.", 360, 350);
		g.drawString("Text2.5.", 360, 370);
		g.drawString("Text2.6.", 360, 390);
		g.drawString("Text2.7.", 360, 410);
		g.drawString("Text2.8.", 360, 430);
		
		g.drawString("Text3.0", 665, 270);
		g.drawString("Text3.1.", 665, 290);
		g.drawString("Text3.2.", 665, 310);
		g.drawString("Text3.3.", 665, 330);
		g.drawString("Text3.4.", 665, 350);
		g.drawString("Text3.5.", 665, 370);
		g.drawString("Text3.6.", 665, 390);
		g.drawString("Text3.7.", 665, 410);
		g.drawString("Text3.8.", 665, 430);
		
		g.drawString("Text4.0", 970, 270);
		g.drawString("Text4.1.", 970, 290);
		g.drawString("Text4.2.", 970, 310);
		g.drawString("Text4.3.", 970, 330);
		g.drawString("Text4.4.", 970, 350);
		g.drawString("Text4.5.", 970, 370);
		g.drawString("Text4.6.", 970, 390);
		g.drawString("Text4.7.", 970, 410);
		g.drawString("Text4.8.", 970, 430);
		
		g.drawString("Text5.0", 55, 680);
		g.drawString("Text5.1", 55, 700);
		g.drawString("Text5.2", 55, 720);
		g.drawString("Text5.3", 55, 740);
		g.drawString("Text5.4", 55, 760);
		g.drawString("Text5.5", 55, 780);
		g.drawString("Text5.6", 55, 800);
		g.drawString("Text5.7", 55, 820);
		g.drawString("Text5.7", 55, 840);
		
		g.drawString("Text6.0", 360, 680);
		g.drawString("Text6.1", 360, 700);
		g.drawString("Text6.2", 360, 720);
		g.drawString("Text6.3", 360, 740);
		g.drawString("Text6.4", 360, 760);
		g.drawString("Text6.5", 360, 780);
		g.drawString("Text6.6", 360, 800);
		g.drawString("Text6.7", 360, 820);
		g.drawString("Text6.8", 360, 840);
		
		g.drawString("Text7.0", 665, 680);
		g.drawString("Text7.1", 665, 700);
		g.drawString("Text7.2", 665, 720);
		g.drawString("Text7.3", 665, 740);
		g.drawString("Text7.4", 665, 760);
		g.drawString("Text7.5", 665, 780);
		g.drawString("Text7.6", 665, 800);
		g.drawString("Text7.7", 665, 820);
		g.drawString("Text7.8", 665, 840);
		
		g.drawString("Text8.0", 970, 480);
		g.drawString("Text8.1", 970, 500);
		g.drawString("Text8.2", 970, 520);
		g.drawString("Text8.3", 970, 540);
		g.drawString("Text8.4", 970, 560);
		g.drawString("Text8.5", 970, 580);
		g.drawString("Text8.6", 970, 600);
		g.drawString("Text8.7", 970, 620);
		g.drawString("Text8.8", 970, 640);
		
	}

	public void mousePressed(int x, int y) {
		repaint();
		if (x >= 950 && y >= 745 && x <= 1250 && y <= 856) {

			frame.playSound("ButtonKlick", -5f);
			frame.startMenu();
		}
	}

}
