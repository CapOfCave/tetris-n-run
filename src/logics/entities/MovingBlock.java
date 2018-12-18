package logics.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import data.Tiles.EmptyTile;
import data.Tiles.Tile;
import graphics.Frame;
import logics.worlds.World;

public class MovingBlock extends Entity {

	private static final long serialVersionUID = 1L;
	protected double lastX, lastY;
	protected boolean sticky = false;
	protected Point offset = new Point(0, 0);
	protected int direction;
	private Tile standingTile;
	private static Tile emptyTile = new EmptyTile('0', 0, 0, null);

	public MovingBlock(World world, double x, double y, String animPath) {
		super(world, x, y, animPath);
		lastX = x;
		lastY = y;
		type = "moveblock";
	}

	@Override
	public void draw(Graphics g, float interpolation) {
		int interpolX = (int) ((x - lastX) * interpolation + lastX);
		int interpolY = (int) ((y - lastY) * interpolation + lastY);

		g.drawImage(akt_animation.getImage(), interpolX - world.cameraX() + akt_animation.getOffsetX(),
				interpolY - world.cameraY() + akt_animation.getOffsetY(), null);
	}

	@Override
	public void drawDebug(Graphics g, float interpolation) {
		int interpolX = (int) ((x - lastX) * interpolation + lastX);
		int interpolY = (int) ((y - lastY) * interpolation + lastY);
		if (sticky) {
			g.setColor(Color.GREEN);
		} else {
			g.setColor(Color.RED);
		}

		g.drawRect(interpolX - world.cameraX(), interpolY - world.cameraY(), Frame.BLOCKSIZE, Frame.BLOCKSIZE);
		g.fillOval(interpolX - world.cameraX(), interpolY - world.cameraY(), 3, 3);
	}

	@Override
	public void tick() {
		if (!sticky) {
			lastX = this.x;
			lastY = this.y;
		}
		akt_animation.next();

	}

	public void interact() {
		world.getPlayer().setMovingBlock(this);
		offset = world.getPlayer().getMovingBlockOffset();
		sticky = true;
		direction = world.getPlayer().getDirection();
	}

	public void setPosition(double x, double y) {
		lastX = this.x;
		lastY = this.y;
		this.x = x + offset.x;
		this.y = y + offset.y;
		Tile akt_Tile = world.getTileAt((int) ((this.y + Frame.BLOCKSIZE / 2) / Frame.BLOCKSIZE),
				(int) ((this.x + Frame.BLOCKSIZE / 2) / Frame.BLOCKSIZE));
		if (standingTile == null) {
			System.out.println("new standingtile");
			setStandingTile(akt_Tile); // Bewegen und entern

		} else if (akt_Tile != standingTile) { // Neuer Block
			standingTile.eventWhenMoveBlockLeaving();
			setStandingTile(akt_Tile); // "Bewegen" & Entern

		}

	}

	private void setStandingTile(Tile akt_Tile) {
		if (akt_Tile == null) { // Air
			standingTile = emptyTile;
		} else { // akt_tile != null, besonderer Block
			standingTile = akt_Tile;
			akt_Tile.eventWhenMoveBlockEntering();
		}
	}

	public void unBind() {
		sticky = false;
	}

	public int getDirection() {
		return direction;
	}

	public void kill() {
		standingTile.eventWhenMoveBlockLeaving();
		unBind();
		world.removeEntity(this);

	}

	public void setCurrentTile(Tile currentTile) {
		standingTile = currentTile;

	}

}
