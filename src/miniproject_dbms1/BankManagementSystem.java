package miniproject_dbms1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.*;
import java.time.LocalDateTime; // Import for LocalDateTime
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;
import java.math.BigDecimal;
import java.util.Scanner;

public class BankManagementSystem {

	private static final String URL = "jdbc:mysql://localhost:3306/mini_project1";
	private static final String USER = "root";
	private static final String PASSWORD = "Siddhi#19";
	private Connection connection;
	private final Scanner scanner = new Scanner(System.in);

	public BankManagementSystem() {
		try {
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Method to display available branches
	public void displayBranches() throws SQLException {
		String query = "SELECT branch_id, branch_name, phone_number FROM Branches";
		try (PreparedStatement stmt = connection.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

			System.out.println("\n--- Available Branches ---");
			System.out.println("Branch ID | Branch Name       | Phone Number");

			while (rs.next()) {
				int branchId = rs.getInt("branch_id");
				String branchName = rs.getString("branch_name");
				String phoneNumber = rs.getString("phone_number");
				System.out.printf("%-10d %-20s %-15s\n", branchId, branchName, phoneNumber);
			}
		}
	}

	// Method to register a new customer
    public void registerCustomer() throws SQLException {

	    displayBranches();

	    System.out.print("Enter First Name: ");
	    String firstName = scanner.next();
	    System.out.print("Enter Last Name: ");
	    String lastName = scanner.next();
	    Date dob = getValidatedDateOfBirth();
	    System.out.print("Enter Branch ID: ");
	    int branchId = scanner.nextInt();

	    // Validate phone number in a loop
	    String phoneNumber;
	    while (true) {
	        System.out.print("Enter Phone Number: ");
	        phoneNumber = scanner.next();
	        if (phoneNumber.matches("\\d{10}")) {
	            break; // Exit the loop if the phone number is valid
	        } else {
	            System.out.println("Invalid phone number. Please enter a 10-digit phone number.");
	        }
	    }

	    // Validate email in a loop
	    String email;
	    String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
	    while (true) {
	        System.out.print("Enter Email: ");
	        email = scanner.next();
	        if (Pattern.matches(emailRegex, email)) {
	            break; // Exit the loop if the email format is valid
	        } else {
	            System.out.println("Invalid email format. Please enter a valid email address.");
	        }
	    }

	    // SQL query to insert customer data
	    String query = "INSERT INTO Customers (first_name, last_name, dob, branch_id, phone_number, email) VALUES (?, ?, ?, ?, ?, ?)";
	    try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
	        stmt.setString(1, firstName);
	        stmt.setString(2, lastName);
	        stmt.setDate(3, dob);
	        stmt.setInt(4, branchId);
	        stmt.setString(5, phoneNumber);
	        stmt.setString(6, email);
	        stmt.executeUpdate();

	        ResultSet generatedKeys = stmt.getGeneratedKeys();
	        if (generatedKeys.next()) {
	            int customerId = generatedKeys.getInt(1);
	            System.out.println("Customer registered successfully! Customer ID: " + customerId);
	        } else {
	            System.out.println("Customer registered successfully, but could not retrieve Customer ID.");
	        }
	    }
	}

    private Date getValidatedDateOfBirth() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false); // Strict parsing
        while (true) {
            try {
                System.out.print("Enter Date of Birth (YYYY-MM-DD): ");
                String input = scanner.next();
                java.util.Date parsedDate = dateFormat.parse(input); // Parse the input
                return new java.sql.Date(parsedDate.getTime()); // Convert to java.sql.Date
            } catch (ParseException e) {
                System.out.println("Invalid date format! Please enter the date in YYYY-MM-DD format.");
            }
        }
    }

	// Method to open a new account for an existing customer
	public void openAccount() throws SQLException {

		String accountType = null;

		System.out.print("Enter Customer ID: ");
		int customerId = scanner.nextInt();
		String branchQuery = "SELECT branch_id FROM Customers WHERE customer_id = ?";
		int branchId = -1;
		try (PreparedStatement branchStmt = connection.prepareStatement(branchQuery)) {
			branchStmt.setInt(1, customerId);
			ResultSet rs = branchStmt.executeQuery();
			if (rs.next()) {
				branchId = rs.getInt("branch_id");
			} else {
				System.out.println("Invalid Customer ID!");
				return;
			}
		}
		do {
			System.out.print("Enter Account Type (savings/checking): ");
			accountType = scanner.next();
			if(!accountType.equalsIgnoreCase("savings") && !accountType.equalsIgnoreCase("checking")) 
			{ 
				System.out.println("Invalid account Type , Enter again");
			}
		} while (!accountType.equalsIgnoreCase("savings") && !accountType.equalsIgnoreCase("checking"));
		System.out.print("Enter Initial Deposit: ");
		BigDecimal initialDeposit = scanner.nextBigDecimal();

		String accountNumber = generateAccountNumber();
		String password = generatePassword();
		String query = "INSERT INTO Accounts (customer_id, account_number, account_type, balance,password, branch_id, status) VALUES (?, ?, ?, ?, ?, ?, 'active')";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setInt(1, customerId);
			stmt.setString(2, accountNumber);
			stmt.setString(3, accountType);
			stmt.setBigDecimal(4, initialDeposit);
			stmt.setString(5, password);
			stmt.setInt(6, branchId);
			stmt.executeUpdate();
			System.out.println(
					"Account opened successfully with Account Number: " + accountNumber + " and PIN: " + password);
		}
	}

	public void login() throws SQLException {
		System.out.println("Press 1 to retrieve your Account ID, Account Number, and PIN.");
		System.out.println("Press 0 if you already know these details.");
		int option = scanner.nextInt();

		if (option == 1) {
			retrieveAccountDetails();
		}

		System.out.print("Enter Account Number: ");
		String accountNumber = scanner.next();
		System.out.print("Enter PIN: ");
		String pin = scanner.next();

		// Check login credentials
		if (authenticate(accountNumber, pin)) {
			// Show account operations if login is successful
			showAccountOperations(accountNumber);
		} else {
			System.out.println(
					"Invalid account number or PIN, or the account has been closed.If account closed, contact the bank");
		}
	}

	private void retrieveAccountDetails() throws SQLException {
		System.out.print("Enter Customer ID: ");
		int customerId = scanner.nextInt();

		String query = "SELECT account_id, account_number, password FROM Accounts WHERE customer_id = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setInt(1, customerId);
			ResultSet rs = stmt.executeQuery();

			// Check if there are any accounts for the customer
			boolean accountFound = false;
			while (rs.next()) {
				int accountId = rs.getInt("account_id");
				String accountNumber = rs.getString("account_number");
				String pin = rs.getString("password");

				// Print the details of each account
				System.out.println("Account ID: " + accountId);
				System.out.println("Account Number: " + accountNumber);
				System.out.println("PIN: " + pin);
				System.out.println("-----------------------------");
				accountFound = true;
			}

			// If no accounts are found
			if (!accountFound) {
				System.out.println("No account found for the given Customer ID.");
			}
		}
	}

	// Method to show account operations after successful login

	private void showAccountOperations(String accountNumber) throws SQLException {
		while (true) {
			 System.out.println("\n=================================");
			    System.out.println("         ACCOUNT OPERATIONS       ");
			    System.out.println("=================================");
			    System.out.printf("%-3s%-25s%n", "1.", "Deposit");
			    System.out.printf("%-3s%-25s%n", "2.", "Withdraw");
			    System.out.printf("%-3s%-25s%n", "3.", "Check Balance");
			    System.out.printf("%-3s%-25s%n", "4.", "Change PIN");
			    System.out.printf("%-3s%-25s%n", "5.", "Loan Management");
			    System.out.printf("%-3s%-25s%n", "6.", "Mini-Statement");
			    System.out.printf("%-3s%-25s%n", "7.", "Change Account Details");
			    System.out.printf("%-3s%-25s%n", "8.", "Close Account");
			    System.out.printf("%-3s%-25s%n", "9.", "Transfer Money");
			    System.out.printf("%-3s%-25s%n", "0.", "Logout");
			    System.out.println("=================================");
			    System.out.print("Choose an option: ");

			int choice = scanner.nextInt();

			switch (choice) {
			case 1 -> deposit(accountNumber);
			case 2 -> withdraw(accountNumber);
			case 3 -> checkBalance(accountNumber);
			case 4 -> changePin(accountNumber);
			case 5 -> loanManagement(accountNumber); // pass account number if needed in loan management
			case 6 -> miniStatement(accountNumber);
			case 7 -> changeAccountDetails(accountNumber);
			case 8 -> {
				closeAccount(accountNumber); // Close account and log out
				System.out.println("Logging out after closing the account.");
				return; // Exit the loop to log out
			}
			case 9 -> transferMoney(accountNumber);
			case 0 -> {
				System.out.println("Logged out successfully.");
				return; // Exit the loop to log out
			}
			default -> System.out.println("Invalid choice. Please try again.");
			}
		}
	}

	// Method to close an account
	private void closeAccount(String accountNumber) throws SQLException {
	    // Step 1: Check if the account exists and if it has a balance
	    String query = "SELECT balance, status FROM Accounts WHERE account_number = ?";
	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setString(1, accountNumber);
	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	            BigDecimal balance = rs.getBigDecimal("balance");
	            String accountStatus = rs.getString("status");

	            // Step 2: Handle the case where the account is already closed
	            if ("closed".equals(accountStatus)) {
	                System.out.println("This account is already closed.");
	                return;
	            }

	            // Step 3: Check if there is a balance
	            if (balance.compareTo(BigDecimal.ZERO) > 0) {
	            	System.out.println("\n=================================");
	            	System.out.println("    ACCOUNT CLOSURE OPTIONS     ");
	            	System.out.println("=================================");
	            	System.out.println("Your account has a balance of " + balance + ".");
	            	System.out.println("You need to withdraw or transfer this amount before closing the account.\n");
	            	System.out.printf("%-3s%-30s%n", "1.", "Withdraw the balance");
	            	System.out.printf("%-3s%-30s%n", "2.", "Transfer the balance to another account");
	            	System.out.printf("%-3s%-30s%n", "0.", "Cancel account closure");
	            	System.out.println("=================================");
	            	System.out.print("Choose an option: ");


	                int choice = scanner.nextInt();

	                switch (choice) {
	                    case 1:
	                        // Withdraw balance from account
	                        withdraw(accountNumber);
	                        break;
	                    case 2:
	                        // Transfer balance to another account
	                        boolean transferSuccessful = false;
	                        while (!transferSuccessful) {
	                            System.out.print("Enter your account number: ");
	                            String senderAccountNumber = scanner.next();
	                            transferSuccessful = transferMoneyWithValidation(senderAccountNumber);
	                            if (!transferSuccessful) {
	                                System.out.println("Invalid recipient details. Please try again.");
	                            }
	                        }
	                        break;
	                    case 0:
	                        // Cancel account closure
	                        System.out.println("Account closure cancelled.");
	                        return; // Cancel the closure process
	                    default:
	                        // Invalid choice
	                        System.out.println("Invalid choice. Account closure cancelled.");
	                        return;
	                }
	            }

	            // Step 4: Close the account
	            String updateQuery = "UPDATE Accounts SET status = 'closed' WHERE account_number = ?";
	            try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
	                updateStmt.setString(1, accountNumber);
	                int rowsAffected = updateStmt.executeUpdate();

	                if (rowsAffected > 0) {
	                    System.out.println("Account " + accountNumber + " has been successfully closed.");
	                } else {
	                    System.out.println("Failed to close the account. Please try again.");
	                }
	            }
	        } else {
	            System.out.println("Account does not exist.");
	        }
	    }
	}
	private boolean transferMoneyWithValidation(String senderAccountNumber) throws SQLException {
	    try {
	        // Prompt for recipient details
	        System.out.print("Enter recipient's branch ID: ");
	        int recipientBranchId = scanner.nextInt();

	        System.out.print("Enter recipient's account number: ");
	        String recipientAccountNumber = scanner.next();

	        System.out.print("Enter the amount to transfer: ");
	        BigDecimal transferAmount = scanner.nextBigDecimal();

	        // Validate recipient account
	        String recipientStatusQuery = "SELECT status FROM Accounts WHERE branch_id = ? AND account_number = ?";
	        try (PreparedStatement stmt = connection.prepareStatement(recipientStatusQuery)) {
	            stmt.setInt(1, recipientBranchId);
	            stmt.setString(2, recipientAccountNumber);

	            ResultSet rs = stmt.executeQuery();
	            if (!rs.next()) {
	                System.out.println("Recipient account does not exist.");
	                return false;
	            }
	            String recipientStatus = rs.getString("status");
	            if (!"active".equalsIgnoreCase(recipientStatus)) {
	                System.out.println("Recipient account is not active.");
	                return false;
	            }
	        }

	        // Step 2: Validate sender's balance
	        String senderBalanceQuery = "SELECT balance, over_draft_limit FROM Accounts WHERE account_number = ?";
	        BigDecimal senderBalance = BigDecimal.ZERO;
	        BigDecimal overdraftLimit = BigDecimal.ZERO;
	        try (PreparedStatement stmt = connection.prepareStatement(senderBalanceQuery)) {
	            stmt.setString(1, senderAccountNumber);
	            ResultSet rs = stmt.executeQuery();
	            if (rs.next()) {
	                senderBalance = rs.getBigDecimal("balance");
	                overdraftLimit = rs.getBigDecimal("over_draft_limit");
	            } else {
	                System.out.println("Sender account does not exist.");
	                return false;
	            }
	        }

	        // Check if transfer is possible
	        BigDecimal maxAvailable = senderBalance.add(overdraftLimit);
	        if (transferAmount.compareTo(maxAvailable) > 0) {
	            System.out.println("Insufficient funds for the transfer.");
	            return false;
	        }

	        // Step 3: Perform the transfer
	        try {
	            connection.setAutoCommit(false); // Begin transaction

	            // Deduct from sender's account
	            String deductQuery = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
	            try (PreparedStatement stmt = connection.prepareStatement(deductQuery)) {
	                stmt.setBigDecimal(1, transferAmount);
	                stmt.setString(2, senderAccountNumber);
	                stmt.executeUpdate();
	            }

	            // Add to recipient's account
	            String addQuery = "UPDATE Accounts SET balance = balance + ? WHERE branch_id = ? AND account_number = ?";
	            try (PreparedStatement stmt = connection.prepareStatement(addQuery)) {
	                stmt.setBigDecimal(1, transferAmount);
	                stmt.setInt(2, recipientBranchId);
	                stmt.setString(3, recipientAccountNumber);
	                stmt.executeUpdate();
	            }

	            // Log the transaction for sender
	            logTransaction(senderAccountNumber, "transfer", transferAmount.negate());

	            // Log the transaction for recipient
	            logTransaction(recipientAccountNumber, "transfer", transferAmount);

	            connection.commit(); // Commit transaction
	            System.out.println("Transfer successful!");
	            System.out.println("Transaction logged successfully.");

	            return true; // Successful transfer
	        } catch (SQLException ex) {
	            connection.rollback(); // Rollback transaction on failure
	            System.out.println("Error during transfer: " + ex.getMessage());
	            return false;
	        } finally {
	            connection.setAutoCommit(true); // Restore auto-commit
	        }
	    } catch (SQLException ex) {
	        System.out.println("Error during validation or transfer: " + ex.getMessage());
	        return false; // Unsuccessful transfer
	    }
	}

	private boolean authenticate(String accountNumber, String pin) throws SQLException {
		String query = "SELECT password, status FROM Accounts WHERE account_number = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, accountNumber);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				String correctPin = rs.getString("password");
				String status = rs.getString("status");

				// Check if the account is active and PIN matches
				if ("active".equalsIgnoreCase(status)) {
					return correctPin.equals(pin);
				} else if ("closed".equalsIgnoreCase(status)) {
					System.out.println("This account has been closed.");
				}
			}
		}
		return false;
	}

	// Methods for account operations (deposit, withdraw, check balance, and change
	// PIN)
	public void deposit(String accountNumber) throws SQLException {
		System.out.print("Enter Deposit Amount: ");
		BigDecimal amount = scanner.nextBigDecimal();
		String query = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setBigDecimal(1, amount);
			stmt.setString(2, accountNumber);
			int rowsUpdated = stmt.executeUpdate();
			logTransaction(accountNumber, "deposit", amount);
	//		System.out.println("Transaction logged successfully.");

			if (rowsUpdated > 0) {
				logTransaction(accountNumber, "deposit", amount);
				System.out.println("Deposit successful!");

				// Retrieve and display the updated balance
				String balanceQuery = "SELECT balance FROM Accounts WHERE account_number = ?";
				try (PreparedStatement balanceStmt = connection.prepareStatement(balanceQuery)) {
					balanceStmt.setString(1, accountNumber);
					ResultSet rs = balanceStmt.executeQuery();
					if (rs.next()) {
						BigDecimal balance = rs.getBigDecimal("balance");
						System.out.println("Current Balance: " + balance);
					}
				}
			} else {
				System.out.println("Failed to update the account. Please check the account number.");
			}
		}
	}

//	public void withdraw(String accountNumber) throws SQLException {
//		System.out.print("Enter Withdrawal Amount: ");
//		BigDecimal amount = scanner.nextBigDecimal();
//		String query = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ? AND balance >= ?";
//		try (PreparedStatement stmt = connection.prepareStatement(query)) {
//			stmt.setBigDecimal(1, amount);
//			stmt.setString(2, accountNumber);
//			stmt.setBigDecimal(3, amount);
//			int rowsAffected = stmt.executeUpdate();
//			if (rowsAffected > 0) {
//				logTransaction(accountNumber, "withdrawal", amount);
//				System.out.println("Withdrawal successful!");
//				 String balanceQuery = "SELECT balance FROM Accounts WHERE account_number = ?";
//		            try (PreparedStatement balanceStmt = connection.prepareStatement(balanceQuery)) {
//		                balanceStmt.setString(1, accountNumber);
//		                ResultSet rs = balanceStmt.executeQuery();
//		                if (rs.next()) {
//		                    BigDecimal balance = rs.getBigDecimal("balance");
//		                    System.out.println("Current Balance: " + balance);
//		                }
//		            }
//		        } else {
//		            System.out.println("Insufficient Balance");
//		        }
//			}
//	}

	public void withdraw(String accountNumber) throws SQLException {
		System.out.print("Enter Withdrawal Amount: ");
		BigDecimal amount = scanner.nextBigDecimal();

		// First, get the account type and balance for the given account
		String accountTypeQuery = "SELECT account_type, balance, over_draft_limit FROM Accounts WHERE account_number = ?";
		try (PreparedStatement stmt = connection.prepareStatement(accountTypeQuery)) {
			stmt.setString(1, accountNumber);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				String accountType = rs.getString("account_type");
				BigDecimal balance = rs.getBigDecimal("balance");
				BigDecimal overdraftLimit = rs.getBigDecimal("over_draft_limit");

				// If it's a Savings Account
				if (accountType.equals("savings")) {
					// For savings account, withdrawal is allowed only if there's sufficient balance
					if (balance.compareTo(amount) >= 0) {
						// Proceed with withdrawal
						String updateQuery = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ? AND balance >= ?";
						try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
							updateStmt.setBigDecimal(1, amount);
							updateStmt.setString(2, accountNumber);
							updateStmt.setBigDecimal(3, amount);
							int rowsAffected = updateStmt.executeUpdate();
							if (rowsAffected > 0) {
								logTransaction(accountNumber, "withdrawal", amount);
								System.out.println("Withdrawal successful!");
								checkBalance(accountNumber); // Show updated balance
							} else {
								System.out.println("Withdrawal failed. Please check the details.");
							}
						}
					} else {
						System.out.println("Insufficient balance. Withdrawal cannot be processed.");
					}
				}
				// If it's a Checking Account
				else if (accountType.equals("checking")) {
					// Case 1: Withdrawal is less than or equal to balance
					if (balance.compareTo(amount) >= 0) {
						// Proceed with withdrawal
						String updateQuery = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ? AND balance >= ?";
						try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
							updateStmt.setBigDecimal(1, amount);
							updateStmt.setString(2, accountNumber);
							updateStmt.setBigDecimal(3, amount);
							int rowsAffected = updateStmt.executeUpdate();
							if (rowsAffected > 0) {
								logTransaction(accountNumber, "withdrawal", amount);
								System.out.println("Withdrawal successful!");
								checkBalance(accountNumber); // Show updated balance
							} else {
								System.out.println("Withdrawal failed. Please check the details.");
							}
						}
					}
					// Case 2: Withdrawal is greater than balance but within overdraft limit
					else if (balance.add(overdraftLimit).compareTo(amount) >= 0) {
						// Use the overdraft limit to cover the difference
						BigDecimal overdraftUsed = amount.subtract(balance); // The part of the withdrawal from
																				// overdraft

						// Update balance and overdraft limit
						String updateQuery = "UPDATE Accounts SET balance = 0, over_draft_limit = over_draft_limit - ? WHERE account_number = ? AND over_draft_limit >= ?";
						try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
							updateStmt.setBigDecimal(1, overdraftUsed);
							updateStmt.setString(2, accountNumber);
							updateStmt.setBigDecimal(3, overdraftUsed);
							int rowsAffected = updateStmt.executeUpdate();
							if (rowsAffected > 0) {
								logTransaction(accountNumber, "withdrawal", overdraftUsed);
								System.out.println("Withdrawal successful! Rs " + overdraftUsed
										+ " used from overdraft limit. You can be charged interest after a week.");
								checkBalance(accountNumber); // Show updated balance and overdraft limit
							} else {
								System.out.println("Withdrawal failed. Please check the details.");
							}
						}
					}
					// Case 3: Withdrawal exceeds balance + overdraft limit
					else {
						System.out.println("Withdrawal unsuccessful. Overdraft limit exceeded.");
					}
				} else {
					System.out.println("Invalid account type.");
				}
			} else {
				System.out.println("Account not found.");
			}
		}
	}

	public void checkBalance(String accountNumber) throws SQLException {
		String query = "SELECT balance FROM Accounts WHERE account_number = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, accountNumber);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				BigDecimal balance = rs.getBigDecimal("balance");
				System.out.println("Current Balance: " + balance);
			}
		}
	}

	public void changePin(String accountNumber) throws SQLException {
		System.out.print("Enter Old PIN: ");
		String oldPin = scanner.next();

		// Step 1: Verify the old PIN
		String verifyQuery = "SELECT password FROM Accounts WHERE account_number = ?";
		try (PreparedStatement verifyStmt = connection.prepareStatement(verifyQuery)) {
			verifyStmt.setString(1, accountNumber);
			ResultSet rs = verifyStmt.executeQuery();

			if (rs.next()) {
				String currentPin = rs.getString("password");

				if (!currentPin.equals(oldPin)) {
					System.out.println("Incorrect Old PIN. Try again.");
					return;
				}
			} else {
				System.out.println("Account not found!");
				return;
			}
		}

		// Step 2: Allow user to enter a new PIN
		System.out.print("Enter New PIN: ");
		String newPin = scanner.next();

		String updateQuery = "UPDATE Accounts SET password = ? WHERE account_number = ?";
		try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
			updateStmt.setString(1, newPin);
			updateStmt.setString(2, accountNumber);
			updateStmt.executeUpdate();
			System.out.println("PIN changed successfully!");
		}
	}

	private void logTransaction(String accountNumber, String transactionType, BigDecimal amount) throws SQLException {
		String logQuery = "INSERT INTO Transactions (account_id, transaction_type, amount, transaction_date) VALUES ((SELECT account_id FROM Accounts WHERE account_number = ?), ?, ?, NOW())";

		try (PreparedStatement stmt = connection.prepareStatement(logQuery)) {
			stmt.setString(1, accountNumber);
			stmt.setString(2, transactionType);
			stmt.setBigDecimal(3, amount);
			stmt.executeUpdate();
			// System.out.println("Transaction logged successfully.");
		} catch (SQLException e) {
			System.out.println("Error logging transaction: " + e.getMessage());
		}
	}

	// Helper methods for generating account number and PIN
	private String generateAccountNumber() {
		return "ACC" + (int) (Math.random() * 1000000);
	}

	private String generatePassword() {
		return String.valueOf((int) (Math.random() * 10000));
	}

	// Method to show the mini statement of a customer's account
	public void miniStatement(String accountNumber) throws SQLException {

		LocalDateTime now = LocalDateTime.now();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy HH:mm:ss");
		String formattedDateTime = now.format(formatter);

		// Retrieve branch name
		String branchName = getBranchName(accountNumber);

		System.out.println();
		System.out.println("\n--- Mini Statement ---");
		System.out.println();

		System.out.println(formattedDateTime);
		System.out.println(branchName);

		System.out.println("---------------------------------------------");
		System.out.println("Account Number: " + accountNumber);
		System.out.println("Transaction: MINISTATEMENT");

		String query = "SELECT transaction_type, amount, transaction_date FROM Transactions WHERE account_id = (SELECT account_id FROM Accounts WHERE account_number = ?) ORDER BY transaction_date DESC LIMIT 5";

		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, accountNumber);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				System.out.printf("Type: %s | Amount: %.2f | Date: %s\n", rs.getString("transaction_type"),
						rs.getBigDecimal("amount"), rs.getDate("transaction_date"));
				String balanceQuery = "SELECT balance FROM Accounts WHERE account_number = ?";
				try (PreparedStatement balanceStmt = connection.prepareStatement(balanceQuery)) {
					balanceStmt.setString(1, accountNumber);
					ResultSet balanceRs = balanceStmt.executeQuery();

					if (balanceRs.next()) {
						BigDecimal balance = balanceRs.getBigDecimal("balance");
						System.out.println("---------------------------------------------");

						System.out.println("\nAvailable Balance: " + balance);
					}
				}
			}
		}
	}

	private String getBranchName(String accountNumber) throws SQLException {
		String branchQuery = "SELECT b.branch_name " + "FROM Accounts a "
				+ "JOIN Branches b ON a.branch_id = b.branch_id " + "WHERE a.account_number = ?";
		try (PreparedStatement branchStmt = connection.prepareStatement(branchQuery)) {
			branchStmt.setString(1, accountNumber);
			ResultSet rs = branchStmt.executeQuery();
			if (rs.next()) {
				return rs.getString("branch_name");
			}
		}
		return "Unknown Branch"; // If no branch is found, return Unknown
	}

	// Method to change account details
	public void changeAccountDetails(String accountNumber) throws SQLException {
		int customerId = getCustomerIdByAccountNumber(accountNumber);
		if (customerId == -1) {
			System.out.println("No customer found for the given account number.");
			return;
		}

		// Fetch and display current details
		String fetchQuery = "SELECT first_name, last_name, email, phone_number FROM Customers WHERE customer_id = ?";
		try (PreparedStatement fetchStmt = connection.prepareStatement(fetchQuery)) {
			fetchStmt.setInt(1, customerId);
			ResultSet rs = fetchStmt.executeQuery();
			if (rs.next()) {
				System.out.println("\nCurrent Details:");
				System.out.println("First Name: " + rs.getString("first_name"));
				System.out.println("Last Name: " + rs.getString("last_name"));
				System.out.println("Email: " + rs.getString("email"));
				System.out.println("Phone Number: " + rs.getString("phone_number"));
			} else {
				System.out.println("Failed to retrieve customer details.");
				return;
			}
		}

		boolean continueUpdating = true;
		while (continueUpdating) {
			System.out.println("\nWhat do you want to update?");
			System.out.println("1. First Name");
			System.out.println("2. Last Name");
			System.out.println("3. Email");
			System.out.println("4. Phone Number");
			System.out.println("5. Exit");
			System.out.print("Enter your choice: ");
			int choice = scanner.nextInt();

			String query = "";
			String newValue = "";

			switch (choice) {
			case 1:
				System.out.print("Enter new First Name: ");
				newValue = scanner.next();
				query = "UPDATE Customers SET first_name = ? WHERE customer_id = ?";
				break;
			case 2:
				System.out.print("Enter new Last Name: ");
				newValue = scanner.next();
				query = "UPDATE Customers SET last_name = ? WHERE customer_id = ?";
				break;
			case 3:
				System.out.print("Enter new Email: ");
				newValue = scanner.next();
				query = "UPDATE Customers SET email = ? WHERE customer_id = ?";
				break;
			case 4:
				System.out.print("Enter new Phone Number: ");
				newValue = scanner.next();
				query = "UPDATE Customers SET phone_number = ? WHERE customer_id = ?";
				break;
			case 5:
				continueUpdating = false;
				System.out.println("Exiting update process.");
				continue;
			default:
				System.out.println("Invalid choice. Please try again.");
				continue;
			}

			if (!query.isEmpty()) {
				try (PreparedStatement stmt = connection.prepareStatement(query)) {
					stmt.setString(1, newValue);
					stmt.setInt(2, customerId);
					stmt.executeUpdate();
					System.out.println("Details updated successfully.");
				}
			}

			if (choice != 5) {
				System.out.print("Do you want to update anything else? (yes/no): ");
				String response = scanner.next();
				continueUpdating = response.equalsIgnoreCase("yes");
			}
		}
	}

	// Loan methods

	// Method to fetch customer ID using account number
	public int getCustomerIdByAccountNumber(String accountNumber) throws SQLException {
		String query = "SELECT customer_id FROM Accounts WHERE account_number = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, accountNumber);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getInt("customer_id");
			} else {
				System.out.println("No customer found with the provided account number.");
				return -1; // Indicate that the customer was not found
			}
		}
	}

	// Method for Loan Management with the 3 options
	public void loanManagement(String accountNumber) throws SQLException {
	    while (true) { // Loop to allow multiple operations in loan management
	    	System.out.println("\n=================================");
	    	System.out.println("          LOAN MANAGEMENT         ");
	    	System.out.println("=================================");
	    	System.out.printf("%-3s%-30s%n", "1.", "Loan Application Process");
	    	System.out.printf("%-3s%-30s%n", "2.", "Loan Repayment (EMI)");
	    	System.out.printf("%-3s%-30s%n", "3.", "Loan History for Customers");
	    	System.out.printf("%-3s%-30s%n", "0.", "Exit to Login Menu");
	    	System.out.println("=================================");
	    	System.out.print("Choose an option: ");


	        int choice = scanner.nextInt();
	        switch (choice) {
	            case 1 -> loanApplicationProcess(getCustomerIdByAccountNumber(accountNumber));
	            case 2 -> loanRepayment(getCustomerIdByAccountNumber(accountNumber));
	            case 3 -> loanHistory(getCustomerIdByAccountNumber(accountNumber));
	            case 0 -> {
	                System.out.println("Exiting Loan Management and returning to the Login Menu...");
	                return; // Exit the method, returning to the calling code
	            }
	            default -> System.out.println("Invalid choice. Please try again.");
	        }
	    }
	}


	// Loan Application Process
	private void loanApplicationProcess(int c) throws SQLException {
		int customerId = c;

		// Display employees from the customer's branch
		System.out.print("Enter Branch ID to know list of Employee available at the branch: ");
		int branchId = scanner.nextInt();
		displayEmployees(branchId);

		System.out.print("Enter Employee ID who initiates the loan: ");
		int employeeId = scanner.nextInt();
		String loanType;
		while (true) {
			System.out.print("Enter Loan Type (personal/home/vehicle/education): ");
			loanType = scanner.next().toLowerCase();
			if (loanType.equals("personal") || loanType.equals("home") || loanType.equals("vehicle")
					|| loanType.equals("education")) {
				break;
			}
			System.out.println(
					"Invalid loan type. Please enter one of the following: personal, home, vehicle, education.");
		}

		// Validate loan amount
		BigDecimal loanAmount;
		while (true) {
			System.out.print("Enter Loan Amount: ");
			loanAmount = scanner.nextBigDecimal();
			if (loanAmount.compareTo(BigDecimal.ZERO) > 0) {
				break;
			}
			System.out.println("Invalid loan amount. Please enter a positive amount.");
		}

		// Validate tenure
		int tenure;
		while (true) {
			System.out.print("Enter Loan Tenure (in years): ");
			tenure = scanner.nextInt();
			if (tenure > 0) {
				break;
			}
			System.out.println("Invalid tenure. Please enter a positive number of years.");
		}
		double interestRate = switch (loanType) {
		case "personal" -> 10.85;
		case "home" -> 9.00;
		case "vehicle" -> 11.26;
		case "education" -> 10.85;
		default -> throw new IllegalArgumentException("Invalid loan type");
		};

		String query = "INSERT INTO Loans (customer_id, employee_id, loan_type, amount, interest_rate, tenure, start_date) VALUES (?, ?, ?, ?, ?, ?, NOW())";
		try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setInt(1, customerId);
			stmt.setInt(2, employeeId);
			stmt.setString(3, loanType);
			stmt.setBigDecimal(4, loanAmount);
			stmt.setDouble(5, interestRate);
			stmt.setInt(6, tenure);
			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected > 0) {
				try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						int loanId = generatedKeys.getInt(1); // Get the generated loan_id
						System.out.println("Loan application process completed successfully!");
						System.out.println("Your Loan ID is: " + loanId);
					} else {
						System.out.println("Loan application completed, but Loan ID could not be retrieved.");
					}
				}
			} else {
				System.out.println("Loan application process failed.");
			}
		}
	}

	// Display employees by branch
	private void displayEmployees(int branchId) throws SQLException {
		String query = "SELECT employee_id, first_name, last_name FROM Employees WHERE branch_id = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setInt(1, branchId);
			ResultSet rs = stmt.executeQuery();
			System.out.println("\n--- Employees in Branch " + branchId + " ---");
			while (rs.next()) {
				System.out.printf("Employee ID: %d, Name: %s %s\n", rs.getInt("employee_id"),
						rs.getString("first_name"), rs.getString("last_name"));
			}
		}
	}

	// Loan Re-payment
	private void loanRepayment(int customerId) throws SQLException {
		System.out.println("Press 1: Do you want to view your loan details?");
		System.out.println("Press 0: If you already know your Loan ID.");
		int choice = scanner.nextInt();

		int loanId;

		if (choice == 1) {
			// Fetch and display loans connected to the customer
			String fetchLoansQuery = "SELECT loan_id, loan_type, amount, status FROM Loans WHERE customer_id = ?";
			try (PreparedStatement stmt = connection.prepareStatement(fetchLoansQuery)) {
				stmt.setInt(1, customerId);
				try (ResultSet rs = stmt.executeQuery()) {
					System.out.println("Loan ID | Loan Type | Remaining Amount | Status");
					while (rs.next()) {
						System.out.printf("%d | %s | %.2f | %s%n", rs.getInt("loan_id"), rs.getString("loan_type"),
								rs.getBigDecimal("amount"), rs.getString("status"));
					}
				}
			}
			System.out.print("Enter Loan ID to proceed with repayment: ");
			loanId = scanner.nextInt();
		} else if (choice == 0) {
			System.out.print("Enter Loan ID: ");
			loanId = scanner.nextInt();
		} else {
			System.out.println("Invalid choice! Exiting repayment process.");
			return;
		}

		System.out.print("Enter Payment Amount: ");
		BigDecimal paymentAmount = scanner.nextBigDecimal();

		// Fetch current loan amount and validate loan ID
		String fetchAmountQuery = "SELECT amount, status FROM Loans WHERE loan_id = ?";
		BigDecimal remainingAmount;
		String loanStatus;

		try (PreparedStatement fetchStmt = connection.prepareStatement(fetchAmountQuery)) {
			fetchStmt.setInt(1, loanId);
			try (ResultSet rs = fetchStmt.executeQuery()) {
				if (rs.next()) {
					remainingAmount = rs.getBigDecimal("amount");
					loanStatus = rs.getString("status");

					if (loanStatus.equals("completed")) {
						System.out.println("This loan is already fully repaid.");
						return;
					}
				} else {
					System.out.println("Invalid Loan ID. Exiting repayment process.");
					return;
				}
			}
		}

		// Validate payment amount
		if (paymentAmount.compareTo(remainingAmount) > 0) {
			System.out.printf("Loan amount remaining is: %.2f. Please enter a valid amount.%n", remainingAmount);
			System.out.println("Payment amount exceeds remaining loan amount. Please enter a valid amount.");
			return;
		}

		// Update loan details
		String updateLoanQuery = "UPDATE Loans SET amount = amount - ?, last_payment_date = NOW(), "
				+ "status = CASE WHEN amount - ? <= 0 THEN 'completed' ELSE 'active' END WHERE loan_id = ?";
		try (PreparedStatement updateStmt = connection.prepareStatement(updateLoanQuery)) {
			updateStmt.setBigDecimal(1, paymentAmount);
			updateStmt.setBigDecimal(2, paymentAmount);
			updateStmt.setInt(3, loanId);
			int rowsAffected = updateStmt.executeUpdate();

			if (rowsAffected > 0) {
				BigDecimal newRemainingAmount = remainingAmount.subtract(paymentAmount);
				System.out.println("Repayment successful!");
				if (newRemainingAmount.compareTo(BigDecimal.ZERO) == 0) {
					System.out.println("Congratulations! You have fully repaid the loan.");
				} else {
					System.out.printf("Loan ID: %d | Remaining Amount: %.2f%n", loanId, newRemainingAmount);
				}
			} else {
				System.out.println("Repayment failed. Please try again.");
			}
		}
	}

	// Loan History
	private void loanHistory(int c) throws SQLException {
		int customerId = c;

		String query = "SELECT loan_id, loan_type, amount, interest_rate, tenure, start_date FROM Loans WHERE customer_id = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setInt(1, customerId);
			ResultSet rs = stmt.executeQuery();

			System.out.println("\n--- Loan History for Customer ID: " + customerId + " ---");
			while (rs.next()) {
				System.out.printf(
						"Loan ID: %d, Type: %s, Amount: %.2f, Interest Rate: %.2f, Tenure: %d years, Start Date: %s\n",
						rs.getInt("loan_id"), rs.getString("loan_type"), rs.getBigDecimal("amount"),
						rs.getDouble("interest_rate"), rs.getInt("tenure"), rs.getDate("start_date"));
			}
		}
	}

	private void transferMoney(String senderAccountNumber) throws SQLException {

	    // Step 1: Ask for recipient details
	    System.out.print("Enter recipient's branch ID: ");
	    int recipientBranchId = scanner.nextInt();

	    System.out.print("Enter recipient's account number: ");
	    String recipientAccountNumber = scanner.next();

	    System.out.print("Enter the amount to transfer: ");
	    BigDecimal transferAmount = scanner.nextBigDecimal();

	    // Validate recipient account
	    String recipientStatusQuery = "SELECT status FROM Accounts WHERE branch_id = ? AND account_number = ?";
	    try (PreparedStatement stmt = connection.prepareStatement(recipientStatusQuery)) {
	        stmt.setInt(1, recipientBranchId);
	        stmt.setString(2, recipientAccountNumber);

	        ResultSet rs = stmt.executeQuery();
	        if (!rs.next()) {
	            System.out.println("Money Not transferred.");
	            System.out.println("Recipient account does not exist.");
	            return;
	        }
	        String recipientStatus = rs.getString("status");
	        if (!"active".equalsIgnoreCase(recipientStatus)) {
	            System.out.println("Recipient account is not active.");
	            return;
	        }
	    }

	    // Step 2: Check sender's account details (type, balance, overdraft)
	    String senderQuery = "SELECT account_type, balance, over_draft_limit FROM Accounts WHERE account_number = ?";
	    BigDecimal senderBalance = BigDecimal.ZERO;
	    BigDecimal overdraftLimit = BigDecimal.ZERO;
	    String accountType = null;

	    try (PreparedStatement stmt = connection.prepareStatement(senderQuery)) {
	        stmt.setString(1, senderAccountNumber);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            accountType = rs.getString("account_type");
	            senderBalance = rs.getBigDecimal("balance");
	            overdraftLimit = rs.getBigDecimal("over_draft_limit");
	        } else {
	            System.out.println("Sender account does not exist.");
	            return;
	        }
	    }

	    // Step 3: Validate funds based on account type
	    if ("savings".equalsIgnoreCase(accountType)) {
	        if (transferAmount.compareTo(senderBalance) > 0) {
	            System.out.println("Insufficient funds for the transfer. Savings account cannot go below zero.");
	            return;
	        }
	    } else if ("checking".equalsIgnoreCase(accountType)) {
	        BigDecimal maxAvailable = senderBalance.add(overdraftLimit);
	        if (transferAmount.compareTo(maxAvailable) > 0) {
	            System.out.println("Insufficient funds for the transfer. Overdraft limit exceeded.");
	            return;
	        }
	    } else {
	        System.out.println("Invalid account type for the sender.");
	        return;
	    }

	    // Step 4: Perform the transfer
	    try {
	        connection.setAutoCommit(false); // Begin transaction

	        // Deduct from sender's account
	        String deductQuery = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
	        try (PreparedStatement stmt = connection.prepareStatement(deductQuery)) {
	            stmt.setBigDecimal(1, transferAmount);
	            stmt.setString(2, senderAccountNumber);
	            stmt.executeUpdate();
	        }

	        // Add to recipient's account
	        String addQuery = "UPDATE Accounts SET balance = balance + ? WHERE branch_id = ? AND account_number = ?";
	        try (PreparedStatement stmt = connection.prepareStatement(addQuery)) {
	            stmt.setBigDecimal(1, transferAmount);
	            stmt.setInt(2, recipientBranchId);
	            stmt.setString(3, recipientAccountNumber);
	            stmt.executeUpdate();
	        }

	        // Log the transaction for sender
	        logTransaction(senderAccountNumber, "transfer", transferAmount.negate());

	        // Log the transaction for recipient
	        logTransaction(recipientAccountNumber, "transfer", transferAmount);

	        connection.commit(); // Commit transaction
	        System.out.println("Transfer successful!");
	        System.out.println("Transaction logged successfully.");

	    } catch (SQLException ex) {
	        connection.rollback(); // Rollback transaction on failure
	        System.out.println("Error during transfer: " + ex.getMessage());
	    } finally {
	        connection.setAutoCommit(true); // Restore auto-commit
	    }
	}


//	private void logTransaction(String accountNumber, String transactionType, BigDecimal amount) throws SQLException {
//	    String logQuery = "INSERT INTO TransactionLogs (account_number, transaction_type, amount, transaction_date) VALUES (?, ?, ?, NOW())";
//	    try (PreparedStatement stmt = connection.prepareStatement(logQuery)) {
//	        stmt.setString(1, accountNumber);
//	        stmt.setString(2, transactionType);
//	        stmt.setBigDecimal(3, amount);
//	        stmt.executeUpdate();
//	        System.out.println("Transaction logged successfully.");
//	    } catch (SQLException e) {
//	        System.out.println("Error logging transaction: " + e.getMessage());
//	    }
//	}

	public static void main(String[] args) {
		BankManagementSystem system = new BankManagementSystem();

		try {
			// system.displayBranches();
			while (true) {
				System.out.println("\n--- Bank Management System ---");
				System.out.println("1. Register Customer");
				System.out.println("2. Open Account");
				System.out.println("3. Login");
				// System.out.println("4. Loan Management");
				System.out.println("0. Exit");
				System.out.print("Choose an option: ");

				int choice = system.scanner.nextInt();
				switch (choice) {
				case 1 -> system.registerCustomer();
				case 2 -> system.openAccount();
				case 3 -> system.login();

				case 0 -> {
					System.out.println("Exiting system.");
					return;
				}
				default -> System.out.println("Invalid choice. Please try again.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}