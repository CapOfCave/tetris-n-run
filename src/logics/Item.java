package logics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import logics.entities.Entity;

public class Item extends Entity{

	public BufferedImage previewImg;
	
	public Item(BufferedImage previewImg, int blockSize) {
		super(previewImg, blockSize);
		this.previewImg = previewImg;
	}
	
	public void drawPreview(Graphics2D g, int position) {
		g.drawImage(previewImg, 20 + (32 * position + 5 * position), 20, 32, 32, null);
	}

	public void onClickInInventoryEnvent() {
		
		
	}
	
	public void collectingEvent() {
		
	}

	@Override
	public void draw(Graphics g, float interpolation, boolean debugMode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}
}
