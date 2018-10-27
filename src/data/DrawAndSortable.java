package data;

import java.awt.Graphics;

import graphics.Renderer;

public interface DrawAndSortable extends Comparable<DrawAndSortable> {

	public double getHeight();

	public int compareTo(DrawAndSortable o);

	public void draw(Graphics g, float interpolation);

	public void addTo(Renderer renderer);
}
