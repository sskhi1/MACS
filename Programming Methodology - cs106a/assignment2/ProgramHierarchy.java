/*
 * File: ProgramHierarchy.java
 * Name: 
 * Section Leader: 
 * ---------------------------
 * This file is the starter file for the ProgramHierarchy problem.
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class ProgramHierarchy extends GraphicsProgram {	
	private static final double rectWidth = 200;
	private static final double rectHeight = 50;
	private static final int gap = 10;
	
	public void run() {
		drawRect1();
		drawRect2();
		drawRect3();
		drawRect4();
		line12();
		line13();
		line14();
	}
	
	private void drawRect(double x, double y) {
		GRect rect = new GRect(x, y, rectWidth, rectHeight);
		add(rect);
	}
	
	//draws upper rectangle with label "Program".
	private void drawRect1() {
		drawRect(getWidth()/2 - rectWidth/2, getHeight()/2 - 2*rectHeight);
		GLabel program = new GLabel("Program");
		program.setLocation(getWidth()/2 - program.getWidth()/2,
							getHeight()/2 - 3*rectHeight/2 + program.getAscent()/2 );
		add(program);
	}
	
	//draws rectangle with label "ConsoleProgram".
	private void drawRect2() {
		drawRect(getWidth()/2 - rectWidth/2, getHeight()/2 + rectHeight);
		GLabel program = new GLabel("ConsoleProgram");
		program.setLocation(getWidth()/2 - program.getWidth()/2,
							getHeight()/2 + 3*rectHeight/2 + program.getAscent()/2  );
		add(program);
	}

	//draws rectangle with label "GraphicsProgram".
	private void drawRect3() {
		drawRect(getWidth()/2 - rectWidth/2 - rectWidth - gap, getHeight()/2 + rectHeight);
		GLabel program = new GLabel("GraphicsProgram");
		program.setLocation(getWidth()/2 - rectWidth/2 - rectWidth - gap + rectWidth/2 - program.getWidth()/2,
							getHeight()/2 + 3*rectHeight/2 + program.getAscent()/2 );
		add(program);
	}

	//draws Rectangle with label "DialogProgram".
	private void drawRect4() {
		drawRect(getWidth()/2 - rectWidth/2 + rectWidth + gap, getHeight()/2 + rectHeight);
		GLabel program = new GLabel("DialogProgram");
		program.setLocation(getWidth()/2 - rectWidth/2 + rectWidth + gap + rectWidth/2 - program.getWidth()/2,
							getHeight()/2 + 3*rectHeight/2 + program.getAscent()/2  );
		add(program);
	}
	
	//draws line between upper and middle rectangles.
	private void line12() {
		GLine line = new GLine(getWidth()/2, getHeight()/2 - rectHeight,
								getWidth()/2, getHeight()/2 + rectHeight);
		add(line);
	}
	
	//draws line between upper and left rectangles.
	private void line13() {
		GLine line = new GLine(getWidth()/2, getHeight()/2 - rectHeight, 
								getWidth()/2 - rectWidth - gap, getHeight()/2 + rectHeight);
		add(line);
	}
	
	//draws line between upper and right rectangles.
	private void line14() {
		GLine line = new GLine(getWidth()/2, getHeight()/2 - rectHeight, 
								getWidth()/2 + rectWidth + gap, getHeight()/2 + rectHeight);
		add(line);
	}
	
}
