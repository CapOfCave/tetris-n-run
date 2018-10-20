package logics.entities.items;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import graphics.Frame;
import loading.AnimationLoader;
import logics.worlds.World;

/**
 * @author Lars Created on 16.09.2018
 */
public class Weapon extends Item {

	private static final long serialVersionUID = 1L;
//	private transient BufferedImage imgHit;
//	private String imgPath, imgHitPath;

	//TODO offsets
	@SuppressWarnings("unused")
	private Point imgOffset;
	private Point imgHitOffset;

	private int damage;

	// Hitbox
	private double hitWidth;
	private double theta;
	private double range;
	private int cooldownTicks = 50;
	

	public static double tmpx, tmpy;

	public Weapon(World world, int damage, String imgPath, Point imgOffset, Point imgHitOffset, double hitWidth,
			double theta, double range, int cooldownTicks) {
		super(world, imgPath);

//		this.imgHitPath = imgHitPath;
		this.imgOffset = imgOffset;
		this.imgHitOffset = imgHitOffset;
		this.hitWidth = hitWidth;
		this.theta = theta;
		this.range = range;
		this.damage = damage;
		this.cooldownTicks = cooldownTicks;
	}

	@Override
	public void init() {
		super.init();
		anims = AnimationLoader.loadAnimations(imgPath);
		akt_animation = anims.get(anims.keySet().toArray()[0]);
	}

	public void draw(Graphics g, int x, int y, String animkey, int animFrame, boolean debugMode) {

		akt_animation = anims.get(animkey);
		akt_animation.setFrame(animFrame);

		g.drawImage(akt_animation.getImage(), x + imgHitOffset.x, y + imgHitOffset.y, Frame.BLOCKSIZE,
				Frame.BLOCKSIZE, null);

		if (debugMode) {
			drawDebug(g, x, y);
		}

	}

	public void tick() {
		
	}

	@Override
	public void collectingEvent() {
		world.getPlayer().addToInventory(this, 0);
	}

	@Override
	public void onClickInInventoryEnvent() {
		world.getPlayer().setWeapon(this);
	}

	private void drawDebug(Graphics g, int x, int y) {
	}

	public void hit() {
	}

	public boolean isInRange(double x, double y, double angleDeg, Rectangle eBounds) {
		double nullx = x + Frame.BLOCKSIZE / 2;
		double nully = y + Frame.BLOCKSIZE / 2;

		// Calc nearest point
		double npX; // TODO not optimal & x < 0 unmöglich
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
			System.err.println("Fehler!!! Weapon#hitbox");
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

		return intersectsTop || intersectsBottom || (dist < range
				&& ((alpha - angleDeg + 360) % 360 > 360 - theta / 2 || (alpha - angleDeg + 360) % 360 < +theta / 2)
				&& trfx > nullx);
	}

	public int getDamage() {
		return damage;
	}

	public int getCooldownTicks() {
		return cooldownTicks;
	}

	public void setCooldownTicks(int cooldownTicks) {
		this.cooldownTicks = cooldownTicks;
	}
	
	
}
