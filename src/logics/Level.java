package logics;

import java.util.ArrayList;

import data.RawTetro;
import data.TetroType;
import data.Tiles.Tile;

/**
 * @author Lars Created on 13.08.2018
 */
public class Level {
	private ArrayList<TetroType> tetroTypes;
	private ArrayList<RawTetro> rawTetros;
	private Tile[][] arrWorld;
	private int blockSize;
	private String tetrofileUrl;
	private int playerX;
	private int playerY;

	public Level(ArrayList<TetroType> tetroTypes, ArrayList<RawTetro> rawTetros, Tile[][] arrWorld, int blockSize, String tetrofileUrl, int playerX,
			int playerY) {
		this.tetroTypes = tetroTypes;
		this.rawTetros = rawTetros;
		this.arrWorld = arrWorld;
		this.blockSize = blockSize;
		this.tetrofileUrl = tetrofileUrl;
		this.playerX = playerX;
		this.playerY = playerY;
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

	// public UnfinishedPlayer getUnfinishedPlayer() {
	// return unfinishedPlayer;
	// }
}
