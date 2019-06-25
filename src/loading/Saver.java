package loading;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public abstract class Saver {

	protected static void print(ArrayList<String> content, String url) {
		File file = new File(url);// path + "\\" + fileName
		if (!file.exists()) {
			try {
				File temp = file.getParentFile();
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

	protected static void print(ArrayList<String> content, String path, String fileName) {
		print(content, path + "\\" + fileName);
	}
}
