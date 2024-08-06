/*
 * File: Hailstone.java
 * Name: 
 * Section Leader: 
 * --------------------
 * This file is the starter file for the Hailstone problem.
 */

import acm.program.*;

public class Hailstone extends ConsoleProgram {
	//Int "steps" will count how many steps it takes for a number to reach 1 
	//in Hailstone sequence.
	private int steps = 0;
	private int x;
	
	public void run() {
		sequence();
	}
	
	//User enters a number. If it is even, program takes half and if it is odd
	//it takes 3*n+1. It happens until this number reaches 1.
	private void sequence() {
		x = readInt("Enter a number: ");
		if(x < 1) {
			println("please, enter a natural number.");
		}else {
			while(x != 1) {
				steps++;
				if(x % 2 == 0) {
					println(x + " is even so I take half: " + x/2);
					x /= 2;
				} else {
					println(x + " is odd, so I make 3n+1 " + (3*x + 1));
					x = 3*x +1;
				}
			}
			println("The process took " + steps + " steps to reach 1. ");
		}
	}
}

