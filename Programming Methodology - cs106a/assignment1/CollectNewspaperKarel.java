/*
 * File: CollectNewspaperKarel.java
 * --------------------------------
 * At present, the CollectNewspaperKarel subclass does nothing.
 * Your job in the assignment is to add the necessary code to
 * instruct Karel to walk to the door of its house, pick up the
 * newspaper (represented by a beeper, of course), and then return
 * to its initial position in the upper left corner of the house.
 */

import stanford.karel.*;

public class CollectNewspaperKarel extends SuperKarel {

	// precondition: karel is on position (3,4) facing east. newspaper is on (6,3)
	private void moveToNewspaper() {
		move();
		move();
		turnRight();
		move();
		turnLeft();
		move();			//after this karel is standing on the newspaper facing east.
	}
	
	private void pickNewspaper() {
		pickBeeper();   //karel picks newspaper
		turnAround();	//now karel is facing west
	}
	
	
	private void backHome() {
		for(int i = 0; i < 3; i++) {
			move();						//karel moves three times, so it moves to the wall
		}
		turnRight();
		move();
		putBeeper();					//karel puts ball on the position it started
		turnRight();					//just to return to its starting condition
	}
	
	
	public void run() {
		moveToNewspaper();
		pickNewspaper();
		backHome();
		 
	}
}
