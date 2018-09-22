package data;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import logics.Item;
import logics.entities.Player;

/**
 * @author Lars Created on 16.09.2018
 */
public class Weapon extends Item{

	private BufferedImage img;
	private BufferedImage imgHit;
	private Point imgOffset;
	private Point imgHitOffset;
	private int blockSize;
	private int hitTicks = 0;
	private int damage;
	private Player player;

	// Hitbox
	private double hitWidth;
	private double theta;
	private double range;

	public static double tmpx, tmpy;

	public Weapon(int damage, BufferedImage img, BufferedImage imgHit, Point imgOffset, Point imgHitOffset, int blockSize, double hitWidth,
			double theta, double range, Player player) {
		super(img, blockSize);
		this.img = img;
		this.imgHit = imgHit;
		this.imgOffset = imgOffset;
		this.imgHitOffset = imgHitOffset;
		this.blockSize = blockSize;
		this.hitWidth = hitWidth;
		this.theta = theta;
		this.range = range;
		this.damage = damage;
		this.player = player;
	}

	public void draw(Graphics g, Graphics2D g2d, int x, int y, boolean debugMode) {
		if (hitTicks != 0) {
			g2d.drawImage(imgHit, x + imgHitOffset.x, y + imgHitOffset.y, blockSize, blockSize, null);
			hitTicks--;
		} else {
			g2d.drawImage(img, x + imgOffset.x, y + imgOffset.y, blockSize, blockSize, null);
		}

		if (debugMode) {
			drawDebug(g2d, x, y);
		}

	}
	
	public void equip() {
		if(player != null) {
			player.setWeapon(this);
		}
	}
	
	@Override
	public void collectingEvent() {
		player.addToInventory(this, 0);
	}
	
	@Override
	public void onClickInInventoryEnvent() {
		equip();
	}

	private void drawDebug(Graphics g, int x, int y) {
		Rectangle rect = new Rectangle((int) (x + blockSize / 2 - range), (int) (y + blockSize / 2 - range), (int) (2 * range), (int) (2 * range));
		g.setColor(Color.RED);
		g.drawArc(rect.x, rect.y, rect.width, rect.height, (int) (-theta / 2), (int) (theta));

		g.drawLine(x + blockSize / 2, (int) (y + blockSize / 2 - hitWidth), x + blockSize / 2, (int) (y + blockSize / 2 + hitWidth));

		g.drawLine(x + blockSize / 2, (int) (y + blockSize / 2 - hitWidth), (int) (x + blockSize / 2 + range * Math.cos(Math.toRadians(theta / 2))),
				(int) (y + blockSize / 2 - range * Math.sin(Math.toRadians(theta / 2))));
		g.drawLine(x + blockSize / 2, (int) (y + blockSize / 2 + hitWidth), (int) (x + blockSize / 2 + range * Math.cos(Math.toRadians(theta / 2))),
				(int) (y + blockSize / 2 + range * Math.sin(Math.toRadians(theta / 2))));
		g.setColor(Color.RED);

	}

	public void hit() {
		hitTicks = 10;
	}

	public boolean isInRange(double x, double y, double angleDeg, Rectangle eBounds) {
		int halfBlockSize = blockSize / 2;
		double nullx = x + halfBlockSize;
		double nully = y + halfBlockSize;

		// Calc nearest point
		double npX;
		double npY;

		npX = nullx;
		if (npX < eBounds.x) {
			npX = eBounds.x;
		} else if (npX > eBounds.x + eBounds.width) {
			npX = eBounds.x + eBounds.width;
		}
		npY = nully;
		if (npY < eBounds.y) {
			npY = eBounds.y;
		} else if (npY > eBounds.y + eBounds.height) {
			npY = eBounds.y + eBounds.height;
		}

		// Calc distance np- "0"
		double dist = Math.sqrt((npX - nullx) * (npX - nullx) + (npY - nully) * (npY - nully));
		if (dist == 0) {
			return true;
		}

		// Calc angle alpha [degrees]
		double alpha;
		if (npX >= nullx && npY >= nully) {
			alpha = 90 + Math.toDegrees(Math.atan((npY - nully) / (npX - nullx)));
		} else if (npX >= nullx && npY < nully) {
			alpha = 90 + Math.toDegrees(Math.atan((npY - nully) / (npX - nullx)));
		} else if (npX < nullx && npY < nully) {
			alpha = 270 + Math.toDegrees(Math.atan((npY - nully) / (npX - nullx)));
		} else if (npX < nullx && npY >= nully) {
			alpha = 270 + Math.toDegrees(Math.atan((npY - nully) / (npX - nullx)));
		} else {
			alpha = 0;
			System.out.println("Fehler!!! Weapon#hitbox");
		}
		// Math.toDegrees(Math.atan((npY - nully) / (npX - nullx)));
		// Transformation in ein rechtwinkliges Koordinatensystem
		double trfx = nullx + dist * Math.cos(Math.toRadians(alpha - angleDeg));
		double trfy = nully + dist * Math.sin(Math.toRadians(alpha - angleDeg));

		tmpx = trfx;
		tmpy = trfy;

		double bx = nullx + Math.cos(Math.toRadians(theta / 2)) * range;
		double by1 = nully - Math.sin(Math.toRadians(theta / 2)) * range;
		double by2 = nully + Math.sin(Math.toRadians(theta / 2)) * range;

		double xFactor = (trfx - nullx) / (bx - nullx);

		double yFactorD1 = (trfy - by1) / (nully - by1);
		double intersectsD1value = xFactor + yFactorD1;

		double yFactorD2 = (trfy - by2) / (nully - by2);
		double intersectsD2value = xFactor + yFactorD2;

		double yFactorN1 = (trfy - by1) / (nully - hitWidth - by1);
		double intersectsN1value = xFactor + yFactorN1;

		double yFactorN2 = (trfy - by2) / (nully + hitWidth - by2);
		double intersectsN2value = xFactor + yFactorN2;
		boolean intersectsD1 = intersectsD1value <= 1 && xFactor >= 0 && yFactorD1 >= 0;
		boolean intersectsD2 = intersectsD2value <= 1 && xFactor >= 0 && yFactorD2 >= 0;
		boolean intersectsN1 = intersectsN1value <= 1 && xFactor >= 0 && yFactorN1 >= 0;
		boolean intersectsN2 = intersectsN2value <= 1 && xFactor >= 0 && yFactorN2 >= 0;

		boolean intersectsTop = intersectsD1 && !intersectsN1;
		boolean intersectsBottom = intersectsD2 && !intersectsN2;
		// if (intersectsTop)
		// System.out.println("matching top tile");
		// if (intersectsBottom)
		// System.out.println("matching bottom tile");
		// if (dist < range && Math.abs(alpha) < theta / 2)
		// System.out.println("matching center tile");

		return intersectsTop || intersectsBottom || (dist < range
				&& ((alpha - angleDeg + 360) % 360 > 360 - theta / 2 || (alpha - angleDeg + 360) % 360 < +theta / 2) && trfx > nullx);
	}

	public int getDamage() {
		return damage;
	}
	
	public void getPlayer(Player player) {
		this.player = player;
	}
}
