package logics.entities.items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import logics.World;

public class Item {

	protected World world;
	private BufferedImage previewImg;
	
	public Item(World world, BufferedImage previewImg) {
		this.world = world;
		this.previewImg = previewImg;
	}
	
	public void drawPreview(Graphics2D g, int position) {
		g.drawImage(previewImg, 20 + (32 * position + 5 * position), 20, 32, 32, null);
	}

	public void onClickInInventoryEnvent() {
		System.out.println(previewImg);
		
	}

	public void collectingEvent() {
		// TODO Auto-generated method stub
		
	}
}
