package logics;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

import loading.ImageLoader;
import logics.entities.Entity;
import logics.entities.items.Item;
import logics.worlds.World;

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
		

	
	
		if(x-195 >= 0 && x-195 <= 62 && y - 709 >= 0 && y - 709 <= 110) 
			scrollLeft();
		
		for(int i = 0; i <= 3; i++) {
			if(x - 267 - ((125 + 10) * i) >= 0 && x - 267 - ((125 + 10) * i) <= 125 && y - 701 >= 0 && y - 701 <= 125)
				items.get(firstVisibleItem + i).onClickInInventoryEnvent();
		}
		
		if(x - 807 >= 0 && x - 807 <= 62 && y - 708 >= 0 && y - 708 <= 110)
			scrollRight();
		
	}

	public void init() {
		for(int i = 0; i < items.size(); i++) 
		items.get(i).init();
		
		
	}
	
	public void setWorld(World world) {
		for(int i = 0; i < items.size(); i++) 
			items.get(i).setWorld(world);
	}
}
