import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

/**
 * Manage connection to database and perform SQL statements.
 */
public class BankingSystem {
	// Connection properties
	private static String driver;
	private static String url;
	private static String username;
	private static String password;
	
	// JDBC Objects
	private static Connection con;
	private static Statement stmt;
	private static ResultSet rs;

	/**
	 * Initialize database connection given properties file.
	 * @param filename name of properties file
	 */
	public static void init(String filename) {
		try {
			Properties props = new Properties();						// Create a new Properties object
			FileInputStream input = new FileInputStream(filename);	// Create a new FileInputStream object using our filename parameter
			props.load(input);										// Load the file contents into the Properties object
			driver = props.getProperty("jdbc.driver");				// Load the driver
			url = props.getProperty("jdbc.url");						// Load the url
			username = props.getProperty("jdbc.username");			// Load the username
			password = props.getProperty("jdbc.password");			// Load the password
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test database connection.
	 */
	public static void testConnection() {
		System.out.println(":: TEST - CONNECTING TO DATABASE");
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			con.close();
			System.out.println(":: TEST - SUCCESSFULLY CONNECTED TO DATABASE");
			} catch (Exception e) {
				System.out.println(":: TEST - FAILED CONNECTED TO DATABASE");
				e.printStackTrace();
			}
	}
	
	/**
	 * scan user input
	 * @return String of the user's input
	 * @return null if the input equals "exit"
	 */
	private static String getInput() {
		Scanner scan = new Scanner(System.in);
		String input = scan.nextLine();
		if (input.toLowerCase().trim().equals("exit"))
			return null;
		return input;
	}
	
	/**
	 * Print invalid entry and delay .5 seconds
	 */
	private static void invalidEntry() {
		sopln("Invalid Entry!\n");
		wait(500);
	}
	
	/**
	 * causes a timed delay
	 * @param milliseconds
	 */
	private static void wait(int milliseconds) {
		try {
			TimeUnit.MILLISECONDS.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Prompt user for input
	 * @param prompt is the string to print before taking user input
	 * @return trimmed user input
	 * @return null if there is no user input
	 */
	private static String promptCustomerInput(String prompt) {
		sopln("\n" + prompt);
		String input = getInput();
		if (input == null)
			return null;
		return input.trim();
	}
	
	/**
	 * homescreen activity
	 */
	public static void homescreen() {
		boolean done = false;
		
		sopln("\nWelcome to the self-services banking system!\n");
		
		while (!done) {
			sopln("\nMAIN MENU-----\n"
					+ "1. \tNew Customer\n"
					+ "2. \tCustomer Login\n"
					+ "3. \tExit\n");
			
			sop("Entry: ");
			String input = getInput();
			if (input == null)
				return;
			
			switch (input) {
			
			case "1":
				newCustomerScreen();
				break;
			case "2":
				customerLoginScreen();
				break;
			case "3":
				done = true;
				break;
			default:
				sopln("invalid input. Please select a valid number.");
				break;
			}
		}
	}
	
	/**
	 * New Customer Registration activity
	 * prompts customer for:
	 * name, gender, age, and pin
	 * prints their unique customer id on successful registration
	 */
	public static void newCustomerScreen() {
		Scanner scan = new Scanner(System.in);
		boolean done = false;
		
		String name = "", gender = "";
		int age = 0, pin = 0;
		
		sopln("\nNEW CUSTOMER-----\n"
				+ "type 'exit' at any time to return home.");
		
		while (!done) {
			name = promptCustomerInput("Please Enter your name: ");
			if (name == null)
				return;
			
			if (name.length() <= 15 && !name.equals("")) 
				done = true;
			else
				invalidEntry();
		}
		
		done = false;
		while (!done) {
			String genderInput = promptCustomerInput("Please enter your gender (M or F): ");
			if (genderInput == null)
				return;
			
			gender = genderInput.toUpperCase().substring(0, 1);
			switch (gender) {
			
			case "M":
				done = true;
				break;
			case "F":
				done = true;
				break;
			default:
				invalidEntry();
			}
		}
		
		done = false;
		while (!done) {
			String ageInput = promptCustomerInput("Please enter your age (must be 18 or over): ");
			if (ageInput == null)
				return;
			
			try {
				age = Integer.parseInt(ageInput);
				if (age < 18)
					invalidEntry();
				else
					done = true;
			}	
			catch(NumberFormatException e) {
				invalidEntry();
			}
		}
		
		done = false;
		while (!done) {
			String pinInput = promptCustomerInput("Please create a pin: ");
			if (pinInput == null)
				return;
			try {
				pin = Integer.parseInt(pinInput);
				if (pin < 1)
					invalidEntry();
				else
					done = true;
			}
			catch(NumberFormatException e) {
				invalidEntry();
			}
		}
		newCustomer(name, gender, Integer.toString(age), Integer.toString(pin));
	}
	
	/**
	 * Returning Customer Login Activity
	 * prompts user for customer id and pin
	 */
	public static void customerLoginScreen() {
		boolean done = false;
		String id = "", pin = "";
		
		while (!done) {
			sopln("\nCUSTOMER LOGIN ------");
			sopln("Enter Customer ID and Pin #.");
			sopln("Type 'exit' at any time to return home");
			
			id = promptCustomerInput("\nPlease Enter Customer ID:");
			if (id == null)
				return;
			id = id.trim();
			
			pin = promptCustomerInput("\nPlease enter your pin #:");
			if (pin == null)
				return;
			pin = pin.trim();
			
			if (id.equals("0") && pin.equals("0")) {
				adminMenuScreen();
				done = true;
			} else {
				try {
					if (login(id, pin) != null) {
						done = true;
						profileScreen(id, pin);
					} else {
						invalidEntry();
					}
				}
				catch (SQLException e) {
					sopln("Incorrect ID and/or pin#\n");
				}
			}
		}
	}
	
	/**
	 * Main Menu for logged in customers
	 * @param customerId id of logged in customer
	 * @param customerPin pin of logged in customer
	 */
	public static void profileScreen(String customerId, String customerPin) {
		boolean done = false;
		
		while (!done) {
			sopln("\nCUSTOMER PROFILE-----\n"
					+ "Select option or type 'exit' at any time to return home.\n");
			
			sopln("1. \tOpen Account\n"
					+ "2. \tClose Account\n"
					+ "3. \tDeposit\n"
					+ "4. \tWithdraw\n"
					+ "5. \tTransfer\n"
					+ "6. \tAccount Summary\n"
					+ "7. \tExit\n");
			sop("Entry: ");
			String input = getInput();
			if (input == null)
				return;
			
			switch (input.trim()) {
			
			case "1":
				openAccountScreen();
				break;
			case "2":
				closeAccountScreen(customerId);
				break;
			case "3":
				depositScreen();
				break;
			case "4":
				withdrawScreen(customerId);
				break;
			case "5":
				transferScreen(customerId);
				break;
			case "6":
				accountSummaryScreen(customerId);
				break;
			case "7":
				done = true;
				break;
			default:
				invalidEntry();
				break;
			}
		}
		
	}
	
	/**
	 * Admin menu activity
	 */
	public static void adminMenuScreen() {
		boolean done = false;
		while (!done) {
			sopln("\nADMIN MENU-----");
			sopln("Select option or type 'exit' at any time to return home.");
			
			sopln("1. \tAccount Summary for Customer\n"
					+ "2. \tReport A: Customer Information with total balance in decreasing order\n"
					+ "3. \tReport B: Average total balance between age groups\n"
					+ "4. \tExit\n");
			sop("Entry: ");
			String input = getInput();
			if (input == null)
				return;
			
			switch (input) {
			
			case "1":
				accountSummaryForCustomerScreen();
				break;
			case "2":
				reportAScreen();
				break;
			case "3":
				reportBScreen();
				break;	
			case "4":
				done = true;
				break;
			default:
				invalidEntry();
				break;
			}
		}
	}
	
	/**
	 * Show's any customer's list of accounts
	 * admin only
	 */
	public static void accountSummaryForCustomerScreen() {
		boolean done = false;
		String customerId = "";
		while (!done) {
			sopln("\nACCOUNT SUMMARY FOR CUSTOMER-----");
			sopln("Type 'exit' at any time to return home.\n");
			customerId = promptCustomerInput("Please enter customer ID: "); 
			if (customerId == null)
				return;
			
			try {
				customerIDExists(customerId);
				done = true;
			}
			catch (SQLException e) {
				sopln("Error: Customer ID# " + customerId + " was not found!");
			}
		}
		
		accountSummaryScreen(customerId);
	}
	
	public static void reportAScreen() {
		sopln("\nREPORT A-----");
		sopln("Display all customer information in descending order of total funds across all accounts.");
		sopln("Type 'exit' at any time to go back.\n");
		
		reportA();
		promptCustomerInput("Press 'Enter' to return to admin menu.");
	}
	
	public static void reportBScreen() {
		boolean done = false;
		String min = "", max = "";
		
		sopln("\nREPORT B-----");
		sopln("Get the average account balance by specified age range.");
		sopln("Type 'exit' at any time to go back.\n");
		
		while (!done) {
			min = promptCustomerInput("Please enter minimum age: ");
			if (min == null)
				return;
			try {
				int minAge = Integer.parseInt(min);
				if (minAge < 18)
					sopln("Error: age range must start higher than 18.");
				else
					done = true;
			}
			catch (NumberFormatException e) {
				invalidEntry();
			}
		}
		
		done = false;
		while (!done) {
			max = promptCustomerInput("Please enter maximum age: ");
			if (max == null)
				return;
			
			try {
				int maxAge = Integer.parseInt(max);
				if (maxAge < 18)
					sopln("Error: age range must start higher than 18.");
				else
					done = true;
			}
			catch (NumberFormatException e) {
				invalidEntry();
			}
		}
		
		reportB(min, max);
		promptCustomerInput("Press 'Enter' to return to admin menu:");
	}
	
	public static void openAccountScreen() {
		boolean done = false;
		String id = "", type = "", balance = "";
		
		while (!done) {
			sopln("\nOPEN ACCOUNT-----");
			sopln("Type 'exit' at any time to return to customer profile.");
			
			id = promptCustomerInput("Please enter customer ID");
			if (id == null)
				return;
			
			type = promptCustomerInput("Please enter account type (type 'c' for checking, 's' for savings): ");
			if (type == null)
				return;
			
			balance = promptCustomerInput("Please enter the amount for the initial deposit: ");
			if (balance == null)
				return;
			
			openAccount(id, type, balance);
			done = true;
		}
	}
	
	public static void closeAccountScreen(String currentId) {
		boolean done = false;
		String number = "";
		
		sopln("\nCLOSE ACCOUNT-----");
		sopln("Type 'exit' at any time to return to customer profile.");
		
		while (!done) {
			number = promptCustomerInput("Pleases enter the account number: ");
			if (number == null)
				return;
			
			try {
				if (!getAccountOwner(number).equals(currentId))
					sopln("Sorry, you cannot close other people's accounts!");
				else
					done = true;
			}
			catch(SQLException e) {
				sopln("Error: Account # " + number + " not found!\n");
			}
		}
		
		closeAccount(number);
	}
	
	public static void depositScreen() {
		boolean done = false;
		String number = "", amount = "";
		
		sopln("\nDEPOSIT-----");
		sopln("Type 'exit' at any time to return to customer profile.");
		
		while (!done) {
			number = promptCustomerInput("Please enter account number: ");
			if (number == null)
				return;
			
			try {
				getAccountOwner(number);
				done = true;
			}
			catch(SQLException e) {
				sopln("Error: Account # " + number + " not found!\n");
			}
		}
		
		amount = promptCustomerInput("Please enter amount to deposit: ");
		if (amount == null)
			return;
		
		deposit(number, amount);
	}
	
	public static void withdrawScreen(String currentId) {
		boolean done = false;
		String number = "", amount = "";
		sopln("\nWITHDRAW-----");
		sopln("Type 'exit' at any time to return to customer profile.");
		
		while (!done) {
			number = promptCustomerInput("Please enter account number: ");
			if (number == null)
				return;
			
			try {
				if (!getAccountOwner(number).equals(currentId))
					sopln("Sorry, you can only withdraw funds from your own accounts!");
				else
					done = true;
			} catch (SQLException e) {
				sopln("Error: account# " + number + " was not found.");
			}
		}
		
		done = false;
		while (!done) {
			amount = promptCustomerInput("Please enter amount to withdraw: ");
			if (amount == null)
				return;
			
			try {
				int balance = getAccountBalance(number);
				if (Integer.parseInt(amount) > balance)
					sopln("Error: withdraw amount (" + amount + ") is greater than account balance (" + balance + ").");
				else
					done = true;
			}
			catch (SQLException e) {
				sopln("Error: account# " + number + " was not found.");
			}
		}
		
		
		
		withdraw(number, amount);
	}
	
	public static void transferScreen(String currentId) {
		boolean done = false;
		String source = "", dest = "", amount = "";
		
		sopln("\nTRANSFER-----");
		sopln("Type 'exit' at any time to return to customer profile.");
		
		while (!done) {
			source = promptCustomerInput("Please enter the source account#: ");
			if (source == null)
				return;
			
			try {
				int balance = getAccountBalance(source);
				
				if (!getAccountOwner(source).equals(currentId))
					sopln("Sorry, you can only withdraw funds from your own accounts!");
				else
					done = true;
			}
			catch (SQLException e) {
				sopln("Error: account# " + source + " was not found.");
			}
		}
		
		done = false;
		while (!done) {
			dest = promptCustomerInput("Please enter the destination account#: ");
			if (dest == null)
				return;
			
			try {
				getAccountOwner(dest);
				done = true;
			}
			catch (SQLException e) {
				sopln("Error: account# " + source + " was not found.");
			}
		}
		
		done = false;
		while (!done) {
			amount = promptCustomerInput("Please enter the amount to be transferred: ");
			if (amount == null)
				return;
			
			try {
				int balance = getAccountBalance(source);
				if (Integer.parseInt(amount) > balance)
					sopln("Error: withdraw amount (" + amount + ") cannot be higher than the balance (" + balance + ")!");
				else
					done = true;
			}
			catch (SQLException e) {
				sopln("Error: account# " + source + " was not found.");
			}
		}
		
		transfer(source, dest, amount);
	}
	
	public static void accountSummaryScreen(String currentId) {
		accountSummary(currentId);
		String input = promptCustomerInput("Press 'Enter' to go back: ");
	}
	
	public static void runInterface() {
		boolean done = false;
		while (!done) {
			sopln("Would you like to run the Banking System using the CLI, or with a GUI?");
			sopln("1. \tCommand Line Interface\n"
					+ "2. \tGraphical User Interface");
			sop("Entry: ");
			String selection =  getInput();
			
			switch (selection) {
			case "1":
				homescreen();
				done = true;
				break;
			case "2":
				Screen s = new Screen();
				done = true;
				break;
			default:
				invalidEntry();
				break;
			}
		}
	}
	
	public static String getAccountOwner(String accNum) throws SQLException {
		con = DriverManager.getConnection(url, username, password);
		String query = "SELECT id FROM p1.account WHERE number = '" + accNum + "'"
				+ "AND status = 'A';";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		
		String ownerId = rs.getString(1);
		
		stmt.close();
		con.close();
		
		return ownerId;
	}
	
	public static int getAccountBalance(String accNum) throws SQLException {
		con = DriverManager.getConnection(url, username, password);
		String query = "SELECT balance FROM p1.account WHERE number = '" + accNum + "';";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		
		int balance = rs.getInt(1);
		
		stmt.close();
		con.close();
		
		return balance;
	}
	
	public static void customerIDExists(String cusID) throws SQLException {
		con = DriverManager.getConnection(url, username, password);
		stmt = con.createStatement();
		rs = stmt.executeQuery("SELECT name FROM p1.customer WHERE id = '" + cusID + "';");
		rs.next();
		String name = rs.getString(1);
		
		sopln("Found account information for " + name + " (" + cusID + ").");
	}
	

	/**
	 * Create a new customer.
	 * @param name customer name
	 * @param gender customer gender
	 * @param age customer age
	 * @param pin customer pin
	 */
	public static void newCustomer(String name, String gender, String age, String pin) 
	{
		try {
			con = DriverManager.getConnection(url, username, password);
			
			String query = "INSERT INTO p1.customer (name, gender, age, pin)"
					+ "VALUES ('" + name + "', '" + gender + "', '"
					+ age + "', '" + pin + "');";
			
			PreparedStatement create = con.prepareStatement(query);
			create.executeUpdate();
			
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT MAX(id) FROM p1.customer;");
			rs.next();
			String newCustomerId = rs.getString(1);
			
			stmt.close();
			con.close();
			
			
			System.out.println("\n:: CREATE NEW CUSTOMER - SUCCESS");
			sopln("New Customer ID: \t" + newCustomerId + "\n");
			JOptionPane.showMessageDialog(null, "New Customer ID: \t" + newCustomerId);
			
			TimeUnit.SECONDS.sleep(2);
		}
		catch (SQLException e) {
			sopln(e.getMessage());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static String login(String id, String pin) throws SQLException {
		con = DriverManager.getConnection(url, username, password);
		stmt = con.createStatement();
		String query = "SELECT name FROM p1.customer WHERE id = '" + id + "' AND pin = '"
				+ pin + "';";
		rs = stmt.executeQuery(query);
		
		rs.next();
		String name = rs.getString(1);
		sopln("\nHello, " + name);
		stmt.close();
		con.close();
		return name;
	}

	/**
	 * Open a new account.
	 * @param id customer id
	 * @param type type of account
	 * @param amount initial deposit amount
	 * @throws SQLException 
	 */
	public static void openAccount(String id, String type, String amount)
	{
		try {
			con = DriverManager.getConnection(url, username, password);
			String query = "INSERT INTO p1.account (id, balance, type, status)"
					+ "VALUES ( '" + id + "', '" + amount + "', '" + type.toUpperCase() + "', 'A' );";
			PreparedStatement create = con.prepareStatement(query);
			create.executeUpdate();
			
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT MAX(number) FROM p1.account");
			rs.next();
			String accountNumber = rs.getString(1);
			
			System.out.println("\n:: OPEN ACCOUNT - SUCCESS");
			sopln("New Account Number: \t" + accountNumber);
			JOptionPane.showMessageDialog(null, "New Account Number: \t" + accountNumber);
			
			stmt.close();
			con.close();
		}
		catch (SQLException e) {
			invalidEntry();
			JOptionPane.showMessageDialog(null, "Failed to open an account for Customer " + id);
		}
	}

	/**
	 * Close an account.
	 * @param accNum account number
	 */
	public static void closeAccount(String accNum) 
	{
		try {
			con = DriverManager.getConnection(url, username, password);
			String query = "UPDATE p1.account "
					+ "SET balance = 0, status = 'I' "
					+ "WHERE number = '" + accNum + "';";
			PreparedStatement create = con.prepareStatement(query);
			create.executeUpdate();
			
			System.out.println("\n:: CLOSE ACCOUNT - SUCCESS");
			
			create.close();
			con.close();
		}
		catch(SQLException e) {
			sopln(e.getMessage());
			invalidEntry();
		}
	}

	/**
	 * Deposit into an account.
	 * @param accNum account number
	 * @param amount deposit amount
	 */
	public static void deposit(String accNum, String amount) 
	{
		try {
			con = DriverManager.getConnection(url, username, password);
			String query = "UPDATE p1.account "
					+ "SET balance = balance + '" + amount + "' "
					+ "WHERE number = '" + accNum + "';";
			PreparedStatement create = con.prepareStatement(query);
			create.executeUpdate();
			
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT balance FROM p1.account WHERE number = '" + accNum + "';");
			rs.next();
			String balance = rs.getString(1);
			
			System.out.println("\n:: DEPOSIT - SUCCESS");
			sopln("Account#: \t\t" + accNum);
			sopln("Current Balance:\t" + balance);
			
			stmt.close();
			con.close();
		}
		catch(SQLException e) {
			sopln("Error: account # " + accNum + " not found!");
		}
	}

	/**
	 * Withdraw from an account.
	 * @param accNum account number
	 * @param amount withdraw amount
	 */
	public static void withdraw(String accNum, String amount) 
	{
		try {
			con = DriverManager.getConnection(url, username, password);
			String query = "UPDATE p1.account "
					+ "SET balance = balance - '" + amount + "' "
					+ "WHERE number = '" + accNum + "';";
			PreparedStatement create = con.prepareStatement(query);
			create.executeUpdate();
			
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT balance FROM p1.account WHERE number = '" + accNum + "';");
			rs.next();
			String balance = rs.getString(1);
			
			System.out.println("\n:: WITHDRAW - SUCCESS");
			sopln("Account#: \t\t" + accNum);
			sopln("Current Balance:\t" + balance);
			
			stmt.close();
			con.close();
		}
		catch(SQLException e) {
			sopln("Error: account # " + accNum + " not found!");
		}
	}

	/**
	 * Transfer amount from source account to destination account. 
	 * @param srcAccNum source account number
	 * @param destAccNum destination account number
	 * @param amount transfer amount
	 */
	public static void transfer(String srcAccNum, String destAccNum, String amount) 
	{
		withdraw(srcAccNum, amount);
		deposit(destAccNum, amount);
		
		System.out.println("\n:: TRANSFER - SUCCESS");
	}

	/**
	 * Display account summary.
	 * @param cusID customer ID
	 */
	public static void accountSummary(String cusID) 
	{
		String[] columnNames = { "ACCOUNT #", "BALANCE" };
		String guiOut = "";
		try {
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT number, balance FROM p1.account WHERE id = '" + cusID 
					+ "' AND status = 'A' ORDER BY number ASC;");
			
			System.out.println("\n:: ACCOUNT SUMMARY - SUCCESS");
			
			for (String s : columnNames) {
				String tbsp = "";
				if (s.length() < 15)
					tbsp += "\t";
				if (s.length() < 10)
					tbsp += "\t";
				if (s.length() < 5)
					tbsp += "\t";
				sop(s + tbsp);
				guiOut += s + tbsp;
			}
			String lnbrk = "";
			for (int i = 0; i < 35; i++) {
				lnbrk += "-";
			}
			sopln();
			sopln(lnbrk);
			guiOut += "\n" + lnbrk + "\n";
			
			while (rs.next()) {
				String[] fields = { rs.getString(1), rs.getString(2) };
				
				for (String s : fields) {
					String tbsp = "";
					if (s.length() < 15)
						tbsp += "\t";
					if (s.length() < 10)
						tbsp += "\t";
					if (s.length() < 5)
						tbsp += "\t";
					sop(s + tbsp);
					guiOut += s + tbsp;
				}
				sopln();
				guiOut +="\n";
			}
			
			stmt.close();
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT SUM(balance) FROM p1.account WHERE id = '" + cusID
					+ "' AND status = 'A';");
			
			rs.next();
			String total = rs.getString(1);
			
			if (total != null) {
				sopln(lnbrk + "\nTotal:\t\t\t" + total);
				guiOut += lnbrk + "\nTotal:\t\t\t" + total + "\n";
			}
			else {
				sopln("You currently have no open accounts. Open one to see it here!");
				guiOut += "You currently have no open accounts. Open one to see it here!\n";
			}
			
			JOptionPane.showMessageDialog(null, guiOut);
			
			stmt.close();
			con.close();
		}
		catch (SQLException  e) {
			sopln("Error: no active accounts found for ID# " + cusID);
		}
	}

	/**
	 * Display Report A - Customer Information with Total Balance in Decreasing Order.
	 */
	public static void reportA() 
	{
		String[] columnNames = { "ID", "NAME", "AGE", "GENDER", "TOTAL BALANCE" };
		String guiOut = "";
		try {
			con = DriverManager.getConnection(url, username, password);
			String createView = "CREATE OR REPLACE VIEW sum_balance AS SELECT DISTINCT id, "
					+ "sum(balance) AS totalbalance FROM p1.account GROUP BY id;";
			PreparedStatement create = con.prepareStatement(createView);
			create.executeUpdate();
			
			String query = "SELECT p1.customer.id, name, age, gender, totalbalance "
					+ "FROM p1.customer JOIN sum_balance ON p1.customer.id = sum_balance.id ORDER BY totalbalance DESC;";
			
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			
			System.out.println(":: REPORT A - SUCCESS");
			
			for (String s : columnNames) {
				String tabspace = "\t";
				if (s.length() < 15)
					tabspace += "\t";
				if (s.length() < 10)
					tabspace += "\t";
				
				sop(s + tabspace);
				guiOut += s + tabspace;
			}
			String lnbrk = "\n";
			for (int i = 0; i < 109; i++)
				lnbrk += "-";
			sopln(lnbrk);
			guiOut += lnbrk + "\n";
			
			while (rs.next()) {				
				for (int i = 0; i < 5; i++) {
					String field = rs.getString(i + 1);
					
					String tbsp = "\t";
					if (field.length() < 15)
						tbsp += "\t";
					if (field.length() < 10)
						tbsp += "\t";
					sop(field + tbsp);
					guiOut += field + tbsp;
				}
				sopln();
				guiOut += "\n";
			}
		}
		catch (SQLException  e) {
			sopln(e.getMessage());
		}
		
		JOptionPane.showMessageDialog(null, guiOut);
	}

	/**
	 * Display Report B - Customer Information with Total Balance in Decreasing Order.
	 * @param min minimum age
	 * @param max maximum age
	 */
	public static void reportB(String min, String max) 
	{			
		try {
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement();
			
			String query = "SELECT AVG(balance) FROM p1.customer "
					+ "JOIN p1.account ON p1.customer.id = p1.account.id "
					+ "WHERE status = 'A' AND age >= '" + min + "' AND age <= '" + max + "';";
			rs = stmt.executeQuery(query);
			rs.next();
			String value = rs.getString(1);
			
			stmt.close();
			con.close();

			System.out.println("\n:: REPORT B - SUCCESS");
			sopln("Avg. account balance for customers ages " + min + " to " + max + ":\n"
					+ value);
			
			JOptionPane.showMessageDialog(null, "Avg. account balance for customers ages " + min + " to " + max + ":\n"
					+ value);
			
		}
		catch (SQLException e) {
			sopln(e.getMessage());
		}
	}
	
	public static void sopln(Object x) {
		System.out.println(x);
	}
	public static void sopln() {
		System.out.println();
	}
	public static void sop(Object x) {
		System.out.print(x);
	}
}
