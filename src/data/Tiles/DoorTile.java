package data.Tiles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import data.Animation;
import data.DrawAndSortable;
import graphics.Frame;
import graphics.Renderer;
import loading.AnimationLoader;
import tools.Tools;

public class DoorTile extends Tile {

	private int color = -1;
	private HashMap<String, Animation> pictures;
	private int rotation;
	Color drawColor = Color.BLACK;
	private BufferedImage image3d;

	private String str_akt_anim;
	private DrawAndSortable bottomPart;
	private boolean standardOpened;

	public DoorTile(int color, int x, int y, int rotation, boolean open, Frame frame) {
		super('D', x, y, open, open, true, frame);
		this.rotation = rotation;
		this.color = color;
		this.standardOpened = open;
		pictures = AnimationLoader.loadAnimations("/res/anims/door.txt");

		if (color == 0) {
			drawColor = Color.RED;
		} else if (color == 1) {
			drawColor = Color.GREEN;
		} else if (color == 2) {
			drawColor = Color.BLUE;
		} else if (color == 3) {
			drawColor = Color.YELLOW;
		}
		str_akt_anim = (open ? "opened" : "closed") + rotation;
		image3d = Tools.setColor(pictures.get(str_akt_anim).getImage(), drawColor);

		BufferedImage bottomImage = Tools.setColor(pictures.get("bottom_image").getImage(), drawColor);
		bottomPart = new DrawAndSortable() {

			public double getHeight() {
				return DoorTile.this.getHeight() + 1;
			}

			@Override
			public void draw(Graphics g, float interpolation) {

				if (walkable) {
					g.drawImage(bottomImage,
							(int) (posX * Frame.BLOCKSIZE - world.cameraX()
									+ pictures.get("bottom_image").getOffsetX()),
							(int) ((posY) * Frame.BLOCKSIZE - world.cameraY()
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
		};
	}

	public DoorTile() {
		super('D', -1, -1, false, false, true, null);
	}

	public void changeState() {
		walkable = !walkable;
		walkableWithTetro = !walkableWithTetro;

		str_akt_anim = (walkable ? "opened" : "closed") + rotation;
		image3d = Tools.setColor(pictures.get(str_akt_anim).getImage(), drawColor);

	}

	public int getColor() {
		return color;
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
		g.drawImage(image3d, (int) (posX * Frame.BLOCKSIZE - world.cameraX() + pictures.get(str_akt_anim).getOffsetX()),
				(int) (posY * Frame.BLOCKSIZE - world.cameraY() + pictures.get(str_akt_anim).getOffsetY()), null);
	}

	@Override
	public void addTo(Renderer renderer) {

		renderer.addDrawable(this);
		// bottom part
		if (rotation % 2 == 1) {
			renderer.addDrawable(bottomPart);
		}
	}

	public boolean isToggled() {
		return standardOpened;
	}
}
