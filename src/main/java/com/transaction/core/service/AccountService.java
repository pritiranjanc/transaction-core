package com.transaction.core.service;

import com.transaction.core.dto.response.AccountDTO;
import com.transaction.core.dto.response.TransactionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountService {
    Page<TransactionHistory> getTransactionHistory(Long accountId, Pageable pageable);
    AccountDTO getAccount(Long accountId);
}
