package com.transaction.core.service;

import com.transaction.core.constants.*;
import com.transaction.core.dto.response.TransactionDTO;
import com.transaction.core.entity.Account;
import com.transaction.core.entity.LedgerEntry;
import com.transaction.core.entity.Transaction;
import com.transaction.core.exception.AccountNotFoundException;
import com.transaction.core.exception.InsufficientBalanceException;
import com.transaction.core.exception.InvalidTransactionException;
import com.transaction.core.repository.AccountRepository;
import com.transaction.core.repository.LedgerEntryRepository;
import com.transaction.core.service.transfer.TransferServiceFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.transaction.core.dto.response.LedgerEntryDTO.createLedgerEntry;
import static com.transaction.core.dto.response.TransactionDTO.createTransaction;

@Service
@AllArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService{

    private final AccountRepository accountRepository;
    private final LedgerEntryRepository ledgerEntryRepository;
    private final TransferServiceFactory serviceFactory;


    @Transactional
    public TransactionDTO processDeposit(Long accountId, BigDecimal amount) {
        log.info("Starting deposit: accountId={}, amount={}", accountId, amount);
        validateAmount(amount);
        Account account = accountRepository.findByIdForUpdate(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));
        validateAccount(account);
        account.setBalance(account.getBalance().add(amount));
        Transaction transaction = createTransaction(TransactionType.DEPOSIT, amount, TransactionStatus.COMPLETED,account,null);
        LedgerEntry entry =  createLedgerEntry(transaction, account, EntryType.CREDIT, amount);
        ledgerEntryRepository.save(entry);
        accountRepository.save(account);
        log.info("Deposit completed: transactionId={}, accountId={}", transaction.getId(), accountId);
        return TransactionDTO.from(transaction);
    }

    @Transactional
    public TransactionDTO processWithdrawal(Long accountId, BigDecimal amount) {
        log.info("Starting withdrawal: accountId={}, amount={}", accountId, amount);
        validateAmount(amount);
        Account account = accountRepository.findByIdForUpdate(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));
        validateAccount(account);
        if (account.getBalance().compareTo(amount) < 0) {
            log.warn("Withdrawal rejected due to insufficient balance: accountId={}", accountId);
            throw new InsufficientBalanceException(accountId);
        }
        account.setBalance(account.getBalance().subtract(amount));
        Transaction transaction = createTransaction(TransactionType.WITHDRAWAL, amount,TransactionStatus.COMPLETED,account,null);
        LedgerEntry entry = createLedgerEntry(transaction, account, EntryType.DEBIT, amount);
        ledgerEntryRepository.save(entry);
        accountRepository.save(account);
        log.info("Withdrawal completed: transactionId={}, accountId={}", transaction.getId(), accountId);
        return TransactionDTO.from(transaction);
    }

    @Transactional
    public TransactionDTO processTransfer(Long fromAccountId, Long toAccountId, BigDecimal amount, TransferType type) {
        log.info("Starting transfer: fromAccountId={}, toAccountId={}, amount={}", fromAccountId, toAccountId, amount);
        validateAmount(amount);
        if (fromAccountId.equals(toAccountId)) {
            throw new InvalidTransactionException("Source and destination accounts must be different");
        }
        Long firstId = Math.min(fromAccountId, toAccountId);
        Long secondId = Math.max(fromAccountId, toAccountId);
        Account first = accountRepository.findByIdForUpdate(firstId).orElseThrow(() -> new AccountNotFoundException(firstId));
        Account second = accountRepository.findByIdForUpdate(secondId).orElseThrow(() -> new AccountNotFoundException(secondId));

        Account source = fromAccountId.equals(firstId) ? first : second;
        Account destination = toAccountId.equals(firstId) ? first : second;
        validateAccount(source);
        validateAccount(destination);

        if (source.getBalance().compareTo(amount) <= 0) {
            throw new InsufficientBalanceException(fromAccountId);
        }
        Transaction transaction = serviceFactory.getTransferService(type.name()).processTransfer(source,destination,amount);
        log.info("Transfer completed: transactionId={}, fromAccountId={}, toAccountId={}", transaction.getId(), fromAccountId, toAccountId);
        return TransactionDTO.from(transaction);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException("Amount must be greater than zero");
        }
    }

    private void validateAccount(Account account) {
        if (!AccountStatus.ACTIVE.name().equals(account.getStatus().name())) {
            throw new InvalidTransactionException("Account is not active");
        }
    }


}
