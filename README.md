# Bank Management System

A Java-based project using JDBC to manage banking operations like customer registration, account creation, deposits, withdrawals, loans, and more. The system interacts with a MySQL database for persistent data storage.

## Features

- **Customer Management**: Register customers with full validation (email, phone, date of birth).
- **Account Operations**: Open accounts, deposit, withdraw, and check balances.
- **Loan Management**: Apply for loans, repay loans (EMI), and track loan history.
- **Transaction History**: View mini-statements, track transactions (deposit/withdrawal/transfer).
- **Account Closure**: Close accounts after transferring or withdrawing all funds.
  
## Technologies Used

- **Java**: Core programming language.
- **JDBC**: Java Database Connectivity for interaction with MySQL database.
- **MySQL**: Database management system for data storage.

## Setup Instructions

### Prerequisites:
- Java JDK (version 8 or higher)
- MySQL Server installed and running
- MySQL Workbench or a similar SQL client (optional, for database management)

### 1. Clone the Repository:
Clone the project repository to your local machine:

```bash
git clone https://github.com/yourusername/BankManagementSystem.git
cd BankManagementSystem

2. Set Up the Database:
Open MySQL Workbench or your MySQL command line tool.

Run the following SQL script to create the necessary tables and populate them with sample data:

source C:/Users/usidh/Documents/BankManagementSystem/sql/schema.sql;
source C:/Users/usidh/Documents/BankManagementSystem/sql/data.sql;

3. Configure the Database Connection:
Open BankManagementSystem.java and update the database connection settings (username, password, and database name) in the connection URL.

private static final String URL = "jdbc:mysql://localhost:3306/bank_system";
private static final String USER = "root";
private static final String PASSWORD = "yourpassword";

4. Compile and Run:
Compile the Java program:

javac -d bin src/main/java/miniproject_dbms1/BankManagementSystem.java

Run the program:
java -cp bin miniproject_dbms1.BankManagementSystem

License
This project is licensed under the MIT License - see the LICENSE file for details.

Contributing
Feel free to fork the repository, make your changes, and submit a pull request. Contributions are welcome!

