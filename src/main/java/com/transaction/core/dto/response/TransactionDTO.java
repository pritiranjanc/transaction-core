package com.transaction.core.dto.response;

import com.transaction.core.constants.TransactionStatus;
import com.transaction.core.constants.TransactionType;
import com.transaction.core.constants.TransferType;
import com.transaction.core.entity.Account;
import com.transaction.core.entity.Transaction;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record TransactionDTO(
        Long transactionId,
        String transactionType,
        BigDecimal amount,
        String status,
        UUID reference,
        LocalDateTime createdAt) {

    public static TransactionDTO from(Transaction transaction) {
        return TransactionDTO.builder()
                .transactionId(transaction.getId())
                .transactionType(transaction.getTransactionType().name())
                .amount(transaction.getAmount())
                .status(transaction.getStatus().name())
                .reference(transaction.getReference())
                .createdAt(transaction.getCreatedAt())
                .build();
    }

    public static Transaction createTransaction(TransactionType type, BigDecimal amount, TransactionStatus status,
                                                Account account, Account to, TransferType transferType) {
        return Transaction.builder()
                .transactionType(type)
                .amount(amount)
                .status(status)
                .reference(UUID.randomUUID())
                .fromAccount(account)
                .toAccount(to)
                .transferType(transferType)
                .build();
    }
}
