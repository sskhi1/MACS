/*
 * File: FindRange.java
 * Name: 
 * Section Leader: 
 * --------------------
 * This file is the starter file for the FindRange problem.
 */

import acm.program.*;

public class FindRange extends ConsoleProgram {
	public void run() {
		println("This program finds the largest and smallest numbers");
		range();	
	}
	
	private static final int stop = 0;
	private int min = stop;
	private int max = stop;
	
	private void range() {
		while(true) {
			int number = readInt("?  ");
			if(number == stop) {
				break;	// program reads numbers until it is "stop" (in our case 0)
			//The first entered number becomes maximum and minimum at the same time.
				//after that new entered number becomes minimum if it's smaller than the first one (or the one which is "min").
			}else if(number < min || min == stop) {
				min = number;
				if(max == stop) {
					max = number;
				}	
			//A new entered number becomes maximum if it's larger than the number "max"
			}else if(number > max) {
				max = number;
			}
		}
		if(min == stop) {
			println("BRUH");
		}else {
			println("Largest: " + max);
			println("Smallest: " + min);
		}
	}
}

