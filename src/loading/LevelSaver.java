package loading;

import java.util.ArrayList;

import data.Level;
import data.RawTetro;
import data.Tiles.DoorTile;
import data.Tiles.SaveNLoadTile;
import data.Tiles.Tile;
import graphics.GameFrame;
import logics.entities.Entity;
import logics.entities.MovingBlockSpawner;
import logics.entities.Switch;

/**
 * @author Lars Created on 13.08.2018
 */
public class LevelSaver extends Saver {

	public void saveLevel(Level level, String url) {
		print(createOutput(level), url);
	}

	private ArrayList<String> createOutput(Level level) {
		ArrayList<String> outpLines = new ArrayList<>();

		// player position
		outpLines.add("p;x=" + level.getPlayerX() + ";y=" + level.getPlayerY());

		// Toggle states
		boolean[] toggleStates = level.getToggleStates();
		sout(toggleStates);
		outpLines.add("o;" + toggleStates[0] + ";" + toggleStates[1] + ";" + toggleStates[2] + ";" + toggleStates[3]
				+ ";" + toggleStates[4] + ";" + toggleStates[5]);
		int[] tetroAmounts = level.getTetroAmounts();
		for (int i = 0; i < tetroAmounts.length; i++) {
			if (tetroAmounts[i] != 0) {
				outpLines.add("m;type=" + i + ";amount=" + tetroAmounts[i]);
			}
		}

		// tetros
		ArrayList<RawTetro> rawTetros = level.getUnfinishedTetros();
		for (RawTetro rt : rawTetros) {
			outpLines.add("t;x=" + rt.getX() + ";y=" + rt.getY() + ";r=" + rt.getRotation() + ";t=" + rt.getType());
		}

		// doors
		ArrayList<DoorTile> doors = level.getDoors();
		for (DoorTile dT : doors) {
			outpLines.add("d;x=" + dT.getPosX() + ";y=" + dT.getPosY() + ";r=" + dT.getRotation() + ";c="
					+ dT.getColorAsInt() + ";o=" + dT.isStandardOpened());
		}

		// Other entities
		ArrayList<Entity> entities = level.getEntities();
		for (Entity entity : entities) {
			String outp = "e;rx=" + (int) entity.getX() + ";ry=" + (int) entity.getY() + ";type=" + entity.getType();
			switch (entity.getType()) {
			case "moveblock":
				//Don't save the moveblock!
				break;
			case "moveblockspawner":
				MovingBlockSpawner cubeSpawner = (MovingBlockSpawner) entity;
				outp +=";cx=" + (int) cubeSpawner.getCX() / GameFrame.BLOCKSIZE + ";cy="
						+ (int) cubeSpawner.getCY() / GameFrame.BLOCKSIZE;
				if (cubeSpawner.getCurrentCubeX() != -1000) {
					outp += ";currentCubeX=" + cubeSpawner.getCurrentCubeX() + ";currentCubeY=" + cubeSpawner.getCurrentCubeY();
				}
				outpLines.add(outp);
				
				break;
			case "switch":
				Switch entitySwitch = (Switch) entity;
				outpLines.add(outp + ";color=" + entitySwitch.getColorAsInt());
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
				if (field != null)
					worldLine.append(field.getKey());
				else
					worldLine.append('0');
			}
			outpLines.add(worldLine.toString());
		}

		// ###Line
		outpLines.add("###");

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

		return outpLines;
	}

	public static void sout(boolean[] toggleStates) {
		for(int i = 0; i < toggleStates.length;i++) {
			System.out.println(i + ": " + toggleStates[i]);
		}
	}

}
