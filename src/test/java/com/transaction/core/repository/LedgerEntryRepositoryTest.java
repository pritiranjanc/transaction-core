package com.transaction.core.repository;

import com.transaction.core.constants.EntryType;
import com.transaction.core.constants.TransactionType;
import com.transaction.core.entity.Account;
import com.transaction.core.entity.LedgerEntry;
import com.transaction.core.entity.Transaction;
import com.transaction.core.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;

import static com.transaction.core.utils.TestUtils.getLedgerEntry;
import static com.transaction.core.utils.TestUtils.getTransaction;

@DataJpaTest
public class LedgerEntryRepositoryTest {

    @Autowired
    private LedgerEntryRepository ledgerEntryRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;
    private Account account;

    @BeforeEach
    void prepareTest(){
        account = TestUtils.getAccount();
        account.setId(null);
        account = accountRepository.save(account);
        Transaction transaction1 = getTransaction(null, TransactionType.DEPOSIT);
        Transaction transaction2 = getTransaction(null, TransactionType.WITHDRAWAL);
        transactionRepository.saveAll(List.of(transaction1,transaction2));
        LedgerEntry entry1= getLedgerEntry(null,account, EntryType.CREDIT, BigDecimal.TEN,BigDecimal.TEN,transaction1);
        LedgerEntry entry2 = getLedgerEntry(null,account, EntryType.DEBIT, BigDecimal.TEN,BigDecimal.ZERO,transaction2);
        ledgerEntryRepository.saveAll(List.of(entry1,entry2));
    }

    @Test
    void findByAccountId(){
        Page<LedgerEntry> ledgers =  ledgerEntryRepository.findByAccountId(account.getId(), PageRequest.of(0,10));
        Assertions.assertEquals(2,ledgers.getContent().size());
    }
}
