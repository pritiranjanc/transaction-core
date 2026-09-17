package com.transaction.core.dto.response;

import java.math.BigDecimal;

public record AccountDTO(
        Long accountId,
        String accountNumber,
        BigDecimal balance,
        String currency,
        String status
){
}
