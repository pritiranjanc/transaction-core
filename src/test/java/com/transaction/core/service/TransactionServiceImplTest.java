package com.transaction.core.service;

import com.transaction.core.constants.AccountStatus;
import com.transaction.core.dto.response.TransactionDTO;
import com.transaction.core.entity.Account;
import com.transaction.core.entity.LedgerEntry;
import com.transaction.core.exception.AccountNotFoundException;
import com.transaction.core.exception.InsufficientBalanceException;
import com.transaction.core.exception.InvalidTransactionException;
import com.transaction.core.repository.AccountRepository;
import com.transaction.core.repository.LedgerEntryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private LedgerEntryRepository ledgerEntryRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Account sourceAccount;
    private Account destinationAccount;

    @BeforeEach
    void setUp() {
        sourceAccount = Account.builder()
                .id(1L)
                .accountNumber("ACC001")
                .balance(new BigDecimal("1000.00"))
                .currency("USD")
                .status(AccountStatus.ACTIVE)
                .build();

        destinationAccount = Account.builder()
                .id(2L)
                .accountNumber("ACC002")
                .balance(new BigDecimal("500.00"))
                .currency("USD")
                .status(AccountStatus.ACTIVE)
                .build();
    }

    @Test
    void shouldDepositSuccessfully() {
        BigDecimal amount = new BigDecimal("200.00");
        when(accountRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(sourceAccount));
        TransactionDTO result = transactionService.processDeposit(1L, amount);
        assertNotNull(result);
        assertEquals(new BigDecimal("1200.00"), sourceAccount.getBalance());
        verify(accountRepository).save(sourceAccount);
        verify(ledgerEntryRepository).save(any(LedgerEntry.class));
    }

    @Test
    void shouldRejectDepositWhenAmountIsZero() {
        BigDecimal amount = BigDecimal.ZERO;
        assertThrows(InvalidTransactionException.class, () -> transactionService.processDeposit(1L, amount));
    }

    @Test
    void shouldThrowExceptionWhenDepositAccountDoesNotExist() {
        when(accountRepository.findByIdForUpdate(1L)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class,
                () -> transactionService.processDeposit(1L, new BigDecimal("100.00")));
    }

    @Test
    void shouldWithdrawSuccessfully() {
        BigDecimal amount = new BigDecimal("300.00");
        when(accountRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(sourceAccount));
        transactionService.processWithdrawal(1L, amount);
        assertEquals(new BigDecimal("700.00"), sourceAccount.getBalance());
        verify(accountRepository).save(sourceAccount);
        verify(ledgerEntryRepository).save(any(LedgerEntry.class));
    }

    @Test
    void shouldRejectWithdrawalWhenBalanceIsInsufficient() {
        BigDecimal amount = new BigDecimal("1500.00");
        when(accountRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(sourceAccount));
        assertThrows(InsufficientBalanceException.class, () -> transactionService.processWithdrawal(1L, amount));
        assertEquals(new BigDecimal("1000.00"), sourceAccount.getBalance());
    }

    @Test
    void shouldRejectWithdrawalWhenAmountIsNegative() {
        BigDecimal amount = new BigDecimal("-50.00");
        assertThrows(InvalidTransactionException.class, () -> transactionService.processWithdrawal(1L, amount));
    }

    @Test
    void shouldTransferSuccessfully() {
        BigDecimal amount = new BigDecimal("400.00");
        when(accountRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByIdForUpdate(2L)).thenReturn(Optional.of(destinationAccount));
        TransactionDTO result = transactionService.processTransfer(1L, 2L, amount);
        assertNotNull(result);
        assertEquals(new BigDecimal("600.00"), sourceAccount.getBalance());
        assertEquals(new BigDecimal("900.00"), destinationAccount.getBalance());
        verify(accountRepository).saveAll(List.of(sourceAccount,destinationAccount));
        verify(ledgerEntryRepository, times(1)).saveAll(anyList());
    }

    @Test
    void shouldRejectTransferToSameAccount() {
        BigDecimal amount = new BigDecimal("100.00");
        assertThrows(InvalidTransactionException.class, () -> transactionService.processTransfer(1L, 1L, amount));
    }

    @Test
    void shouldRejectTransferWhenInsufficientBalance() {
        BigDecimal amount = new BigDecimal("1500.00");
        when(accountRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByIdForUpdate(2L)).thenReturn(Optional.of(destinationAccount));
        assertThrows(InsufficientBalanceException.class, () -> transactionService.processTransfer(1L, 2L, amount));
        assertEquals(new BigDecimal("1000.00"), sourceAccount.getBalance());
        assertEquals(new BigDecimal("500.00"), destinationAccount.getBalance());
    }

    @Test
    void shouldRejectTransferWhenAmountIsInvalid() {
        BigDecimal amount = BigDecimal.ZERO;
        assertThrows(InvalidTransactionException.class, () -> transactionService.processTransfer(1L, 2L, amount));
    }

    @Test
    void shouldRejectTransactionForBlockedAccount() {
        sourceAccount.setStatus(AccountStatus.BLOCKED);
        when(accountRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(sourceAccount));
        Assertions.assertThrows(InvalidTransactionException.class, () -> transactionService.processDeposit(1L,
                new BigDecimal("100.00")));
        verify(accountRepository).findByIdForUpdate(1L);
        verifyNoInteractions(ledgerEntryRepository);
    }

}



