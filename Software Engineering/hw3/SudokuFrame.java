import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import java.awt.*;

public class SudokuFrame extends JFrame {
	JTextArea puzzle;
	JTextArea solution;

	/**
	 * What should happen when 'auto check' is selected.
	 */
	public void solveP(){
		String result = "";
		try{
			Sudoku sudoku = new Sudoku(Sudoku.textToGrid(puzzle.getText()));
			int numSolutions = sudoku.solve();
			if(numSolutions >= 1){
				result = sudoku.getSolutionText() + "\nSolutions: " + numSolutions
						+ "\nElapsed: " + sudoku.getElapsed() + "ms\n";
			}
			if(numSolutions == -1){
				result = "Incorrect Input";
			}
			solution.setText(result);
		}catch(Exception e){
			solution.setText("Parsing Problem");
		}
	}

	/**
	 * Sets up frame for the program.
	 */
	public SudokuFrame() {
		super("Sudoku Solver");
		setLayout(new BorderLayout(4, 4));

		puzzle = new JTextArea(15, 20);
		puzzle.setBorder(new TitledBorder("Puzzle"));
		puzzle.setEditable(true);

		solution = new JTextArea(15, 20);
		solution.setBorder(new TitledBorder("Solution"));
		solution.setEditable(false);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		JButton button = new JButton("Check");
		JCheckBox checkBox = new JCheckBox("Auto Check");
		checkBox.setSelected(true);

		panel.add(button);
		panel.add(checkBox);

		button.addActionListener(e -> solveP());
		puzzle.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				if(checkBox.isSelected()) solveP();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				if(checkBox.isSelected()) solveP();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				if(checkBox.isSelected()) solveP();
			}
		});

		add(puzzle, BorderLayout.CENTER);
		add(solution, BorderLayout.EAST);
		add(panel, BorderLayout.SOUTH);

		setLocationByPlatform(true);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}


	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }

		SudokuFrame frame = new SudokuFrame();
		frame.setVisible(true);
	}

}