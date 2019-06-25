package loading;

import java.util.ArrayList;


public class SettingSaver extends Saver{
	
	public static void saveSettings(ArrayList<Integer> keyCodes, int difficulty, ArrayList<Integer> levelSolved,  String path, String fileName) {
		print(createOutput(keyCodes, difficulty, levelSolved), path, fileName);
	}

	

	public static ArrayList<String> createOutput(ArrayList<Integer> keyCodes, int difficulty, ArrayList<Integer> levelSolved) {
		ArrayList<String> outpLines = new ArrayList<>();
		StringBuilder keyCodeBuilder = new StringBuilder();
		for(int i = 0; i < keyCodes.size(); i++) {
			keyCodeBuilder.append(keyCodes.get(i) + ",");
		}
		StringBuilder levelSolvedBuilder = new StringBuilder();
		for(int i = 0; i < levelSolved.size(); i++) {
			levelSolvedBuilder.append(levelSolved.get(i) + ",");
		}
		outpLines.add("k;" + keyCodeBuilder);
		outpLines.add("d;" + difficulty);
		outpLines.add("l;" + levelSolvedBuilder);
		
		return outpLines;
	}

}
