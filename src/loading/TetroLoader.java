package loading;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Scanner;

import data.TetroType;

/**
 * @author Lars Created on 05.08.2018
 */
public class TetroLoader {

	private static final String img_prefix = "/res/";
	private static final String img_suffix = "b.png";

	public TetroLoader() {
	}

	public static ArrayList<TetroType> loadTetros(String path, int blockSize) {
		Scanner sc = new Scanner(Toolkit.getDefaultToolkit().getClass().getResourceAsStream(path));
		ArrayList<TetroType> tetros = new ArrayList<>();
		while (sc.hasNextLine()) {
			String str = sc.nextLine();
			tetros.add(new TetroType(str.substring(0, 8),
					ImageLoader.loadImage(img_prefix + str.substring(8) + img_suffix, blockSize), blockSize));
		}
		sc.close();
		return tetros;
	}

}
