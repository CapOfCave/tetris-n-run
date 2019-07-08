package data;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import graphics.GameFrame;
import graphics.Renderer;
import logics.World;

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
			images[15] = world.getImage("/res/blocks/walls/tttt.png");
			images[14] = world.getImage("/res/blocks/walls/tttf.png");
			images[13] = world.getImage("/res/blocks/walls/ttft.png");
			images[12] = world.getImage("/res/blocks/walls/ttff.png");
			images[11] = world.getImage("/res/blocks/walls/tftt.png");
			images[10] = world.getImage("/res/blocks/walls/tftf.png");
			images[9] = world.getImage("/res/blocks/walls/tfft.png");
			images[8] = world.getImage("/res/blocks/walls/tfff.png");
			images[7] = world.getImage("/res/blocks/walls/fttt.png");
			images[6] = world.getImage("/res/blocks/walls/fttf.png");
			images[5] = world.getImage("/res/blocks/walls/ftft.png");
			images[4] = world.getImage("/res/blocks/walls/ftff.png");
			images[3] = world.getImage("/res/blocks/walls/fftt.png");
			images[2] = world.getImage("/res/blocks/walls/fftf.png");
			images[1] = world.getImage("/res/blocks/walls/ffft.png");
			images[0] = world.getImage("/res/blocks/walls/ffff.png");
		}
	}

	@Override
	public double getHeight() {
		if ((imageId % 2) == 1 || (((int) (imageId / 2)) % 2) == 1) { // Luft unten
			return (posY - 0.5) * GameFrame.BLOCKSIZE;
		} else {
			return (posY + 1) * GameFrame.BLOCKSIZE; // Beide Wand, also wahrscheinlich oben
			
			//negativ: tief, verdeckt
		}
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
				(int) ((posY - 1.5) * GameFrame.BLOCKSIZE - world.cameraY()), null); // -1.5 weil um 1 nach oben
																						// angehoben (Mauerfront)
//		g.drawImage(images[imageId], 45, 45, null);
//		g.setColor(Color.BLACK);
//		g.setFont(new Font("", 0, 7));
//		g.drawString(posX + "|" + posY, (int) ((posX - .5) * GameFrame.BLOCKSIZE - world.cameraX()),
//				(int) ((posY - 1.5) * GameFrame.BLOCKSIZE - world.cameraY()));
//		g.setColor(Color.RED);
//
//		g.drawOval((int) ((posX - .5) * GameFrame.BLOCKSIZE - world.cameraX()),
//				(int) ((posY - 1.5) * GameFrame.BLOCKSIZE - world.cameraY()), 3, 3);
//		if (posX == 2 && posY == 2) {
//			g.drawRect((int) ((posX - .5) * GameFrame.BLOCKSIZE - world.cameraX()),
//				(int) ((posY - 1.5) * GameFrame.BLOCKSIZE - world.cameraY()), 45, 45);
//		}
	}

	@Override
	public void addTo(Renderer renderer) {
		renderer.addDrawable(this);
	}

	@Override
	public double getX() {
		return posX * GameFrame.BLOCKSIZE;
	}

	@Override
	public double getY() {
		return posY * GameFrame.BLOCKSIZE;
	}
	
	@Override
	public int getPriorityInDrawQueue() {
		return 0;
	}

}
