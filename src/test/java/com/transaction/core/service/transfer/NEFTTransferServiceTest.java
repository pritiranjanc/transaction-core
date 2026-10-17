package com.transaction.core.service.transfer;

import com.transaction.core.constants.*;
import com.transaction.core.dto.response.TransactionDTO;
import com.transaction.core.entity.Account;
import com.transaction.core.entity.Transaction;
import com.transaction.core.repository.AccountRepository;
import com.transaction.core.repository.LedgerEntryRepository;
import com.transaction.core.repository.TransactionRepository;
import com.transaction.core.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NEFTTransferServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private LedgerEntryRepository ledgerEntryRepository;

    @InjectMocks
    private NEFTTransferService neftTransferService;

    private Account sourceAccount;
    private Account destinationAccount;

    @BeforeEach
    void setUp(){
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
    void testTransfer(){
        when(transactionRepository.save(any())).thenReturn(TestUtils.getTransaction(1L, TransactionType.TRANSFER));
        Transaction transaction = neftTransferService.processTransfer(sourceAccount,destinationAccount,BigDecimal.TEN);
        assertNotNull(transaction);
    }

    @Test
    void processPendingTransfer(){
        Transaction txn = TransactionDTO.createTransaction(TransactionType.TRANSFER,BigDecimal.TEN,
                TransactionStatus.PENDING,sourceAccount,destinationAccount, TransferType.NEFT);
        txn.setId(1L);
        when(transactionRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(txn));
        when(ledgerEntryRepository.existsByTransactionIdAndEntryType(any(), any())).thenReturn(false);
        neftTransferService.processPendingTransfer(1L);
        assertEquals(new BigDecimal("990.00"), sourceAccount.getBalance());
        assertEquals(new BigDecimal("510.00"), destinationAccount.getBalance());
        assertEquals(TransactionStatus.COMPLETED, txn.getStatus());
    }
}
