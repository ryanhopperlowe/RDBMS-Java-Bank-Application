import java.awt.event.*;
import java.sql.SQLException;

import javax.swing.*;

public class CloseAccountScreen extends JPanel {

	private Screen parent;
	
	private String id, accNo;
	
	private JTextField accInput = new JTextField(6);
	private JLabel errbox = new JLabel("");
	private JButton back,
			submit = new JButton("Close Account >");

	public CloseAccountScreen(Screen window, String customerId) {
		super();
		this.setLayout(new BoxLayout(this, JFrame.EXIT_ON_CLOSE));
		parent = window;
		
		id = customerId;
		
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
				accNo = src.getText();
				enableSubmit();
			}
		});
		
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (BankingSystem.getAccountOwner(accNo).trim().equals(id.trim())) {
						BankingSystem.closeAccount(accNo);
						parent.goBack();
					}
					else
						displayError("Error: You can only close accounts that belong to you");
				} catch (SQLException e1) {
					displayError("Error: Account#: " + accNo + " not found!");
				}
				repaint();
				parent.frame.pack();
			}
		});
		
		back = Screen.backButton(parent);
		
		title.add(new JLabel("CLOSE ACCOUNT"));
		accField.add(new JLabel("Account Number: "));
		accField.add(accInput);
		buttons.add(back);
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
			if (accNo == null || accNo.contentEquals("")) {
				displayError("Missing Inputs");
			} else {
				Integer.parseInt(accNo);
				displayError("");
				this.repaint();
				parent.frame.pack();
				return true;
			}
		}
		catch(NumberFormatException e) {
			displayError("Error: Account Number must be numeric!");
		}
		
		this.repaint();
		parent.frame.pack();
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
