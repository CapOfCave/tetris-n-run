package logics.searchalgorithm;

import java.awt.Point;
import java.util.ArrayList;

import logics.World;

/**
 * @author Lars Created on 19.09.2018
 */
public class SearchAlgorithm {

	public static ArrayList<Point> calcShortestPath(World world, Point start, Point end) {

		ArrayList<Node> openlist = new ArrayList<>();
		ArrayList<Node> closedlist = new ArrayList<>();

		Node[][] grid = new Node[world.getVirtualMaxY()][world.getVirtualMaxX()]; // x|y
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				grid[i][j] = new Node(world, i, j);
			}
		}
		openlist.add(0, grid[start.x][start.y]);

		grid[start.x][start.y].h = heuristic(grid[start.x][start.y], end);

		do {
			int lowestIndex = 0;
			for (int i = 0; i < openlist.size(); i++) {
				if (openlist.get(i).f < openlist.get(lowestIndex).f) {
					lowestIndex = i;
				}
			}

			Node current = openlist.get(lowestIndex);

			if (current.equals(end)) {
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

			ArrayList<Node> neighbors = current.getNeighbors(grid);
			for (int i = 0; i < neighbors.size(); i++) {
				Node neighbor = neighbors.get(i);
				if (!closedlist.contains(neighbor)) {
					float tempg = current.g + (float) Math.sqrt((current.x - neighbor.x) * (current.x - neighbor.x)
							+ (current.y - neighbor.y) * (current.y - neighbor.y));

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
		for (int i = 0; i < closedlist.size(); i++) {
			if (closedlist.get(i).h < closedlist.get(lowestIndex).h) {
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

	}

	private static float heuristic(Node neighbor, Point end) {
		return (float) Math
				.sqrt((neighbor.x - end.x) * (neighbor.x - end.x) + (neighbor.y - end.y) * (neighbor.y - end.y));
	}

	private static class Node {

		public int x = 0;
		public int y = 0;
		public float f = 0;
		public float g = 0;
		public float h = 0;
		public Node previous = null;

		private World world;

		public Node(World world, int x, int y) {
			this.world = world;
			this.x = x;
			this.y = y;
		}

		public boolean equals(Point p) {
			return x == p.x && y == p.y;
		}

		public ArrayList<Node> getNeighbors(Node[][] grid) {
			ArrayList<Node> outp = new ArrayList<>();
			if (x >= 1) {
				if (isPassable(0, -1))
					outp.add(grid[x - 1][y]);
				if (y >= 1) {
					if (isPassableDiagonally(-1, -1))
						outp.add(grid[x - 1][y - 1]);
				}
			}
			if (x <= grid.length - 2) {
				if (isPassable(0, 1))
					outp.add(grid[x + 1][y]);
				if (y <= grid[0].length - 2) {
					if (isPassableDiagonally(1, 1))
						outp.add(grid[x + 1][y + 1]);
				}
			}
			if (y >= 1) {
				if (isPassable(-1, 0))
					outp.add(grid[x][y - 1]);
				if (x <= grid.length - 2) {
					if (isPassableDiagonally(-1, 1))
						outp.add(grid[x + 1][y - 1]);
				}
			}
			if (y <= grid[0].length - 2) {
				if (isPassable(1, 0))
					outp.add(grid[x][y + 1]);
				if (x >= 1) {
					if (isPassableDiagonally(1, -1))
						outp.add(grid[x - 1][y + 1]);
				}
			}

			return outp;
		}

		@Override
		public String toString() {
			return "[" + x + "|" + y + "|" + f + "|" + g + "|" + h + "]";
		}

		private boolean isPassable(int dy, int dx) {
			return (world.isTetroAt(y + dy, x + dx) && world.getTileAt(y + dy, x + dx).isWalkableWithTetro()) || world.getTileAt(y + dy, x + dx).isWalkable();
		}

		private boolean isPassableDiagonally(int dy, int dx) {
			return isPassable(dy, dx) && isPassable(0, dx) && isPassable(dy, 0);
		}

	}

}
