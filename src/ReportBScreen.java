import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;


public class ReportBScreen extends JPanel {

	private Screen parent;
	
	private Integer minAge, maxAge;
	private JSpinner 
			minInput = new JSpinner(),
			maxInput = new JSpinner();
	private JButton submit = new JButton("Run >");
	private JLabel errbox = new JLabel("");

	public ReportBScreen(Screen window) {
		super();
		this.setLayout(new BoxLayout(this, JFrame.EXIT_ON_CLOSE));
		parent = window;
		
		JPanel title = new JPanel(),
				minField = new JPanel(),
				maxField = new JPanel(),
				buttons = new JPanel();
		
		title.setLayout(new BoxLayout(title, JFrame.EXIT_ON_CLOSE));
		
		minInput.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner src = (JSpinner) e.getSource();
				minAge = (Integer) src.getValue();
				enableSubmit();
			}	
		});
		
		maxInput.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner src = (JSpinner) e.getSource();
				maxAge = (Integer) src.getValue();
				enableSubmit();
			}	
		});
		
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BankingSystem.reportB(Integer.toString(minAge), Integer.toString(maxAge));
			}
		});
	
		
		title.add(new JLabel("REPORT B"));
		title.add(new JLabel("Compute the average total balance between selected age range"));
		minField.add(new JLabel("Min Age: "));
		minField.add(minInput);
		maxField.add(new JLabel("Max Age: "));
		maxField.add(maxInput);
		buttons.add(Screen.backButton(parent));
		buttons.add(submit);
		this.add(title);
		this.add(minField);
		this.add(maxField);
		this.add(buttons);
		this.add(errbox);
		this.setAlignmentX(CENTER_ALIGNMENT);
	}

	public void enableSubmit() {
		submit.setEnabled(this.validateInputs());
	}
	
	public boolean validateInputs() {
		if (minAge == null || maxAge == null) {
			displayError("Missing Values!");
		} else if (maxAge < minAge) {
			displayError("Min age cannot be greater than Min Age!");
		} else if (minAge < 0 || maxAge < 0) {
			displayError("Age cannot be negative!");
		} else {
			displayError("");
			return true;
		}
	
		return false;
	}
	
	public void displayError(String message) {
		if (message.equals("")) 
			errbox.setText("");
		else
			errbox.setText("Error: " + message);
		errbox.setAlignmentX(CENTER_ALIGNMENT);
		this.repaint();
		parent.frame.pack();
	}
}
