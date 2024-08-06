/*
 * File: Target.java
 * Name: 
 * Section Leader: 
 * -----------------
 * This file is the starter file for the Target problem.
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class Target extends GraphicsProgram {
	
	private static final int RADIUS1 = 72;
	private static final int RADIUS2 = RADIUS1 * 165/254;
	private static final int RADIUS3 = RADIUS1 * 76/254;
	
	public void run() {
		drawTarget();
	}
	
	//draws target.
	private void drawTarget() {
		drawCircle1();
		drawCircle2();
		drawCircle3();
	}
	
	//draws outer red circle.
	private void drawCircle1() {
		GOval circle = new GOval(getWidth()/2 - RADIUS1/2, getHeight()/2 - RADIUS1/2, RADIUS1, RADIUS1);
		circle.setFilled(true);
		circle.setFillColor(Color.red);
		circle.setColor(Color.red);
		add(circle);
	}
	
	//draws white circle inside big red circle.
	private void drawCircle2() {
		GOval circle = new GOval(getWidth()/2 - RADIUS2/2, getHeight()/2 - RADIUS2/2, RADIUS2, RADIUS2);
		circle.setFilled(true);
		circle.setFillColor(Color.WHITE);
		circle.setColor(Color.WHITE);
		add(circle);
	}

	//draws little red circle in the center of target.
	private void drawCircle3() {
		GOval circle = new GOval(getWidth()/2 - RADIUS3/2, getHeight()/2 - RADIUS3/2, RADIUS3, RADIUS3);
		circle.setFilled(true);
		circle.setFillColor(Color.red);
		circle.setColor(Color.red);
		add(circle);
	}
}
