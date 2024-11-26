create database mini_project1;
use mini_project1;

CREATE TABLE Branches (
    branch_id INT PRIMARY KEY AUTO_INCREMENT,
    branch_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(15),
    CONSTRAINT chk_phone_number CHECK (phone_number REGEXP '^[0-9]{10}$')
);

CREATE TABLE Customers (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    dob DATE,
    branch_id INT,
    phone_number VARCHAR(15),
    email VARCHAR(100) UNIQUE,
        FOREIGN KEY (branch_id) REFERENCES Branches(branch_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
        CONSTRAINT phone_number CHECK (phone_number REGEXP '^[0-9]{10}$'),
    CONSTRAINT email CHECK (email REGEXP '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$')
);

CREATE TABLE Accounts (
    account_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    account_number VARCHAR(20) UNIQUE NOT NULL,
    account_type ENUM('savings', 'checking') NOT NULL,
    balance DECIMAL(15, 2) DEFAULT 0.0,
    branch_id INT,
    
    status ENUM('active', 'closed') DEFAULT 'active',
    password VARCHAR(255) NOT NULL,
    over_draft_limit DECIMAL(15, 2) DEFAULT 5000.0,
    FOREIGN KEY (branch_id) REFERENCES Branches(branch_id)
		ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (customer_id) REFERENCES Customers(customer_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE Employees (
    employee_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    position ENUM('manager', 'teller', 'loan officer', 'customer service') NOT NULL,
    branch_id INT,
    email VARCHAR(100) UNIQUE,
    phone_number VARCHAR(15),
    
    FOREIGN KEY (branch_id) REFERENCES Branches(branch_id)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
        CONSTRAINT p_phone_number CHECK (phone_number REGEXP '^[0-9]{10}$'),
        CONSTRAINT e_email CHECK (email REGEXP '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$')
);

CREATE TABLE Transactions (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    account_id INT,
    transaction_type ENUM('deposit', 'withdrawal', 'transfer') NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    transaction_date DATE NOT NULL,
    date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES Accounts(account_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE Loans (
    loan_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    
    loan_type ENUM('personal', 'home', 'vehicle', 'education') NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    interest_rate DECIMAL(5, 2),  
    tenure INT NOT NULL,  -- in months
    status ENUM('active', 'completed') DEFAULT 'active',
    start_date DATE,
    last_payment_date DATE Default null,
    employee_id INT,  -- Employee who initiated the loan
    FOREIGN KEY (customer_id) REFERENCES Customers(customer_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (employee_id) REFERENCES Employees(employee_id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);
UPDATE accounts
SET status = "active";