package logics.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import data.Tiles.EmptyTile;
import data.Tiles.Tile;
import graphics.GameFrame;
import logics.World;

public class MovingBlock extends Entity {

	private static final long serialVersionUID = 1L;
	private static final String animPath = "/res/anims/movingblock.txt";
	protected double lastX, lastY;
	protected boolean sticky = false;
	protected Point offset = new Point(0, 0);
	protected int direction;
	private Tile standingTile;
	private static Tile emptyTile = new EmptyTile('0', 0, 0);

	public MovingBlock(World world, double x, double y) {
		super(world, x, y, animPath, new Rectangle(0, 0, GameFrame.BLOCKSIZE, GameFrame.BLOCKSIZE));
		lastX = x;
		lastY = y;

		type = "moveblock";
		if (world != null) {
			setWorld(world);
		}
		
		setPosition(x, y);

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
//		if (sticky) {
//			g.setColor(Color.GREEN);
//		} else {
//			g.setColor(Color.RED);
//		}
//
//		g.drawRect(interpolX - world.cameraX(), interpolY - world.cameraY(), Frame.BLOCKSIZE, Frame.BLOCKSIZE);
		g.fillOval(interpolX - world.cameraX(), interpolY - world.cameraY(), 3, 3);
		g.setColor(Color.RED);
		g.fillRect((int) (x + relCollisionsRect.x - world.cameraX()), (int) (y + relCollisionsRect.y - world.cameraY()),
				(int) (relCollisionsRect.width), (int) relCollisionsRect.height);

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

		Tile last_Tile = standingTile; // vor der Bewegung: standingTile
		Tile akt_Tile = world.getTileAt(getTileY(), getTileX()); // nach der Bewegung: akt_Tile
		if (standingTile == null || akt_Tile != standingTile) {
			setStandingTile(akt_Tile); // Bewegen und entern
			if (last_Tile != null && last_Tile != emptyTile) {
				last_Tile.eventWhenMoveBlockLeaving();
			}
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
		if (standingTile != null) {
			standingTile.eventWhenMoveBlockLeaving();
		}
		unBind();
		world.initiateRemoveEntity(this);

	}

	public void setCurrentTile(Tile currentTile) {
		standingTile = currentTile;

	}

}
