package logics;

/**
 * @author Lars Created on 05.08.2018
 */
public class Camera {
	private int x, y;
	private int lastX, lastY;
	private int drawX, drawY;
	private Player player;
	private int maxY;
	private int maxX;

	private double stickyness = .3; // höher -> spieler näher an der Mitte
	private int centerX;
	private int centerY;

	public Camera(int maxY, int maxX, int centerX, int centerY) {
		this.maxX = maxX;
		this.maxY = maxY;
		this.centerX = centerX;
		this.centerY = centerY;

	}

	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}

	public void setCenterY(int centerY) {
		this.centerY = centerY;
	}

	public int getX() {
		return drawX;
	}

	public int getY() {
		return drawY;
	}

	public void tick() {
		lastX = x;
		lastY = y;

		if (player != null) {
			// y = (int) Math.max(0, Math.min(maxY, player.getRealY() - 300));
			// x = (int) Math.max(0, Math.min(maxX, player.getRealX() - 300));
//			System.out.println(maxX + " und " + maxY + " bin bei " + ((player.getRealY() - centerY) * stickyness + lastY * (1 - stickyness)));
			y = (int) Math.max(0, Math.min(maxY, (player.getRealY() - centerY) * stickyness + lastY * (1 - stickyness)));
			x = (int) Math.max(0, Math.min(maxX, (player.getRealX() - centerX) * stickyness + lastX * (1 - stickyness)));
//			
			
			
			
		}
	}

	public void prepareDraw(float interpolation) {
		drawX = (int) ((x - lastX) * interpolation + lastX);
		drawY = (int) ((y - lastY) * interpolation + lastY);
	}

	public void setPlayer(Player player) {
		this.player = player;

	}

}
