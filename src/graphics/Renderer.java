package graphics;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Comparator;

import data.Tiles.DrawAndSortable;

public class Renderer {

	ArrayList<DrawAndSortable> drawables;

	public Renderer() {
		drawables = new ArrayList<>();
	}

	public void addDrawable(DrawAndSortable das) {
		drawables.add(das);
	}

	public void draw(Graphics g) {
		drawables.sort(new Comparator<DrawAndSortable>() {

			@Override
			public int compare(DrawAndSortable o1, DrawAndSortable o2) {
				if (o1.getHeight() == o2.getHeight()) {
					return 0;
				} else if (o1.getHeight() < o2.getHeight()) {
					return -1;
				} else {
					return 1;
				}
			}
		});
	
		for(DrawAndSortable das:drawables) {
			das.draw(g);
		}
	}
}
