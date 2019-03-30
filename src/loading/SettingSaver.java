package loading;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class SettingSaver {
	
	public static void saveSettings(ArrayList<Integer> keyCodes, int levelSolved,  String path, String fileName) {
		print(createOutput(keyCodes, levelSolved), path, fileName);
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

	public static ArrayList<String> createOutput(ArrayList<Integer> keyCodes, int levelSolved) {
		ArrayList<String> outpLines = new ArrayList<>();
		String codeInString = "";
		
		for(int i = 0; i < keyCodes.size(); i++) {
			codeInString += keyCodes.get(i) + ",";
		}
		outpLines.add("k;" + codeInString);
		outpLines.add("l;" + levelSolved);
		return outpLines;
	}

}
