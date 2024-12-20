INSERT INTO Branches (branch_name, phone_number) VALUES
('Mumbai Main Branch', '9876543210'),
('Pune Branch', '9123456789'),
('Delhi Branch', '9988776655'),
('Bangalore Branch', '8877665544'),
('Chennai Branch', '7766554433'),
('Hyderabad Branch', '6677889900'),
('Kolkata Branch', '9988112233'),
('Ahmedabad Branch', '9090909090'),
('Lucknow Branch', '9191919191'),
('Jaipur Branch', '9292929292'),
('Chandigarh Branch', '9393939393'),
('Indore Branch', '9494949494'),
('Patna Branch', '9595959595'),
('Bhopal Branch', '9696969696'),
('Goa Branch', '9797979797'),
('Thiruvananthapuram Branch', '9898989898'),
('Nagpur Branch', '9999999999'),
('Dehradun Branch', '8787878787'),
('Ranchi Branch', '8686868686'),
('Guwahati Branch', '8585858585');

INSERT INTO Customers (first_name, last_name, dob, branch_id, phone_number, email) VALUES
('Rajesh', 'Sharma', '1985-06-15', 5, '9876543210', 'rajesh.sharma@gmail.com'),
('Anita', 'Gupta', '1992-03-12', 8, '9123456789', 'anita.gupta@yahoo.com'),
('Rohit', 'Verma', '1988-11-22', 3, '9988776655', 'rohit.verma@outlook.com'),
('Sneha', 'Patil', '1995-09-10', 4, '8877665544', 'sneha.patil@gmail.com'),
('Vikram', 'Singh', '1990-05-30', 6, '7766554433', 'vikram.singh@hotmail.com'),
('Pooja', 'Kumar', '1997-02-28', 7, '6677889900', 'pooja.kumar@gmail.com'),
('Amit', 'Jain', '1983-12-05', 1, '9988112233', 'amit.jain@yahoo.com'),
('Ruchi', 'Mehta', '1991-07-19', 3, '9090909090', 'ruchi.mehta@rediffmail.com'),
('Kiran', 'Nair', '1994-04-15', 9, '9191919191', 'kiran.nair@gmail.com'),
('Suresh', 'Rao', '1986-10-08', 2, '9292929292', 'suresh.rao@hotmail.com'),
('Divya', 'Menon', '1993-01-20', 5, '9393939393', 'divya.menon@yahoo.com'),
('Prakash', 'Joshi', '1989-09-25', 6, '9494949494', 'prakash.joshi@gmail.com'),
('Asha', 'Thakur', '1996-06-18', 7, '9595959595', 'asha.thakur@outlook.com'),
('Rahul', 'Kapoor', '1998-03-11', 8, '9696969696', 'rahul.kapoor@gmail.com'),
('Geeta', 'Desai', '1992-12-22', 2, '9797979797', 'geeta.desai@hotmail.com'),
('Manoj', 'Shetty', '1984-05-09', 4, '9898989898', 'manoj.shetty@yahoo.com'),
('Komal', 'Choudhary', '1990-08-15', 1, '9999999999', 'komal.choudhary@gmail.com'),
('Vivek', 'Malhotra', '1987-02-03', 3, '8787878787', 'vivek.malhotra@rediffmail.com'),
('Neha', 'Kulkarni', '1995-11-17', 9, '8686868686', 'neha.kulkarni@gmail.com'),
('Arjun', 'Bose', '1988-07-23', 6, '8585858585', 'arjun.bose@hotmail.com'),
('Isha', 'Chauhan', '1992-05-20', 5, '7854123690', 'isha.chauhan@gmail.com'),
('Aakash', 'Malik', '1989-12-30', 1, '8891234567', 'aakash.malik@hotmail.com'),
('Priya', 'Shah', '1994-07-15', 7, '9985647321', 'priya.shah@rediffmail.com'),
('Nikhil', 'Khanna', '1985-11-01', 4, '9123458876', 'nikhil.khanna@gmail.com'),
('Meera', 'Iyer', '1996-03-10', 6, '8012345698', 'meera.iyer@yahoo.com'),
('Sumit', 'Deshmukh', '1993-06-25', 2, '8765432109', 'sumit.deshmukh@outlook.com'),
('Ananya', 'Reddy', '1997-01-19', 9, '9123567890', 'ananya.reddy@gmail.com'),
('Ramesh', 'Pandey', '1988-09-09', 3, '8976542310', 'ramesh.pandey@hotmail.com'),
('Shruti', 'Kapoor', '1991-12-22', 8, '8790654321', 'shruti.kapoor@rediffmail.com'),
('Kunal', 'Bhatia', '1987-03-18', 7, '9812346578', 'kunal.bhatia@gmail.com'),
('Swati', 'Mishra', '1995-08-12', 4, '8654321987', 'swati.mishra@yahoo.com'),
('Tanya', 'Agarwal', '1993-04-29', 9, '7986543210', 'tanya.agarwal@hotmail.com'),
('Arvind', 'Chatterjee', '1992-10-17', 2, '8234567890', 'arvind.chatterjee@gmail.com'),
('Naina', 'Jaiswal', '1998-07-13', 6, '8123456790', 'naina.jaiswal@outlook.com'),
('Harish', 'Rajput', '1989-02-26', 5, '7981234567', 'harish.rajput@gmail.com'),
('Sunita', 'Pawar', '1994-11-30', 1, '9876543102', 'sunita.pawar@yahoo.com'),
('Aditya', 'Singh', '1990-06-05', 3, '7990123456', 'aditya.singh@hotmail.com'),
('Rohini', 'Venkatesh', '1986-08-11', 7, '8023456789', 'rohini.venkatesh@gmail.com'),
('Kabir', 'Joshi', '1991-05-15', 4, '8795432101', 'kabir.joshi@yahoo.com'),
('Seema', 'Desai', '1997-10-23', 9, '9192345678', 'seema.desai@outlook.com');

INSERT INTO Accounts (customer_id, account_number, account_type, balance, branch_id, status, password, over_draft_limit) VALUES
(1, 'ACC1000000001', 'savings', 15000.00, 5, 'active', 'password1', 5000.00),
(1, 'ACC1000000002', 'checking', 10000.00, 5, 'active', 'password2', 5000.00),
(2, 'ACC1000000003', 'savings', 20000.00, 8, 'active', 'password3', 5000.00),
(3, 'ACC1000000004', 'savings', 8000.00, 3, 'active', 'password4', 5000.00),
(3, 'ACC1000000005', 'checking', 5000.00, 3, 'active', 'password5', 5000.00),
(4, 'ACC1000000006', 'savings', 12000.00, 4, 'active', 'password6', 5000.00),
(4, 'ACC1000000007', 'checking', 9000.00, 4, 'active', 'password7', 5000.00),
(5, 'ACC1000000008', 'savings', 3000.00, 6, 'active', 'password8', 5000.00),
(6, 'ACC1000000009', 'savings', 25000.00, 7, 'active', 'password9', 5000.00),
(6, 'ACC1000000010', 'checking', 15000.00, 7, 'active', 'password10', 5000.00),
(7, 'ACC1000000011', 'savings', 10000.00, 1, 'active', 'password11', 5000.00),
(7, 'ACC1000000012', 'checking', 20000.00, 1, 'active', 'password12', 5000.00),
(8, 'ACC1000000013', 'savings', 18000.00, 3, 'active', 'password13', 5000.00),
(9, 'ACC1000000014', 'savings', 12000.00, 2, 'active', 'password14', 5000.00),
(9, 'ACC1000000015', 'checking', 5000.00, 2, 'active', 'password15', 5000.00),
(10, 'ACC1000000016', 'savings', 15000.00, 5, 'active', 'password16', 5000.00),
(10, 'ACC1000000017', 'checking', 12000.00, 5, 'active', 'password17', 5000.00),
(11, 'ACC1000000018', 'savings', 20000.00, 6, 'active', 'password18', 5000.00),
(12, 'ACC1000000019', 'savings', 30000.00, 7, 'active', 'password19', 5000.00),
(12, 'ACC1000000020', 'checking', 15000.00, 7, 'active', 'password20', 5000.00),
(13, 'ACC1000000021', 'savings', 18000.00, 8, 'active', 'password21', 5000.00),
(13, 'ACC1000000022', 'checking', 7000.00, 8, 'active', 'password22', 5000.00);

INSERT INTO Transactions (account_id, transaction_type, amount, transaction_date) VALUES
(1, 'deposit', 5000.00, '2024-11-01'),
(1, 'withdrawal', 2000.00, '2024-11-02'),
(2, 'deposit', 10000.00, '2024-11-03'),
(2, 'withdrawal', 5000.00, '2024-11-04'),
(3, 'deposit', 3000.00, '2024-11-05'),
(4, 'deposit', 7000.00, '2024-11-06'),
(5, 'withdrawal', 1500.00, '2024-11-07'),
(6, 'deposit', 10000.00, '2024-11-08'),
(6, 'transfer', 2000.00, '2024-11-09'),
(7, 'withdrawal', 2500.00, '2024-11-10'),
(8, 'deposit', 5000.00, '2024-11-11'),
(9, 'deposit', 15000.00, '2024-11-12'),
(10, 'withdrawal', 3000.00, '2024-11-13'),
(10, 'transfer', 5000.00, '2024-11-14'),
(11, 'deposit', 8000.00, '2024-11-15'),
(12, 'withdrawal', 2000.00, '2024-11-16'),
(13, 'deposit', 12000.00, '2024-11-17'),
(14, 'transfer', 4000.00, '2024-11-18'),
(15, 'deposit', 2000.00, '2024-11-19'),
(16, 'withdrawal', 1000.00, '2024-11-20'),
(17, 'deposit', 6000.00, '2024-11-21');

INSERT INTO Employees (first_name, last_name, position, branch_id, email, phone_number) VALUES
('Arjun', 'Patil', 'manager', 1, 'arjun.patil@bankindia.com', '9876543210'),
('Sneha', 'Sharma', 'teller', 1, 'sneha.sharma@bankindia.com', '9123456789'),
('Vikram', 'Deshmukh', 'loan officer', 2, 'vikram.deshmukh@bankindia.com', '9812345678'),
('Anita', 'Joshi', 'customer service', 2, 'anita.joshi@bankindia.com', '9923456780'),
('Rajesh', 'Rao', 'manager', 3, 'rajesh.rao@bankindia.com', '9876541230'),
('Pooja', 'Kumar', 'teller', 3, 'pooja.kumar@bankindia.com', '9012345678'),
('Amit', 'Verma', 'loan officer', 4, 'amit.verma@bankindia.com', '9102345678'),
('Neha', 'Gupta', 'customer service', 4, 'neha.gupta@bankindia.com', '9123456700'),
('Rakesh', 'Singh', 'manager', 5, 'rakesh.singh@bankindia.com', '9323456789'),
('Preeti', 'Nair', 'teller', 5, 'preeti.nair@bankindia.com', '9234567890'),
('Karan', 'Mehta', 'loan officer', 1, 'karan.mehta@bankindia.com', '9345678901'),
('Simran', 'Kapoor', 'customer service', 2, 'simran.kapoor@bankindia.com', '9456789012'),
('Sunil', 'Pandey', 'manager', 3, 'sunil.pandey@bankindia.com', '9567890123'),
('Ritika', 'Jain', 'teller', 4, 'ritika.jain@bankindia.com', '9678901234'),
('Abhishek', 'Kulkarni', 'loan officer', 5, 'abhishek.kulkarni@bankindia.com', '9789012345'),
('Maya', 'Reddy', 'customer service', 1, 'maya.reddy@bankindia.com', '9890123456'),
('Siddharth', 'Iyer', 'manager', 2, 'siddharth.iyer@bankindia.com', '9901234567'),
('Nisha', 'Desai', 'teller', 3, 'nisha.desai@bankindia.com', '9012345670'),
('Vivek', 'Chawla', 'loan officer', 4, 'vivek.chawla@bankindia.com', '9123456781'),
('Anjali', 'Roy', 'customer service', 5, 'anjali.roy@bankindia.com', '9234567891');

INSERT INTO Loans (customer_id, loan_type, amount, interest_rate, tenure, status, start_date, last_payment_date, employee_id) VALUES
(5, 'home', 3500000.00, 8.50, 240, 'active', '2023-01-05', NULL, 3),
(3, 'personal', 200000.00, 10.50, 36, 'completed', '2020-02-15', '2023-02-15', 5),
(8, 'education', 800000.00, 7.25, 96, 'active', '2022-07-20', NULL, 6),
(2, 'vehicle', 600000.00, 9.00, 60, 'completed', '2019-08-10', '2023-08-10', 4),
(10, 'home', 2500000.00, 8.75, 240, 'active', '2023-06-15', NULL, 8),
(4, 'personal', 300000.00, 11.00, 48, 'active', '2023-03-25', NULL, 9),
(7, 'education', 1000000.00, 7.50, 120, 'active', '2022-12-01', NULL, 12),
(12, 'home', 4000000.00, 8.25, 180, 'active', '2022-10-20', NULL, 1),
(6, 'vehicle', 750000.00, 9.50, 72, 'completed', '2019-01-30', '2023-01-30', 10),
(15, 'personal', 150000.00, 10.75, 24, 'active', '2024-02-01', NULL, 2),
(9, 'education', 1200000.00, 7.00, 144, 'active', '2023-09-25', NULL, 13),
(1, 'personal', 250000.00, 11.25, 36, 'active', '2024-03-15', NULL, 14),
(8, 'vehicle', 500000.00, 9.25, 60, 'completed', '2020-04-20', '2023-04-20', 7),
(13, 'home', 5000000.00, 8.00, 300, 'active', '2022-05-01', NULL, 15),
(2, 'personal', 400000.00, 9.75, 48, 'completed', '2020-09-10', '2024-09-10', 16),
(6, 'education', 700000.00, 7.30, 72, 'active', '2024-04-10', NULL, 17),
(14, 'home', 6000000.00, 8.10, 360, 'active', '2023-07-01', NULL, 18),
(3, 'vehicle', 800000.00, 9.40, 60, 'active', '2023-03-05', NULL, 19),
(7, 'personal', 350000.00, 10.00, 48, 'completed', '2019-12-20', '2023-12-20', 11),
(12, 'education', 900000.00, 7.15, 84, 'active', '2021-11-11', NULL, 20),
(9, 'home', 2500000.00, 8.65, 240, 'active', '2023-02-10', NULL, 4),
(5, 'vehicle', 850000.00, 9.20, 72, 'active', '2023-05-15', NULL, 8);