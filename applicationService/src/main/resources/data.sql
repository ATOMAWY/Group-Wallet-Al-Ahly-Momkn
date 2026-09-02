INSERT IGNORE INTO users (id, email, name) VALUES (1, 'khaled@alahly.com', 'Khaled');
INSERT IGNORE INTO users (id, email, name) VALUES (2, 'intern@alahly.com', 'Junior Intern');
INSERT IGNORE INTO users (id, email, name) VALUES (3, 'lead@alahly.com', 'Team Lead');


INSERT INTO wallets (balance, type, user_id, group_id, version)
SELECT 5000.00, 'PERSONAL', 1, NULL, 0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM wallets WHERE user_id = 1 AND type = 'PERSONAL');

INSERT INTO wallets (balance, type, user_id, group_id, version)
SELECT 5000.00, 'PERSONAL', 2, NULL, 0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM wallets WHERE user_id = 2 AND type = 'PERSONAL');