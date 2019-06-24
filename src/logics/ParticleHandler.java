package logics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import data.Tetro;
import logics.worlds.World;
import particles.BreakingAnimation;
import particles.ParticleAnimation;

public class ParticleHandler {

	ArrayList<ParticleAnimation> anims;
	BufferedImage img;
	private World world;

	public ParticleHandler(World world) {
		anims = new ArrayList<>();
		this.world = world;
	}

	public void tick() {
		int i = 0;
		while (i < anims.size()) {
			anims.get(i).tick();
			if (anims.get(i).isOver()) {
				anims.remove(i);
			} else {
				i++;
			}
		}
	}

	public void show(Graphics2D g) {
		try {
			for (ParticleAnimation p : anims) {
				p.show(g);
			}
		} catch (Exception e) {
			System.err.println("Fehler beim zerspringen");
		}
	}

	public void startBreakingAnimation(int x, int y, Tetro tetro) {

		for (int j = 0; j < tetro.getType().getHitbox().length; j++) {
			for (int i = 0; i < tetro.getType().getHitbox()[j].length; i++) {
				if (tetro.getType().getHitbox()[j][i]) {
					switch (tetro.getRotation()) {
					case 0:
						anims.add(new BreakingAnimation(world, tetro.getType().getSliced(), i + x, j + y));
						break;
					case 1:
						anims.add(new BreakingAnimation(world, tetro.getType().getSliced(), j + x, -i + y + 3));
						break;
					case 2:
						anims.add(new BreakingAnimation(world, tetro.getType().getSliced(), -i + x + 3, -j + y + 1));
						break;
					case 3:
						anims.add(new BreakingAnimation(world, tetro.getType().getSliced(), -j + x + 1, i + y));
						break;
					}
				}
			}
		}
	}

}
