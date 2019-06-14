package data.Tiles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import data.Animation;
import data.DrawAndSortable;
import graphics.GameFrame;
import graphics.Renderer;
import loading.AnimationLoader;
import tools.GraphicalTools;

public class DoorTile extends Tile {

	private int color = -1;
	private HashMap<String, Animation> pictures;
	private int rotation;
	private Color drawColor = Color.BLACK;
	private BufferedImage image3d;

	private String str_akt_anim;
	private DrawAndSortable bottomPart;
	private boolean standardOpened;
	private boolean playerOnTile = false;
	private int movingBlocksOnTile = 0;

	private int changesSinceOccupied = 0;

	public DoorTile(int color, int x, int y, int rotation, boolean open, GameFrame frame) {
		super('D', x, y, false, open, true, frame);
		needsBackGround = true;
		this.rotation = rotation;
		this.color = color;
		this.standardOpened = open;
		pictures = AnimationLoader.loadAnimations("/res/anims/door.txt");

		if (color == 0) {
			drawColor = new Color(209, 17, 65);
		} else if (color == 1) {
			drawColor = new Color(0, 177, 89);
		} else if (color == 2) {
			drawColor = new Color(0, 174, 219);
		} else if (color == 3) {
			drawColor = new Color(255, 196, 37);
		}else if (color == 4) {
			drawColor = new Color(243, 119, 53);
		}else if (color == 5) {
			drawColor = new Color(210, 114, 255);
		}
		str_akt_anim = (open ? "opened" : "closed") + rotation;
		image3d = GraphicalTools.setColor(pictures.get(str_akt_anim).getImage(), drawColor);

		BufferedImage bottomImage = GraphicalTools.setColor(pictures.get("bottom_image").getImage(), drawColor);
		bottomPart = new DrawAndSortable() {

			public double getHeight() {
				return DoorTile.this.getHeight() + 1;
			}

			@Override
			public void draw(Graphics g, float interpolation) {

				if (walkable) {
					g.drawImage(bottomImage,
							(int) (posX * GameFrame.BLOCKSIZE - world.cameraX()
									+ pictures.get("bottom_image").getOffsetX()),
							(int) ((posY) * GameFrame.BLOCKSIZE - world.cameraY()
									+ pictures.get("bottom_image").getOffsetY()),
							null);
				}
			}

			@Override
			public int compareTo(DrawAndSortable o) {
				return DoorTile.this.compareTo(o);
			}

			@Override
			public void addTo(Renderer renderer) {
				DoorTile.this.addTo(renderer);

			}

			@Override
			public double getX() {
				return DoorTile.this.getX();
			}

			@Override
			public double getY() {
				return DoorTile.this.getY();
			}
		};
	}

	public DoorTile() {
		super('D', -1, -1, false, false, true, null);
	}

	public void changeState() {
		if (playerOnTile || movingBlocksOnTile > 0) {
			changesSinceOccupied++;
		} else {
			walkableWithTetro = !walkableWithTetro;

			str_akt_anim = (walkableWithTetro ? "opened" : "closed") + rotation;
			image3d = GraphicalTools.setColor(pictures.get(str_akt_anim).getImage(), drawColor);
		}

	}

	public int getColorAsInt() {
		return color;
	}
	public Color getColor() {
		return drawColor;
	}

	@Override
	public String toString() {
		return "DoorTile(x=" + posX + ";y=" + posY + ";open=" + walkable + ";color=" + color;
	}

	public int getRotation() {
		return rotation;
	}

	@Override
	public void draw(Graphics g, float interpolation) {
		g.drawImage(image3d, (int) (posX * GameFrame.BLOCKSIZE - world.cameraX() + pictures.get(str_akt_anim).getOffsetX()),
				(int) (posY * GameFrame.BLOCKSIZE - world.cameraY() + pictures.get(str_akt_anim).getOffsetY()), null);
	}

	@Override
	public void addTo(Renderer renderer) {

		renderer.addDrawable(this);
		// bottom part
		if (rotation % 2 == 1) {
			renderer.addDrawable(bottomPart);
		}
	}

	public boolean isStandardOpened() {
		return standardOpened;
	}

	@Override
	public void eventWhenEntering() {
		playerOnTile = true;
	}

	@Override
	public void eventWhenLeaving() {
		playerOnTile = false;
		leave();

	}

	private void leave() {
		if (!playerOnTile && movingBlocksOnTile == 0) {
			if (changesSinceOccupied % 2 == 1) {
				changeState();
			}
			changesSinceOccupied = 0;
		}
	}

	@Override
	public void eventWhenMoveBlockEntering() {
		movingBlocksOnTile++;
	}

	@Override
	public void eventWhenMoveBlockLeaving() {
		movingBlocksOnTile--;
		leave();
	}

	public boolean isOpen() {
		return walkableWithTetro;
	}
	
}
