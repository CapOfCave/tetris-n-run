package logics.entities.items;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

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
		//TODO empty method body
	}

	public void drawPreview(Graphics2D g, int position, int size) {

		g.drawRect(20 + (32 * position + 5 * position), 20, size, size);

		akt_animation = anims.get("preview");
		g.drawImage(akt_animation.getImage(), 20 + (32 * position + 5 * position), 20, size, size, null);
		akt_animation.next();
	}

	public void onClickInInventoryEnvent() {
		System.out.println("Cliccc " + this);

	}

	public void collectingEvent() {
		System.out.println("Collecting " + this);

	}

	@Override
	public void draw(Graphics g, float interpolation, boolean debugMode) {
		g.drawImage(akt_animation.getImage(), (int) (x * world.blockSize() - world.cameraX()),
				(int) (y * world.blockSize() - world.cameraY()), world.blockSize(), world.blockSize(), null);
		if (debugMode) {
			g.setColor(Color.GREEN);
			g.fillRect((int) (x * world.blockSize() - world.cameraX()), (int) (y * world.blockSize() - world.cameraY()),
					world.blockSize(), world.blockSize());

		}
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
