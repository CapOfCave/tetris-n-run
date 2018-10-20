package tools;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Tools {

	public static BufferedImage setColor(BufferedImage image, Color goal) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				int current = image.getRGB(i, j);
				int red = (current >> 16) & 0x0ff;
				int green = (current >> 8) & 0x0ff;
				int blue = (current) & 0x0ff;
				int alpha = (current >> 24) & 0x0ff;
				double lumien = 0.2126 * red + 0.7152 * green + 0.0722 * blue;
				img.setRGB(i, j, getRGB(goal.getRed(), goal.getGreen(), goal.getBlue(), alpha, lumien));
			}
		}
		
		return img;
	}

	private static int getRGB(int red, int green, int blue, int alpha, double lumien) {
		int r = (int) (red * lumien / 255);
		int g = (int) (green * lumien / 255);
		int b = (int) (blue * lumien / 255);
		return (((alpha & 0x0ff) << 24) |(r & 0x0ff) << 16) | ((g & 0x0ff) << 8) | (b & 0x0ff);
//		int rgb = (int) (red * lumien / 255);
//		rgb = (rgb << 8) + (int) (green * lumien / 255);
//		rgb = (rgb << 8) + (int) (blue * lumien / 255);
//		return rgb;
	}

}
