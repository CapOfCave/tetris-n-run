package logics.worlds;

import java.awt.Rectangle;

import data.Level;
import graphics.Frame;
import input.KeyHandler;

/**
 * @author Marius Created on 13.09.2018
 */
public class Overworld extends World {

	public Overworld(Rectangle graphicClip, Level level, KeyHandler keyHandler, Frame frame) {
		super(graphicClip, level, keyHandler, frame);
	}

}
