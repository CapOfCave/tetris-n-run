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

	private boolean epressed = false;
	private boolean f3pressed = false;
	
	public KeyHandler() {
	}
	
	@Override
	public void keyPressed(KeyEvent ev) {
		if (ev.getKeyCode() == KeyEvent.VK_W) {
			w = true;
		}
		if (ev.getKeyCode() == KeyEvent.VK_A) {
			a = true;
		}
		if (ev.getKeyCode() == KeyEvent.VK_S) {
			s = true;
		}
		if (ev.getKeyCode() == KeyEvent.VK_D) {
			d = true;
		}
		if (ev.getKeyCode() == KeyEvent.VK_F3) {
			f3pressed = true;
		}
		if (ev.getKeyCode() == KeyEvent.VK_E) {
			epressed = true;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent ev) {
		if (ev.getKeyCode() == KeyEvent.VK_W) {
			w = false;
		}
		if (ev.getKeyCode() == KeyEvent.VK_A) {
			a = false;
		}
		if (ev.getKeyCode() == KeyEvent.VK_S) {
			s = false;
		}
		if (ev.getKeyCode() == KeyEvent.VK_D) {
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
	
	public boolean isF3pressed() {
		return f3pressed;
	}
	public boolean isEpressed() {
		return epressed;
	}
	@Override
	public void keyTyped(KeyEvent e) {

	}

	public void setF3pressed(boolean b) {
		this.f3pressed = b;
	}
	public void setEpressed(boolean b) {
		this.epressed = b;
	}
	
}
