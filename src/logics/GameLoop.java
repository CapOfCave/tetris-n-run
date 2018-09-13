package logics;

/**
 * @author Lars Created on 12.08.2018
 * @see http://www.java-gaming.org/index.php?topic=24220.0
 */
public class GameLoop implements Runnable {

	private final double FREQUENCY = 30.;
	private final double TIME_BETWEEN_UPDATES = 1000000000 / FREQUENCY;
	private final int MAX_UPDATES_BEFORE_RENDER = 5;
	private final double MAX_FPS = 60;
	private final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / MAX_FPS;

	private Thread th;
	private Playable game;

	private boolean running;
	private boolean paused;

	@SuppressWarnings("unused")
	private int fps = 60;
	private int frameCount = 0;

	public GameLoop(Playable game) {
		this.game = game;
	}

	@Override
	public void run() {
		double lastUpdateTime = System.nanoTime();
		double lastRenderTime = System.nanoTime();

		int lastSecondTime = (int) (lastUpdateTime / 1000000000);

		while (running) {
			double currentTime = System.nanoTime();
			int updateCount = 0;

			if (!paused) {
				while (currentTime - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER) {
					tick();
					lastUpdateTime += TIME_BETWEEN_UPDATES;
					updateCount++;
				}
				if (currentTime - lastUpdateTime > TIME_BETWEEN_UPDATES) {
					lastUpdateTime = currentTime - TIME_BETWEEN_UPDATES;
				}
				float interpolation = Math.min(1.0f, (float) ((currentTime - lastUpdateTime) / TIME_BETWEEN_UPDATES));
				render(interpolation);
				lastRenderTime = currentTime;

				int currentTimeSeconds = (int) (lastUpdateTime / 1000000000);
				if (currentTimeSeconds > lastSecondTime) {
					// System.ot.println("NEW SECOND " + currentTimeSeconds + " " + frameCount);
					fps = frameCount;
					frameCount = 0;
					lastSecondTime = currentTimeSeconds;
				}

				while (currentTime - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS && currentTime - lastUpdateTime < TIME_BETWEEN_UPDATES) {
					Thread.yield();
					try {
						Thread.sleep(1);
					} catch (Exception e) {
					}

					currentTime = System.nanoTime();
				}
			}
		}
	}

	public void start() {
		if (!running) {
			System.out.println("starting...");
			running = true;
			th = new Thread(this);
			th.start();
		}
	}

	public void stop() {
		if (running) {
			System.out.println("Stopping...");
			running = false;
			try {
				th.join(1000);
				System.out.println("Stopped succesfully");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void tick() {
		game.tick();
	}

	private void render(float interpolation) {
		game.render(interpolation);
	}

	public void changePlayable(Playable game) {
		this.game = game;
	}

}
