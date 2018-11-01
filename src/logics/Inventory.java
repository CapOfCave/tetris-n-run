package logics;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

import loading.ImageLoader;
import logics.entities.items.Item;

public class Inventory implements Serializable{

	private static final long serialVersionUID = 1L;
	private ArrayList<Item> items;
	private int firstVisibleItem = 0;
	public int visibleItems = 4;
	
	
	public Inventory() {
		items = new ArrayList<>();
		
	}

	public void draw(Graphics2D g) {
		
		
		for(int i = firstVisibleItem; i < firstVisibleItem + visibleItems; i++) {
			
			items.get(i).drawPreview(g,  i - firstVisibleItem, 110);
		}
		
			
	}
	
	public void scrollLeft() {
		if(firstVisibleItem > 0)
			firstVisibleItem--;
	}
	
	public void scrollRight() {
		if((firstVisibleItem  + visibleItems) <= items.size() - 1) {
			firstVisibleItem++;
		}
			
	}
	
	public void addItem(Item item) {
		items.add(item);
			
	}
	
	public void addItem(int position, Item item) {
		items.add(position, item);
			
	}

	public void click(int x, int y) {
		

	
//	
//		if(x-40 >= 0 && x-40 <= arrowLeft.getWidth() && y - 661 >= 0 && y - 661 <= arrowLeft.getHeight())
//			scrollLeft();
		
//		for(int i = 1; i <= 4; i++) {
//			if(x - 40 - ((arrowLeft.getWidth() + 5) * i) >= 0 && x - 40 - ((arrowLeft.getWidth() + 5) * i) <= arrowLeft.getWidth() && y - 661 >= 0 && y - 661 <= arrowLeft.getHeight())
//				items.get(firstVisibleItem + i - 1).onClickInInventoryEnvent();
//		}
//		
//		if(x - 40 - ((arrowLeft.getWidth() + 5) * 5) >= 0 && x - 40 - ((arrowLeft.getWidth() + 5) * 5) <= arrowLeft.getWidth() && y - 661 >= 0 && y - 661 <= arrowLeft.getHeight())
//			scrollRight();
		
	}

	public void init() {
		for(int i = 0; i < items.size(); i++)
		items.get(i).init();
		
	}
}
