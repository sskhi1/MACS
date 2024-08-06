/*
 * File: StoneMasonKarel.java
 * --------------------------
 * The StoneMasonKarel subclass as it appears here does nothing.
 * When you finish writing it, it should solve the "repair the quad"
 * problem from Assignment 1.  In addition to editing the program,
 * you should be sure to edit this comment so that it no longer
 * indicates that the program does nothing.
 */

import stanford.karel.*;

public class StoneMasonKarel extends SuperKarel {

	// this method builds column.
	private void fillColumn() {
		turnLeft();
		if(noBeepersPresent()) {
			putBeeper();			//puts beeper on the first position of columns
		}
		while(frontIsClear()) {
			move();
			if(noBeepersPresent()) {
				putBeeper();		//karel puts beepers on every point of columns
			}
		}
	}
	
	// precondition: karel is on the top of a column facing north.
	private void startingPosition() {
		turnAround();
		while(frontIsClear()) {
			move();
		}
		turnLeft();					//post condition: karel returns to its starting position
	}
	
	// this method moves karel to new column.
	private void nextAvenue() {
		for(int i = 0; i < 4; i++) {
			if(frontIsClear()) {
				move();				//karel moves forward 4 times if front is clear
			}
		}
	}
	
	
	//precondition: karel is on (1,1) position facing east.
	public void run() {
		while(frontIsClear()) {
			fillColumn();
			startingPosition();
			nextAvenue();			//karel "builds" columns on every fourth position of rows (1st, 5th, 9th, etc.)
		}
		fillColumn();				//because the condition of "while" is "whileFrontIsClear()"
									//karel needs to build wall on the last position too,
									//because front is not clear anymore
		
		
		startingPosition();			//just to return to its starting position
	}
}
	
	
	
	
	
