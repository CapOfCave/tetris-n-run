package loading;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import data.RawPlayer;


public class RawPlayerLoader {

public static RawPlayer readRawPlayer(String typeUrl) {
		
		FileInputStream fin = null;
		ObjectInputStream ois = null;
		RawPlayer rawPlayer = null;
		
		if(!LevelLoader.isAbsolute(typeUrl)) {
			try {
				ois = new ObjectInputStream(Toolkit.getDefaultToolkit().getClass().getResourceAsStream(typeUrl));
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				fin = new FileInputStream(new File(typeUrl));
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
