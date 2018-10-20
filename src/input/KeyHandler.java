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
	
	private boolean f3pressed = false;
	
	private boolean[] tetroHotkeyPressed;
	private int[] tetroHotkeys = {KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4, KeyEvent.VK_5, KeyEvent.VK_6, KeyEvent.VK_7, };

	public KeyHandler() {
		tetroHotkeyPressed = new boolean[tetroHotkeys.length];
		for(int i = 0; i < tetroHotkeyPressed.length; i++) {
			tetroHotkeyPressed[i] = false;
		}
	}
	
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
		if (e.getKeyCode() == KeyEvent.VK_F3) {
			f3pressed = true;
		}
		for(int i = 0; i < tetroHotkeys.length; i++) {
			if (e.getKeyCode() == tetroHotkeys[i]) {
				tetroHotkeyPressed[i] = true;
			}
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
		
		for(int i = 0; i < tetroHotkeys.length; i++) {
			if (e.getKeyCode() == tetroHotkeys[i]) {
				tetroHotkeyPressed[i] = false;
			}
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
	
	public boolean isHotkeyPressed(int index) {
		return tetroHotkeyPressed[index];
	}
	
	public void setHotkeyPressed(int index, boolean b) {
		tetroHotkeyPressed[index] = b;
	}

	public boolean isF3pressed() {
		return f3pressed;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {

	}

	public void setF3pressed(boolean b) {
		this.f3pressed = b;
	}

	
}
