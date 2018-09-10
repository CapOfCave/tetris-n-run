package loading;

import java.awt.Graphics;
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

	public static BufferedImage loadImage(String path, int blockSize) {
		BufferedImage img = new BufferedImage(blockSize + 1, blockSize + 1, BufferedImage.TYPE_4BYTE_ABGR);
		//TODO blocksize increased by one for the 2nd border
		Graphics g = img.getGraphics();
		g.drawImage(loadImage(path), 0, 0, blockSize + 1, blockSize + 1, null);
		g.dispose();
		return img;
	}

	
	
}
