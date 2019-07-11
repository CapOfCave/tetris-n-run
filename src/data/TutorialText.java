package data;

import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import graphics.GameFrame;

public class TutorialText {

	private final int dy = 20;
	private ArrayList<String[]> content;
	private Font font = new Font(GameFrame.fontString, 0, 16);
	
	public void loadContent(ArrayList<String> unSplitContent, Graphics g, int width) {
		
	}

	public void drawPanel(int index, Graphics g, int x, int y) {
		g.setFont(font);
		String[] currentText = content.get(index);
		for (int i = 0; i < currentText.length; i++) {
			g.drawString(currentText[i], x, y + i * dy);
		}
		
	}

}
