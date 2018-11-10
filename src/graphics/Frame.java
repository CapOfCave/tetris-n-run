package graphics;

import java.awt.CardLayout;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;

import data.RawPlayer;
import input.KeyHandler;
import loading.LevelLoader;
import loading.RawPlayerLoader;
import loading.RawPlayerSaver;
import logics.GameLoop;
import logics.Inventory;
import logics.worlds.World;

/**
 * @author Lars Created on 05.08.2018
 * 
 *         Der Rahmen, der alles hält.
 */
public class Frame extends JFrame {

	private static final long serialVersionUID = 1L;
	private OverworldPanel oPanel;
	private GameWorldPanel lPanel;
	private RawPlayer rawPlayer;
	private GameLoop gameLoop;
	private char nextLevel;
	private int levelSolved = 3;
	private boolean inOverworld = true;
	public static final int BLOCKSIZE = 45;

	private KeyHandler keyHandler;

	public static void main(String[] args) {

		new Frame();
	}

	public Frame() {
		keyHandler = new KeyHandler();
		
		
		rawPlayer = RawPlayerLoader.readRawPlayer("C:\\JavaEclipse\\Player.txt");
		rawPlayer.init();
		
		
		oPanel = new OverworldPanel(LevelLoader.loadLevel("/res/levels/overworld" + levelSolved + ".txt", this, rawPlayer),
				keyHandler, this, rawPlayer);
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

	public void changeToOverworld(boolean died, RawPlayer rawPlayer) {
		
		
		RawPlayerSaver.writePlayer("C:\\JavaEclipse\\Player.txt", rawPlayer);
		
		if(!died) {
			File file = new File("C:\\Users\\Marius\\AppData\\Roaming\\tetris-n-run\\levelSaves\\saveNLodeTile.txt");
			file.delete();
		}
		
		if (((int) nextLevel - 96) > levelSolved && !died)
			levelSolved = ((int) nextLevel - 96);

		oPanel = new OverworldPanel(LevelLoader.loadLevel("/res/levels/overworld" + levelSolved + ".txt", this, rawPlayer),
				keyHandler, this, rawPlayer);

		add(oPanel);
		remove(lPanel);
		gameLoop.changePlayable(oPanel);
		inOverworld = true;

	}

	public void startLevel() {
		if (Character.isLowerCase(nextLevel)) {
			File file = new File("C:\\Users\\Marius\\AppData\\Roaming\\tetris-n-run\\levelSaves\\saveNLodeTile.txt");
			file.delete();
			
			lPanel = new GameWorldPanel(LevelLoader.loadLevel("/res/levels/level" + nextLevel + ".txt", this, rawPlayer),
					keyHandler, this, rawPlayer);

			add(lPanel);
			remove(oPanel);
			gameLoop.changePlayable(lPanel);
		}
	}
	
	public void swithLevel(String path) {
		
			GameWorldPanel tempPanel = lPanel;
			
			lPanel = new GameWorldPanel(LevelLoader.loadLevel(path, this, rawPlayer),
					keyHandler, this, rawPlayer);
			
			

			add(lPanel);
			remove(tempPanel);
			gameLoop.changePlayable(lPanel);
			
		
	}

	public char getNextLevel() {
		return nextLevel;
	}

	public void setNextLevel(char nextLevel) {
		this.nextLevel = nextLevel;
	}

}
