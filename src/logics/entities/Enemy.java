package logics.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

import data.Animation;
import logics.EnemySpawner;
import logics.World;
import logics.entities.items.Weapon;
import logics.searchalgorithm.SearchAlgorithm;

public class Enemy extends LivingEntity {

	private static final long serialVersionUID = 1L;

	private EnemySpawner parent;
	private Weapon activeWeapon;

	private boolean active;

	private int minX, minY, maxX, maxY;
	private int playerx, playery;

	private ArrayList<Point> path;

	private Point goal;

	public Enemy(World world, EnemySpawner parent, int health, int x, int y, HashMap<String, Animation> anims) {
		super(world, anims);
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

		activeWeapon = new Weapon(world, 5, "/res/anims/sword.txt", new Point(0, 0), new Point(0, 0), 10, 50, 30, 50);
	}

	public void draw(Graphics g, float interpolation, boolean debugMode) {
		int interpolX = (int) ((x - lastX) * interpolation + lastX);
		int interpolY = (int) ((y - lastY) * interpolation + lastY);

//		g2d.translate(interpolX - world.cameraX() + world.blockSize() / 2, interpolY - world.cameraY() + world.blockSize() / 2);
		// g2d.drawImage(img, (int) (interpolX) - world.cameraX(), (int) (interpolY) -
		// world.cameraY(), blockSize,
		// blockSize, null);
		g.drawImage(akt_animation.getImage(), interpolX - world.cameraX(), interpolY - world.cameraY(),
				world.blockSize(), world.blockSize(), null);
//		if (activeWeapon != null)
//			activeWeapon.draw(g, -world.blockSize() / 2, -world.blockSize() / 2, debugMode);
//		if (debugMode) {
//			drawDebug(g, g2d);
//TODO waffen
//		}
		if (debugMode) {
			drawDebug(g);
		}

	}

	private void drawDebug(Graphics g) {
		g.setColor(Color.ORANGE);
		g.fillOval((int) (x - world.cameraX()), (int) (y - world.cameraY()), 5, 5);
		g.fillOval((int) (x - world.cameraX() + world.blockSize() - 1), (int) (y - world.cameraY()), 5, 5);
		g.fillOval((int) (x - world.cameraX() + world.blockSize() - 1),
				(int) (y - world.cameraY() + world.blockSize() - 1), 5, 5);
		g.fillOval((int) (x - world.cameraX()), (int) (y - world.cameraY() + world.blockSize() - 1), 5, 5);

		if (active) {
			g.setColor(Color.RED);
		} else {
			g.setColor(Color.GREEN);
		}
		g.fillOval((int) (x - world.cameraX() + world.blockSize() / 2), (int) (y - world.cameraY() - 6), 5, 5);

		g.setColor(Color.RED);
		g.drawString(Integer.toString(health), (int)(x - world.cameraX()) + 10, (int)(y - world.cameraY()) - 20);
	}

	public void tick() {
		super.tick();

		

		if (active)
			aktionInActiveMode();
		else
			aktionInPassiveMode();

		move();

		if (activeWeapon != null)
			activeWeapon.tick();
	}

	public void aktionInActiveMode() {

		if (distanceToPlayer() <= world.blockSize()) {
			hit();
			resetMoveDirections();
		} else if (playerx != world.getPlayer().getTileX() || playery != world.getPlayer().getY()) {
			playerx = world.getPlayer().getTileX();
			playery = world.getPlayer().getTileY();

			goal = new Point(playerx, playery);
			path = SearchAlgorithm.calcShortestPath(world, new Point(getTileX(), getTileY()), goal);
			continuePath();

			if (distanceToPlayer() > 5 * world.blockSize()) {
				active = false;
			}
		}

	}

	private void hit() {
		if (activeWeapon != null && attackReady()) {
			activeWeapon.hit();
			hitTicks += activeWeapon.getCooldownTicks();
			if (activeWeapon.isInRange(x - world.cameraX(), y - world.cameraY(), rotation,
					new Rectangle((int) (world.getPlayer().getX() - world.cameraX()),
							(int) (world.getPlayer().getY() - world.cameraY()), world.blockSize(),
							world.blockSize()))) {
				world.getPlayer().applyDamage(activeWeapon);
			}
		}
	}

	public void aktionInPassiveMode() {

		if (goal == null || path == null) {
			goal = new Point(minX + random(maxX - minX + 1), minY + random(maxY - minY + 1));
			path = SearchAlgorithm.calcShortestPath(world, new Point(getTileX(), getTileY()), goal);
		} else if ((Math.abs(goal.x * world.blockSize() - x) < 5 && Math.abs(goal.y * world.blockSize() - y) < 5)
				|| path.isEmpty()) {
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
			resetMoveDirections();
			return;
		} else if (path.isEmpty()) {
			resetMoveDirections();
			return;
		}
		if (Math.abs(path.get(0).x * world.blockSize() - x) < 5
				&& Math.abs(path.get(0).y * world.blockSize() - y) < 5) {
			path.remove(0);
		}
		if (!path.isEmpty()) {
			wantsToGoUp = y - path.get(0).y * world.blockSize() > 1;
			wantsToGoDown = y - path.get(0).y * world.blockSize() < -1;
			wantsToGoLeft = x - path.get(0).x * world.blockSize() > 1;
			wantsToGoRight = x - path.get(0).x * world.blockSize() < -1;
		}
	}

	private void resetMoveDirections() {
		wantsToGoUp = false;
		wantsToGoDown = false;
		wantsToGoLeft = false;
		wantsToGoRight = false;
	}

	private double distanceToPlayer() {
		return Math.sqrt((x - world.getPlayer().getX()) * (x - world.getPlayer().getX())
				+ (y - world.getPlayer().getY()) * (y - world.getPlayer().getY()));
	}

	@Override
	protected void kill() {
		world.removeEnemy(this);
		parent.enemyKilled();
	}

	private int random(int max) {
		return (int) (Math.random() * max);
	}

}
