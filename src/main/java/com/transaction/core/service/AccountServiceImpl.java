package com.transaction.core.service;

import com.transaction.core.dto.response.AccountDTO;
import com.transaction.core.dto.response.PageDTO;
import com.transaction.core.dto.response.LedgerEntryDTO;
import com.transaction.core.entity.Account;
import com.transaction.core.entity.LedgerEntry;
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
    public PageDTO<LedgerEntryDTO> getTransactionHistory(Long accountId, Pageable pageable) {
        log.info("Starting Transaction History: accountId={}", accountId);
        if (!accountRepository.existsById(accountId)) {
            throw new AccountNotFoundException(accountId);
        }
        Page<LedgerEntry> page = ledgerEntryRepository.findByAccountId(accountId, pageable);
        List<LedgerEntryDTO> transactions =  page.getContent()
                .stream()
                .map(LedgerEntryDTO::from)
                .toList();
        log.info("Completed Transaction History: accountId={}", accountId);
        return PageDTO.from(transactions,page.getTotalPages(),page.getNumber(),page.getSize(), page.getTotalElements());
    }

    @Override
    public AccountDTO getAccountBalance(Long accountId) {
        log.info("Starting getAccount Balance : accountId={}", accountId);
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));
        log.info("Completed getAccount Balance : accountId={}", accountId);
        return AccountDTO.from(account);
    }



}
