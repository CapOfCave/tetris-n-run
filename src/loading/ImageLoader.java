package loading;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;

import javax.imageio.ImageIO;

/**
 * @author Lars Created on 05.08.2018
 */
public class ImageLoader {

	private HashMap<String, BufferedImage> images;
	private boolean everythingLoaded = false;

	private boolean eclipseVersion = false;

	public ImageLoader() {
		images = new HashMap<>();
	}

	public void loadAll() {
		everythingLoaded = true;
		Enumeration<URL> en;
		try {
			en = getClass().getClassLoader().getResources("res");
			if (en.hasMoreElements()) {
				try {
					// Thx to
					// https://stackoverflow.com/questions/5193786/how-to-use-classloader-getresources-correctly/5194002#5194002
					JarURLConnection metaInf = (JarURLConnection) en.nextElement().openConnection();
					Enumeration<JarEntry> enumer = metaInf.getJarFile().entries();
					while (enumer.hasMoreElements()) {
						String element = enumer.nextElement().getName();
						if (element.endsWith(".png")) { // additional constraints?
							loadAndSave("/" + element); // TODO welche?
						}

					}

				} catch (ClassCastException e) {
					System.err.println("Dieses Feature funktioniert z.Z. nur beim Exportieren, nicht in Eclipse.");
					eclipseVersion = true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

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

	public boolean isEverythingLoaded() {
		return everythingLoaded;
	}

}
