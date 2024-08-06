/*
 * File: MidpointFindingKarel.java
 * -------------------------------
 * When you finish writing it, the MidpointFindingKarel class should
 * leave a beeper on the corner closest to the center of 1st Street
 * (or either of the two central corners if 1st Street has an even
 * number of corners).  Karel can put down additional beepers as it
 * looks for the midpoint, but must pick them up again before it
 * stops.  The world may be of any size, but you are allowed to
 * assume that it is at least as tall as it is wide.
 */

import stanford.karel.*;

public class MidpointFindingKarel extends SuperKarel {

	// this method moves karel to the top left corner of the world, facing south.
	private void topLeft() {
		turnLeft();
		while(frontIsClear()) {
			move();
		}
		turnAround();		
	}
	
	//precondition:karel is on the top left corner of the world, facing south.
	// this method moves karel to the middle of the first row.
	private void mainMove() {
		while(frontIsClear()) {
			move();
			if(frontIsClear()) {
				move();
			}
			turnLeft();
			if(frontIsClear()) {
				move();
			}
			turnRight();
		}
	}									//this algorithm works on every square shaped world and 
										//moves karel to the middle of the first row.
	
	//karel starts from (1, 1) position, facing east.
	public void run() {
		topLeft();
		mainMove();
		putBeeper();		//puts beeper in the middle of the first row
	}
}



