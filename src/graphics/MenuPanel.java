package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JPanel;

import input.MenuMouseHandler;
import logics.Playable;

public class MenuPanel extends JPanel implements Playable {
	private static final long serialVersionUID = 1L;

	private MenuFrame frame;
	private MenuMouseHandler mouseHandler;
	private BufferedImage menu;

	public MenuPanel(MenuFrame frame) {
		
		this.frame = frame;
		mouseHandler = new MenuMouseHandler(frame, this);
		setPreferredSize(new Dimension(GameFrame.PANEL_WIDTH, GameFrame.PANEL_HEIGHT));
		addMouseListener(mouseHandler);
		setBackground(Color.WHITE);
		menu = frame.getImage("/res/Menu.png");

		repaint();

	}

	int loadingx = 20;
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (frame.isLoading()) {
			g.setFont(new Font(GameFrame.fontString, 1, 70));
			g.drawString("Loading. Screen fehlt noch. add pls. ",
					loadingx, 400);
			g.drawString("kann man sogar animieren.", loadingx + 50, 500);
		} else {
			g.drawImage(menu, 0, 0, null);
			g.setColor(Color.BLACK);
			g.setFont(new Font(GameFrame.fontString, 1, 130));
			g.drawString("Play", 490, 472);
			g.setFont(new Font(GameFrame.fontString, 1, 100));
			g.drawString("Tutorial", 135, 705);
			g.setFont(new Font(GameFrame.fontString, 1, 100));
			g.drawString("Settings", 745, 705);
		}
	}

	public void mousePressed(int x, int y) {

		repaint();

		if (!frame.isLoading()) {
			if (x > 35 && y > 332 && x < 1252 && y < 523) {
				frame.playSound("ButtonKlick", -5f);

				File akt_Overworld = new File(System.getenv("APPDATA") + "\\tetris-n-run\\saves\\overworldSave.txt");
				if (akt_Overworld.exists()) {
					frame.loadLevel(akt_Overworld.getPath());
				} else {
					frame.loadLevel("/res/levels/overworld" + frame.getLevelSolved() + ".txt");
				}
				
			}

			if (x > 35 && y > 571 && x < 644 && y < 764) {
				frame.playSound("ButtonKlick", -5f);
				frame.startTutorial();
			}
			if (x > 655 && y > 571 && x < 1254 && y < 764) {
				frame.playSound("ButtonKlick", -5f);
				frame.startOption();
			}
		}
	}

	@Override
	public void tick() {
		frame.checkIfLoading();
		if (frame.isLoading()) {
			loadingx = (loadingx + 3290) % 2300 - 1000;
		}
		
	}

	@Override
	public void render(float interpolation, int fps, int ups) {
		repaint();
	}

	@Override
	public void secondPassed() {
		
	}
}
