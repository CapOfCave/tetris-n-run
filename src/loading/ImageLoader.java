package loading;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author Lars
 * Created on 05.08.2018
 */
public class ImageLoader {

	public static BufferedImage loadImage(String path) {
		try {
			return ImageIO.read(Toolkit.getDefaultToolkit().getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
			return new BufferedImage(10, 10, BufferedImage.TYPE_4BYTE_ABGR);
		}
	}

	
}
