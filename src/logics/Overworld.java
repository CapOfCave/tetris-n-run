package logics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import data.Tetro;
import input.KeyHandler;

/**
 * @author Marius Created on 13.09.2018
 */
public class Overworld extends World {

	public Overworld(Rectangle graphicClip, int blockSize, Level level, KeyHandler keyHandler) {
		super(graphicClip, blockSize, level, keyHandler);

	}

	@Override
	public void draw(Graphics2D g, float interpolation, boolean debugMode) {

		camera.prepareDraw(interpolation);

		// blocker blocks
		for (int j = 0; j < tileWorld.length; j++) {
			for (int i = 0; i < tileWorld[j].length; i++) {

				// if (world[j][i].get == '1') {
				g.drawImage(tileWorld[j][i].getImg(), i * blockSize - camera.getX(), j * blockSize - camera.getY(), blockSize, blockSize, null);
				// } else if (world[j][i] == '0') {
				// g.drawImage(backgroundImg, i * blockSize - camera.getX(), j * blockSize - camera.getY(), blockSize,
				// blockSize, null);

				// }
			}
		}

		// Lines - vertikal
		for (int i = 0; i <= tileWorld[0].length * blockSize; i += blockSize) {
			g.drawLine((i - camera.getX()), 0, (i - camera.getX()), Math.min(graphicClip.height, tileWorld.length * blockSize - camera.getY()));
		}
		// Lines - horizontal
		for (int i = 0; i <= tileWorld.length * blockSize; i += blockSize) {
			g.drawLine(0, (i - camera.getY()), Math.min(graphicClip.width, tileWorld[0].length * blockSize), (i - camera.getY()));
		}

		// Tetros
		for (Tetro t : tetros) {
			t.draw(g, debugMode);
		}

		if (debugMode)
			for (int j = 0; j < tileWorld.length; j++) {
				for (int i = 0; i < tileWorld[j].length; i++) {
					if (tetroWorldHitbox[j][i]) {
						g.setColor(Color.RED);
						g.drawRect(i * blockSize - camera.getX(), j * blockSize - camera.getY(), blockSize, blockSize);
					}
				}
			}

	}
	
	@Override
	public void tick() {
		// Player movement
		player.tick();

		// camera adjustment
		camera.tick(player.getX(), player.getY());
	}

}
