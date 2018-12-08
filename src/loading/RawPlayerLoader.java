package loading;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import data.RawPlayer;


public class RawPlayerLoader {

public static RawPlayer readRawPlayer() {
	
	boolean isInAppdata = true;
	
			File file = new File(System.getenv("APPDATA") + "\\tetris-n-run\\saves\\player.txt");
		if(!file.exists()) {
			file = new File("/res/saves/Player.txt");
			isInAppdata = false;
		}
			
		
			System.out.println(file.getPath());
		
		FileInputStream fin = null;
		ObjectInputStream ois = null;
		RawPlayer rawPlayer = null;
		
		if(!isInAppdata) {
			try {
				ois = new ObjectInputStream(Toolkit.getDefaultToolkit().getClass().getResourceAsStream("/res/saves/Player.txt"));
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				
				fin = new FileInputStream(file);
				ois = new ObjectInputStream(fin);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			
		
		
		try {
			if(ois != null) {
				rawPlayer = (RawPlayer) ois.readObject();
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
		return rawPlayer;
	}
}
