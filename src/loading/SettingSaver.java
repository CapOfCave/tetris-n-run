package loading;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class SettingSaver {
	
	public static void saveSettings(ArrayList<Integer> keyCodes, int difficulty, ArrayList<Integer> levelSolved,  String path, String fileName) {
		print(createOutput(keyCodes, difficulty, levelSolved), path, fileName);
	}

	public static void print(ArrayList<String> content, String path, String fileName) {
		File file = new File(path + "\\" + fileName);
		if (!file.exists()) {
			try {
				File temp = new File(path);
				temp.mkdirs();

				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for (String str : content) {
				bw.write(str);
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
