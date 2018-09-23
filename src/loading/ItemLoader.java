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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			try {
				fin = new FileInputStream(new File(typeUrl));
				ois = new ObjectInputStream(fin);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		
		
		try {
			if(ois != null)
			item = (Weapon)ois.readObject();
			item.init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return item;
	}

}
