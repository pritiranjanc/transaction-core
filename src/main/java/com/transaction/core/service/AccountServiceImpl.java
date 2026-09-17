package com.transaction.core.service;

import com.transaction.core.dto.response.AccountDTO;
import com.transaction.core.dto.response.PageDTO;
import com.transaction.core.dto.response.TransactionHistory;
import com.transaction.core.entity.Account;
import com.transaction.core.entity.LedgerEntry;
import com.transaction.core.entity.Transaction;
import com.transaction.core.exception.AccountNotFoundException;
import com.transaction.core.repository.AccountRepository;
import com.transaction.core.repository.LedgerEntryRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;
    private final LedgerEntryRepository ledgerEntryRepository;

    @Override
    public PageDTO<TransactionHistory> getTransactionHistory(Long accountId, Pageable pageable) {
        if (!accountRepository.existsById(accountId)) {
            throw new AccountNotFoundException(accountId);
        }
        Page<LedgerEntry> entries = ledgerEntryRepository.findByAccountIdOrderByCreatedAtDesc(accountId, pageable);
        Page<TransactionHistory> transactionHistories =  entries.map(this::toResponse);
        return PageDTO.<TransactionHistory>builder()
                .content(transactionHistories.getContent())
                .page(transactionHistories.getNumber())
                .size(transactionHistories.getSize())
                .totalElements(transactionHistories.getTotalElements())
                .totalPages(transactionHistories.getTotalPages())
                .build();
    }

    @Override
    public AccountDTO getAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
        return new AccountDTO(
                account.getId(),
                "*".repeat(2)
                        + account.getAccountNumber().substring(2),
                account.getBalance(),
                account.getCurrency(),
                account.getStatus()
        );
    }

    private TransactionHistory toResponse(LedgerEntry entry) {
        Transaction transaction = entry.getTransaction();
        return new TransactionHistory(
                transaction.getId(),
                transaction.getReference(),
                transaction.getTransactionType().name(),
                entry.getEntryType().name(),
                entry.getAmount(),
                entry.getBalanceAfter(),
                entry.getCreatedAt()
        );
    }

}
