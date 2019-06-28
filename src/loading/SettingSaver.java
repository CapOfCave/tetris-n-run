package loading;

import java.util.ArrayList;


public class SettingSaver extends Saver{
	
	public static void saveSettings(ArrayList<Integer> keyCodes, int levelSolved,  String path, String fileName) {
		print(createOutput(keyCodes, levelSolved), path, fileName);
	}

	

	public static ArrayList<String> createOutput(ArrayList<Integer> keyCodes, int levelSolved) {
		ArrayList<String> outpLines = new ArrayList<>();
		StringBuilder keyCodeBuilder = new StringBuilder();
		for(int i = 0; i < keyCodes.size(); i++) {
			keyCodeBuilder.append(keyCodes.get(i) + ",");
		}
		outpLines.add("k;" + keyCodeBuilder);
		outpLines.add("l;" + levelSolved);
		
		return outpLines;
	}

}
