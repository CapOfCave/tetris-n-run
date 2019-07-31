package graphics;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JPanel;

import data.CreditLine;
import input.CreditsMouseHandler;
import tools.Fonts;

public class CreditsPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private MenuFrameHandler menuFrame;
	private CreditsMouseHandler mouseHandler;
	private BufferedImage background;
	private int highlight = -1;

	private int textX;
	private int textY;
	private int textYDif;
	private int indentXDif;

	private Map<TextAttribute, Object> map = new Hashtable<TextAttribute, Object>();
	private Font hyperLinkFont;
	private Font normalFont;
	private Font boldFont;
	private Font[] fonts;
	private double fontMultiplier;

	ArrayList<CreditLine> creditLines;

	private int fontSize;

	public CreditsPanel(MenuFrameHandler menuFrame) {

		this.menuFrame = menuFrame;
		textX = 320 * menuFrame.getPanelWidth() / 1920;
		textY = 270 * menuFrame.getPanelHeight() / 1080;
		textYDif = 60 * menuFrame.getPanelHeight() / 1080;
		indentXDif = 75 * menuFrame.getPanelHeight() / 1080;
		fontMultiplier = (menuFrame.getPanelWidth() / 1920.);
		fontSize = (int) (40 * fontMultiplier);
		map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		map.put(TextAttribute.FOREGROUND, Color.BLUE);
		normalFont = new Font(GameFrameHandler.FONTSTRING, 0, 40);
		boldFont = new Font(GameFrameHandler.FONTSTRING, Font.BOLD, 40);
		hyperLinkFont = normalFont.deriveFont(map);
		fonts = new Font[3];
		fonts[0] = boldFont;
		fonts[1] = normalFont;
		fonts[2] = hyperLinkFont;

		mouseHandler = new CreditsMouseHandler(menuFrame, this);
		setPreferredSize(new Dimension(menuFrame.getScreenSize()));
		addMouseListener(mouseHandler);
		addMouseMotionListener(mouseHandler);
		setBackground(Color.WHITE);

		

		loadFromFile("/res/allCredits.txt");
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
		g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, (int) (80 * fontMultiplier)));
		String headline = "Credits";
		g.drawString(headline, menuFrame.getPanelWidth() / 2 - (g.getFontMetrics().stringWidth(headline) / 2),
				170 * menuFrame.getPanelHeight() / 1080);

		g.setColor(Color.BLACK);

		int size = (int) (55 * fontMultiplier);
		int sizeDif = (int) (10 * fontMultiplier);

		// Color stays gray if needed

		if (highlight == 0) {
			g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, sizeDif + size));
		} else {
			g.setFont(new Font(GameFrameHandler.FONTSTRING, 1, size));
		}
		Fonts.drawCenteredString("Menu", getMenuBounds(), g);
		drawText(g);
		
	}

	private void drawText(Graphics g) {
		g.setFont(normalFont);
		for (CreditLine creditLine : creditLines) {
			drawLine(g, creditLine);
		}
	}

	private void drawLine(Graphics g, CreditLine creditLine) {
		creditLine.initxOffsets(g);
		int line = creditLine.getLine();
		int fontIndex = creditLine.getStartFont();
		int xOffset = textX + creditLine.getIndent() * indentXDif;
		for (String str : creditLine.getContent()) {
			g.setFont(fonts[fontIndex % 2 + 1]);
			g.drawString(str, xOffset, textY + line * textYDif);
			xOffset += g.getFontMetrics().stringWidth(str);
			fontIndex++;
		}

	}

	public Rectangle getMenuBounds() {
		return new Rectangle(1331 * menuFrame.getPanelWidth() / 1920, 906 * menuFrame.getPanelHeight() / 1080,
				354 * menuFrame.getPanelWidth() / 1920, 120 * menuFrame.getPanelHeight() / 1080);

	}

	public void mousePressed(int x, int y) {

		if (getMenuBounds().contains(x, y)) {
			menuFrame.playSound("ButtonKlick", -5f);
		}
		for(CreditLine cL: creditLines) {
			if (cL.getLinkIndexAt(x, y) >= 0) {
				menuFrame.playSound("ButtonKlick", -5f);
			}
		}

	}

	public void mouseReleased(int x, int y) {
		if (getMenuBounds().contains(x, y)) {
			menuFrame.startMenu();
		}
		for(CreditLine cL: creditLines) {
			openLink(cL.getLinkAt(x, y));
		}

	}

	private void openLink(String link) {
		if(link == null) {
			return;
		}
		try {
			Desktop.getDesktop().browse(URI.create(link));
		} catch (IOException e) {
			System.err.println("wrong url: " + link);
			e.printStackTrace();
		}
		
	}

	public void highlight(int i) {
		highlight = i;
	}

	public boolean isHighlighted(int i) {
		return i == highlight;
	}

	public void loadFromFile(String path) {
		Scanner sc = new Scanner(Toolkit.getDefaultToolkit().getClass().getResourceAsStream(path));
		creditLines = new ArrayList<>();
		while (sc.hasNextLine()) {
			String[] split = sc.nextLine().split("\\|");
			String[] text = split[2].split("\\*");
			String[] links = null;
			if (split.length >= 4) {
				links = split[3].split("\\*");
			}
			creditLines.add(new CreditLine(this, creditLines.size(), Integer.parseInt(split[0]), Integer.parseInt(split[1]), text, links));
		}

		sc.close();
	}

	public void mouseMoved(int x, int y) {
		if (getMenuBounds().contains(x, y)) {
			if (!isHighlighted(0)) {
				menuFrame.playSound("menuHover", -5f);
				highlight(0);
			}
		} else {
			for(int i = 0; i < creditLines.size(); i++) {
				CreditLine cL= creditLines.get(i);
				int linkIndex = cL.getLinkIndexAt(x, y);
				
				if (linkIndex >= 0) {
					
				
					if (!isHighlighted(i * 10 + linkIndex + 1)) {
						menuFrame.playSound("menuHover", -5f);
						highlight(i * 10 + linkIndex + 1);
						repaint();
					}
					return;
				}
			}
			highlight(-1);
			
		} 

		repaint();
	}

	public int getTextX() {
		return textX;
	}

	public int getIndentXDif() {
		return indentXDif;
	}

	public Font getFontAt(int i) {
		return fonts[i];
	}

	public int getTextY() {
		return textY;
	}

	public int getTextYDif() {
		return textYDif;
	}

	public int getFontSize() {
		return fontSize ;
	}

}
