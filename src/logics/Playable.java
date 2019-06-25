package logics;

/**
 * @author Lars
 * Created on 12.08.2018
 */
public interface Playable {
	public void tick();
	public void render(float interpolation, int fps, int ups);
	public void secondPassed();
}
