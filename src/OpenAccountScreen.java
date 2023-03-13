import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

import javax.swing.*;

public class OpenAccountScreen extends JPanel {

	private Screen parent;
	
	private String id, type, deposit;
	
	private JTextField 
			idInput = new JTextField(6),
			depositInput = new JTextField(12);
	
	private JRadioButton 
			checking = new JRadioButton("Checking"), 
			savings = new JRadioButton("Savings");
	
	private JButton submit = new JButton("Open Account >");
	private JLabel errbox = new JLabel("");

	public OpenAccountScreen(Screen window, String customerId) {
		super();
		this.setLayout(new BoxLayout(this, JFrame.EXIT_ON_CLOSE));
		parent = window;
		
		id = customerId;
		deposit = "0";
		
		JPanel idField = new JPanel(),
				typeField = new JPanel(),
				depositField = new JPanel(),
				buttons = new JPanel();
		
		JLabel title = new JLabel("Open New Account");
		
		
		JLabel idLabel = new JLabel("Customer ID#: "),
				typeLabel = new JLabel("Account Type: "),
				depositLabel = new JLabel("Initial Deposit ($): ");
		
		ButtonGroup typeInput = new ButtonGroup();
		typeInput.add(checking);
		typeInput.add(savings);
		
		
		idField.add(idLabel);
		idField.add(idInput);
		typeField.add(typeLabel);
		typeField.add(checking);
		typeField.add(savings);
		depositField.add(depositLabel);
		depositField.add(depositInput);
		buttons.add(Screen.backButton(parent));
		buttons.add(submit);
		
		idInput.addKeyListener(new KeyListener() {
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
		
		checking.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				type = "C";
				enableSubmit();
			}
		});
		
		savings.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				type = "S";
				enableSubmit();
			}
		});
		
		depositInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {
				JTextField src = ((JTextField) e.getSource());
				deposit = src.getText();
				enableSubmit();
			}
		});
		
		
		idInput.setText(id);
		depositInput.setText(deposit);
		
		
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					BankingSystem.customerIDExists(id);
					BankingSystem.openAccount(id, type, deposit);
					parent.goBack();
				} catch (SQLException e1) {
					errbox.setText("Error: Customer ID#: " + id + " does not exist!");
					repaint();
					parent.frame.pack();
				}
			}
		});
		
		this.add(title);
		title.setAlignmentX(CENTER_ALIGNMENT);
		this.add(idField);
		this.add(typeField);
		this.add(depositField);
		this.add(buttons);
		this.add(errbox);
		this.setAlignmentX(CENTER_ALIGNMENT);

	}
	
	public void enableSubmit() {
		submit.setEnabled(this.validateInputs());
	}
	
	public boolean validateInputs() {
		try {
			if (id == null || deposit == null || type == null) {
				displayError("Missing values!");
			} else if (Integer.parseInt(id) < 100) {
				displayError("Customer id must be > 100!");
			} else if (Integer.parseInt(deposit) < 0) {
				displayError("Initial deposit cannot be < 0");
			} else {
				displayError("");
				return true;
			}
		} catch (NumberFormatException e) {
			displayError("Values for customer ID, and Initial deposit must be numeric!");
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
