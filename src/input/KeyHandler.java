package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import graphics.GameFrameHandler;

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
	private boolean deleteCheckpoint = false;

	private boolean actionPressed = false;
	private boolean f5Pressed = false;
	private ArrayList<Character> lastPressed;
	private final String noClipCheat = "NOCLIPMODE";
	private final String debugModeCheat = "DEBUGMODE";
	private final String simpleCheatsCheat = "SIMPLECHEATS";
	private boolean altPressed = false;

	private boolean noClipMode = false;
	private boolean debugMode = false;
	private boolean simpleCheatMode = false;
	private GameFrameHandler gameFrameHandler;

	public KeyHandler(ArrayList<Integer> keyCodes, GameFrameHandler gameFrameHandler) {
		this.keyCodes = keyCodes;
		lastPressed = new ArrayList<>();
		this.gameFrameHandler = gameFrameHandler;
	}

	@Override
	public void keyPressed(KeyEvent ev) {
		if (ev.getKeyCode() == KeyEvent.VK_ALT) {
			altPressed = true;
		}
		if (altPressed && !keyCodes.contains(KeyEvent.VK_ALT)) {
			lastPressed.add(KeyEvent.getKeyText(ev.getKeyCode()).charAt(0));
			if (lastPressed.size() > 12) {
				lastPressed.remove(0);
			}
			if (checkCheat(simpleCheatsCheat)) {
				simpleCheatMode = !simpleCheatMode;
				gameFrameHandler.addLineToText("simpleCheatMode " + (simpleCheatMode ? "enabled" : "disabled"));
				lastPressed.clear();
			}

			if (checkCheat(noClipCheat)) {
				noClipMode = !noClipMode;
				gameFrameHandler.addLineToText("noClip " + (noClipMode ? "enabled" : "disabled"));
				lastPressed.clear();
			}
			if (checkCheat(debugModeCheat)) {
				debugMode = !debugMode;
				gameFrameHandler.addLineToText("debugmode " + (debugMode ? "enabled" : "disabled"));
				lastPressed.clear();
			}
			return;
		}
		if (ev.getKeyCode() == keyCodes.get(0)) {
			upKey = true;
		}
		if (ev.getKeyCode() == keyCodes.get(1)) {
			leftKey = true;
		}
		if (ev.getKeyCode() == keyCodes.get(2)) {
			downKey = true;
		}
		if (ev.getKeyCode() == keyCodes.get(3)) {
			rightKey = true;
		}
		if (ev.getKeyCode() == keyCodes.get(5)) {
			rotateKey = true;
		}
		if (ev.getKeyCode() == keyCodes.get(6)) {
			removeKey = true;
		}

		if (ev.getKeyCode() == keyCodes.get(7)) {
			kameraKey = true;
		}
		if (ev.getKeyCode() == KeyEvent.VK_F5) {
			f5Pressed = true;
		}
		if (ev.getKeyCode() == KeyEvent.VK_F4) {
			if (simpleCheatMode) {
				noClipMode = !noClipMode;
				gameFrameHandler.addLineToText("noClip " + (noClipMode ? "enabled" : "disabled"));
			} else if (noClipMode) {
				noClipMode = false;
				gameFrameHandler.addLineToText("noClip disabled");
			}
		}
		if (ev.getKeyCode() == KeyEvent.VK_F3) {
			if (simpleCheatMode) {
				debugMode = !debugMode;
				gameFrameHandler.addLineToText("debugmode " + (debugMode ? "enabled" : "disabled"));
			} else if (debugMode) {
				debugMode = false;
				gameFrameHandler.addLineToText("debugmode disabled");
			}
		}
		if (ev.getKeyCode() == keyCodes.get(4)) {
			actionPressed = true;
		}
		if (ev.getKeyCode() == keyCodes.get(8)) {
			killPlayer = true;
		}
		if (ev.getKeyCode() == keyCodes.get(9)) {
			shift = true;
		}
		if (ev.getKeyCode() == keyCodes.get(10)) {
			deleteCheckpoint = true;
		}

	}

	private boolean checkCheat(String cheatcode) {

		if (lastPressed.size() < cheatcode.length()) {
			return false;
		}

		for (int i = 0; i < cheatcode.length(); i++) {
			if (!lastPressed.get(lastPressed.size() - i - 1).equals(cheatcode.charAt(cheatcode.length() - i - 1))) {
				return false;
			}
		}

		return true;

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
		if (ev.getKeyCode() == keyCodes.get(6)) {
			removeKey = false;
		}

		if (ev.getKeyCode() == keyCodes.get(7)) {
			kameraKey = false;
		}
		if (ev.getKeyCode() == keyCodes.get(8)) {
			killPlayer = false;
		}
		if (ev.getKeyCode() == keyCodes.get(9)) {
			shift = false;
		}
		if (ev.getKeyCode() == keyCodes.get(10)) {
			deleteCheckpoint = false;
		}
		if (ev.getKeyCode() == KeyEvent.VK_ALT) {
			altPressed = false;
			lastPressed.clear();
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

	public boolean isF5pressed() {
		return f5Pressed;
	}

	public boolean isActionPressed() {
		return actionPressed;
	}

	public boolean isDeletePressed() {
		return deleteCheckpoint;
	}

	public void resetDeletePressed() {
		deleteCheckpoint = false;
	}

	public void resetF5pressed() {
		this.f5Pressed = false;
	}

	public void resetActionpressed() {
		this.actionPressed = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	public void resetKillPlayer() {
		killPlayer = false;

	}

	public boolean inNoclipMode() {
		return noClipMode;
	}

	public boolean inDebugMode() {
		return debugMode;
	}

	public void resetKeyboardInputs() {
		upKey = false;
		leftKey = false;
		downKey = false;
		rightKey = false;
		rotateKey = false;
		removeKey = false;
		shift = false;
		kameraKey = false;
		killPlayer = false;
		actionPressed = false;
		f5Pressed = false;
		altPressed = false;
		removeKey = false;

	}

}
