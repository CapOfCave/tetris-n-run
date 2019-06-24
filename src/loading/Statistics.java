package loading;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import logics.AchievementHandler;

public class Statistics extends Saver {

	private int inGameSeconds; // s
	private int totalSeconds; // s
	private int totalTetrosPlaced; // t
	private int totalTetrosRemoved; // r
	private int totalLevelLoaded; // r

	private String url;
	private AchievementHandler achievementHandler;

	public Statistics(String url, AchievementHandler achievementHandler) {
		this.url = url;
		this.achievementHandler = achievementHandler;
	}

	public void saveStats() {
		print(createOutput(), url);
	}

	private ArrayList<String> createOutput() {
		ArrayList<String> outpLines = new ArrayList<>();
		outpLines.add("i;" + inGameSeconds);
		outpLines.add("s;" + totalSeconds);
		outpLines.add("t;" + totalTetrosPlaced);
		outpLines.add("r;" + totalTetrosRemoved);
		outpLines.add("l;" + totalLevelLoaded);
		return outpLines;
	}

	public void loadStats() {
		Scanner sc = null;

		if (!LevelLoader.isAbsolute(url)) {
			sc = new Scanner(Toolkit.getDefaultToolkit().getClass().getResourceAsStream(url));
		} else {
			File file = new File(url);
			if (!file.exists()) {
				return; // no loading needed
			}
			try {
				sc = new Scanner(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		}
		while (sc.hasNext()) {
			String nextLine = sc.nextLine();
			if (nextLine.startsWith("s")) {
				totalSeconds = Integer.parseInt(nextLine.substring(2));
			} else if (nextLine.startsWith("i")) {
				inGameSeconds = Integer.parseInt(nextLine.substring(2));
			} else if (nextLine.startsWith("t")) {
				totalTetrosPlaced = Integer.parseInt(nextLine.substring(2));
			} else if (nextLine.startsWith("r")) {
				totalTetrosRemoved = Integer.parseInt(nextLine.substring(2));
			} else if (nextLine.startsWith("l")) {
				totalLevelLoaded = Integer.parseInt(nextLine.substring(2));
			}

		}
		sc.close();
	}

	public void addTetro() {
		totalTetrosPlaced++;
		if (totalTetrosPlaced == 100) { // AchievementGoal
			achievementHandler.achieve("baumeister");
		} else if (totalTetrosPlaced == 1) { // AchievementGoal
			achievementHandler.achieve("beginneinesAbenteuers");
		}

	}

	public void removeTetro() {
		totalTetrosRemoved++;
		if (totalTetrosRemoved == 100) { // AchievementGoal
			achievementHandler.achieve("baumeister");
		}
	}

	public void addSecond() {
		totalSeconds++;
		if (totalSeconds == 1800) { // AchievementGoal
			achievementHandler.achieve("mechanikenkennengelernt");
		} else if (totalSeconds == 14400) { // AchievementGoal
			achievementHandler.achieve("veteran");
		}
	}

	public void addInGameSecond() {
		addSecond();
		totalSeconds++;
	}

	public void loadLevel() {
		totalLevelLoaded++;
		if (totalLevelLoaded == 50) { // AchievementGoal
			achievementHandler.achieve("suizidgefaehrdet");
		} else if (totalLevelLoaded == 1) { // AchievementGoal
			achievementHandler.achieve("alleranfangistschwer");
		}
	}

	public int getTotalInGameSeconds() {
		return inGameSeconds;
	}

}
