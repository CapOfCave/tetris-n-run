package graphics;

import java.awt.CardLayout;

import javax.swing.JFrame;

import input.KeyHandler;
import loading.LevelLoader;
import logics.GameLoop;

/**
 * @author Lars Created on 05.08.2018
 * 
 *         Der Rahmen, der alles hält.
 */
public class Frame extends JFrame {

	private static final long serialVersionUID = 1L;
	private Panel panel;
	private GameLoop gameLoop;

	private KeyHandler keyHandler;

	public static void main(String[] args) {
		new Frame("/res/level.txt");

	}

	public Frame(String url) {
		keyHandler = new KeyHandler();
		panel = new Panel(LevelLoader.loadLevel(url), keyHandler);
		setLayout(new CardLayout());
		add(panel);
		gameLoop = new GameLoop(panel);
		addKeyListener(keyHandler);
		setResizable(false);
		pack();
		setDefaultCloseOperation(3);
		setLocationRelativeTo(null);
		gameLoop.start();
		setVisible(true);
		
		changePanel(url);
	}

	public void changePanel(String url) {
		OverworldPanel oPanel = new OverworldPanel(LevelLoader.loadLevel(url), keyHandler);

		add(oPanel);
		remove(panel);
		gameLoop.changePlayable(oPanel);

	}
}
