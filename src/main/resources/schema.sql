CREATE DATABASE transaction_core;

CREATE TABLE accounts (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    account_number  VARCHAR(50) NOT NULL UNIQUE,
    balance         NUMERIC(19, 2) NOT NULL DEFAULT 0,
    currency        CHAR(3) NOT NULL DEFAULT 'INR',
    status          VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE transactions (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    transaction_type  VARCHAR(20) NOT NULL,
    amount            NUMERIC(19, 2) NOT NULL,
    status            VARCHAR(20) NOT NULL DEFAULT 'COMPLETED',
    reference         UUID NOT NULL UNIQUE,
    created_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ledger_entries (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    transaction_id   INT NOT NULL,
    account_id       INT NOT NULL,
    entry_type       VARCHAR(10) NOT NULL,
    amount           NUMERIC(19, 2) NOT NULL,
    balance_after    NUMERIC(19, 2) NOT NULL,
    created_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_ledger_transaction FOREIGN KEY (transaction_id) REFERENCES transactions(id),
    CONSTRAINT fk_ledger_account FOREIGN KEY (account_id) REFERENCES accounts(id),
    CONSTRAINT chk_entry_type CHECK (entry_type IN ('DEBIT', 'CREDIT')),
    CONSTRAINT chk_ledger_amount CHECK (amount > 0),
    CONSTRAINT chk_balance_after CHECK (balance_after >= 0)
);


INSERT INTO accounts
    (account_number,balance,currency,status)
VALUES
    ('ACC001',1500.00,'INR','ACTIVE');

INSERT INTO accounts
    (account_number,balance,currency,status)
VALUES
    ('ACC002',500.00,'INR','ACTIVE');

SELECT
    le.id,
    le.transaction_id,
    t.transaction_type,
    le.entry_type,
    le.amount,
    le.balance_after,
    le.created_at
FROM ledger_entries le
JOIN transactions t ON t.id = le.transaction_id
WHERE le.account_id = :accountId
ORDER BY le.created_at DESC;