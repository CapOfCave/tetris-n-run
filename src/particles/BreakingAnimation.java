package particles;

import data.Pics;
import graphics.GameFrame;
import logics.World;

public class BreakingAnimation extends ParticleAnimation {

	private Pics imgs;

	private double maxSpeed = 6;// 5
	private int lifeSpan = 13; // 7

	public BreakingAnimation(World world, Pics pics, int x, int y) {
		super(world);
		imgs = pics;
		for (int i = 0; i < imgs.getAmount(); i++) {
			particles.add(new Particle(world, x * GameFrame.BLOCKSIZE, y * GameFrame.BLOCKSIZE,
					Math.random() * 2 * maxSpeed - maxSpeed, Math.random() * 2 * maxSpeed - maxSpeed, null, lifeSpan,
					imgs.get(i)));
		}
	}

}
