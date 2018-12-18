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
	private boolean shift = false;
	private boolean ctrl = false;

	private boolean actionPressed = false;
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
		if (ev.getKeyCode() == KeyEvent.VK_SHIFT) {
			shift = true;
		}
		if (ev.getKeyCode() == KeyEvent.VK_CONTROL) {
			ctrl = true;
		}
		if (ev.getKeyCode() == KeyEvent.VK_F3) {
			f3pressed = true;
		}
		if (ev.getKeyCode() == KeyEvent.VK_SPACE) {
			actionPressed = true;
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
		if (ev.getKeyCode() == KeyEvent.VK_SHIFT) {
			shift = false;
		}
		if (ev.getKeyCode() == KeyEvent.VK_CONTROL) {
			ctrl = false;
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

	public boolean getShift() {
		return shift;
	}
	
	public boolean getCtrl() {
		return ctrl;
	}

	public boolean isF3pressed() {
		return f3pressed;
	}

	public boolean isActionPressed() {
		return actionPressed;
	}

	public void setF3pressed(boolean b) {
		this.f3pressed = b;
	}

	public void setActionpressed(boolean b) {
		this.actionPressed = b;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}
}
