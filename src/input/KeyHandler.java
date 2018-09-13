package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author Lars Created on 13.09.2018
 */
public class KeyHandler implements KeyListener {

	private boolean w = false;
	private boolean a = false;
	private boolean s = false;
	private boolean d = false;

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W) {
			w = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_A) {
			a = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_S) {
			s = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_D) {
			d = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W) {
			w = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_A) {
			a = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_S) {
			s = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_D) {
			d = false;
		}
	}
	
	public boolean getW() {
		return w;
	}

	public boolean getA() {
		return a;
	}

	public boolean getS() {
		return s;
	}

	public boolean getD() {
		return d;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

}
