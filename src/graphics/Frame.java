package graphics;

import javax.swing.JFrame;

import graphics.Panel;
import loading.LevelLoader;
import logics.GameLoop;

/**
 * @author Lars Created on 05.08.2018
 * 
 * Der Rahmen, der alles hält.
 */
public class Frame extends JFrame {

	private static final long serialVersionUID = 1L;
	private Panel panel;
	private GameLoop gameLoop;

	public static void main(String[] args) {
		new Frame("/res/level.txt");
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
}
