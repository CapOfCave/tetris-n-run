package logics;

import graphics.GameFrame;
import loading.Statistics;

public class AchievementHandler {

	Statistics stats;
	GameFrame frame;
	
	public AchievementHandler(GameFrame gameFrame) {
		frame = gameFrame;
	}

	public void achieve(String string) {
		frame.addLineToText("Achievement unlocked: " + string + " (TODO: echte Namen)", 2);
	}
	
	
}
