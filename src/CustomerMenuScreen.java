import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

public class CustomerMenuScreen extends JPanel {
	
	private Screen parent;
	
	private String name, id, pin;
	
	public CustomerMenuScreen(Screen window, String customerName, String customerId, String customerPin) {
		super();
		this.setLayout(new BoxLayout(this, JFrame.EXIT_ON_CLOSE));
		parent = window;
		
		JLabel title = new JLabel("CUSTOMER MENU");
		
		JButton open = new JButton("Open Account >"), 
				close = new JButton("Close Account >"), 
				deposit = new JButton("Deposit Funds >"), 
				withdraw = new JButton("Withdraw Funds >"), 
				transfer = new JButton("Transfer Funds >"), 
				summary = new JButton("View Account Summary >");

		
		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.switchScreen(new OpenAccountScreen(parent, customerId));
			}
		});
		
		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.switchScreen(new CloseAccountScreen(parent, customerId));
			}
		});
		
		deposit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.switchScreen(new DepositScreen(parent));
			}
		});
		
		withdraw.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.switchScreen(new WithdrawScreen(parent, customerId));
			}
		});
		
		transfer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.switchScreen(new TransferScreen(parent, customerId));
			}
		});
		
		summary.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.switchScreen(new AccountSummaryScreen(parent, customerId));
			}
		});
		
		this.add(title);
		this.add(open);
		this.add(close);
		this.add(deposit);
		this.add(withdraw);
		this.add(transfer);
		this.add(summary);
		
		this.add(Screen.backButton(parent));
	}

}
