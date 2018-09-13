package graphics;

import javax.swing.JFrame;

import input.KeyHandler;
import loading.LevelLoader;
import logics.GameLoop;

/**
 * @author Lars Created on 05.08.2018
 */
public class Frame extends JFrame {

	private static final long serialVersionUID = 1L;
	private Panel panel;
	
	private GameLoop gameLoop;
	
	private KeyHandler keyHandler;

	public static void main(String[] args) {

		

		System.out.println("test marius");
		
	

		new Frame("/res/level.txt");

	}

	//loads a Level
	public Frame(String url) {
		keyHandler = new KeyHandler();
		panel = new Panel(LevelLoader.loadLevel(url), keyHandler);
		add(panel);
		gameLoop = new GameLoop(panel);
		addKeyListener(keyHandler);
		setResizable(false);
		pack();
		setDefaultCloseOperation(3);
		setLocationRelativeTo(null);
		setVisible(true);
		gameLoop.start();
	}
	
	//loads the Gui
	public Frame() {
		//panel = new Panel();
		add(panel);
		setResizable(false);
		pack();
		setDefaultCloseOperation(3);
		setLocationRelativeTo(null);
		setVisible(true);
	}

//	public Frame() {
//		panel = new Panel();
//		add(panel);
//		gameLoop = new GameLoop(panel);
//		setResizable(false);
//		pack();
//		setDefaultCloseOperation(3);
//		setLocationRelativeTo(null);
//		setVisible(true);
//		gameLoop.start();
//	}

}
