SELECT customer_id, first_name, last_name, dob
     FROM Customers
     WHERE dob BETWEEN '1990-01-01' AND '1995-12-31';

SELECT employee_id, first_name, email
     FROM Employees
     WHERE email LIKE '%@gmail.com';

SELECT transaction_id, account_id, transaction_date
     FROM Transactions
     WHERE transaction_date >= CURDATE() - INTERVAL 7 DAY;

SELECT branch_id, branch_name
     FROM Branches
     ORDER BY branch_name ASC;

SELECT branch_id, COUNT(account_id) AS account_count
     FROM Accounts
     GROUP BY branch_id;

SELECT e.first_name AS employee_name, b.branch_name
FROM Employees e, Branches b;

SELECT a.account_id, c.first_name, c.last_name, a.balance
     FROM Accounts a
     JOIN Customers c ON a.customer_id = c.customer_id;

SELECT b.branch_name, e.first_name AS employee_name
     FROM Branches b
     LEFT JOIN Employees e ON b.branch_id = e.branch_id;

SELECT e.first_name, b.branch_name
     FROM Employees e
     RIGHT JOIN Branches b ON e.branch_id = b.branch_id;

SELECT account_id, balance
     FROM Accounts
     WHERE balance > (SELECT AVG(balance) FROM Accounts);

CREATE VIEW BranchDeposits AS
     SELECT branch_id, SUM(balance) AS total_balance
     FROM Accounts
     GROUP BY branch_id;

SELECT * FROM BranchDeposits;

CREATE INDEX idx_account_number ON Accounts(account_number);

CREATE TRIGGER after_customer_delete
     AFTER DELETE ON Customers
     FOR EACH ROW
     INSERT INTO DeletedCustomers (customer_id, first_name, last_name)
     VALUES (OLD.customer_id, OLD.first_name, OLD.last_name);


> DELIMITER //
   CREATE PROCEDURE GetCustomerAccounts(IN customerId INT)
     BEGIN
         SELECT account_id, account_type, balance
         FROM Accounts
         WHERE customer_id = customerId;
     END //

CALL GetCustomerAccounts(1);

SELECT DISTINCT b.branch_name
     FROM Accounts a
     JOIN Branches b ON a.branch_id = b.branch_id
     WHERE a.balance > 10000;

 SELECT position, COUNT(employee_id) AS total_employees
     FROM Employees
     GROUP BY position;

SELECT c.first_name, c.last_name
     FROM Customers c
     JOIN Branches b ON c.branch_id = b.branch_id
     WHERE b.branch_name = 'Delhi Branch';

SELECT account_id, account_number, balance
     FROM Accounts
     WHERE account_id < '2024-01-01';

 CREATE VIEW ActiveLoans AS
     SELECT l.loan_id, c.first_name, l.amount
     FROM Loans l
     JOIN Customers c ON l.customer_id = c.customer_id
     WHERE l.status = 'active';

SELECT *
     FROM ActiveLoans;


