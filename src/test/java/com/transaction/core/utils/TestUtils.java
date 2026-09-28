package com.transaction.core.utils;

import com.transaction.core.constants.AccountStatus;
import com.transaction.core.constants.EntryType;
import com.transaction.core.constants.TransactionStatus;
import com.transaction.core.constants.TransactionType;
import com.transaction.core.entity.Account;
import com.transaction.core.entity.LedgerEntry;
import com.transaction.core.entity.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class TestUtils {

    public static LedgerEntry getLedgerEntry(
            Long id, Account account, EntryType type,
            BigDecimal amount, BigDecimal balanceAfter, Transaction transaction) {
        return LedgerEntry.builder()
                .id(id)
                .account(account)
                .entryType(type)
                .amount(amount)
                .balanceAfter(balanceAfter)
                .createdAt(LocalDateTime.now())
                .transaction(transaction)
                .build();
    }

    public static Transaction getTransaction(Long id, TransactionType type){
        return Transaction.builder()
                .id(id)
                .transactionType(type)
                .reference(UUID.randomUUID())
                .status(TransactionStatus.COMPLETED)
                .build();
    }

    public static Account getAccount(){
        return Account.builder()
                .id(1L)
                .accountNumber("ACC001")
                .balance(new BigDecimal("1500.00"))
                .currency("USD")
                .status(AccountStatus.ACTIVE)
                .build();
    }

}
