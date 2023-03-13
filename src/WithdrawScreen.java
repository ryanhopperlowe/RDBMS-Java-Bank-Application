import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;

import javax.swing.*;

public class WithdrawScreen extends JPanel {

	private Screen parent;
	
	private String accNo, id, amount;
	
	private JTextField accInput = new JTextField(6),
			amountInput = new JTextField(12);
	
	private JButton submit = new JButton("Withdraw >");
	private JLabel errbox = new JLabel("");

	public WithdrawScreen(Screen window, String customerId) {
		super();
		this.setLayout(new BoxLayout(this, JFrame.EXIT_ON_CLOSE));
		parent = window;
		id = customerId;
		
		JPanel title = new JPanel(),
				accField = new JPanel(),
				amountField = new JPanel(),
				buttons = new JPanel();
		
		
		title.add(new JLabel("WITHDRAW FUNDS"));
		
		
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
					if (BankingSystem.getAccountOwner(accNo).equals(id)) {
						
						int balance = BankingSystem.getAccountBalance(accNo);
						
						if (Integer.parseInt(amount) <= balance) {
							BankingSystem.withdraw(accNo, amount);
							parent.goBack();
						} else
							displayError("Insufficient funds! (Balance: $" + balance + ")");
					} else {
						displayError("You can only withdraw funds from your own accounts!");
					}
				} catch (SQLException e1) {
					displayError("Account " + accNo + " does not exist!");
				}
			}
		});
		
		accField.add(new JLabel("Account Number: "));
		accField.add(accInput);
		amountField.add(new JLabel("Withdraw Amount: "));
		amountField.add(amountInput);
		buttons.add(Screen.backButton(parent));
		buttons.add(submit);
		this.add(title);
		title.setAlignmentX(CENTER_ALIGNMENT);
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
