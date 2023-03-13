import java.awt.event.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;

public class NewCustomerScreen extends JPanel {
	
	private Screen parent;
	
	String genderVal, nameVal = "18", ageVal, pinVal;
	
	private JTextField nameInput, pinInput;
	private JSpinner ageInput;
	private JRadioButton male, female;
	private ButtonGroup genderInput;
	private JLabel errbox = new JLabel("");
	private JButton submit;
	
	public NewCustomerScreen(Screen window) {
		super();
						
		this.parent = window;
		
		this.setLayout(new BoxLayout(this, JFrame.EXIT_ON_CLOSE));
				
		JLabel title = new JLabel("New Customer Registration");
		this.add(title);
		title.setAlignmentX(CENTER_ALIGNMENT);
		
		JLabel nameLabel = new JLabel("Name:"), 
				ageLabel = new JLabel("Age:"), 
				genderLabel = new JLabel("Gender:"), 
				pinLabel = new JLabel("Pin #:");
		
		nameInput = new JTextField(12);
		nameInput.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {
				JTextField src = ((JTextField) e.getSource());
				String text = src.getText();
				nameVal = text;
				enableSubmit();
			}
		});
		
		ageInput = new JSpinner();
		ageInput.setValue(18);
		
		ageInput.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner src = (JSpinner) e.getSource();
				int val = (int) src.getValue();
				ageVal = Integer.toString(val);
				enableSubmit();
			}
		});
		
		
		genderInput = new ButtonGroup();
		male = new JRadioButton("Male");
		female = new JRadioButton("Female");
		genderInput.add(male);
		genderInput.add(female);
		
		male.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				genderVal = "M";
				enableSubmit();
			}
		});
		
		female.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				genderVal = "F";
				enableSubmit();
			}
		});
		
		pinInput = new JTextField(6);
		pinInput.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}
			@Override
			public void keyPressed(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {
				JTextField src = ((JTextField) e.getSource());
				pinVal = src.getText();
				enableSubmit();
			}
		});
		
		
		JPanel name = new JPanel(),
				age = new JPanel(), 
				gender = new JPanel(),
				pin = new JPanel(),
				buttons = new JPanel();
		
		name.add(nameLabel);
		name.add(nameInput);
		this.add(name);
		
		age.add(ageLabel);
		age.add(ageInput);
		this.add(age);

		gender.add(genderLabel);
		gender.add(male);
		gender.add(female);
		this.add(gender);
		
		pin.add(pinLabel);
		pin.add(pinInput);
		this.add(pin);
		
		submit = new JButton("Submit >");
		submit.setEnabled(false);
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BankingSystem.newCustomer(nameVal, genderVal, ageVal, pinVal);
				parent.goBack();
			}
		});
		
		buttons.add(Screen.backButton(parent));
		buttons.add(submit);
		
		this.add(buttons);
		
		this.add(errbox);
		this.setAlignmentX(CENTER_ALIGNMENT);
	}
	
	public boolean enableSubmit() {
		boolean valid = validateInputs();
		submit.setEnabled(valid);
		return valid;
	}
	
	public boolean validateInputs() {
				
		try {
			if (nameVal == null || ageVal == null || genderVal == null || pinVal == null) {
				displayError("Missing values");
			} else if (nameVal.length() > 15 || nameVal.length() < 1) {
				displayError("Error: Name length must be between 1 and 15 characters");
			} else if (Integer.parseInt(ageVal) < 1) {
				displayError("Error: Cannot be < 1 years old!");
			} else if (Integer.parseInt(pinVal) < 1) {
				displayError("Error: ");
			} else {
				displayError("");
				return true;
			}
		}
		catch(NumberFormatException e) {
			displayError("Error: Pin must be numeric!");
			return false;
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
