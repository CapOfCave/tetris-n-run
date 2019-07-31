package data;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Scanner;

import graphics.GameFrameHandler;
import input.OptionButton;

public class TutorialText {

	private int dy = 1;
	private ArrayList<String[]> finishedContent;
	private ArrayList<String> unsplitContent;

	private Font font = new Font(GameFrameHandler.FONTSTRING, 0, 55);
	private boolean createdLines = false;

	public void loadFromFile(String path) {
		Scanner sc = new Scanner(Toolkit.getDefaultToolkit().getClass().getResourceAsStream(path));
		ArrayList<String> outp = new ArrayList<>();
		while (sc.hasNextLine()) {
			outp.add(sc.nextLine());
		}
		sc.close();
		unsplitContent = outp;
	}

	public void loadContent(Graphics g, int width, ArrayList<Integer> keyCodes) {
		ArrayList<String> unsplitReplacedContent = replaceRegex(unsplitContent, keyCodes);
		
		finishedContent = new ArrayList<>();
		for (String str : unsplitReplacedContent) {
			finishedContent.add(loadContent(str, g, width));
		}
	}

	private ArrayList<String> replaceRegex(ArrayList<String> contentToRegex, ArrayList<Integer> keyCodes) {
		ArrayList<String> outp = new ArrayList<>();
		for (String strs : contentToRegex) {
			outp.add(replaceRegex(strs, keyCodes));
		}
		return outp;
	}


	private String replaceRegex(String contentToRegex, ArrayList<Integer> keyCodes) {
		while (contentToRegex.matches(".*<[0-9]*>.*")) {
			contentToRegex = contentToRegex.replaceFirst("<[0-9a-zA-Z]*>", OptionButton.getKeyText(keyCodes.get(Integer.parseInt(
					contentToRegex.substring(contentToRegex.indexOf("<") + 1, contentToRegex.indexOf(">"))))));
		}
		return contentToRegex;
	}

	public String[] loadContent(String unSplitLine, Graphics g, int width) {
		font = g.getFont();
		FontMetrics fm = g.getFontMetrics(font);
		dy = (fm.getAscent() + fm.getDescent());
		ArrayList<String> outp = new ArrayList<>();

		int string_start = 0;
		int lastSpace = -1;
		for (int i = 0; i < unSplitLine.length(); i++) {

			if (unSplitLine.charAt(i) == ' ' || unSplitLine.charAt(i) == '-') {
				lastSpace = i;
			}

			if (fm.stringWidth(unSplitLine.substring(string_start, i)) > width) {

				if (lastSpace < 0) {
					outp.add(unSplitLine.substring(string_start, i));
					string_start = i + 1;

				} else {
					if (unSplitLine.charAt(lastSpace) == ' ') {
						outp.add(unSplitLine.substring(string_start, lastSpace));
					} else {
						outp.add(unSplitLine.substring(string_start, lastSpace + 1));
					}
					string_start = lastSpace + 1;
				}

			}

		}
		outp.add(unSplitLine.substring(string_start));

		return toArray(outp);

	}

	private static String[] toArray(ArrayList<String> arrayList) {
		String[] outp = new String[arrayList.size()];
		for (int i = 0; i < outp.length; i++) {
			outp[i] = arrayList.get(i) == null ? "" : arrayList.get(i);
		}
		return outp;
	}

	public void drawPanel(int index, Graphics g, int x, int y, int width, ArrayList<Integer> keyCodes) {
		//g.setFont(font);
		if (!createdLines) {
			loadContent(g, width, keyCodes);
		}
		String[] currentText = finishedContent.get(index);
		for (int i = 0; i < currentText.length; i++) {
			g.drawString(currentText[i], x, y + i * dy);
		}

	}

}
