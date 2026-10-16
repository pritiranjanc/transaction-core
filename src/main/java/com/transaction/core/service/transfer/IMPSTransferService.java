package com.transaction.core.service.transfer;

import com.transaction.core.constants.EntryType;
import com.transaction.core.constants.TransactionStatus;
import com.transaction.core.constants.TransactionType;
import com.transaction.core.constants.TransferType;
import com.transaction.core.dto.response.LedgerEntryDTO;
import com.transaction.core.dto.response.TransactionDTO;
import com.transaction.core.entity.Account;
import com.transaction.core.entity.LedgerEntry;
import com.transaction.core.entity.Transaction;
import com.transaction.core.repository.AccountRepository;
import com.transaction.core.repository.LedgerEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.transaction.core.dto.response.TransactionDTO.createTransaction;

@Service
@RequiredArgsConstructor
public class IMPSTransferService implements TransferService{

    private final LedgerEntryRepository ledgerEntryRepository;
    private final AccountRepository accountRepository;

    @Override
    public Transaction processTransfer(Account source, Account destination, BigDecimal amount) {
        source.setBalance(source.getBalance().subtract(amount));
        destination.setBalance(destination.getBalance().add(amount));
        source.setUpdatedAt(LocalDateTime.now());
        destination.setUpdatedAt(LocalDateTime.now());
        Transaction transaction = createTransaction(TransactionType.TRANSFER, amount, TransactionStatus.COMPLETED,source,destination);
        LedgerEntry debitEntry = LedgerEntryDTO.createLedgerEntry(transaction, source, EntryType.DEBIT, amount);
        LedgerEntry creditEntry = LedgerEntryDTO.createLedgerEntry(transaction, destination, EntryType.CREDIT, amount);
        ledgerEntryRepository.saveAll(List.of(debitEntry,creditEntry));
        accountRepository.saveAll(List.of(source,destination));
        return transaction;
    }

    @Override
    public String supportedTransferType() {
        return TransferType.IMPS.name();
    }
}
