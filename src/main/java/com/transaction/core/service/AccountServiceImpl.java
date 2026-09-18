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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;
    private final LedgerEntryRepository ledgerEntryRepository;

    @Override
    public PageDTO<TransactionHistory> getTransactionHistory(Long accountId, Pageable pageable) {
        log.info("Starting Transaction History: accountId={}", accountId);
        if (!accountRepository.existsById(accountId)) {
            throw new AccountNotFoundException(accountId);
        }
        Page<LedgerEntry> entries = ledgerEntryRepository.findByAccountId(accountId, pageable);
        List<TransactionHistory> transactionHistories =  entries.getContent()
                .stream()
                .map(this::toResponse)
                .toList();
        log.info("Completed Transaction History: accountId={}", accountId);
        return PageDTO.<TransactionHistory>builder()
                .content(transactionHistories)
                .page(entries.getNumber())
                .size(entries.getSize())
                .totalElements(entries.getTotalElements())
                .totalPages(entries.getTotalPages())
                .build();
    }

    @Override
    public AccountDTO getAccountBalance(Long accountId) {
        log.info("Starting getAccount Balance : accountId={}", accountId);
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
        log.info("Completed getAccount Balance : accountId={}", accountId);
        return new AccountDTO(account.getId(), "*".repeat(4) + account.getAccountNumber().substring(4),
                account.getBalance(), account.getCurrency(), account.getStatus());
    }

    private TransactionHistory toResponse(LedgerEntry entry) {
        Transaction transaction = entry.getTransaction();
        return new TransactionHistory(transaction.getId(), transaction.getReference(),
                transaction.getTransactionType().name(), entry.getEntryType().name(),
                entry.getAmount(), entry.getBalanceAfter(), entry.getCreatedAt());
    }

}
