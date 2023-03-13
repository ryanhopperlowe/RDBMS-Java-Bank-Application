import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


public class ReportAScreen extends JPanel {

	private Screen parent;

	public ReportAScreen(Screen window) {
		super();
		this.setLayout(new BoxLayout(this, JFrame.EXIT_ON_CLOSE));
		parent = window;
		
		JPanel title = new JPanel();
		title.add(new JLabel("REPORT A"));
		
		BankingSystem.reportA();
		
		this.add(Screen.backButton(parent));
	}

}
