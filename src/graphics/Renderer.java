package graphics;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.stream.Collectors;

import data.DrawAndSortable;

public class Renderer {

	ArrayList<DrawAndSortable> drawables1;
	ArrayList<DrawAndSortable> toRemove1;
	ArrayList<DrawAndSortable> toAdd1;

	ArrayList<DrawAndSortable> drawables2;
	ArrayList<DrawAndSortable> toRemove2;
	ArrayList<DrawAndSortable> toAdd2;
	boolean drawable1 = true;

	public Renderer() {
		drawables1 = new ArrayList<>();
		drawables2 = new ArrayList<>();

		toRemove1 = new ArrayList<>();
		toAdd1 = new ArrayList<>();
		toRemove2 = new ArrayList<>();
		toAdd2 = new ArrayList<>();
	}

	public void addDrawable(DrawAndSortable das) {
		 if (drawable1) {
		 addAfterPrioritization(drawables2, das);
		 addAfterPrioritization(toAdd1, das);
//		 drawables2.add(das);
//		 toAdd1.add(das);
		
		 } else {
		 addAfterPrioritization(drawables1, das);
		 addAfterPrioritization(toAdd2, das);
//		 drawables1.add(das);
//		 toAdd2.add(das);
		 }
		 
	}

	public void addAfterPrioritization(ArrayList<DrawAndSortable> drawableList, DrawAndSortable das) {
		if (drawableList.isEmpty()) {
			drawableList.add(das);
		}
//		else {
//			return;
//		}

		for (int i = 0; i < drawableList.size(); i++) {
			if (-das.getPriorityInDrawQueue() > -drawableList.get(i).getPriorityInDrawQueue()) {
				drawableList.add(i, das);
				return;
			}
		}
		drawableList.add(das);
	}

	public ArrayList<DrawAndSortable> cleanUpList(ArrayList<DrawAndSortable> passive_ArrayList) {
		return (ArrayList<DrawAndSortable>) passive_ArrayList.stream().distinct().collect(Collectors.toList());
	}

	public void removeDrawable(DrawAndSortable das) {

		if (drawable1) {
			drawables2.remove(das);
			toRemove1.add(das);
		} else {
			drawables1.remove(das);
			toRemove2.add(das);
		}
	}

	public void tick() {
		ArrayList<DrawAndSortable> passive_ArrayList;
		if (drawable1) {
			workOffAR(drawables2, toRemove2, toAdd2);
			drawables2 = cleanUpList(drawables2);
			passive_ArrayList = drawables2;

		} else {
			workOffAR(drawables1, toRemove1, toAdd1);
			drawables1 = cleanUpList(drawables1);
			passive_ArrayList = drawables1;

		}

		passive_ArrayList.sort(new Comparator<DrawAndSortable>() {

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
		drawable1 = !drawable1;
	}

	private void workOffAR(ArrayList<DrawAndSortable> drawables, ArrayList<DrawAndSortable> toRemove,
			ArrayList<DrawAndSortable> toAdd) {
		while (toRemove.size() + toAdd.size() != 0) {
			if (toRemove.size() > 0) {
				drawables.remove(toRemove.get(0));
				toRemove.remove(0);
			}
			if (toAdd.size() > 0) {
				addAfterPrioritization(drawables,toAdd.get(0));
//				drawables.add(toAdd.get(0));
				toAdd.remove(0);
			}
		}
	}

	public void draw(Graphics g, float interpolation) {
		try {
			if (drawable1) {
				for (DrawAndSortable das : drawables1) {
					das.draw(g, interpolation);
				}
			} else {
				for (DrawAndSortable das : drawables2) {
					das.draw(g, interpolation);
				}
			}
		} catch (ConcurrentModificationException ex) {
		//do nothing
		}
		

	}

	public boolean isDAScontained(DrawAndSortable entity) {
		return (drawables1.contains(entity) || toAdd1.contains(entity))
				&& (drawables2.contains(entity) || toAdd2.contains(entity));
	}

	public void reset() {
		drawables1 = new ArrayList<>();
		drawables2 = new ArrayList<>();
		toAdd1 = new ArrayList<>();
		toAdd2 = new ArrayList<>();
		toRemove1 = new ArrayList<>();
		toRemove2 = new ArrayList<>();
	}

	public String getElementAmount() {
		return (drawable1 ? "1   " : "2   ") + drawables1.size() + " bzw. " + drawables2.size();
	}
}
