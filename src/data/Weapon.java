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

	public void draw(Graphics g, int rotation, int x, int y, boolean debugMode) {
		if (hitTicks != 0) {
			g.drawImage(imgHit, x + imgHitOffset.x, y + imgHitOffset.y, blockSize, blockSize, null);
			hitTicks--;
		} else {
			g.drawImage(img, x + imgOffset.x, y + imgOffset.y, blockSize, blockSize, null);
		}

		if (debugMode) {
			drawDebug(g, rotation, x, y);
		}
	}

	private void drawDebug(Graphics g, int rotation, int x, int y) {
		Rectangle rect = new Rectangle((int) (x + blockSize / 2 - range), (int) (y + blockSize / 2 - range), (int) (2 * range), (int) (2 * range));
		g.setColor(Color.BLACK);
		g.drawArc(rect.x, rect.y, rect.width, rect.height, (int) (-theta / 2), (int) (theta));

		g.drawLine(x + blockSize / 2, (int) (y + blockSize / 2 - hitWidth), x + blockSize / 2, (int) (y + blockSize / 2 + hitWidth));

		g.drawLine(x + blockSize / 2, (int) (y + blockSize / 2 - hitWidth), (int) (x + blockSize / 2 + range * Math.cos(Math.toRadians(theta / 2))),
				(int) (y + blockSize / 2 - range * Math.sin(Math.toRadians(theta / 2))));
		g.drawLine(x + blockSize / 2, (int) (y + blockSize / 2 + hitWidth), (int) (x + blockSize / 2 + range * Math.cos(Math.toRadians(theta / 2))),
				(int) (y + blockSize / 2 + range * Math.sin(Math.toRadians(theta / 2))));

	}

	public void hit() {
		hitTicks = 10;
	}

	private boolean isInRange(int x, int y, Rectangle eBounds) {
		double nullx = x + blockSize / 2;
		double nully = y + blockSize / 2;

		// Calc nearest point
		double npX = 0;
		if (npX < eBounds.x) {
			npX = eBounds.x;
		} else if (npX > eBounds.x + eBounds.width) {
			npX = eBounds.x + eBounds.width;
		}
		double npY = 0;
		if (npY < eBounds.y) {
			npY = eBounds.y;
		} else if (npY > eBounds.y + eBounds.height) {
			npY = eBounds.y + eBounds.height;
		}

		// Calc distance np- "0"
		double dist = Math.sqrt((npX - nullx) * (npX - nullx) + (npY - nully) * (npY - nully));

		// Calc angle alpha [degrees]
		double alpha = Math.atan((npY - nully) / (npX / nullx));

		double deltabx = Math.cos(Math.toRadians(theta / 2)) * range;
		double by1 = nully - Math.sin(Math.toRadians(theta / 2)) * range;
		double by2 = nully + Math.sin(Math.toRadians(theta / 2)) * range;

		double intersectsD1value = (npX - nullx) / (deltabx) + (npY - by1) / (nully - by1);
		double intersectsD2value = (npX - nullx) / (deltabx) + (npY - by2) / (nully - by2);
		double intersectsN1value = (npX - nullx) / (deltabx) + (npY - nully) / (nully - hitWidth - by1);
		double intersectsN2value = (npX - nullx) / (deltabx) + (npY - nully) / (nully + hitWidth - by2);
		boolean intersectsD1 = intersectsD1value <= 1 && intersectsD1value >= 0;
		boolean intersectsD2 = intersectsD2value <= 1 && intersectsD2value >= 0;
		boolean intersectsN1 = intersectsN1value <= 1 && intersectsN1value >= 0;
		boolean intersectsN2 = intersectsN2value <= 1 && intersectsN2value >= 0;
		
		boolean intersectsTop = intersectsD1 && !intersectsN1;
		boolean intersectsBottom = intersectsD2 && !intersectsN2;
		
		
		
		return false;
	}

}
