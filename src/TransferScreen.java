import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;

import javax.swing.*;

public class TransferScreen extends JPanel {

	private Screen parent;
	
	private String srcAcc, destAcc, id, amount;
	
	private JTextField srcInput = new JTextField(6),
			destInput = new JTextField(6),
			amountInput = new JTextField(12);
	
	private JButton submit = new JButton("Transfer >");
	private JLabel errbox = new JLabel("");

	public TransferScreen(Screen window, String customerId) {
		super();
		this.setLayout(new BoxLayout(this, JFrame.EXIT_ON_CLOSE));
		parent = window;
		id = customerId;
		
		JPanel title = new JPanel(),
				srcField = new JPanel(),
				destField = new JPanel(),
				amountField = new JPanel(),
				buttons = new JPanel();
		
		
		title.add(new JLabel("TRANSFER FUNDS"));
		
		
		srcInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {
				JTextField src = ((JTextField) e.getSource());
				srcAcc = src.getText();
				enableSubmit();
			}
		});
		
		destInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {
				JTextField src = ((JTextField) e.getSource());
				destAcc = src.getText();
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
					if (BankingSystem.getAccountOwner(srcAcc).equals(id)) {
						
						int balance = BankingSystem.getAccountBalance(srcAcc);
						if (Integer.parseInt(amount) <= balance) {
							if (BankingSystem.getAccountOwner(destAcc) != null) {
								BankingSystem.transfer(srcAcc, destAcc, amount);
								parent.goBack();
							}
						} else
							displayError("Insufficient funds! (Balance: $" + balance + ")");
					} else {
						displayError("You can only withdraw funds from your own accounts!");
					}
				} catch (SQLException e1) {
					displayError("Source and/or destination account number(s) do not exist!");
				}
			}
		});
		
		srcField.add(new JLabel("Source Account Number: "));
		srcField.add(srcInput);
		destField.add(new JLabel("Destination Account Number: "));
		destField.add(destInput);
		amountField.add(new JLabel("Withdraw Amount: "));
		amountField.add(amountInput);
		buttons.add(Screen.backButton(parent));
		buttons.add(submit);
		this.add(title);
		title.setAlignmentX(CENTER_ALIGNMENT);
		this.add(srcField);
		this.add(destField);
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
			if (srcAcc == null || destAcc == null || amount == null || srcAcc.equals("") || destAcc.equals("") || amount.equals("")) {
				this.displayError("Missing values!");
			} else if (Integer.parseInt(srcAcc) < 0){
				this.displayError("Account# cannot be negative!");
			} else if (Integer.parseInt(destAcc) < 0) {
				this.displayError("Account# cannot be negative!");
			} else if (Integer.parseInt(amount) < 0) {
				this.displayError("Transfer amount cannot be negative!");
			} else {
				displayError("");
				return true;
			}
		} catch (NumberFormatException e) {
			displayError("Account numbers and Transfer Amount must be numeric values");
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
