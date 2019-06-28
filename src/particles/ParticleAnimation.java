package particles;

import java.awt.Graphics2D;
import java.util.ArrayList;

import logics.World;

public class ParticleAnimation {
	protected ArrayList<Particle> particles;
	protected World world;
	
	
	
	public ParticleAnimation(World world) {
		this.world = world;
		particles = new ArrayList<>();
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
	}
	
	public void show(Graphics2D g) {
		try {
			for (Particle p : particles) {
				p.show(g);
			}
		} catch (Exception e) {
			System.err.println("Fehler beim zerspringen");
		}
	}
	
	public boolean isOver() {
		return particles.size() == 0;
	}
}
