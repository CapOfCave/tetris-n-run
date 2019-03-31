package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * @author Lars Created on 13.09.2018
 */
public class KeyHandler implements KeyListener {

	private ArrayList<Integer> keyCodes;
	private boolean upKey = false;
	private boolean leftKey = false;
	private boolean downKey = false;
	private boolean rightKey = false;
	private boolean rotateKey = false;
	private boolean removeKey = false;
	private boolean shift = false;
	private boolean kameraKey = false;
	private boolean killPlayer = false;
	
	private boolean f4Pressed = false;
	private boolean actionPressed = false;
	private boolean f3Pressed = false;

	public KeyHandler(ArrayList<Integer> keyCodes) {
		this.keyCodes = keyCodes;
	}

	@Override
	public void keyPressed(KeyEvent ev) {
		if (ev.getKeyCode() == keyCodes.get(0)) {
			upKey = true;
		}
		if (ev.getKeyCode() == keyCodes.get(1)) {
			leftKey = true;
		}
		if (ev.getKeyCode() == keyCodes.get(2)) {
			downKey = true;
		}
		if (ev.getKeyCode() ==keyCodes.get(3)) {
			rightKey = true;
		}
		if (ev.getKeyCode() == keyCodes.get(5)) {
			rotateKey = true;
		}
		if (ev.getKeyCode() == keyCodes.get(7)) {
			removeKey = true;
		}
		if (ev.getKeyCode() == KeyEvent.VK_SHIFT) {
			shift = true;
		}
		if (ev.getKeyCode() == keyCodes.get(6)) {
			kameraKey = true;
		}
		if (ev.getKeyCode() == KeyEvent.VK_F4) {
			f4Pressed = true;
		}
		if (ev.getKeyCode() == KeyEvent.VK_F3) {
			f3Pressed = true;
		}
		if (ev.getKeyCode() == keyCodes.get(4)) {
			actionPressed = true;
		}
		if (ev.getKeyCode() == keyCodes.get(8)) {
			System.out.println("kill");
			killPlayer = true;
		}

	}

	@Override
	public void keyReleased(KeyEvent ev) {
		if (ev.getKeyCode() == keyCodes.get(0)) {
			upKey = false;
		}
		if (ev.getKeyCode() == keyCodes.get(1)) {
			leftKey = false;
		}
		if (ev.getKeyCode() == keyCodes.get(2)) {
			downKey = false;
		}
		if (ev.getKeyCode() == keyCodes.get(3)) {
			rightKey = false;
		}
		if (ev.getKeyCode() == keyCodes.get(5)) {
			rotateKey = false;
		}
		if (ev.getKeyCode() == keyCodes.get(7)) {
			removeKey = false;
		}
		if (ev.getKeyCode() == KeyEvent.VK_SHIFT) {
			shift = false;
		}
		if (ev.getKeyCode() == keyCodes.get(6)) {
			kameraKey = false;
		}
		if (ev.getKeyCode() == keyCodes.get(8)) {
			killPlayer = false;
		}

	}

	public boolean getUpKey() {
		return upKey;
	}

	public boolean getLeftKey() {
		return leftKey;
	}

	public boolean getDownKey() {
		return downKey;
	}

	public boolean getRightKey() {
		return rightKey;
	}
	public boolean getRotateKey() {
		return rotateKey;
	}
	public void setRotateKey(boolean r) {
		this.rotateKey = r;
	}
	public boolean getRemoveKey() {
		return removeKey;
	}
	public void setRemoveKey(boolean e) {
		this.removeKey = e;
	}

	public boolean getShift() {
		return shift;
	}
	
	public boolean getKillPlayer() {
		return killPlayer;
	}
	
	public boolean getKameraKey() {
		return kameraKey;
	}

	public boolean isF4pressed() {
		return f4Pressed;
	}
	
	public boolean isF3pressed() {
		return f3Pressed;
	}

	public boolean isActionPressed() {
		return actionPressed;
	}

	public void setF3pressed(boolean b) {
		this.f3Pressed = b;
	}

	public void setActionpressed(boolean b) {
		this.actionPressed = b;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	public void setF4pressed(boolean b) {
		this.f4Pressed = b;
	}

	public void setKillPlayer(boolean b) {
		this.killPlayer = b;
		
	}
}
