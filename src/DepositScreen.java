import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;

import javax.swing.*;

public class DepositScreen extends JPanel {

	private Screen parent;
	
	private String accNo, amount = "0";
	
	private JTextField
			accInput = new JTextField(6),
			amountInput = new JTextField(12);
	
	private JButton submit = new JButton("Deposit >");
	
	private JLabel errbox = new JLabel("");

	public DepositScreen(Screen window) {
		super();
		this.setLayout(new BoxLayout(this, JFrame.EXIT_ON_CLOSE));
		parent = window;
		
		JPanel title = new JPanel(),
				accField = new JPanel(),
				amountField = new JPanel(),
				buttons = new JPanel();
		
		
		
		
		title.add(new JLabel("DEPOSIT FUNDS"));
		
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
		
		amountInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {
				JTextField src = ((JTextField) e.getSource());
				amount = src.getText();
				enableSubmit();
			}
		});
		
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (BankingSystem.getAccountOwner(accNo) != null) {
						BankingSystem.deposit(accNo, amount);
						parent.goBack();
					}
				} catch (SQLException e1) {
					displayError("Account " + accNo + " does not exist!");
				}
			}
		});
		
		accField.add(new JLabel("Account Number: "));
		accField.add(accInput);
		amountField.add(new JLabel("Deposit Amount: "));
		amountField.add(amountInput);
		buttons.add(Screen.backButton(parent));
		buttons.add(submit);
		this.add(title);
		this.add(accField);
		this.add(amountField);
		this.add(buttons);
		this.add(errbox);
		this.setAlignmentX(CENTER_ALIGNMENT);
	}
	
	public void enableSubmit() {
		submit.setEnabled(this.validateInputs());
	}
	
	public boolean validateInputs() {
		try {
			if (accNo == null || amount == null || accNo.equals("") || amount.equals("")) {
				this.displayError("Missing values!");
			} else if (Integer.parseInt(accNo) < 0){
				this.displayError("Account# cannot be negative!");
			} else if (Integer.parseInt(amount) < 0) {
				this.displayError("Deposit amount cannot be negative!");
			} else {
				displayError("");
				return true;
			}
		} catch (NumberFormatException e) {
			displayError("Account# and Amount must be numeric values");
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
