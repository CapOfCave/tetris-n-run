package loading;

import java.util.ArrayList;

import data.Level;
import data.RawTetro;
import data.Tiles.DoorTile;
import data.Tiles.SaveNLoadTile;
import data.Tiles.Tile;
import graphics.GameFrameHandler;
import logics.entities.Entity;
import logics.entities.MovingBlockSpawner;
import logics.entities.Switch;
import tools.Coder;

/**
 * @author Lars Created on 13.08.2018
 */
public class LevelSaver extends Saver {

	private Coder coder;

	public LevelSaver(Coder coder) {
		this.coder = coder;
	}

	public void saveLevel(Level level, String url) {
		print(createOutput(level), url);
	}

	private ArrayList<String> createOutput(Level level) {
		int check = 0;
		int currentLine = 1;
		ArrayList<String> outpLines = new ArrayList<>();

		// player position
		outpLines.add("p;x=" + level.getPlayerX() + ";y=" + level.getPlayerY());
		check = coder.increaseCheck(check, currentLine++, level.getPlayerX(), level.getPlayerY());
		// Toggle states
		boolean[] toggleStates = level.getToggleStates();
		outpLines.add("o;" + toggleStates[0] + ";" + toggleStates[1] + ";" + toggleStates[2] + ";" + toggleStates[3]
				+ ";" + toggleStates[4] + ";" + toggleStates[5]);
		check = coder.increaseCheck(check, currentLine++, toggleStates[0] ? 1 : 0, toggleStates[1] ? 1 : 0,
				toggleStates[2] ? 1 : 0, toggleStates[3] ? 1 : 0, toggleStates[4] ? 1 : 0, toggleStates[5] ? 1 : 0);
		int[] tetroAmounts = level.getTetroAmounts();
		for (int i = 0; i < tetroAmounts.length; i++) {
			if (tetroAmounts[i] != 0) {
				outpLines.add("m;type=" + i + ";amount=" + tetroAmounts[i]);
				check = coder.increaseCheck(check, currentLine++, i, tetroAmounts[i]);
			}
		}

		// tetros
		ArrayList<RawTetro> rawTetros = level.getUnfinishedTetros();
		for (RawTetro rt : rawTetros) {
			outpLines.add("t;x=" + rt.getX() + ";y=" + rt.getY() + ";r=" + rt.getRotation() + ";t=" + rt.getType());
			check = coder.increaseCheck(check, currentLine++, rt.getX(), rt.getY(), rt.getRotation(), rt.getType());
		}

		// doors
		ArrayList<DoorTile> doors = level.getDoors();
		for (DoorTile dT : doors) {
			outpLines.add("d;x=" + dT.getPosX() + ";y=" + dT.getPosY() + ";r=" + dT.getRotation() + ";c="
					+ dT.getColorAsInt() + ";o=" + dT.isStandardOpened());
			check = coder.increaseCheck(check, currentLine++, dT.getPosX(), dT.getPosY(), dT.getRotation(),
					dT.getColorAsInt(), dT.isStandardOpened() ? 1 : 0);
		}

		// Other entities
		ArrayList<Entity> entities = level.getEntities();
		for (Entity entity : entities) {
			String outp = "e;rx=" + (int) entity.getX() + ";ry=" + (int) entity.getY() + ";type=" + entity.getType();
			switch (entity.getType()) {
			case "moveblockspawner":
				MovingBlockSpawner cubeSpawner = (MovingBlockSpawner) entity;
				outp += ";cx=" + (int) cubeSpawner.getCX() / GameFrameHandler.BLOCKSIZE + ";cy="
						+ (int) cubeSpawner.getCY() / GameFrameHandler.BLOCKSIZE;
				if (cubeSpawner.getCurrentCubeX() != -1000) {
					outp += ";currentCubeX=" + cubeSpawner.getCurrentCubeX() + ";currentCubeY="
							+ cubeSpawner.getCurrentCubeY();
					check = coder.increaseCheck(check, currentLine++, (int) entity.getX() / GameFrameHandler.BLOCKSIZE,
							(int) entity.getY() / GameFrameHandler.BLOCKSIZE,
							(int) cubeSpawner.getCX() / GameFrameHandler.BLOCKSIZE,
							(int) cubeSpawner.getCY() / GameFrameHandler.BLOCKSIZE);
				} else {
					check = coder.increaseCheck(check, currentLine++, (int) entity.getX() / GameFrameHandler.BLOCKSIZE,
							(int) entity.getY() / GameFrameHandler.BLOCKSIZE,
							(int) cubeSpawner.getCX() / GameFrameHandler.BLOCKSIZE,
							(int) cubeSpawner.getCY() / GameFrameHandler.BLOCKSIZE, cubeSpawner.getCurrentCubeX(),
							cubeSpawner.getCurrentCubeY());
				}

				outpLines.add(outp);

				break;
			case "switch":
				Switch entitySwitch = (Switch) entity;
				outpLines.add(outp + ";color=" + entitySwitch.getColorAsInt());
				check = coder.increaseCheck(check, currentLine++, (int) entity.getX() / GameFrameHandler.BLOCKSIZE,(int) entity.getY() / GameFrameHandler.BLOCKSIZE,
						entitySwitch.getColorAsInt());
				break;
			default:
				System.err.println("Requested Entity (\"" + entity.getType() + "\") undefind in saving process");
			}

		}

		// world
		Tile[][] world = level.getArrWorld();
		for (Tile[] row : world) {
			StringBuilder worldLine = new StringBuilder("w;");
			for (Tile field : row) {
				if (field != null) {
					worldLine.append(field.getKey());
				} else {
					worldLine.append('0');
				}
				
			}
			check = coder.addWorldLineToChecker(check, currentLine++, worldLine.toString().substring(2));
			outpLines.add(worldLine.toString());
		}

		// ###Line
		outpLines.add("###");
		currentLine++;

		// Tiles in line
		for (Tile[] row : world) {

			for (Tile field : row) {
				// SAL-Tile
				if (field != null && field.getKey() == '2') {
					StringBuilder worldLine = new StringBuilder("Tl;");
					worldLine.append("x=" + field.getPosX() + ";");
					worldLine.append("y=" + field.getPosY() + ";");

					SaveNLoadTile fieldCast = (SaveNLoadTile) field;
					if (fieldCast.getTip() != null) {
						worldLine.append("tip=" + fieldCast.getTip() + ";");
						if (fieldCast.getTip2() != null) {
							worldLine.append("tip2=" + fieldCast.getTip2() + ";");
							if (fieldCast.getTip3() != null) {
								worldLine.append("tip3=" + fieldCast.getTip3() + ";");
								if (fieldCast.getTip4() != null) {
									worldLine.append("tip4=" + fieldCast.getTip4() + ";");

								}
							}
						}
					}
					worldLine.append("amount=");
					for (int amount : field.getTetroAmount())
						worldLine.append(amount + ",");
					check = coder.increaseCheckForSALT(check, currentLine++, field.getPosX(), field.getPosY(), field.getTetroAmount());
					outpLines.add(worldLine.toString());
					// Decoration
				} else if (field != null && field.getKey() == 'X') {
					StringBuilder worldLine = new StringBuilder("Td;");
					worldLine.append("x=" + field.getPosX() + ";");
					worldLine.append("y=" + field.getPosY() + ";");
					worldLine.append("xo=" + (int) field.getOffSet().getX() + ";");
					worldLine.append("yo=" + (int) field.getOffSet().getY() + ";");
					worldLine.append("name=" + field.getName() + ";");

					outpLines.add(worldLine.toString());
				}
			}

		}
		outpLines.add("c;" + check);
		return outpLines;
	}

	

}
