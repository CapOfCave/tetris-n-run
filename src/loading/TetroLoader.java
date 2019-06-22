package loading;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Scanner;

import data.TetroType;

/**
 * @author Lars Created on 05.08.2018
 */
public class TetroLoader {

	private static final String img_prefix = "/res/tetros/floor";
	private static final String img_suffix = "p.png"; //h //1234534

	public static ArrayList<TetroType> loadTetros(String path) {
		Scanner sc = new Scanner(Toolkit.getDefaultToolkit().getClass().getResourceAsStream(path));
		ArrayList<TetroType> tetros = new ArrayList<>();
		while (sc.hasNextLine()) {
			String str = sc.nextLine();
			int color = Integer.parseInt(str.substring(9, 10));
			tetros.add(new TetroType(str.substring(0, 8),
					ImageLoader.loadImage(img_prefix + color + img_suffix), color));
		}
		sc.close();
		return tetros;
	}

}
