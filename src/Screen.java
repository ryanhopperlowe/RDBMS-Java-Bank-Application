import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;

public class Screen {
	
	private Stack<JPanel> history = new Stack<JPanel>();
	private JPanel currentScreen;
	
	public JPanel home = new HomeScreen(this);
	public JPanel newCustomer = new NewCustomerScreen(this);
	public JPanel customerLogin = new CustomerLoginScreen(this);
	
	public JFrame frame;
	
	private boolean isValid;
	
	
	public Screen() {
		frame = new JFrame();
		
//		frame.setSize(500, 500);
		Rectangle r = new Rectangle(300, 1000);
		frame.setMaximizedBounds(r);
		
		switchScreen(home);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	
	
	public void switchScreen(JPanel newScreen) {
		if (currentScreen != null) {
			history.push(currentScreen);
			frame.remove(currentScreen);
		}
		System.out.println("Switch screen: " + newScreen);
	
		frame.add(newScreen);
		currentScreen = newScreen;
		frame.repaint();
		frame.pack();
	}
	
	public void goBack() {
		if (history.peek() != null) {
			frame.remove(currentScreen);
			currentScreen = history.pop();
			frame.add(currentScreen);
			frame.repaint();
			frame.pack();
		}
	}
	
	public void exit() {
		this.frame.setVisible(false);
	}
	
	
	public static void main(String[] argv) {
		
		if (argv.length < 1) {
			BankingSystem.init("db.properties");
			BankingSystem.testConnection();
			BatchInputProcessor.run("db.properties");
		} else {
			BankingSystem.init(argv[0]);
			BankingSystem.testConnection();
//			BatchInputProcessor.run(argv[0]);
			Screen s = new Screen();
		}
	}
	
	public static JButton backButton(Screen parent) {
		JButton back = new JButton("< Back");
		
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.goBack();
			}
		});
		
		return back;
	}
		
}
