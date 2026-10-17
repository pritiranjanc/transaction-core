package com.transaction.core.repository;

import com.transaction.core.constants.EntryType;
import com.transaction.core.entity.LedgerEntry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class LedgerEntryRepositoryTest {

    @Autowired
    private LedgerEntryRepository ledgerEntryRepository;

    @Test
    void findByAccountId(){
        Page<LedgerEntry> ledgers =  ledgerEntryRepository.findByAccountId(1L, PageRequest.of(0,10));
        Assertions.assertEquals(1,ledgers.getContent().size());
    }

    @Test
    void findByAccountIdAndEntryType(){
        BigDecimal amount =  ledgerEntryRepository.findByAccountIdAndEntryType(1L, EntryType.CREDIT);
        assertNull(amount);
    }

    @Test
    void existsByTransactionIdAndEntryType(){
        boolean exist =  ledgerEntryRepository.existsByTransactionIdAndEntryType(1L, EntryType.CREDIT);
        assertTrue(exist);
    }
}
