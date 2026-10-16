package com.transaction.core.service.transfer;

import com.transaction.core.constants.AccountStatus;
import com.transaction.core.entity.Account;
import com.transaction.core.entity.Transaction;
import com.transaction.core.repository.AccountRepository;
import com.transaction.core.repository.LedgerEntryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class IMPSTransferServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private LedgerEntryRepository ledgerEntryRepository;
    @InjectMocks
    private IMPSTransferService impsTransferService;

    private Account sourceAccount;
    private Account destinationAccount;


    @Test
    void testTransfer(){
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

        BigDecimal amount = new BigDecimal("400.00");
        Transaction result = impsTransferService.processTransfer(sourceAccount, destinationAccount, amount);
        assertNotNull(result);
        assertEquals(new BigDecimal("600.00"), sourceAccount.getBalance());
        assertEquals(new BigDecimal("900.00"), destinationAccount.getBalance());
        verify(accountRepository).saveAll(List.of(sourceAccount,destinationAccount));
        verify(ledgerEntryRepository, times(1)).saveAll(anyList());
    }
}
