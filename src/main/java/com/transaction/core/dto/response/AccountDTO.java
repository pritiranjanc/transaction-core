package com.transaction.core.dto.response;

import com.transaction.core.entity.Account;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AccountDTO(
        Long accountId,
        String accountNumber,
        BigDecimal balance,
        String currency,
        String status,
        String type
){
    public static AccountDTO from(Account account){
        return AccountDTO.builder()
                .accountId(account.getId())
                .accountNumber("*".repeat(4) + account.getAccountNumber().substring(4))
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .status(account.getStatus().name())
                .type(account.getType().name())
                .build();
    }
}
