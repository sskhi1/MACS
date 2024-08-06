/*
 * File: PythagoreanTheorem.java
 * Name: 
 * Section Leader: 
 * -----------------------------
 * This file is the starter file for the PythagoreanTheorem problem.
 */

import acm.program.*;

public class PythagoreanTheorem extends ConsoleProgram {
	int a;
	int b;
	double k;
	
	
	public void run() {
		println("Enter values to compute Pythagorean theorem. ");
		pythagoreanTheorem();
	}
	
	//Pythagorean Theorem for numbers: a and b. sqrt(a^2 + b^2)
	private void pythagoreanTheorem() {
		a = readInt("a: ");
		b = readInt("b: ");
		k = a*a + b*b;
		double c = Math.sqrt(k);
		println("c = " + c);
	}
}
