import javax.swing.*;

public class AccountSummaryScreen extends JPanel {

	private Screen parent;
	private String id;

	public AccountSummaryScreen(Screen window, String customerId) {
		super();
		this.setLayout(new BoxLayout(this, JFrame.EXIT_ON_CLOSE));
		parent = window;
		id = customerId;
		
		BankingSystem.accountSummary(id);
	
		this.add(Screen.backButton(parent));
	}

}
