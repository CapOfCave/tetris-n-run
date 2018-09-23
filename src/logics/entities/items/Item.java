package logics.entities.items;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import loading.ImageLoader;

import logics.World;
import logics.entities.Entity;

public class Item extends Entity {

	private static final long serialVersionUID = 1L;

	private transient BufferedImage previewImg;

	private String imgPath;

	public Item(World world, String imgPath) {
		super(world, ImageLoader.loadImage(imgPath));
		this.imgPath = imgPath;
		this.previewImg = ImageLoader.loadImage(imgPath);
	}


	public void init() {
		System.out.println("ItemInit");
		this.img = ImageLoader.loadImage(imgPath);
		setPreviewImg(img);


	}

	public Item(World world, BufferedImage previewImg, int x, int y) {
		super(world, previewImg);
		this.previewImg = previewImg;
	}

	public void drawPreview(Graphics2D g, int position, int size) {
		g.drawImage(previewImg, 20 + (32 * position + 5 * position), 20, size, size, null);
	}

	public void onClickInInventoryEnvent() {
		System.out.println("Cliccc " + this);

	}

	public void collectingEvent() {
		System.out.println("Collecting " + this);

	}

	@Override
	public void draw(Graphics g, float interpolation, boolean debugMode) {
		g.drawImage(img, (int) (x * world.blockSize() - world.cameraX()), (int) (y * world.blockSize() - world.cameraY()), world.blockSize(),
				world.blockSize(), null);
		if (debugMode) {
			g.setColor(Color.GREEN);
			g.fillRect((int) (x * world.blockSize() - world.cameraX()), (int) (y * world.blockSize() - world.cameraY()), world.blockSize(),
					world.blockSize());

		}
	}

	@Override
	public void tick() {
	}

	public void setPreviewImg(BufferedImage img) {
		previewImg = img;
	}
}
