package particles;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import logics.worlds.World;

public class Particle {

	double x, y;
	double vx, vy;
	Color color;
	int lifeSpan;
	BufferedImage img;
	float opacity = 1f;

	boolean dead = false;
	int age = 0;
	World world;

	public Particle(World world, double x, double y, double vx, double vy, Color color, int lifeSpan, BufferedImage img) {
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		this.color = color;
		this.lifeSpan = lifeSpan;
		this.img = img;
		this.world = world;
	}

	double acc = 0.32;
	public void update() {
		x += vx;
		y += vy;
		if (vx > 0) {
			vx = Math.max(0, vx - acc);
		} else {
			vx =  Math.min(0, vx + acc);
		}
		
		if (vy > 0) {
			vy = Math.max(0, vy - acc);
		} else {
			vy =  Math.min(0, vy + acc);
		}
		age++;
		if (age >= lifeSpan) {
			dead = true;
		}
		opacity = 1f - (float) age / lifeSpan;

	}

	public void show(Graphics2D g) {
		if (img != null) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
			g.drawImage(img, (int) x - world.cameraX(), (int) y - world.cameraY(), null);
		} else {
			g.setColor(color);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
			g.fillOval((int) x - world.cameraX(), (int) y - world.cameraY(), 4, 4);
		}
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getVx() {
		return vx;
	}

	public void setVx(double vx) {
		this.vx = vx;
	}

	public double getVy() {
		return vy;
	}

	public void setVy(double vy) {
		this.vy = vy;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean isDead() {
		return dead;
	}

}
