package com.transaction.core.service;

import com.transaction.core.dto.response.AccountDTO;
import com.transaction.core.dto.response.PageDTO;
import com.transaction.core.dto.response.LedgerEntryDTO;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface AccountService {
    PageDTO<LedgerEntryDTO> getTransactionHistory(Long accountId, Pageable pageable);
    AccountDTO getAccountBalance(Long accountId);
    PageDTO<LedgerEntryDTO> getTransactions(Long accountId, LocalDate fromDate,LocalDate toDate, Pageable pageable);
}
