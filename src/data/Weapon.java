package data;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * @author Lars Created on 16.09.2018
 */
public class Weapon {

	private BufferedImage img;
	private BufferedImage imgHit;
	private Point imgOffset;
	private Point imgHitOffset;
	private int blockSize;
	private int hitTicks = 0;

	// Hitbox
	private double hitWidth;
	private double theta;
	private double range;

	private double bx;
	private double by1;
	private double by2;
	
	double nullx;
	double nully;

	
	public Color color = Color.BLACK;

	public Weapon(BufferedImage img, BufferedImage imgHit, Point imgOffset, Point imgHitOffset, int blockSize, double hitWidth, double theta,
			double range) {
		this.img = img;
		this.imgHit = imgHit;
		this.imgOffset = imgOffset;
		this.imgHitOffset = imgHitOffset;
		this.blockSize = blockSize;
		this.hitWidth = hitWidth;
		this.theta = theta;
		this.range = range;
	}

	public void draw(Graphics g, int x, int y, boolean debugMode) {
		if (hitTicks != 0) {
			g.drawImage(imgHit, x + imgHitOffset.x - blockSize / 2, y + imgHitOffset.y - blockSize / 2, blockSize, blockSize, null);
			hitTicks--;
		} else {
			g.drawImage(img, x + imgOffset.x - blockSize / 2, y + imgOffset.y - blockSize / 2, blockSize, blockSize, null);
		}

		if (debugMode) {
			drawDebug(g, x, y);
		}
	}

	private void drawDebug(Graphics g, int x, int y) {
		Rectangle rect = new Rectangle((int) (x + blockSize / 2 - range), (int) (y + blockSize / 2 - range), (int) (2 * range), (int) (2 * range));
		g.setColor(Color.BLACK);
		g.drawArc(rect.x - blockSize / 2, rect.y - blockSize / 2, rect.width, rect.height, (int) (-theta / 2), (int) (theta));

		g.drawLine(x, (int) (y - hitWidth), x, (int) (y + hitWidth));

		g.drawLine(x, (int) (y - hitWidth), (int) (x + range * Math.cos(Math.toRadians(theta / 2))),
				(int) (y - range * Math.sin(Math.toRadians(theta / 2))));
		g.drawLine(x, (int) (y + hitWidth), (int) (x + range * Math.cos(Math.toRadians(theta / 2))),
				(int) (y + range * Math.sin(Math.toRadians(theta / 2))));

//		g.drawOval((int)bx, (int)by1, 5, 5);
//		g.drawOval((int)bx, (int)by2, 5, 5);
		g.setColor(Color.RED);
		g.fillOval((int)0, (int)0, 100, 100);
		System.out.println(nullx + " " + nully);
	}

	public void hit() {
		hitTicks = 10;
	}

	public boolean isInRange(double x, double y, Rectangle eBounds) {
		double nullx = x + blockSize / 2;
		double nully = y + blockSize / 2;
		this.nullx = x + blockSize / 2;
		this.nully = y + blockSize / 2;
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

		// Calc angle alpha [degrees]
		double alpha = Math.atan((npY - nully) / (npX - nullx));

		double bx = nullx + Math.cos(Math.toRadians(theta / 2)) * range;
		double by1 = nully - Math.sin(Math.toRadians(theta / 2)) * range;
		double by2 = nully + Math.sin(Math.toRadians(theta / 2)) * range;
		this.bx = bx;
		this.by1 = by1;
		this.by2 = by2;
		
		double intersectsD1value = (npX - nullx) / (bx - nullx) + (npY - by1) / (nully - by1);
		double intersectsD2value = (npX - nullx) / (bx - nullx) + (npY - by2) / (nully - by2);
		double intersectsN1value = (npX - nullx) / (bx - nullx) + (npY - nully) / (nully - hitWidth - by1);
		double intersectsN2value = (npX - nullx) / (bx - nullx) + (npY - nully) / (nully + hitWidth - by2);
		boolean intersectsD1 = intersectsD1value <= 1 && intersectsD1value >= 0;
		boolean intersectsD2 = intersectsD2value <= 1 && intersectsD2value >= 0;
		boolean intersectsN1 = intersectsN1value <= 1 && intersectsN1value >= 0;
		boolean intersectsN2 = intersectsN2value <= 1 && intersectsN2value >= 0;

		boolean intersectsTop = intersectsD1 && !intersectsN1;
		boolean intersectsBottom = intersectsD2 && !intersectsN2;
		// if (intersectsTop)
		// System.out.println("matching top tile");
		// if (intersectsBottom)
		// System.out.println("matching bottom tile");
		// if (dist < range && Math.abs(alpha) < theta / 2)
		// System.out.println("matching center tile");

		
		
//		if (intersectsD1)
//			color = Color.RED;
//		if (intersectsN1)
//			color = Color.YELLOW;
		
//		if (intersectsD1 && !intersectsN1) {
//			color = Color.ORANGE;
//		}
		
		if (!intersectsTop && !intersectsBottom && !(dist < range && Math.abs(Math.toDegrees(alpha)) < theta / 2 && npX > nullx)) {
			color = Color.WHITE;
		}
		if (dist < range && Math.abs(Math.toDegrees(alpha)) < theta / 2 && npX > nullx)
			color = Color.BLUE;
		
		// System.out.println(Math.toDegrees(alpha));
		return intersectsTop || intersectsBottom || (dist < range && Math.abs(Math.toDegrees(alpha)) < theta / 2 && npX > nullx);
	}

}
