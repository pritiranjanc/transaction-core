package com.transaction.core.dto.response;

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
}
