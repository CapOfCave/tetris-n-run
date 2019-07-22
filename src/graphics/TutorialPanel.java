package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import data.TutorialText;
import input.TutorialMouseHandler;
import tools.Fonts;

public class TutorialPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private MenuFrameHandler menuFrame;
	private TutorialMouseHandler mouseHandler;
	private BufferedImage tutorial;
	private TutorialText tutorialText;
	private int textWidth;
	private double fontMultiplier;

	public TutorialPanel(MenuFrameHandler frame) {
		this.menuFrame = frame;
		mouseHandler = new TutorialMouseHandler(frame, this);
		setPreferredSize(new Dimension(menuFrame.getScreenSize()));
		addMouseListener(mouseHandler);
		setBackground(Color.BLACK);

		tutorialText = new TutorialText();
		tutorialText.loadFromFile("/res/tutorial.txt");
		
		textWidth = 330 * frame.getPanelWidth() / 1920;
		
		fontMultiplier = (menuFrame.getPanelWidth() / 1920.);
	}

	public void initImages() {
		tutorial = menuFrame.getImage("/res/tutorial.png");
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.translate(menuFrame.getPanelOffsetX(), menuFrame.getPanelOffsetY());
		g.drawImage(tutorial, 0, 0, menuFrame.getPanelWidth(), menuFrame.getPanelHeight(), null);
		g.setColor(Color.BLACK);

		g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, (int) (34 * fontMultiplier)));

		int panelWidth = menuFrame.getPanelWidth();
		int panelHeight = menuFrame.getPanelHeight();

		g.drawString("Tutorial1:", (234 + 10) * panelWidth / 1920, (75 + 10) * panelHeight / 1080);
		g.drawString("Tutorial2:", (600 + 10) * panelWidth / 1920, (75 + 10) * panelHeight / 1080);
		g.drawString("Tutorial3:", (966 + 10) * panelWidth / 1920, (75 + 10) * panelHeight / 1080);
		g.drawString("Tutorial4:", (1332 + 10) * panelWidth / 1920, (75 + 10) * panelHeight / 1080);
		g.drawString("Tutorial5:", (234 + 10) * panelWidth / 1920, (566 + 10) * panelHeight / 1080);
		g.drawString("Tutorial6:", (600 + 10) * panelWidth / 1920, (566 + 10) * panelHeight / 1080);
		g.drawString("Tutorial7:", (966 + 10) * panelWidth / 1920, (566 + 10) * panelHeight / 1080);
		
		g.setFont(new Font(GameFrameHandler.FONTSTRING, 0, (int) (23 * fontMultiplier)));

		tutorialText.drawPanel(0, g, (234 + 10) * panelWidth / 1920, (310 + 10) * panelHeight / 1080, textWidth,
				menuFrame.getKeyCodes());
		tutorialText.drawPanel(1, g, (600 + 10) * panelWidth / 1920, (310 + 10) * panelHeight / 1080, textWidth,
				menuFrame.getKeyCodes());
		tutorialText.drawPanel(2, g, (966 + 10) * panelWidth / 1920, (310 + 10) * panelHeight / 1080, textWidth,
				menuFrame.getKeyCodes());
		tutorialText.drawPanel(3, g, (1332 + 10) * panelWidth / 1920, (310 + 10) * panelHeight / 1080, textWidth,
				menuFrame.getKeyCodes());
		tutorialText.drawPanel(4, g, (234 + 10) * panelWidth / 1920, (800 + 10) * panelHeight / 1080, textWidth,
				menuFrame.getKeyCodes());
		tutorialText.drawPanel(5, g, (600 + 10) * panelWidth / 1920, (800 + 10) * panelHeight / 1080, textWidth,
				menuFrame.getKeyCodes());
		tutorialText.drawPanel(6, g, (966 + 10) * panelWidth / 1920, (800 + 10) * panelHeight / 1080, textWidth,
				menuFrame.getKeyCodes());
		tutorialText.drawPanel(7, g, (1332 + 10) * panelWidth / 1920, (557 + 10) * panelHeight / 1080, textWidth,
				menuFrame.getKeyCodes());
		g.setFont(new Font("GameFrame.fontString", 1, (int) (55 * fontMultiplier)));
		Fonts.drawCenteredString("Menu", getMenuBounds(), g);

	}
	
	private Rectangle getMenuBounds() { // TODO bounz
		return new Rectangle(1331 * menuFrame.getPanelWidth() / 1920, 906 * menuFrame.getPanelHeight() / 1080,
				354 * menuFrame.getPanelWidth() / 1920, 120 * menuFrame.getPanelHeight() / 1080);

	}

	public void mousePressed(int x, int y) {
		repaint();
		if (x >= 1331 * menuFrame.getPanelWidth() / 1920 && y >= 906 * menuFrame.getPanelHeight() / 1080
				&& x <= 1685 * menuFrame.getPanelWidth() / 1920 && y <= 1026 * menuFrame.getPanelHeight() / 1080) {

			menuFrame.playSound("ButtonKlick", -5f);
			menuFrame.startMenu();
		}
	}

}
