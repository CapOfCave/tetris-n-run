package graphics;

import javax.swing.JFrame;

import graphics.Panel;
import loading.LevelLoader;
import logics.GameLoop;

/**
 * @author Lars Created on 05.08.2018
 */
public class Frame extends JFrame {

	private static final long serialVersionUID = 1L;
	private Panel panel;
	private GameLoop gameLoop;

	public static void main(String[] args) {
		System.out.println("Wer das liest hat was geändert bekommen, zum zweiten mal");
		new Frame();
	}
	public Frame(String url) {
		panel = new Panel(LevelLoader.loadLevel(url));
		add(panel);
		gameLoop = new GameLoop(panel);
		setResizable(false);
		pack();
		setDefaultCloseOperation(3);
		setLocationRelativeTo(null);
		setVisible(true);
		gameLoop.start();
	}
	
	public Frame() {
		panel = new Panel();
		add(panel);
		gameLoop = new GameLoop(panel);
		setResizable(false);
		pack();
		setDefaultCloseOperation(3);
		setLocationRelativeTo(null);
		setVisible(true);
		gameLoop.start();
	}

}
