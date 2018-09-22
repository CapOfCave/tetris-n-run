package logics.entities.items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Item {

	public BufferedImage previewImg;
	
	public Item(BufferedImage previewImg) {
		this.previewImg = previewImg;
	}
	
	public void drawPreview(Graphics2D g, int position) {
		g.drawImage(previewImg, 20 + (32 * position + 5 * position), 20, 32, 32, null);
	}

	public void onClickInInventoryEnvent() {
		System.out.println(previewImg);
		
	}
}
