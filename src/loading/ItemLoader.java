package loading;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import logics.entities.items.Item;
import logics.entities.items.Weapon;

/**
 * @author Lars
 * Created on 23.09.2018
 */
public class ItemLoader {

	public static Item readItem(String typeUrl) {
		
		FileInputStream fin = null;
		ObjectInputStream ois = null;
		Item item = null;
		
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
				item = (Weapon)ois.readObject();
				item.init();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
		return item;
	}

}
