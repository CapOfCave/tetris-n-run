package logics.worlds;

import java.awt.Rectangle;

import data.Level;
import data.RawPlayer;
import graphics.GameFrame;
import input.KeyHandler;

/**
 * @author Marius Created on 13.09.2018
 */
public class Overworld extends World {

	public Overworld(Rectangle graphicClip, Level level, KeyHandler keyHandler, GameFrame frame, RawPlayer rawPlayer) {
		super(graphicClip, level, keyHandler, frame, rawPlayer);
	}

	

}
