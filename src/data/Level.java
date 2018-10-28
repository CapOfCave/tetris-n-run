package data;

import java.util.ArrayList;

import data.Tiles.DoorTile;
import data.Tiles.Tile;
import logics.entities.Entity;
import logics.entities.items.Item;

/**
 * @author Lars Created on 13.08.2018
 */
public class Level {
	private ArrayList<TetroType> tetroTypes;
	private ArrayList<RawTetro> rawTetros;
	private Tile[][] arrWorld;
	private ArrayList<Item> arrItems;
	private String tetrofileUrl;
	private int playerX;
	private int playerY;
	private ArrayList<DoorTile> doors;
	private ArrayList<RawSpawner> spawner;
	private ArrayList<Entity> entities;

	public Level(ArrayList<TetroType> tetroTypes, ArrayList<RawTetro> rawTetros, Tile[][] arrWorld,
			ArrayList<Item> arrItems, ArrayList<DoorTile> doors, ArrayList<RawSpawner> spawner,
			ArrayList<Entity> entities, String tetrofileUrl, int playerX, int playerY) {
		this.tetroTypes = tetroTypes;
		this.rawTetros = rawTetros;
		this.arrWorld = arrWorld;
		this.arrItems = arrItems;
		this.tetrofileUrl = tetrofileUrl;
		this.playerX = playerX;
		this.playerY = playerY;
		this.doors = doors;
		this.spawner = spawner;
		this.entities = entities;
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

	public ArrayList<Item> getItemWorld() {
		return arrItems;
	}

	public ArrayList<DoorTile> getDoors() {
		return doors;
	}

	public ArrayList<RawSpawner> getSpawner() {
		return spawner;
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

}
