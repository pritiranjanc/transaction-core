package com.transaction.core.service;

import com.transaction.core.constants.AccountStatus;
import com.transaction.core.constants.EntryType;
import com.transaction.core.constants.TransactionStatus;
import com.transaction.core.constants.TransactionType;
import com.transaction.core.dto.response.AccountDTO;
import com.transaction.core.dto.response.PageDTO;
import com.transaction.core.dto.response.TransactionHistory;
import com.transaction.core.entity.Account;
import com.transaction.core.entity.LedgerEntry;
import com.transaction.core.entity.Transaction;
import com.transaction.core.exception.AccountNotFoundException;
import com.transaction.core.repository.AccountRepository;
import com.transaction.core.repository.LedgerEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private LedgerEntryRepository ledgerEntryRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account account;

    @BeforeEach
    void setUp() {
        account = Account.builder()
                .id(1L)
                .accountNumber("ACC001")
                .balance(new BigDecimal("1500.00"))
                .currency("USD")
                .status(AccountStatus.ACTIVE.name())
                .build();
    }

    @Test
    void shouldReturnAccountBalanceSuccessfully() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        AccountDTO response = accountService.getAccountBalance(1L);
        assertNotNull(response);
        assertEquals(1L, response.accountId());
        assertEquals("****01", response.accountNumber());
        assertEquals(new BigDecimal("1500.00"), response.balance());
        assertEquals("USD", response.currency());
        assertEquals(AccountStatus.ACTIVE.name(), response.status());
        verify(accountRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenAccountDoesNotExistForBalance() {
        when(accountRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountBalance(999L));
        verify(accountRepository).findById(999L);
    }

    @Test
    void shouldReturnTransactionHistorySuccessfully() {
        Long accountId = 1L;
        Pageable pageable = PageRequest.of(0,20, Sort.by(Sort.Direction.DESC, "createdAt"));
        LedgerEntry entry1 = LedgerEntry.builder()
                .id(101L)
                .account(account)
                .entryType(EntryType.CREDIT)
                .amount(new BigDecimal("500.00"))
                .balanceAfter(new BigDecimal("1500.00"))
                .createdAt(LocalDateTime.now())
                .transaction(Transaction.builder().transactionType(TransactionType.DEPOSIT).id(101L)
                        .reference(UUID.randomUUID()).status(TransactionStatus.COMPLETED).build())
                .build();

        LedgerEntry entry2 = LedgerEntry.builder()
                .id(102L)
                .account(account)
                .entryType(EntryType.DEBIT)
                .amount(new BigDecimal("200.00"))
                .balanceAfter(new BigDecimal("1000.00"))
                .createdAt(LocalDateTime.now().minusMinutes(10))
                .transaction(Transaction.builder().transactionType(TransactionType.WITHDRAWAL)
                        .reference(UUID.randomUUID()).status(TransactionStatus.COMPLETED).build())
                .build();

        Page<LedgerEntry> ledgerPage = new PageImpl<>(List.of(entry1, entry2), pageable, 2);
        when(accountRepository.existsById(accountId)).thenReturn(true);
        when(ledgerEntryRepository.findByAccountId(accountId, pageable)).thenReturn(ledgerPage);
        PageDTO<TransactionHistory> response = accountService.getTransactionHistory(accountId, pageable);
        assertNotNull(response);
        assertEquals(2, response.getContent().size());

        TransactionHistory first = response.getContent().getFirst();
        assertEquals(101L, first.transactionId());
        assertEquals(EntryType.CREDIT.name(), first.entryType());
        assertEquals(new BigDecimal("500.00"), first.amount());
        assertEquals(new BigDecimal("1500.00"), first.balanceAfter());
        verify(accountRepository).existsById(accountId);
        verify(ledgerEntryRepository).findByAccountId(accountId, pageable);
    }

    @Test
    void shouldReturnEmptyTransactionHistoryWhenNoTransactionsExist() {
        Long accountId = 1L;
        Pageable pageable = PageRequest.of(0, 20);
        Page<LedgerEntry> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(accountRepository.existsById(accountId)).thenReturn(true);
        when(ledgerEntryRepository.findByAccountId(accountId, pageable)).thenReturn(emptyPage);
        PageDTO<TransactionHistory> response = accountService.getTransactionHistory(accountId, pageable);
        assertNotNull(response);
        assertTrue(response.getContent().isEmpty());
        assertEquals(0, response.getTotalElements());
        verify(accountRepository).existsById(accountId);
        verify(ledgerEntryRepository).findByAccountId(accountId, pageable);
    }

    @Test
    void shouldThrowExceptionWhenAccountDoesNotExistForTransactionHistory() {
        Long accountId = 999L;
        Pageable pageable = PageRequest.of(0, 20);
        when(accountRepository.existsById(accountId)).thenReturn(false);
        assertThrows(AccountNotFoundException.class, () -> accountService.getTransactionHistory(accountId, pageable));
        verify(accountRepository).existsById(accountId);
    }

}
