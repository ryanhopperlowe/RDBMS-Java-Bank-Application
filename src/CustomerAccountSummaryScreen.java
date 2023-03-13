import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import javax.swing.*;


public class CustomerAccountSummaryScreen extends JPanel {

	private Screen parent;
	
	private String id;
	
	private JTextField accInput = new JTextField(6);
	private JButton submit = new JButton("Submit >");
	private JLabel errbox = new JLabel("");
	
	public CustomerAccountSummaryScreen(Screen window) {
		super();
		this.setLayout(new BoxLayout(this, JFrame.EXIT_ON_CLOSE));
		parent = window;
		
		JPanel title = new JPanel(),
				accField = new JPanel(),
				buttons = new JPanel();
		
		accInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {
				JTextField src = ((JTextField) e.getSource());
				id = src.getText();
				enableSubmit();
			}
		});
		
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					BankingSystem.customerIDExists(id);
					BankingSystem.accountSummary(id);
				} catch (SQLException x) {
					displayError("Customer ID " + id + " was not found!");
				}
			}
		});
		
		
		
		title.add(new JLabel("ACCOUNT SUMMARY FOR CUSTOMER"));
		accField.add(new JLabel("Customer ID: "));
		accField.add(accInput);
		buttons.add(Screen.backButton(parent));
		buttons.add(submit);
		this.add(title);
		this.add(accField);
		this.add(buttons);
		this.add(errbox);
		this.setAlignmentX(CENTER_ALIGNMENT);

	}

	
	public void enableSubmit() {
		submit.setEnabled(this.validateInputs());
	}
	
	public boolean validateInputs() {
		try {
			if (id == null || id.equals("")) {
				displayError("Missing Values!");
			} else if (Integer.parseInt(id) < 0) {
				displayError("Account number cannot be negative!");
			} else {
				displayError("");
				return true;
			}
		} catch (NumberFormatException e) {
			displayError("Account number must be numeric!");
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
