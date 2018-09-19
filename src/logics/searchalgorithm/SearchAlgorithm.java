package logics.searchalgorithm;

import java.awt.Point;
import java.util.ArrayList;

/**
 * @author Lars Created on 19.09.2018
 */
public class SearchAlgorithm {

	public static ArrayList<Point> calcShortestPath(Point start, Point end, boolean[][] tetroWorldHitbox) {
//		System.out.println("new Path from " + start.x + "|" + start.y + " to "+ end.x + "|" + end.y);
		ArrayList<Node> openlist = new ArrayList<>();
		ArrayList<Node> closedlist = new ArrayList<>();

		Node[][] grid = new Node[tetroWorldHitbox[0].length][tetroWorldHitbox.length]; // x|y
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				grid[i][j] = new Node(i, j);
			}
		}
		openlist.add(0, grid[start.x][start.y]);

		do {
			int lowestIndex = 0;
			for (int i = 0; i < openlist.size(); i++) {
				if (openlist.get(i).f < openlist.get(lowestIndex).f) {
					lowestIndex = i;
				}
			}

			Node current = openlist.get(lowestIndex);
			
			
			if (current.equals(end)) {
				// TODO done
//				System.out.println("Found path.");
				ArrayList<Point> path = new ArrayList<>();
				Node temp = current;
				path.add(0, new Point(temp.x, temp.y));
				while (temp.previous != null) {
					path.add(0, new Point(temp.x, temp.y));
					temp = temp.previous;
				}

				return path;
			}

			openlist.remove(current);
			closedlist.add(current);

			ArrayList<Node> neighbors = current.getNeighbors(grid, tetroWorldHitbox);
			for (int i = 0; i < neighbors.size(); i++) {
				Node neighbor = neighbors.get(i);
				if (!closedlist.contains(neighbor)) {
					float tempg = current.g + 1;

					boolean newPath = false;
					if (openlist.contains(neighbor)) {
						if (tempg < neighbor.g) {
							neighbor.g = tempg;
							newPath = true;
						}
					} else {
						neighbor.g = tempg;
						openlist.add(neighbor);
						newPath = true;
					}
					if (newPath) {
						neighbor.h = heuristic(neighbor, end);
						neighbor.f = neighbor.g + neighbor.h;
						neighbor.previous = current;
					}
				}
			}

		} while (!openlist.isEmpty());

		ArrayList<Point> path = new ArrayList<>();
		
		int lowestIndex = 0;
		for (int i = 0; i < openlist.size(); i++) {
			if (closedlist.get(i).f < closedlist.get(lowestIndex).f) {
				lowestIndex = i;
			}
		}
		
		Node temp = closedlist.get(lowestIndex);
		path.add(0, new Point(temp.x, temp.y));
		while (temp.previous != null) {
			path.add(0, new Point(temp.x, temp.y));
			temp = temp.previous;
		}

		return path;
		
//		System.out.println("no path found from " + start + " to " + end);
	}

	private static float heuristic(Node neighbor, Point end) {
		// return (float)Math.sqrt((neighbor.x - end.x) * (neighbor.x - end.x) + (neighbor.y - end.y) * (neighbor.y -
		// end.y));
		return Math.abs(neighbor.x - end.x) + Math.abs(neighbor.y - end.y);
	}

	private static class Node {

		public int x = 0;
		public int y = 0;
		public float f = 0;
		public float g = 0;
		public float h = 0;
		public Node previous = null;

		public Node(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public boolean equals(Point p) {
			return x == p.x && y == p.y;
		}

		public ArrayList<Node> getNeighbors(Node[][] grid, boolean[][] tetroWorldHitbox) {
			ArrayList<Node> outp = new ArrayList<>();
			if (x > 1) {
				if (tetroWorldHitbox[y][x - 1])
					outp.add(grid[x - 1][y]);
			}
			if (x < grid.length - 2) {
				if (tetroWorldHitbox[y][x + 1])
					outp.add(grid[x + 1][y]);
			}
			if (y > 1) {
				if (tetroWorldHitbox[y - 1][x])
					outp.add(grid[x][y - 1]);
			}
			if (y < grid[0].length - 2) {
				if (tetroWorldHitbox[y + 1][x])
					outp.add(grid[x][y + 1]);
			}
			return outp;
		}

	}

}