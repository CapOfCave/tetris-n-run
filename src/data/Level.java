package data;

import java.util.ArrayList;

import data.Tiles.DoorTile;
import data.Tiles.Tile;
import logics.entities.items.Item;


/**
 * @author Lars Created on 13.08.2018
 */
public class Level {
	private ArrayList<TetroType> tetroTypes;
	private ArrayList<RawTetro> rawTetros;
	private Tile[][] arrWorld;
	private ArrayList<Item> arrItems;
	private int blockSize;
	private String tetrofileUrl;
	private int playerX;
	private int playerY;
	private ArrayList<DoorTile> doors;

	public Level(ArrayList<TetroType> tetroTypes, ArrayList<RawTetro> rawTetros, Tile[][] arrWorld, ArrayList<Item> arrItems, ArrayList<DoorTile> doors, int blockSize, String tetrofileUrl, int playerX,
			int playerY) {
		this.tetroTypes = tetroTypes;
		this.rawTetros = rawTetros;
		this.arrWorld = arrWorld;
		this.arrItems = arrItems;
		this.blockSize = blockSize;
		this.tetrofileUrl = tetrofileUrl;
		this.playerX = playerX;
		this.playerY = playerY;
		this.doors = doors;
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

	public int getBlockSize() {
		return blockSize;
	}

	public ArrayList<Item> getItemWorld() {
		return arrItems;
	}

	public ArrayList<DoorTile> getDoors() {
		return doors;
	}

}
