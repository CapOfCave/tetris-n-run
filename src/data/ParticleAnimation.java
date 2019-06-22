package data;

import java.awt.Graphics2D;
import java.util.ArrayList;

import graphics.GameFrame;

public class ParticleAnimation {
	public ArrayList<Particle> particles;
	private Pics imgs;

	private double maxSpeed = 6;// 5
	private int lifeSpan = 13; // 7

	public ParticleAnimation(Pics pics, int x, int y, int k, int l) {
		imgs = pics;
		particles = new ArrayList<>();
		for (int i = 0; i < imgs.amount; i++) {
			particles.add(new Particle(x * GameFrame.BLOCKSIZE - k, y * GameFrame.BLOCKSIZE - l,
					Math.random() * 2 * maxSpeed - maxSpeed, Math.random() * 2 * maxSpeed - maxSpeed, null, lifeSpan,
					imgs.get(i)));
		}
	}

	public void show(Graphics2D g) {
		try {
			for (Particle p : particles) {
				p.show(g);
			}
		} catch (Exception e) {
			System.err.println("Fehler beim zerspringen");
		}
//		int i = 0;
//		while (i < particles.size()) {
//			particles.get(i).show(g);
//		}

	}

	public void tick() {
		int i = 0;
		while (i < particles.size()) {
			particles.get(i).update();
			if (particles.get(i).isDead()) {
				particles.remove(i);
			} else {
				i++;
			}
		}
//		try {
//		System.out.println(particles.get(0).x);
//		}catch(Exception e) {};
	}

	public boolean isOver() {
		return particles.size() == 0;
	}

}
