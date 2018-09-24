package logics.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import loading.ImageLoader;
import logics.EnemySpawner;
import logics.World;
import logics.entities.items.Weapon;
import logics.searchalgorithm.SearchAlgorithm;

public class Enemy extends MovingEntity {

	private static final long serialVersionUID = 1L;

	private EnemySpawner parent;

	private boolean alive = true;
	private boolean active;

	private int minX, minY, maxX, maxY;
	private int playerx, playery;

	private ArrayList<Point> path;

	private Point goal;

	public Enemy(World world, EnemySpawner parent, int health, int x, int y) {
		super(world, ImageLoader.loadImage("/res/gegner.png"));
		this.health = health;
		this.parent = parent;

		acc = 0.8;
		brake = 4;
		maxSpeed = 4;

		minX = parent.getMinX();
		minY = parent.getMinY();
		maxX = parent.getMaxX();
		maxY = parent.getMaxY();

		this.x = x;
		this.y = y;
		lastX = x;
		lastY = y;

	}

	public void draw(Graphics g, float interpolation, boolean debugMode) {
		float interpolX = (int) ((x - lastX) * interpolation + lastX);
		float interpolY = (int) ((y - lastY) * interpolation + lastY);

		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setColor(Color.blue);
		g2d.translate(interpolX - world.cameraX() + world.blockSize() / 2, interpolY - world.cameraY() + world.blockSize() / 2);
		g2d.rotate(Math.toRadians(rotation - 90));
		// g2d.drawImage(img, (int) (interpolX) - world.cameraX(), (int) (interpolY) - world.cameraY(), blockSize,
		// blockSize, null);
		g2d.drawImage(img, -world.blockSize() / 2, -world.blockSize() / 2, world.blockSize(), world.blockSize(), null);
		g2d.dispose();

		if (debugMode) {
			drawDebug(g);

		}
	}

	private void drawDebug(Graphics g) {
		g.setColor(Color.ORANGE);
		g.fillOval((int) (x - world.cameraX()), (int) (y - world.cameraY()), 5, 5);
		g.fillOval((int) (x - world.cameraX() + world.blockSize() - 1), (int) (y - world.cameraY()), 5, 5);
		g.fillOval((int) (x - world.cameraX() + world.blockSize() - 1), (int) (y - world.cameraY() + world.blockSize() - 1), 5, 5);
		g.fillOval((int) (x - world.cameraX()), (int) (y - world.cameraY() + world.blockSize() - 1), 5, 5);

		if (active) {
			g.setColor(Color.RED);
		} else {
			g.setColor(Color.GREEN);
		}
		g.fillOval((int) (x - world.cameraX() + world.blockSize() / 2), (int) (y - world.cameraY() - 6), 5, 5);

	}

	public void tick() {
		lastX = x;
		lastY = y;

		checkHealth();

		if (active)
			aktionInActiveMode();
		else
			aktionInPassiveMode();

		move();
	}

	public void aktionInActiveMode() {

		if (playerx != world.getPlayer().getTileX() && playery != world.getPlayer().getY()) {
			playerx = world.getPlayer().getTileX();
			playery = world.getPlayer().getTileY();

			goal = new Point(playerx, playery);
			path = SearchAlgorithm.calcShortestPath(world, new Point(getTileX(), getTileY()), goal);
		}
		continuePath();

		if (distanceToPlayer() > 5 * world.blockSize()) {
			active = false;
		}
	}

	public void aktionInPassiveMode() {

		if (goal == null || path == null) {
			goal = new Point(minX + random(maxX - minX + 1), minY + random(maxY - minY + 1));
			path = SearchAlgorithm.calcShortestPath(world, new Point(getTileX(), getTileY()), goal);
		} else if ((Math.abs(goal.x * world.blockSize() - x) < 5 && Math.abs(goal.y * world.blockSize() - y) < 5) || path.isEmpty()) {
			goal = new Point(minX + random(maxX - minX + 1), minY + random(maxY - minY + 1));
			path = SearchAlgorithm.calcShortestPath(world, new Point(getTileX(), getTileY()), goal);
		}

		if (distanceToPlayer() < 3 * world.blockSize()) {
			active = true;
		}
		continuePath();
	}

	private void continuePath() {
		if (path == null) {
			return;
		} else if (path.isEmpty()) {
			return;
		}
		if (Math.abs(path.get(0).x * world.blockSize() - x) < 5 && Math.abs(path.get(0).y * world.blockSize() - y) < 5) {
			path.remove(0);
		}
		if (!path.isEmpty()) {
			wantsToGoUp = y - path.get(0).y * world.blockSize() > 1;
			wantsToGoDown = y - path.get(0).y * world.blockSize() < -1;
			wantsToGoLeft = x - path.get(0).x * world.blockSize() > 1;
			wantsToGoRight = x - path.get(0).x * world.blockSize() < -1;
		}
	}

	private double distanceToPlayer() {
		return Math.sqrt(
				(x - world.getPlayer().getX()) * (x - world.getPlayer().getX()) + (y - world.getPlayer().getY()) * (y - world.getPlayer().getY()));
	}

	private void checkHealth() {
		if (health <= 0) {
			kill();
		}
	}

	private void kill() {
		world.removeEnemy(this);
		parent.enemyKilled();
	}

	public boolean isHittingPlayer(double x2, double y2) {
		return false;

	}

	public void applyDamage(Weapon weapon) {
		health -= weapon.getDamage();

	}

	public int random(int max) {
		return (int) (Math.random() * max);
	}

	public boolean isAlive() {
		return alive;
	}

}
