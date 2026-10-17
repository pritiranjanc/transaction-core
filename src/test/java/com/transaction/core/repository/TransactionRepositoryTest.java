package com.transaction.core.repository;

import com.transaction.core.constants.TransactionStatus;
import com.transaction.core.constants.TransferType;
import com.transaction.core.entity.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class TransactionRepositoryTest {

    @Autowired
    TransactionRepository transactionRepository;

    @Test
    void findByStatusAndTransferType() {
        List<Long> transactions = transactionRepository.findIdByStatusAndTransferTypeOrderById
                (TransactionStatus.PENDING, TransferType.NEFT, PageRequest.of(0,10));
        Assertions.assertEquals(0,transactions.size());
    }

    @Test
    void findByIdForUpdate() {
        Transaction transactions = transactionRepository.findByIdForUpdate(1L).orElse(null);
        assertNotNull(transactions);
    }
}
