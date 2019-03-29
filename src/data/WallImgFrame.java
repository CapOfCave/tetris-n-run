package data;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import graphics.GameFrame;
import graphics.Renderer;
import loading.ImageLoader;
import logics.worlds.World;

public class WallImgFrame implements DrawAndSortable {

	private int posX, posY;
	private int imageId;
	private static BufferedImage[] images;
	private World world;

	public WallImgFrame(World world, int imageId, int posX, int posY) {
		this.world = world;
		this.imageId = imageId;
		this.posX = posX;
		this.posY = posY;
		if (images == null) {
			images = new BufferedImage[16];
			images[15] =ImageLoader.loadImage("/res/blocks/walls/tttt.png");
			images[14] =ImageLoader.loadImage("/res/blocks/walls/tttf.png");
			images[13] = ImageLoader.loadImage("/res/blocks/walls/ttft.png");
			images[12] = ImageLoader.loadImage("/res/blocks/walls/ttff.png");
			images[11] = ImageLoader.loadImage("/res/blocks/walls/tftt.png");
			images[10] = ImageLoader.loadImage("/res/blocks/walls/tftf.png");
			images[9] =ImageLoader.loadImage("/res/blocks/walls/tfft.png");
			images[8] = ImageLoader.loadImage("/res/blocks/walls/tfff.png");
			images[7] =ImageLoader.loadImage("/res/blocks/walls/fttt.png");
			images[6] = ImageLoader.loadImage("/res/blocks/walls/fttf.png");
			images[5] = ImageLoader.loadImage("/res/blocks/walls/ftft.png");
			images[4] = ImageLoader.loadImage("/res/blocks/walls/ftff.png");
			images[3] = ImageLoader.loadImage("/res/blocks/walls/fftt.png");
			images[2] = ImageLoader.loadImage("/res/blocks/walls/fftf.png");
			images[1] = ImageLoader.loadImage("/res/blocks/walls/ffft.png");
			images[0] =ImageLoader.loadImage("/res/blocks/walls/ffff.png");
		}
	}

	@Override
	public double getHeight() {

		return posY * GameFrame.BLOCKSIZE - 0.5;
	}

	@Override
	public int compareTo(DrawAndSortable o) {
		if (this.getHeight() == o.getHeight()) {
			return 0;
		} else if (this.getHeight() < o.getHeight()) {
			return -1;
		} else {
			return 1;
		}
	}

	@Override
	public void draw(Graphics g, float interpolation) {
		g.drawImage(images[imageId], (int) ((posX - .5) * GameFrame.BLOCKSIZE - world.cameraX()),
				(int) ((posY - 1.5) * GameFrame.BLOCKSIZE - world.cameraY()), null);

	}

	@Override
	public void addTo(Renderer renderer) {
		renderer.addDrawable(this);
	}

}
