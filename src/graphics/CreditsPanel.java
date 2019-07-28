package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import input.CreditsMouseHandler;
import tools.Fonts;

public class CreditsPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private MenuFrameHandler menuFrame;
	private CreditsMouseHandler mouseHandler;
	private BufferedImage background;
	private int highlight = -1;
	
	private double fontMultiplier;

	public CreditsPanel(MenuFrameHandler menuFrame) {
		
		this.menuFrame = menuFrame;
		mouseHandler = new CreditsMouseHandler(menuFrame, this);
		setPreferredSize(new Dimension(menuFrame.getScreenSize()));
		addMouseListener(mouseHandler);
		setBackground(Color.WHITE);
		
		fontMultiplier = (menuFrame.getPanelWidth() / 1920.);

		repaint();
	}

	public void initImages() {
		background = menuFrame.getImage("/res/options.png");
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.translate(menuFrame.getPanelOffsetX(), menuFrame.getPanelOffsetY());
		g.drawImage(background, 0, 0, menuFrame.getPanelWidth(), menuFrame.getPanelHeight(), null);

		g.setColor(Color.BLACK);
		g.setFont(new Font("GameFrame.fontString", 1, (int) (80 * fontMultiplier)));
		String headline = "Credits";
		g.drawString(headline, menuFrame.getPanelWidth() / 2 - (g.getFontMetrics().stringWidth(headline) / 2),
				130 * menuFrame.getPanelHeight() / 1080);

		g.setColor(Color.BLACK);
		
		int size = (int) (55 * fontMultiplier);
		int sizeDif = (int) (6 * fontMultiplier);

		// Color stays gray if needed

		System.out.println(highlight);
		if (highlight == 0) {
			g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, sizeDif + size));
		} else {
			g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, size));
		}
		Fonts.drawCenteredString("Menu", getMenuBounds(), g);
		
//		g.setFont(new Font("GameFrame.fontString", 1, (int) (55 * fontMultiplier)));
//		Fonts.drawCenteredString("Menu", getMenuBounds(), g);
//		g.setFont(new Font("GameFrame.fontString", 1, (int) (24 * fontMultiplier)));

	}
	
	

	public Rectangle getMenuBounds() { // TODO bounz
		return new Rectangle(1331 * menuFrame.getPanelWidth() / 1920, 906 * menuFrame.getPanelHeight() / 1080,
				354 * menuFrame.getPanelWidth() / 1920, 120 * menuFrame.getPanelHeight() / 1080);

	}
	

	public void mousePressed(int x, int y) {
		repaint();

		if (x >= 1331 * menuFrame.getPanelWidth() / 1920 && y >= 906 * menuFrame.getPanelHeight() / 1080
				&& x <= 1685 * menuFrame.getPanelWidth() / 1920 && y <= 1026 * menuFrame.getPanelHeight() / 1080) {

			menuFrame.startMenu();
			menuFrame.playSound("ButtonKlick", -5f);
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
