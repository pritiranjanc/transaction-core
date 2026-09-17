package com.transaction.core.service;

import com.transaction.core.dto.response.TransactionDTO;

import java.math.BigDecimal;

public interface TransactionService {

    TransactionDTO processDeposit(Long accountId, BigDecimal amount);
    TransactionDTO processWithdrawal(Long accountId, BigDecimal amount);
    TransactionDTO processTransfer(Long fromAccountId, Long toAccountId, BigDecimal amount);
}
