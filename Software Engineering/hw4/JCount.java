// JCount.java

/*
 Basic GUI/Threading exercise.
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JCount extends JPanel {
	private JTextField count;
	private JLabel currentValue;
	private JButton startButton;
	private JButton stopButton;
	private Worker worker;

	public JCount() {
		// Set the JCount to use Box layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		worker = null;
		count = new JTextField();
		currentValue = new JLabel("0");

		startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(worker != null){
					worker.interrupt();
				}
				currentValue.setText("0");
				int destination;
				if(count.getText().isEmpty()){
					destination = 0;
				}else{
					destination = Integer.parseInt(count.getText());
				}
				worker = new Worker(destination);
				worker.start();
			}
		});

		stopButton = new JButton("Stop");
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(worker != null){
					worker.interrupt();
					worker = null;
				}
			}
		});

		add(count);
		add(currentValue);
		add(startButton);
		add(stopButton);
		add(Box.createRigidArea(new Dimension(0,40)));
	}

	private class Worker extends Thread{
		private int dest;

		public Worker(int num){
			this.dest = num;
		}

		@Override
		public void run(){
			for(int i = 0; i <= dest; i++){
				if(isInterrupted()){
					break;
				}
				if(i % 10000 == 0){
					try {
						sleep(100);
					} catch (InterruptedException e) {
						throw new RuntimeException("interrupted");
					}
					String strI = Integer.toString(i);
					SwingUtilities.invokeLater(() -> currentValue.setText(strI));
				}
			}
			System.out.println("Done");
		}
	}

	static public void main(String[] args)  {
		// Creates a frame with 4 JCounts in it.
		// (provided)
		JFrame frame = new JFrame("The Count");
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

		frame.add(new JCount());
		frame.add(new JCount());
		frame.add(new JCount());
		frame.add(new JCount());

		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

