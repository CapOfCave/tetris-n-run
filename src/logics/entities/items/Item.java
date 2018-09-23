package logics.entities.items;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import logics.World;
import logics.entities.Entity;

public class Item extends Entity{

	private BufferedImage previewImg;
	
	public Item(World world, BufferedImage previewImg) {
		super(world, previewImg);
		this.previewImg = previewImg;
		
	}
	
	public Item(World world, BufferedImage previewImg, int x, int y) {
		super(world, previewImg);
		this.previewImg = previewImg;
	}
	
	public void drawPreview(Graphics2D g, int position, int size) {
		g.drawImage(previewImg, 20 + (32 * position + 5 * position), 20, size, size, null);
	}

	public void onClickInInventoryEnvent() {
		System.out.println(previewImg);
		
	}

	public void collectingEvent() {
		System.out.println("Collecting " + this);
		
	}
	@Override
	public void draw(Graphics g, float interpolation, boolean debugMode) {
		g.drawImage(img, (int)x, (int)y, world.blockSize(), world.blockSize(), null);
	}

	@Override
	public void tick() {
	}
}
