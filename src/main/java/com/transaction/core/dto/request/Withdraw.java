package com.transaction.core.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record Withdraw(
        @NotNull(message = "AccountId can not be null")
        Long accountId,
        @NotNull(message = "Amount can not be null")
        @DecimalMin(value = "0.01",message = "Amount must be greater than zero")
        BigDecimal amount
) {
}
