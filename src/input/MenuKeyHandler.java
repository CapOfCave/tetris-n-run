package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import graphics.OptionPanel;

public class MenuKeyHandler implements KeyListener {
	
	private OptionPanel oPanel;
	public MenuKeyHandler(OptionPanel oPanel) {
		this.oPanel = oPanel;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		oPanel.changeKeyCode(e.getKeyCode());
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
