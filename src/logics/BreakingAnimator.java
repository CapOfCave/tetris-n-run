package logics;

import java.awt.Graphics2D;
import java.util.ArrayList;

import data.ParticleAnimation;
import data.Tetro;

public class BreakingAnimator {

	ArrayList<ParticleAnimation> anims;

	public BreakingAnimator() {
		anims = new ArrayList<>();
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

	public void startAnimation(int x, int y, Tetro tetro, int k, int l) {

		for (int j = 0; j < tetro.getType().getHitbox().length; j++) {
			for (int i = 0; i < tetro.getType().getHitbox()[j].length; i++) {
				if (tetro.getType().getHitbox()[j][i]) {
					switch (tetro.getRotation()) {
					case 0:
						anims.add(new ParticleAnimation(tetro.getType().getSliced(), i + x, j + y, k, l));
						break;
					case 1:
						anims.add(new ParticleAnimation(tetro.getType().getSliced(), j + x, -i + y + 3, k, l));
						break;
					case 2:
						anims.add(new ParticleAnimation(tetro.getType().getSliced(), -i + x + 3, -j + y + 1, k, l));
						break;
					case 3:
						anims.add(new ParticleAnimation(tetro.getType().getSliced(), -j + x + 1, i + y, k, l));
						break;
					}
				}
			}
		}

//		anims.add(new ParticleAnimation(tetro.getType().getSliced(), x, y));
//		int sum = anims.get(0).particles.size();
		//+ anims.get(1).particles.size() + anims.get(2).particles.size()
		//+ anims.get(3).particles.size();
//		System.out.println("Added " + 1 + ", animLength:" + anims.size() + ", part. gesamt: " + sum);
//		System.out.println(anims.get(1).particles.get(1).getX() + " | " + anims.get(0).particles.get(0).getY());
	}
}
