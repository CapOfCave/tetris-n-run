package logics.entities.items;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import graphics.GameFrame;
import loading.AnimationLoader;
import logics.entities.Entity;
import logics.worlds.World;

public class Item extends Entity {

	private static final long serialVersionUID = 1L;

	protected String imgPath;

	private String typeUrl;

	public Item(World world, String imgPath) {
		super(world, imgPath, null);
		this.imgPath = imgPath;
		this.init();
	}

	public void init() {
		
		anims = AnimationLoader.loadAnimations(imgPath);
	}

	public void drawPreview(Graphics2D g, int position, int size) {

		akt_animation = anims.get("preview");
		g.drawImage(akt_animation.getImage(), 105 + (125 * position + 10 * position) + akt_animation.getOffsetX(),
				30 + akt_animation.getOffsetY(), size, size, null);
		akt_animation.next();
	}

	public void onClickInInventoryEnvent() {
		System.out.println("Cliccc " + imgPath);
	}

	public void collectingEvent() {
		System.out.println("Collecting " + this);

	}

	@Override
	public void draw(Graphics g, float interpolation) {
		g.drawImage(akt_animation.getImage(),
				(int) (x * GameFrame.BLOCKSIZE - world.cameraX() + akt_animation.getOffsetX()),
				(int) (y * GameFrame.BLOCKSIZE - world.cameraY() + akt_animation.getOffsetY()), GameFrame.BLOCKSIZE,
				GameFrame.BLOCKSIZE, null);
	}

	public void drawDebug(Graphics g, float interpolation) {
		g.setColor(Color.GREEN);
		g.fillRect((int) (x * GameFrame.BLOCKSIZE - world.cameraX() + akt_animation.getOffsetX()),
				(int) (y * GameFrame.BLOCKSIZE - world.cameraY() + akt_animation.getOffsetY()), GameFrame.BLOCKSIZE,
				GameFrame.BLOCKSIZE);
	}

	@Override
	public void tick() {
		akt_animation.next();
	}

	public String getPath() {
		return typeUrl;
	}

	public void setTypeUrl(String typeUrl) {
		this.typeUrl = typeUrl;

	}

}
