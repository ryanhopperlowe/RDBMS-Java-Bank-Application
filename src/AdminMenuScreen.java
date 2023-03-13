import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class AdminMenuScreen extends JPanel {
	
	private Screen parent;

	public AdminMenuScreen(Screen window) {
		super();
		this.setLayout(new BoxLayout(this, JFrame.EXIT_ON_CLOSE));
		parent = window;
		
		JLabel title = new JLabel("ADMIN MENU");
		
		JButton custAccSummaryButton = new JButton("Customer Account Summary >"),
				reportAButton = new JButton("Report A >"),
				reportBButton = new JButton("Report B >");
		
		custAccSummaryButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.switchScreen(new CustomerAccountSummaryScreen(parent));
			}
		});
		
		reportAButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.switchScreen(new ReportAScreen(parent));
			}
		});
		
		reportBButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.switchScreen(new ReportBScreen(parent));
			}
		});
		
		
		this.add(new JPanel().add(title));
		this.add(custAccSummaryButton);
		this.add(reportAButton);
		this.add(reportBButton);
		this.add(Screen.backButton(parent));
	}

}
