package data;

import java.awt.Color;
import java.awt.Graphics;

public class Particle {

	double x, y;
	double vx, vy;
	double opacity;
	Color color;
	double lifeSpan;
	

	public Particle(double x, double y, double vx, double vy, double opacity, Color color, double lifeSpan) {
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		this.opacity = opacity;
		this.color = color;
		this.lifeSpan = lifeSpan;
	}

	public void update() {
		x += vx;
		y += vy;
	}

	public void show(Graphics g) {
		g.fillOval((int) x, (int) y, 5, 5);
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

	public double getOpacity() {
		return opacity;
	}

	public void setOpacity(double opacity) {
		this.opacity = opacity;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}
