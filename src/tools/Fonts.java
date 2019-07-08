package tools;

import java.awt.FontMetrics;
import java.awt.Graphics;

public final class Fonts {
	public static void drawCenteredString(String s, int xOf, int yOf, int w, int h, Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		int x = xOf + (w - fm.stringWidth(s)) / 2;
		int y = yOf + (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
		g.drawString(s, x, y);
	}
}
