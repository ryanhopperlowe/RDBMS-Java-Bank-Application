import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.*;


public class HomeScreen extends JPanel {
	
	private JPanel panel = new JPanel();
	private Screen parent;
	
	public HomeScreen(Screen window) {
		super();
		
		parent = window;
		this.setLayout(new BoxLayout(this, JFrame.EXIT_ON_CLOSE));
		
		JLabel title = new JLabel("Welcome to the self-service banking system!");
		this.add(title);
		
		JButton newCustomerButton = new JButton("New Customer >"),
				returningCustomerButton = new JButton("Customer Login >"),
				exitButton = new JButton("Exit");
		
		newCustomerButton.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.switchScreen(new NewCustomerScreen(parent));
			}
		});
		
		returningCustomerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.switchScreen(parent.customerLogin);
			}
		});
		
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.exit();
				return;
			}
		});
		
		this.add(newCustomerButton);
		this.add(returningCustomerButton);
		this.add(exitButton);
	}
}
