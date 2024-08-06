
/*
 * File: NameSurfer.java
 * ---------------------
 * When it is finished, this program will implements the viewer for
 * the baby-name database described in the assignment handout.
 */

import acm.program.*;
import java.awt.event.*;
import javax.swing.*;


public class NameSurfer extends Program implements NameSurferConstants {

	/* Method: init() */
	/**
	 * This method has the responsibility for reading in the data base and
	 * initializing the interactors at the bottom of the window.
	 */
	private JTextField namesField;
	private JButton graphButton, clearButton;
	private JLabel label;
	private int MAX = 25; // Maximum length of entered name.
	private NameSurferDataBase dataBase = new NameSurferDataBase(NAMES_DATA_FILE);
	private NameSurferGraph graph;

	public void init() {
		graph = new NameSurferGraph();
		add(graph);

		label = new JLabel("Name:");
		add(label, SOUTH);

		namesField = new JTextField(MAX);
		namesField.addActionListener(this); // detects ENTER press.

		graphButton = new JButton("Graph");
		clearButton = new JButton("Clear");

		add(namesField, SOUTH);
		add(graphButton, SOUTH);
		add(clearButton, SOUTH);

		addActionListeners();
	}

	/* Method: actionPerformed(e) */
	/**
	 * This class is responsible for detecting when the buttons are clicked, so you
	 * will have to define a method to respond to button actions.
	 */
	// Draw entered name data on canvas if you press ENTER or click "Graph". Clear
	// all names if you click "Clear".
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();

		if (src == graphButton || src == namesField) {
			if (namesField.getText() != null) {
				NameSurferEntry entry = dataBase.findEntry(namesField.getText());
				if (entry != null) {
					graph.addEntry(entry);
					graph.update();
					namesField.setText("");
				} else {
					namesField.setText("");
				}

			}

		} else if (src == clearButton) {
			graph.clear();
			graph.update();
		}
	}
}
