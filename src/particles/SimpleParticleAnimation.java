package particles;

import java.awt.Color;
import java.awt.image.BufferedImage;

import logics.worlds.World;

public class SimpleParticleAnimation extends ParticleAnimation {
	
	public SimpleParticleAnimation(World world, int x, int y, BufferedImage img) {
		super(world);
		particles.add(new Particle(world, x + Math.random() * 20 - 10, y, Math.random() * 2 , Math.random() * 8 - 5, Color.WHITE, 5, null));
	}
}
