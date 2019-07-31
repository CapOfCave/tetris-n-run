package data;

import java.awt.Graphics;
import java.util.ArrayList;

import graphics.CreditsPanel;

public class CreditLine {
	private int startFont;
	private int indent;
	private String[] content;
	private int line;

	private int fontAscent;
	private int fontDescent;
	
	private ArrayList<Integer> xOffsetsLinks;
	private ArrayList<Integer> stringWidthsLinks;
	private String[] links;
	

	private CreditsPanel cPanel;

	public CreditLine(CreditsPanel cPanel, int line, int startFont, int indent, String[] content, String[] links) {
		this.cPanel = cPanel;
		this.line = line;
		this.startFont = startFont;
		this.indent = indent;
		this.content = content;
		this.links = links;
	}

	public int getStartFont() {
		return startFont;
	}

	public int getIndent() {
		return indent;
	}

	public String[] getContent() {
		return content;
	}

	public String getLinkAt(int x, int y) {
		
		if (links != null && getLinkIndexAt(x, y) >= 0) {
			return links[getLinkIndexAt(x, y)];
		}
		return null;
	}

	public int getLine() {
		return line;
	}

	public int getLinkIndexAt(int x, int y) {
		if(xOffsetsLinks == null || xOffsetsLinks.size() == 0) {
			return -1;
		}
		if (x < xOffsetsLinks.get(0)) {
			return -1;
		}
		int i = 0;
		while (i + 1 < xOffsetsLinks.size() && xOffsetsLinks.get(i + 1) <= x) {
			i++;
		}
		
		if (i < 0) {
			return -1;
		}
		if (x - xOffsetsLinks.get(i) <= stringWidthsLinks.get(i) && y > cPanel.getTextY() + line * cPanel.getTextYDif() - fontAscent
				&& y < cPanel.getTextY() + line * cPanel.getTextYDif() + fontDescent) {
			return i;
		}
		return -1;
	}

	public void initxOffsets(Graphics g) {
		if (xOffsetsLinks != null) {
			return;
		}
		int fontIndex = getStartFont();
		int xOffset = cPanel.getTextX() + getIndent() * cPanel.getIndentXDif();
		xOffsetsLinks = new ArrayList<>();
		stringWidthsLinks = new ArrayList<>();
		
		for (String str : getContent()) {
			g.setFont(cPanel.getFontAt(fontIndex % 2 + 1));
			if (fontIndex % 2 == 1) {
				xOffsetsLinks.add(xOffset);
				stringWidthsLinks.add(g.getFontMetrics().stringWidth(str));
			}
			xOffset += g.getFontMetrics().stringWidth(str);
			fontIndex++;
		}
		
		fontAscent = g.getFontMetrics().getAscent();
		fontDescent = g.getFontMetrics().getDescent();
		

	}

}
