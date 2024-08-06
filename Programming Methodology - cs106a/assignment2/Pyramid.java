/*
 * File: Pyramid.java
 * Name: 
 * Section Leader: 
 * ------------------
 * This file is the starter file for the Pyramid problem.
 * It includes definitions of the constants that match the
 * sample run in the assignment, but you should make sure
 * that changing these values causes the generated display
 * to change accordingly.
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class Pyramid extends GraphicsProgram {

/** Width of each brick in pixels */
	private static final int BRICK_WIDTH = 30;

/** Width of each brick in pixels */
	private static final int BRICK_HEIGHT = 12;

/** Number of bricks in the base of the pyramid */
	private static final int BRICKS_IN_BASE = 20;
	
	private int bricks = BRICKS_IN_BASE;
	private int Xcoordinate;
	private int Ycoordinate;
	
	public void run() {
		
		Xcoordinate = getWidth()/2 - bricks * BRICK_WIDTH/2;
		Ycoordinate = getHeight() - BRICK_HEIGHT;
		
		buildBricks();
	}
	
	//draws pyramid.
	private void buildBricks() {
		//draws rows of the pyramid.
		for(int row = 0; row < BRICKS_IN_BASE; row++ ) {
			//draws bricks in rows (1 less than previous row).
			for(int i = 0; i < bricks; i++) {
				int x = Xcoordinate + i * BRICK_WIDTH;
				int y = Ycoordinate - row * BRICK_HEIGHT;
				GRect brick = new GRect(x, y, BRICK_WIDTH, BRICK_HEIGHT);
				add(brick);
			}
			bricks--;
			Xcoordinate = getWidth()/2 - bricks * BRICK_WIDTH/2;	// x coordinate of the first brick of a new row will be a half brick width right.
		}
	}
}

