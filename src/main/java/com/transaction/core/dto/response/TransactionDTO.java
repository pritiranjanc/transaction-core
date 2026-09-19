package com.transaction.core.dto.response;

import com.transaction.core.entity.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionDTO(
        Long transactionId,
        String transactionType,
        BigDecimal amount,
        String status,
        UUID reference,
        LocalDateTime createdAt) {

    public static TransactionDTO from(Transaction transaction) {
        return new TransactionDTO(
                transaction.getId(),
                transaction.getTransactionType().name(),
                transaction.getAmount(),
                transaction.getStatus().name(),
                transaction.getReference(),
                transaction.getCreatedAt()
        );
    }
}
