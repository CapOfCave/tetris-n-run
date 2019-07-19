package data;

import java.util.ArrayList;

import data.Tiles.DoorTile;
import data.Tiles.Tile;
import graphics.GameFrameHandler;
import logics.entities.Entity;

/**
 * @author Lars Created on 13.08.2018
 */
public class Level {
	private ArrayList<RawTetro> rawTetros;
	private Tile[][] arrWorld;
	private int playerX;
	private int playerY;
	private ArrayList<DoorTile> doors;
	private ArrayList<Entity> entities;
	private int[] tetroAmounts;
	private boolean[] toggleStates;

	public Level(ArrayList<RawTetro> rawTetros, Tile[][] arrWorld,
			ArrayList<DoorTile> doors, ArrayList<Entity> entities, int[] tetroAmounts, boolean[] toggleStates,
			int playerX, int playerY) {
		this.rawTetros = rawTetros;
		this.arrWorld = arrWorld;
		this.playerX = playerX;
		this.playerY = playerY;
		this.doors = doors;
		this.entities = entities;
		this.tetroAmounts = tetroAmounts;
		this.toggleStates = toggleStates;
	}

	public void init(GameFrameHandler gameFrame) {
		for (Tile[] row : arrWorld) {
			for (Tile tile : row) {
				if (tile != null)
					tile.setFrame(gameFrame);
			}
		}
	}

	public int getPlayerY() {
		return playerY;
	}

	public int getPlayerX() {
		return playerX;
	}

	public ArrayList<RawTetro> getUnfinishedTetros() {
		return rawTetros;
	}

	public Tile[][] getArrWorld() {
		return arrWorld;
	}

	public ArrayList<DoorTile> getDoors() {
		return doors;
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

	public int[] getTetroAmounts() {
		return tetroAmounts;
	}

	public boolean[] getToggleStates() {
		return toggleStates;
	}
	
}
