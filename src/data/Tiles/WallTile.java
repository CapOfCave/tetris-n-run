package data.Tiles;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import graphics.GameFrame;
import loading.ImageLoader;

/**
 * @author Lars Created on 14.09.2018
 */
public class WallTile extends Tile {

	private static Point offset = new Point(0, -45); //0, -45
	private BufferedImage image3d;
	public boolean top;
	private boolean right;
	private boolean bottom;
	private boolean left;
	
	static HashMap<String, BufferedImage> wallimages = null;

	public WallTile(char key, int posX, int posY, GameFrame frame) {
		super(key, posX, posY, false, false, false, frame);
		isBlockingTetro = true;
		img = ImageLoader.loadImage("/res/blocks/block0.png");

//		image3d = ImageLoader.loadImage("/res/blocks/block1.png");
		

	}
	
	public void setNeighbors(boolean top, boolean right, boolean bottom, boolean left) {
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.left = left;
		
		if (wallimages == null) {
		
			wallimages = new HashMap<>();
			System.out.println("Init Wall Map");
			wallimages.put("tttt", ImageLoader.loadImage("/res/blocks/walls/tttt.png"));
			wallimages.put("tttf", ImageLoader.loadImage("/res/blocks/walls/tttf.png"));
			wallimages.put("ttft", ImageLoader.loadImage("/res/blocks/walls/ttft.png"));
			wallimages.put("ttff", ImageLoader.loadImage("/res/blocks/walls/ttff.png"));
			wallimages.put("tftt", ImageLoader.loadImage("/res/blocks/walls/tftt.png"));
			wallimages.put("tftf", ImageLoader.loadImage("/res/blocks/walls/tftf.png"));
			wallimages.put("tfft", ImageLoader.loadImage("/res/blocks/walls/tfft.png"));
			wallimages.put("tfff", ImageLoader.loadImage("/res/blocks/walls/tfff.png"));
			wallimages.put("fttt", ImageLoader.loadImage("/res/blocks/walls/fttt.png"));
			wallimages.put("fttf", ImageLoader.loadImage("/res/blocks/walls/fttf.png"));
			wallimages.put("ftft", ImageLoader.loadImage("/res/blocks/walls/ftft.png"));
			wallimages.put("ftff", ImageLoader.loadImage("/res/blocks/walls/ftff.png"));
			wallimages.put("fftt", ImageLoader.loadImage("/res/blocks/walls/fftt.png"));
			wallimages.put("fftf", ImageLoader.loadImage("/res/blocks/walls/fftf.png"));
			wallimages.put("ffft", ImageLoader.loadImage("/res/blocks/walls/ffft.png"));
			wallimages.put("ffff", ImageLoader.loadImage("/res/blocks/walls/ffff.png"));
		}
		image3d = wallimages.get((top?"t":"f") + (right?"t":"f")+ (bottom?"t":"f")+ (left?"t":"f"));
		System.out.println((top?"t":"f") + (right?"t":"f")+ (bottom?"t":"f")+ (left?"t":"f"));
	}

	@Override
	public void draw(Graphics g, float interpolation) {
//		g.drawImage(image3d, (int) (posX * GameFrame.BLOCKSIZE - world.cameraX() + offset.x),
//				(int) (posY * GameFrame.BLOCKSIZE - world.cameraY() + offset.y), null);
		
	}

	@Override
	public void drawBackground(Graphics g, float interpolation) {
		g.drawImage(img, (int) (posX * GameFrame.BLOCKSIZE - world.cameraX()),
				(int) (posY * GameFrame.BLOCKSIZE - world.cameraY()), null);
	}
}
