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
	private OverworldPanel oPanel;
	private Panel lPanel;
	private GameLoop gameLoop;
	private char nextLevel;
	private int levelSolved = 8;

	private KeyHandler keyHandler;

	public static void main(String[] args) {

		new Frame();
	}

	public Frame() {
		keyHandler = new KeyHandler();
		oPanel = new OverworldPanel(LevelLoader.loadLevel("/res/levels/overworld" + levelSolved + ".txt", this), keyHandler, this);
		setLayout(new CardLayout());
		add(oPanel);
		gameLoop = new GameLoop(oPanel);
		addKeyListener(keyHandler);
		setResizable(false);
		pack();
		setDefaultCloseOperation(3);
		setLocationRelativeTo(null);
		gameLoop.start();
		setVisible(true);

	}

	public void changeToOverworld() {
		oPanel = new OverworldPanel(LevelLoader.loadLevel("/res/levels/overworld" + levelSolved + ".txt", this), keyHandler, this);

		add(oPanel);
		remove(lPanel);
		gameLoop.changePlayable(oPanel);

	}

	public void startLevel() {
		if (Character.isLowerCase(nextLevel)) {
			lPanel = new Panel(LevelLoader.loadLevel("/res/levels/level" + nextLevel + ".txt ", this), keyHandler, this);

			add(lPanel);
			remove(oPanel);
			gameLoop.changePlayable(lPanel);
		}
	}

	public char getNextLevel() {
		return nextLevel;
	}

	public void setNextLevel(char nextLevel) {
		this.nextLevel = nextLevel;
	}

}
