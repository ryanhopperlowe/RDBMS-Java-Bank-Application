import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import javax.swing.*;
import javax.swing.event.*;

public class CustomerLoginScreen extends JPanel {
	
	private Screen parent;
	
	private String id, pin;
	
	private JTextField idInput, pinInput;
	private JLabel errbox;
	private JButton submit, back;

	public CustomerLoginScreen(Screen window) {
		super();
		this.setLayout(new BoxLayout(this, JFrame.EXIT_ON_CLOSE));
		parent = window;
		
		JLabel title = new JLabel("Customer Login");
		this.add(new JPanel().add(title));
		
		JPanel idField = new JPanel(),
				pinField = new JPanel(),
				buttons = new JPanel();
		JLabel idLabel = new JLabel("Please enter your Customer ID: "),
				pinLabel = new JLabel("Please enter your Pin#: ");
		
		idInput = new JTextField(5);
		pinInput = new JTextField(5);
		submit = new JButton("Submit >");
		back = new JButton("< Back");
		
		idInput.setText("");
		pinInput.setText("");
		
		submit.setEnabled(false);
		
		idInput.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				JTextField src = ((JTextField) e.getSource());
				id = src.getText();
				enableSubmit();
			}
			
		});
		
		pinInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {
				JTextField src = ((JTextField) e.getSource());
				pin = src.getText();
				enableSubmit();
			}
			
		});
		
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.goBack();
			}
		});
		
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = null;
				try {
					if (id.trim().equals("0") && pin.trim().equals("0")) {
						parent.switchScreen(new AdminMenuScreen(parent));
					} else if ((name = BankingSystem.login(id, pin)) != null) {
						parent.goBack();
						parent.switchScreen(new CustomerMenuScreen(parent, name, id, pin));
					}
				} catch (SQLException x) {
					displayError("invalid id/pin!");
				}
			}
		});
		
		
		errbox = new JLabel("");
		
		idField.add(idLabel);
		idField.add(idInput);
		pinField.add(pinLabel);
		pinField.add(pinInput);
		buttons.add(back);
		buttons.add(submit);
		this.add(idField);
		this.add(pinField);
		this.add(buttons);
		this.add(new JPanel().add(errbox));
		this.setAlignmentX(CENTER_ALIGNMENT);
	}
	
	public void enableSubmit() {
		submit.setEnabled(validateInputs());
	}
	
	public boolean validateInputs() {
		try {
			if (id == null || pin == null) {
				displayError("Missing Values!");
			} else {
				displayError("");
				return true;
			}
		}
		catch(NumberFormatException e) {
			displayError("Error: Pin must be numeric!");
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
