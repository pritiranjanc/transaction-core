package com.transaction.core.dto.response;

import com.transaction.core.entity.LedgerEntry;
import com.transaction.core.entity.Transaction;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record TransactionHistory(
        Long transactionId,
        UUID reference,
        String transactionType,
        String entryType,
        BigDecimal amount,
        BigDecimal balanceAfter,
        LocalDateTime createdAt){

    public static TransactionHistory toResponse(LedgerEntry entry) {
        Transaction transaction = entry.getTransaction();
        return TransactionHistory.builder()
                .transactionId(transaction.getId())
                .reference(transaction.getReference())
                .transactionType(transaction.getTransactionType().name())
                .entryType(entry.getEntryType().name())
                .amount(entry.getAmount())
                .balanceAfter(entry.getBalanceAfter())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
