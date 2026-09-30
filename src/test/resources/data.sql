
INSERT INTO accounts
    (account_number,type,balance,currency,status)
VALUES
    ('ACC001','SAVINGS',1500.00,'INR','ACTIVE');

INSERT INTO accounts
    (account_number,type,balance,currency,status)
VALUES
    ('ACC002','SAVINGS',500.00,'INR','ACTIVE');

INSERT INTO transactions
    (transaction_type,amount,status,reference)
VALUES
    ('DEPOSIT',1500.00,'COMPLETED','dee95907-fcc8-443b-a01a-93029db2f35b');

INSERT INTO transactions
    (transaction_type,amount,status,reference)
VALUES
    ('DEPOSIT',500.00,'COMPLETED','e1ae5298-08b8-455c-93d5-a41cac1a62a5');


INSERT INTO ledger_entries
    (transaction_id,account_id,entry_type,amount,balance_after)
VALUES
    (1,1,'CREDIT',1500.00,1500.00);

INSERT INTO ledger_entries
    (transaction_id,account_id,entry_type,amount,balance_after)
VALUES
    (2,2,'CREDIT',500.00,500.00);

