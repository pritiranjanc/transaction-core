package com.transaction.core.service.transfer;

import com.transaction.core.constants.EntryType;
import com.transaction.core.constants.TransactionStatus;
import com.transaction.core.constants.TransactionType;
import com.transaction.core.constants.TransferType;
import com.transaction.core.entity.Account;
import com.transaction.core.entity.LedgerEntry;
import com.transaction.core.entity.Transaction;
import com.transaction.core.repository.AccountRepository;
import com.transaction.core.repository.LedgerEntryRepository;
import com.transaction.core.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static com.transaction.core.dto.response.LedgerEntryDTO.createLedgerEntry;
import static com.transaction.core.dto.response.TransactionDTO.createTransaction;

@Service
@RequiredArgsConstructor
public class NEFTTransferService implements TransferService{

    private final TransactionRepository transactionRepository;
    private final LedgerEntryRepository ledgerEntryRepository;
    private final AccountRepository accountRepository;

    @Override
    public Transaction processTransfer(Account source, Account destination, BigDecimal amount) {
        Transaction transaction = createTransaction(TransactionType.TRANSFER, amount,TransactionStatus.PENDING,source,destination,TransferType.NEFT);
        transactionRepository.save(transaction);
        return transaction;
    }

    @Override
    public String supportedTransferType() {
        return TransferType.NEFT.name();
    }

    @Override
    @Transactional
    public void processPendingTransfer(Long transactionId){
        Transaction txn = transactionRepository.findByIdForUpdate(transactionId).orElse(null);
        assert txn != null;
        if (!TransferType.NEFT.equals(txn.getTransferType()) || !TransactionStatus.PENDING.equals(txn.getStatus())) {
            return;
        }
        if(txn.getAmount().compareTo(txn.getSourceAccount().getBalance())>0){
            txn.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(txn);
            return;
        }
        boolean debitExists = ledgerEntryRepository.existsByTransactionIdAndEntryType(transactionId, EntryType.CREDIT);
        boolean creditExists = ledgerEntryRepository.existsByTransactionIdAndEntryType(transactionId, EntryType.CREDIT);
        if(debitExists && creditExists){
            return;
        }
        Account source = txn.getSourceAccount();
        Account destination = txn.getToAccount();
        source.setBalance(source.getBalance().subtract(txn.getAmount()));
        destination.setBalance(destination.getBalance().add(txn.getAmount()));
        LedgerEntry creditEntry = createLedgerEntry(txn,destination,EntryType.CREDIT,txn.getAmount());
        LedgerEntry debitEntry = createLedgerEntry(txn,source,EntryType.DEBIT,txn.getAmount());
        txn.setStatus(TransactionStatus.COMPLETED);
        ledgerEntryRepository.saveAll(List.of(creditEntry,debitEntry));
        accountRepository.saveAll(List.of(source,destination));
        transactionRepository.save(txn);
    }

}
