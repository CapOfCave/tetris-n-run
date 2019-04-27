package data;

import java.util.ArrayList;

import data.Tiles.DoorTile;
import data.Tiles.Tile;
import logics.entities.Entity;

/**
 * @author Lars Created on 13.08.2018
 */
public class Level {
	private ArrayList<TetroType> tetroTypes;
	private ArrayList<RawTetro> rawTetros;
	private Tile[][] arrWorld;
	private String tetrofileUrl;
	private int playerX;
	private int playerY;
	private ArrayList<DoorTile> doors;
	private ArrayList<Entity> entities;
	private int[] tetroAmounts;
	private boolean[] toggleStates;

	public Level(ArrayList<TetroType> tetroTypes, ArrayList<RawTetro> rawTetros, Tile[][] arrWorld,
			ArrayList<DoorTile> doors, ArrayList<Entity> entities, int[] tetroAmounts, boolean[] toggleStates,
			String tetrofileUrl, int playerX, int playerY) {
		this.tetroTypes = tetroTypes;
		this.rawTetros = rawTetros;
		this.arrWorld = arrWorld;
		this.tetrofileUrl = tetrofileUrl;
		this.playerX = playerX;
		this.playerY = playerY;
		this.doors = doors;
		this.entities = entities;
		this.tetroAmounts = tetroAmounts;
		this.toggleStates = toggleStates;
	}

	public int getPlayerY() {
		return playerY;
	}

	public int getPlayerX() {
		return playerX;
	}

	public String getTetrofileUrl() {
		return tetrofileUrl;
	}

	public ArrayList<TetroType> getTetroTypes() {
		return tetroTypes;
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
