package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JPanel;

import data.ConsoleLine;
import data.Level;
import data.TetroType;
import data.Tiles.SaveNLoadTile;
import input.KeyHandler;
import loading.FileHandler;
import logics.Playable;
import logics.World;

public abstract class Panel extends JPanel implements Playable {
	private static final long serialVersionUID = 1L;
	public static final Rectangle gamePanel = new Rectangle(35, 35, 921, 621);
	protected final Rectangle inventoryPanel = new Rectangle(180, 671, 879, 854);

	protected KeyHandler keyHandler;
	protected ArrayList<TetroType> tetroTypes;
	protected ArrayList<Point> tetroDrawPositions;
	protected float interpolation;
	protected int ups;
	protected int fps;
	protected GameFrameHandler frame;
	protected World world;
	protected boolean loading = false;
	int loadingScreenProgression = 0;

	public Panel(Level level, KeyHandler keyHandler, GameFrameHandler frame, ArrayList<TetroType> tetroTypes) {
		this.keyHandler = keyHandler;
		this.tetroTypes = tetroTypes;
		this.frame = frame;
		setPreferredSize(new Dimension(GameFrameHandler.PANEL_WIDTH, GameFrameHandler.PANEL_HEIGHT));

		tetroDrawPositions = new ArrayList<>();

		tetroDrawPositions.add(new Point(1070, 72));
		tetroDrawPositions.add(new Point(1025, 175));
		tetroDrawPositions.add(new Point(1145, 175));
		tetroDrawPositions.add(new Point(1025, 290));
		tetroDrawPositions.add(new Point(1145, 290));
		tetroDrawPositions.add(new Point(1025, 406));
		tetroDrawPositions.add(new Point(1165, 406));
	}

	@Override
	public void render(float interpolation, int fps, int ups) {
		this.interpolation = interpolation;
		this.fps = fps;
		this.ups = ups;
		repaint();
	}

	@Override
	public void tick() {
		frame.checkIfLoadingHasFinished();
		if (frame.isLoading()) {
			loadingScreenProgression = (loadingScreenProgression + 1) % 60;
			return;
		}
		world.tick();
		frame.updateConsole();
		if (keyHandler.isF5pressed()) {
			frame.addLineToText("Graphic Reload initiated.");
			world.resetRenderer();
			keyHandler.resetF5pressed();

		}

		if (keyHandler.isTipPressed()) {
			showTip();
		}

		if (keyHandler.isDeletePressed()) {
			deleteLastSALT();
		}

	}

	private void deleteLastSALT() {
		keyHandler.resetDeletePressed();
		Point deletedFile = FileHandler.deleteNewestSaveNLoadSave();
		if (deletedFile == null) {
			return;
		}
		world.makeSALTInvalid(deletedFile.y, deletedFile.x);
		Point newestFile = FileHandler.getNewestSaveNLoadSave();
		if (newestFile == null) {
			return;
		}
		world.setLastUsedSALTile(((SaveNLoadTile) world.getTileAt(newestFile.y, newestFile.x)));

	}

	private void showTip() {
		int show_duration = 1;
		keyHandler.resetTipPressed();
		if (world.getLastCrossedSALTile() != null && world.getLastCrossedSALTile().getTip() != null) {
			if (world.getLastCrossedSALTile().getTip2() != null) {
				show_duration = 2;
				if (world.getLastCrossedSALTile().getTip3() != null) {
					show_duration = 3;
					if (world.getLastCrossedSALTile().getTip4() != null) {
						show_duration = 4;
						frame.addLineToText(world.getLastCrossedSALTile().getTip4(), show_duration);
					}
					frame.addLineToText(world.getLastCrossedSALTile().getTip3(), show_duration);
				}
				frame.addLineToText(world.getLastCrossedSALTile().getTip2(), show_duration);
			}
			frame.addLineToText(world.getLastCrossedSALTile().getTip(), show_duration);
		}

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

	}

	protected void drawDebug(Graphics g) {

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 200, 60);
		g.setColor(Color.BLACK);
		g.setFont(new Font("", Font.PLAIN, 10));
		g.drawString("FPS: " + fps + " / Updates: " + ups, 15, 25);
		world.drawDebug(g, interpolation);

	}

	protected void drawConsole(Graphics g) {
		ConsoleLine[] text = frame.getText();

		if (text.length > 0) {
			for (int i = 0; i < text.length; i++) {
				if (text[i] != null) {
					g.setFont(new Font(GameFrameHandler.FONTSTRING, Font.PLAIN, text[i].getFontSize())); // 18 / 20
					g.setColor(new Color(0, 0, 0, text[i].getOpacity()));
					g.drawString(text[i].getContent(), 185,
							705 + (i * GameFrameHandler.CONSOLETEXTMARGINY) - text[i].getOffset());
				}
			}
		}
	}

}
