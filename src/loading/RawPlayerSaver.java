package loading;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import data.RawPlayer;


public class RawPlayerSaver {
	
	public static void writePlayer(String saveUrl, RawPlayer rawPlayer) {

		FileOutputStream fout = null;
		ObjectOutputStream oos = null;

		try {

			fout = new FileOutputStream(saveUrl);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(rawPlayer);
		} catch (FileNotFoundException e) {
			System.err.println("File while Item save not found");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fout != null) {
				try {
					fout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	
}
