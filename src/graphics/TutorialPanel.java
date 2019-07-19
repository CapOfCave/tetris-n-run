package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import data.TutorialText;
import input.TutorialMouseHandler;

public class TutorialPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private MenuFrameHandler menuFrame;
	private TutorialMouseHandler mouseHandler;
	private BufferedImage tutorial;
	private TutorialText tutorialText;
	private final int textWidth = 265;

	public TutorialPanel(MenuFrameHandler frame) {
		this.menuFrame = frame;
		mouseHandler = new TutorialMouseHandler(frame, this);
		setPreferredSize(new Dimension(menuFrame.getScreenSize()));
		addMouseListener(mouseHandler);
		setBackground(Color.BLACK);
		
		tutorialText = new TutorialText();
		tutorialText.loadFromFile("/res/tutorial.txt");
	}
	
	public void initImages() {
		tutorial = menuFrame.getImage("/res/tutorial.png");
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.translate(menuFrame.getPanelOffsetX(), menuFrame.getPanelOffsetY());
		g.drawImage(tutorial, 0, 0, null);
		g.setColor(Color.BLACK);
		g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, 55));
		g.drawString("Menu", 1035, 823);
		
		g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, 28));
		g.drawString("Tutorial1:", 60, 80);
		g.drawString("Tutorial2:", 365, 80);
		g.drawString("Tutorial3:", 670, 80);
		g.drawString("Tutorial4:", 980, 80);
		g.drawString("Tutorial5:", 60, 490);
		g.drawString("Tutorial6:", 365, 490);
		g.drawString("Tutorial7:", 670, 490);
		
		
		tutorialText.drawPanel(0, g, 55, 270, textWidth, menuFrame.getKeyCodes());
		tutorialText.drawPanel(1, g, 360, 270, textWidth, menuFrame.getKeyCodes());
		tutorialText.drawPanel(2, g, 665, 270, textWidth, menuFrame.getKeyCodes());
		tutorialText.drawPanel(3, g, 970, 270, textWidth, menuFrame.getKeyCodes());
		tutorialText.drawPanel(4, g, 55, 680, textWidth, menuFrame.getKeyCodes());
		tutorialText.drawPanel(5, g, 360, 680, textWidth, menuFrame.getKeyCodes());
		tutorialText.drawPanel(6, g, 665, 680, textWidth, menuFrame.getKeyCodes());
		tutorialText.drawPanel(7, g, 970, 480, textWidth, menuFrame.getKeyCodes());
		
		
		
	}

	public void mousePressed(int x, int y) {
		repaint();
		if (x >= 950 && y >= 745 && x <= 1250 && y <= 856) {

			menuFrame.playSound("ButtonKlick", -5f);
			menuFrame.startMenu();
		}
	}

}
