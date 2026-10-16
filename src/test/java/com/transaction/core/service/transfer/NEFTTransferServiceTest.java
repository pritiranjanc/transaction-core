package com.transaction.core.service.transfer;

import com.transaction.core.constants.AccountStatus;
import com.transaction.core.constants.TransactionType;
import com.transaction.core.entity.Account;
import com.transaction.core.entity.Transaction;
import com.transaction.core.repository.TransactionRepository;
import com.transaction.core.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NEFTTransferServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private NEFTTransferService neftTransferService;

    @Test
    void testTransfer(){
        Account sourceAccount = Account.builder()
                .id(1L)
                .accountNumber("ACC001")
                .balance(new BigDecimal("1000.00"))
                .currency("USD")
                .status(AccountStatus.ACTIVE)
                .build();

        Account destinationAccount = Account.builder()
                .id(2L)
                .accountNumber("ACC002")
                .balance(new BigDecimal("500.00"))
                .currency("USD")
                .status(AccountStatus.ACTIVE)
                .build();
        when(transactionRepository.save(any())).thenReturn(TestUtils.getTransaction(1L, TransactionType.TRANSFER));
        Transaction transaction = neftTransferService.processTransfer(sourceAccount,destinationAccount,BigDecimal.TEN);
        assertNotNull(transaction);
    }
}
