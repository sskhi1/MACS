/*
 * File: Sierpinski.cpp
 * --------------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Sierpinski problem
 * on Assignment #3.
 * [TODO: extend the documentation]
 */

#include <iostream>
#include "gwindow.h"
#include "simpio.h"
using namespace std;

const int width = 500;
const int height = 500;

void initConvAndTriangle(GWindow &window, double width, double height);
void drawSierpinski(GWindow &window, GPoint p1, GPoint p2, GPoint p3, int order, int edge);

int main() {
    // [TODO: fill with your code]
	GWindow window(width, height);
	initConvAndTriangle(window, width, height);
    return 0;
}

// Ask for edge length and Sierpinski triangle order.
// Draw initial (0 order) triangle.
void initConvAndTriangle(GWindow &window, double width, double height){
	int edge = getInteger("Edge Length: ");
	int order = getInteger("Order: ");
	// Draw 0 order triangle.
	GPoint p1(width/2, (height - edge)/2);				// upper point of the triangle
	GPoint p2 = window.drawPolarLine(p1, edge, 240);    // lower left point
	GPoint p3 = window.drawPolarLine(p2, edge, 0);		// lower right point
	window.drawPolarLine(p3, edge, 120);

	drawSierpinski(window, p1, p2, p3, order, edge/2);
}

void drawSierpinski(GWindow &window, GPoint p1, GPoint p2, GPoint p3, int order, int edge){
	if(order < 1) return;
	// triangle midpoints.
	GPoint midP12((p1.getX() + p2.getX())/2, (p1.getY() + p2.getY())/2);
	GPoint midP13((p1.getX() + p3.getX())/2, (p1.getY() + p3.getY())/2);
	GPoint midP23((p2.getX() + p3.getX())/2, (p2.getY() + p3.getY())/2);
	//connect midpoints of a triangle.
	window.drawPolarLine(midP13, edge, 180);
	window.drawPolarLine(midP12, edge, 300);
	window.drawPolarLine(midP23, edge, 60);
	
	drawSierpinski(window, p1, midP12, midP13, order - 1, edge/2);
	drawSierpinski(window, midP12, p2, midP23, order - 1, edge/2);
	drawSierpinski(window, midP13, midP23, p3, order - 1, edge/2);
}
