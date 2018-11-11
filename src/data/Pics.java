package data;

import java.awt.image.BufferedImage;

import loading.ImageLoader;

/**
 * @author Lars Created on 07.10.2018
 */
public class Pics {

	BufferedImage img;
	int width;
	int amount;

	public Pics(String url, int width) {

		this(ImageLoader.loadImage(url), width);

	}

	public Pics(BufferedImage img, int width) {
		this.img = img;
		this.width = width;
		amount = img.getWidth() / width;
		if (amount == 0) {
			amount = 1;
			this.width = img.getWidth();
		}
	}

	public BufferedImage get(int i) {
		if (i < 0 || i >= amount) {
			return null;
		} else {
			return img.getSubimage(i * width, 0, width, img.getHeight());
		}
	}

	@Override
	public String toString() {
		return "Pics(width=" + width + ",amount=" + amount + ", img=" + img + ")";
	}

}
