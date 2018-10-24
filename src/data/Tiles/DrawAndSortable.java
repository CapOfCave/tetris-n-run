package data.Tiles;

import java.awt.Graphics;

public interface DrawAndSortable extends Comparable<DrawAndSortable> {

	public void draw(Graphics g);

	public double getHeight();

	public int compareTo(DrawAndSortable o);

}
