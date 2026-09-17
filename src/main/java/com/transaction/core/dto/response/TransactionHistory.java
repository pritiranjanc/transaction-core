package com.transaction.core.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionHistory(
        Long transactionId,
        UUID reference,
        String transactionType,
        String entryType,
        BigDecimal amount,
        BigDecimal balanceAfter,
        LocalDateTime createdAt){
}
