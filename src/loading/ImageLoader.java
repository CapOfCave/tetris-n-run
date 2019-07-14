package loading;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * @author Lars Created on 05.08.2018
 */
public class ImageLoader {

	private HashMap<String, BufferedImage> images;
	private boolean eclipseVersion = false;

	

	public ImageLoader() {
		images = new HashMap<>();
	}

	

	public void loadAndSave(String path) {
		images.put(path, loadImage(path));
	}

	public BufferedImage getImage(String path) {
		if (images.get(path) == null) {
			loadAndSave(path);
			if (!eclipseVersion) {
				System.err.println("Had to load Image at " + path);
			}
		}
		return images.get(path);
	}

	private static BufferedImage loadImage(String path) {
		try {
			return ImageIO.read(Toolkit.getDefaultToolkit().getClass().getResource(path));

		} catch (IOException e) {
			e.printStackTrace();

			return new BufferedImage(10, 10, BufferedImage.TYPE_4BYTE_ABGR);
		}
	}

	public void setEclipseVersion(boolean b) {
		this.eclipseVersion  = b;
	}
}
