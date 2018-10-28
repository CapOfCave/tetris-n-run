package logics.entities.items;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import graphics.Frame;
import loading.AnimationLoader;
import logics.entities.Entity;
import logics.worlds.World;

public class Item extends Entity {

	private static final long serialVersionUID = 1L;

	protected String imgPath;

	private String typeUrl;

	public Item(World world, String imgPath) {
		super(world, AnimationLoader.loadAnimations(imgPath));
		this.imgPath = imgPath;
		this.init();
	}

	public void init() {
		anims = AnimationLoader.loadAnimations(imgPath);
	}

	public void drawPreview(Graphics2D g, int position, int size) {

		g.drawRect(20 + (32 * position + 5 * position), 20, size, size);

		akt_animation = anims.get("preview");
		g.drawImage(akt_animation.getImage(), 20 + (32 * position + 5 * position) + akt_animation.getOffsetX(),
				20 + akt_animation.getOffsetY(), size, size, null);
		akt_animation.next();
	}

	public void onClickInInventoryEnvent() {
		System.out.println("Cliccc " + this);

	}

	public void collectingEvent() {
		System.out.println("Collecting " + this);

	}

	@Override
	public void draw(Graphics g, float interpolation) {
		g.drawImage(akt_animation.getImage(),
				(int) (x * Frame.BLOCKSIZE - world.cameraX() + akt_animation.getOffsetX()),
				(int) (y * Frame.BLOCKSIZE - world.cameraY() + akt_animation.getOffsetY()), Frame.BLOCKSIZE,
				Frame.BLOCKSIZE, null);
	}

	public void drawDebug(Graphics g, float interpolation) {
		g.setColor(Color.GREEN);
		g.fillRect((int) (x * Frame.BLOCKSIZE - world.cameraX() + akt_animation.getOffsetX()),
				(int) (y * Frame.BLOCKSIZE - world.cameraY() + akt_animation.getOffsetY()), Frame.BLOCKSIZE,
				Frame.BLOCKSIZE);
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
