
/*
 * File: NameSurferGraph.java
 * ---------------------------
 * This class represents the canvas on which the graph of
 * names is drawn. This class is responsible for updating
 * (redrawing) the graphs whenever the list of entries changes or the window is resized.
 */

import acm.graphics.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;

public class NameSurferGraph extends GCanvas implements NameSurferConstants, ComponentListener {
	private ArrayList<NameSurferEntry> displayNames = new ArrayList<NameSurferEntry>();;

	/**
	 * Creates a new NameSurferGraph object that displays the data.
	 */
	public NameSurferGraph() {
		addComponentListener(this);
	}

	/**
	 * Clears the list of name surfer entries stored inside this class.
	 */
	public void clear() {
		displayNames.clear();
	}

	/* Method: addEntry(entry) */
	/**
	 * Adds a new NameSurferEntry to the list of entries on the display. Note that
	 * this method does not actually draw the graph, but simply stores the entry;
	 * the graph is drawn by calling update.
	 */
	public void addEntry(NameSurferEntry entry) {
		// You fill this in //
		displayNames.add(entry);
	}

	/**
	 * Updates the display image by deleting all the graphical objects from the
	 * canvas and then reassembling the display according to the list of entries.
	 * Your application must call update after calling either clear or addEntry;
	 * update is also called whenever the size of the canvas changes.
	 */
	public void update() {
		removeAll();
		// draw initial vertical and horizontal lines and years.
		drawInitial();
		// draw names' data on canvas if this names are entered (they are 1 ore more).
		if (displayNames.size() != 0) {
			addData(displayNames);
		}
	}

	private void drawInitial() {
		GLine NorthHorizontal = new GLine(0, GRAPH_MARGIN_SIZE, getWidth(), GRAPH_MARGIN_SIZE);
		GLine SouthHorizontal = new GLine(0, getHeight() - GRAPH_MARGIN_SIZE, getWidth(),
				getHeight() - GRAPH_MARGIN_SIZE);
		add(NorthHorizontal);
		add(SouthHorizontal);
		int Xcoordinate = 0;
		int year = START_DECADE;
		for (int i = 0; i < NDECADES; i++) {
			GLine vertical = new GLine(Xcoordinate, 0, Xcoordinate, getHeight());
			add(vertical);
			GLabel Label = new GLabel("" + year);
			Label.setLocation(Xcoordinate + 2, getHeight() - GRAPH_MARGIN_SIZE + 12);
			add(Label);
			Xcoordinate += getWidth() / NDECADES;
			year += 10;
		}
	}

	private void addData(ArrayList<NameSurferEntry> displayNames) {
		for (int i = 0; i < displayNames.size(); i++) {
			drawName(displayNames.get(i), i);
		}
	}

	double YcoordinateStart = 0; // Starting coordinate of a line. (In each decade)
	double YcoordinateEnd = 0; // Ending coordinate of a line. (In each decade)
	GLabel label; // Name.

	private void drawName(NameSurferEntry entry, int color) {
		int Xspacing = getWidth() / NDECADES;
		// draw lines.
		for (int i = 0; i < NDECADES - 1; i++) {
			YcoordinateStart = Ycoordinate(entry.getRank(i));
			YcoordinateEnd = Ycoordinate(entry.getRank(i + 1));
			GLine line = new GLine(Xspacing * i, YcoordinateStart, Xspacing * (i + 1), YcoordinateEnd);
			if (color % 4 == 0) {
				line.setColor(Color.BLACK);
			} else if (color % 4 == 1) {
				line.setColor(Color.RED);
			} else if (color % 4 == 2) {
				line.setColor(Color.BLUE);
			} else {
				line.setColor(Color.YELLOW);
			}
			add(line);
		}
		// draw names.
		for (int i = 0; i < NDECADES; i++) {
			if (entry.getRank(i) == 0) {
				label = new GLabel(entry.getName() + " " + "*");
			} else {
				label = new GLabel(entry.getName() + " " + entry.getRank(i));
			}
			label.setLocation(Xspacing * i + 3, Ycoordinate(entry.getRank(i)) - 1);
			if (color % 4 == 0) {
				label.setColor(Color.BLACK);
			} else if (color % 4 == 1) {
				label.setColor(Color.RED);
			} else if (color % 4 == 2) {
				label.setColor(Color.BLUE);
			} else {
				label.setColor(Color.YELLOW);
			}
			add(label);
		}
	}

	// calculation of lines' starting and ending coordinates in each decade.
	private double Ycoordinate(int n) {
		double rank = n;
		if (rank != 0) {
			rank = rank / MAX_RANK * (getHeight() - 2 * GRAPH_MARGIN_SIZE) + GRAPH_MARGIN_SIZE;
		} else {
			rank = getHeight() - GRAPH_MARGIN_SIZE;
		}
		return rank;
	}

	/* Implementation of the ComponentListener interface */
	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentResized(ComponentEvent e) {
		update();
	}

	public void componentShown(ComponentEvent e) {
	}
}
