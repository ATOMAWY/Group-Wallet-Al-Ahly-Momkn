INSERT IGNORE INTO users (id, name, email) VALUES (1, 'Khaled', 'khaled@momkn.com');
INSERT IGNORE INTO users (id, name, email) VALUES (2, 'Intern', 'intern@momkn.com');
INSERT IGNORE INTO users (id, name, email) VALUES (3, 'Manager', 'manager@momkn.com');

INSERT IGNORE INTO wallets (id, balance, type, user_id, group_id, version) VALUES (1, 5000.00, 'PERSONAL', 1, NULL, 0);
INSERT IGNORE INTO wallets (id, balance, type, user_id, group_id, version) VALUES (2, 5000.00, 'PERSONAL', 2, NULL, 0);
INSERT IGNORE INTO wallets (id, balance, type, user_id, group_id, version) VALUES (3, 5000.00, 'PERSONAL', 3, NULL, 0);